package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.cache.impl.TimFriendCache;
import com.im.common.entity.*;
import com.im.common.entity.enums.RegisterTypeEnum;
import com.im.common.entity.enums.TrendsReviewTypeEnum;
import com.im.common.entity.enums.TrendsTypeEnum;
import com.im.common.mapper.UserBillMapper;
import com.im.common.mapper.UserMomentsMapper;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.*;
import com.im.common.util.*;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 朋友圈实现类
 * @author max.stark
 */
@Service
public class UserMomentsServiceImpl extends MyBatisPlusServiceImpl<UserMomentsMapper, UserMoments>
        implements UserMomentsService {

    private UserMomentsTrendsService userMomentsTrendsService;
    private UserMomentsCallTrendsService userMomentsCallTrendsService;
    private UserMomentsReviewService userMomentsReviewService;
    private UserMomentsReviewTrendsService userMomentsReviewTrendsService;
    private TimFriendCache timFriendCache;
    private PortalUserCache portalUserCache;
    private static final int MAX_IN_CONDITION = 500;

    /**
     * 动态提醒
     * @param sessionUser
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public RestResponse<UserMomentsTrendsVO> trendsForPortal(PortalSessionUser sessionUser) {
        Long userId = sessionUser.getId();
        List<UserMomentsCallTrends> callTrendsList = userMomentsCallTrendsService.listByUser(userId, Boolean.FALSE);
        List<UserMomentsReviewTrends> reviewTrendsList = userMomentsReviewTrendsService.listByUser(userId, Boolean.FALSE);

        List<Long> callTrends = CollectionUtil.toList(callTrendsList, UserMomentsCallTrends::getMomentsId);
        Map<Long, List<UserMomentsReviewTrends>> listMap = CollectionUtil.toMapList(reviewTrendsList, UserMomentsReviewTrends::getMomentsId);
        Map<Long, Integer> reviewTrends = new HashMap<>();
        for (Long id : listMap.keySet()) {
            int size = listMap.get(id).size();
            reviewTrends.put(id, size);
        }
        int sumCount = callTrendsList.size() + reviewTrendsList.size();
        UserMomentsTrendsVO vo = new UserMomentsTrendsVO();
        vo.setSumCount(sumCount);
        vo.setCallTrends(callTrends);
        vo.setReviews(reviewTrends);
        return RestResponse.ok(vo);
    }

    /**
     * 朋友圈分页 前台
     * @param sessionUser
     * @param param
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public RestResponse<PageVO<UserMomentsVO>> pageForPortal(PortalSessionUser sessionUser, UserMomentsPortalParam param) {
        Long userId = sessionUser.getId();
        List<Long> friendIds = timFriendCache.getFriendByIdFromLocal(userId);
        if (CollectionUtil.isEmpty(friendIds)) {
            return RestResponse.ok(new PageVO<>(param));
        }
        int groupCount = 1;
        if (friendIds.size() > MAX_IN_CONDITION) {
            groupCount += friendIds.size() / MAX_IN_CONDITION;
        }
        List<List<Long>> lists = CollectionUtil.splitBySubList(friendIds, groupCount);

        // 拿出所有朋友圈
        List<UserMoments> userMomentsList = new ArrayList<>();
        // TODO 最近180天的
        String dateStr = DateTimeUtil.plusDaysFromNowToDateStr(180);
        for (List<Long> ids : lists) {
            List<UserMoments> userMoments = lambdaQuery()
                    .in(UserMoments::getUserId, ids)
                    .ge(UserMoments::getCreateTime, dateStr)
                    .orderByDesc(UserMoments::getCreateTime).list();

            List<Long> momentsIds = CollectionUtil.toList(userMoments, UserMoments::getId);
            // 筛选朋友圈设定的权限
            List<UserMomentsTrends> trendsList = userMomentsTrendsService.lambdaQuery().in(UserMomentsTrends::getMomentsId, momentsIds).list();
            for (UserMoments moments: userMoments) {
                // 自己发的直接加进去
                if (moments.getUserId().equals(userId)){
                    userMomentsList.add(moments);
                }else{
                    if (moments.getTrendsType().equals(TrendsTypeEnum.ALLOW_FRIEND)){
                        long allowCount = trendsList.stream()
                                .filter(e -> e.getMomentsId().equals(moments.getId()) &&
                                        e.getAllow().equals(Boolean.TRUE) &&
                                        e.getUserId().equals(userId)).count();
                        if (allowCount > 0) {
                            userMomentsList.add(moments);
                        }
                    }else if (moments.getTrendsType().equals(TrendsTypeEnum.REJECT_FRIEND)) {
                        long allowCount = trendsList.stream()
                                .filter(e -> e.getMomentsId().equals(moments.getId()) &&
                                        e.getAllow().equals(Boolean.FALSE) &&
                                        e.getUserId().equals(userId)).count();
                        if (allowCount == 0) {
                            userMomentsList.add(moments);
                        }
                    }else if (moments.getTrendsType().equals(TrendsTypeEnum.ALL_FRIEND)) {
                        userMomentsList.add(moments);
                    }
                }
            }
        }


        // 排序一下
        CollectionUtil.sort(userMomentsList, Comparator.comparing(UserMoments::getCreateTime, (t1, t2) -> t2.compareTo(t1)));

        // 手动分页
        Long sunPage = 0L;
        if ((userMomentsList.size() % param.getSize()) == 0 ){
            sunPage = userMomentsList.size() / param.getSize();
        }else {
            sunPage = 1 + (userMomentsList.size() / param.getSize());
        }
        List<List<UserMoments>> userMomentsPages = CollectionUtil.splitBySubList(userMomentsList, sunPage.intValue());
        List<UserMoments> resultPages = userMomentsPages.get(param.getCurrent().intValue() - 1);

        // 加载朋友圈相关评论和点赞
        List<Long> momentsIds = CollectionUtil.toList(resultPages, UserMoments::getId);
        Map<Long, List<UserMomentsReview>> longListMap = loadReviews(friendIds, momentsIds);

        // 装载返回数据
        PageVO<UserMomentsVO> pageVO = new PageVO<>();
        pageVO.setPages(sunPage);
        pageVO.setCurrent(param.getCurrent());
        pageVO.setSize(param.getSize());
        pageVO.setTotal(Long.valueOf(resultPages.size()));
        List<UserMomentsVO> userMomentsVOList = new ArrayList<>();
        for (UserMoments userMoments : resultPages) {
            List<UserMomentsReview> likeReviews = longListMap.get(userMoments.getId());
            List<UserMomentsReview> likes = likeReviews.stream().filter(r -> r.getReviewType() == TrendsReviewTypeEnum.LIKE).collect(Collectors.toList());
            List<UserMomentsReview> reviews = likeReviews.stream().filter(r -> r.getReviewType() == TrendsReviewTypeEnum.REVIEW).collect(Collectors.toList());
            List<UserMomentsReviewVO> likeVOs = CollectionUtil.toList(likes, e -> new UserMomentsReviewVO(e));
            List<UserMomentsReviewVO> reviewVOs = CollectionUtil.toList(reviews, e -> new UserMomentsReviewVO(e));
            UserMomentsVO vo = new UserMomentsVO(userMoments, likeVOs, reviewVOs);
            userMomentsVOList.add(vo);
        }
        pageVO.setRecords(userMomentsVOList);
        return RestResponse.ok(pageVO);
    }

    /**
     * 朋友圈分页 后台
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public RestResponse<PageVO<UserMomentsAdminVO>> pageForAdmin(UserMomentsPageAdminParam param) {
        PageVO<UserMomentsAdminVO> pageVO = pageVO(param, UserMomentsAdminVO::new);
        if (CollectionUtil.isEmpty(pageVO.getRecords())) {
            return RestResponse.ok(new PageVO<>(param));
        }
        // 组装数据
        List<Long> momentsIds = CollectionUtil.toList(pageVO.getRecords(), UserMomentsAdminVO::getId);
        // 所有评论
        List<UserMomentsReview> reviewList = userMomentsReviewService.lambdaQuery()
                .in(UserMomentsReview::getMomentsId, momentsIds)
                .list();
        // 所有评论动态
        List<Long> reviewIds = CollectionUtil.toList(reviewList, UserMomentsReview::getId);
        List<UserMomentsReviewTrends> reviewTrendsList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(reviewIds)) {
            reviewTrendsList = userMomentsReviewTrendsService
                    .lambdaQuery()
                    .in(UserMomentsReviewTrends::getMomentsReviewId, reviewIds)
                    .list();
        }

        List<UserMomentsReviewVO> likesVO = new ArrayList<>();
        List<UserMomentsReviewVO> reviewsVO = new ArrayList<>();
        for (UserMomentsReview review: reviewList) {
            List<UserMomentsReviewTrends> trends = reviewTrendsList.stream().filter(r -> r.getMomentsReviewId().equals(review.getId())).collect(Collectors.toList());
            List<UserMomentsReviewTrendsAdminVO> trendsAdminVOS = CollectionUtil.toList(trends, e -> new UserMomentsReviewTrendsAdminVO(e));
            UserMomentsReviewVO reviewVO = new UserMomentsReviewVO(review);
            reviewVO.setReviewTrends(trendsAdminVOS);
            if (TrendsReviewTypeEnum.LIKE.equals(review.getReviewType())){
                likesVO.add(reviewVO);
            }else if (TrendsReviewTypeEnum.REVIEW.equals(review.getReviewType())){
                reviewsVO.add(reviewVO);
            }
        }

        // 所有提醒谁看
        List<UserMomentsCallTrends> callList = userMomentsCallTrendsService.lambdaQuery()
                .in(UserMomentsCallTrends::getMomentsId, momentsIds)
                .list();
        List<UserMomentsCallTrendsAdminVO> callTrendsVO = CollectionUtil.toList(callList, e -> new UserMomentsCallTrendsAdminVO(e));

        // 朋友圈动态
        List<UserMomentsTrends> trendsList = userMomentsTrendsService.lambdaQuery()
                .in(UserMomentsTrends::getMomentsId, momentsIds)
                .list();
        List<UserMomentsTrends> allowList = trendsList.stream().filter(e -> Boolean.TRUE.equals(e.getAllow())).collect(Collectors.toList());
        List<UserMomentsTrends> shieldList = trendsList.stream().filter(e -> Boolean.FALSE.equals(e.getAllow())).collect(Collectors.toList());
        List<UserMomentsTrendsAdminVO> allowsVO = CollectionUtil.toList(allowList, e -> new UserMomentsTrendsAdminVO(e));
        List<UserMomentsTrendsAdminVO> shieldVO = CollectionUtil.toList(shieldList, e -> new UserMomentsTrendsAdminVO(e));

        for (UserMomentsAdminVO vo : pageVO.getRecords()) {
            // 点赞
            List<UserMomentsReviewVO> likes = likesVO.stream().filter(e -> e.getMomentsId().equals(vo.getId())).collect(Collectors.toList());
            // 评论
            List<UserMomentsReviewVO> reviews = reviewsVO.stream().filter(e -> e.getMomentsId().equals(vo.getId())).collect(Collectors.toList());
            // 提醒谁看
            List<UserMomentsCallTrendsAdminVO> callTrends = callTrendsVO.stream().filter(e -> e.getMomentsId().equals(vo.getId())).collect(Collectors.toList());
            // 允许谁看
            List<UserMomentsTrendsAdminVO> allows = allowsVO.stream().filter(e -> e.getMomentsId().equals(vo.getId())).collect(Collectors.toList());
            // 拒绝谁看
            List<UserMomentsTrendsAdminVO> shields = shieldVO.stream().filter(e -> e.getMomentsId().equals(vo.getId())).collect(Collectors.toList());

            vo.setLikeCount(likes.size());
            vo.setLikeUsers(likes);
            vo.setReviewCount(reviews.size());
            vo.setReviewUsers(reviews);
            vo.setCallCount(callTrends.size());
            vo.setCallTrends(callTrends);
            vo.setAllowCount(allows.size());
            vo.setMomentsByAllow(allows);
            vo.setShieldCount(shields.size());
            vo.setMomentsByShield(shields);
        }

        return RestResponse.ok(pageVO);
    }

    /**
     * 某个用户的朋友圈
     *
     * @param sessionUser
     * @param param
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public RestResponse<PageVO<UserMomentsVO>> pageByUserForPortal(PortalSessionUser sessionUser, UserMomentsByUserPortalParam param) {
        Long userId = sessionUser.getId();
        Long friendId = portalUserCache.getIdByUsernameFromLocal(param.getUsername());
        if (friendId == null) {
            // 没有查询到用户，不再查询
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }
        List<Long> friendIds = timFriendCache.getFriendByIdFromLocal(userId);
        if (CollectionUtil.isEmpty(friendIds)) {
            return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
        }
        // 检查用户好友关系
        int findSize = friendIds.stream().filter(f -> f.equals(friendId)).collect(Collectors.toList()).size();
        if (findSize == 0) {
            return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
        }

        PageVO<UserMomentsVO> pageVO = pageVO(param, sessionUser, e -> new UserMomentsVO(e, null, null));
        List<Long> momentsIds = CollectionUtil.toList(pageVO.getRecords(), UserMomentsVO::getId);

        Map<Long, List<UserMomentsReview>> longListMap = loadReviews(friendIds, momentsIds);

        for (UserMomentsVO vo : pageVO.getRecords()) {
            List<UserMomentsReview> likeReviews = longListMap.get(vo.getId());
            List<UserMomentsReview> likes = likeReviews.stream().filter(r -> r.getReviewType() == TrendsReviewTypeEnum.LIKE).collect(Collectors.toList());
            List<UserMomentsReview> reviews = likeReviews.stream().filter(r -> r.getReviewType() == TrendsReviewTypeEnum.REVIEW).collect(Collectors.toList());
            List<UserMomentsReviewVO> likeVOs = CollectionUtil.toList(likes, e -> new UserMomentsReviewVO(e));
            List<UserMomentsReviewVO> reviewVOs = CollectionUtil.toList(reviews, e -> new UserMomentsReviewVO(e));
            vo.setLikeUsers(likeVOs);
            vo.setReviewUsers(reviewVOs);
        }
        return RestResponse.ok(pageVO);
    }

    /**
     * 查看某条朋友圈
     *
     * @param sessionUser
     * @param param
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public RestResponse<UserMomentsVO> detailForPortal(PortalSessionUser sessionUser, IdParam param) {
        Long userId = sessionUser.getId();

        UserMoments userMoments = getById(param.getId());
        if (null == userMoments) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }
        List<Long> friendIds = timFriendCache.getFriendByIdFromLocal(userId);
        if (CollectionUtil.isEmpty(friendIds)) {
            return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
        }

        // 检查用户好友关系
        int findSize = friendIds.stream().filter(f -> f.equals(userMoments.getUserId())).collect(Collectors.toList()).size();
        if (findSize == 0) {
            return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
        }

        List<Long> momentsIds = new ArrayList<>();
        momentsIds.add(userMoments.getId());
        Map<Long, List<UserMomentsReview>> longListMap = loadReviews(friendIds, momentsIds);

        List<UserMomentsReview> likeReviews = longListMap.get(userMoments.getId());
        List<UserMomentsReview> likes = likeReviews.stream().filter(r -> r.getReviewType() == TrendsReviewTypeEnum.LIKE).collect(Collectors.toList());
        List<UserMomentsReview> reviews = likeReviews.stream().filter(r -> r.getReviewType() == TrendsReviewTypeEnum.REVIEW).collect(Collectors.toList());
        List<UserMomentsReviewVO> likeVOs = CollectionUtil.toList(likes, e -> new UserMomentsReviewVO(e));
        List<UserMomentsReviewVO> reviewVOs = CollectionUtil.toList(reviews, e -> new UserMomentsReviewVO(e));
        UserMomentsVO vo = new UserMomentsVO(userMoments, likeVOs, reviewVOs);
        return RestResponse.ok(vo);
    }

    /**
     * 加载朋友圈相关评论和点赞
     * @return
     */
    private Map<Long, List<UserMomentsReview>> loadReviews(List<Long> friendIds, List<Long> momentsIds){
        // 加载朋友圈相关评论和点赞
        List<UserMomentsReview> userMomentsReviews = userMomentsReviewService.lambdaQuery()
                .in(UserMomentsReview::getMomentsId, momentsIds)
                .orderByDesc(UserMomentsReview::getCreateTime).list();

        // 进行一次筛选，不是好友列表的点赞和评论 不可见
        List<UserMomentsReview> collect = userMomentsReviews.stream().filter(r -> friendIds.stream().filter(f -> f.equals(r.getUserId())).collect(Collectors.toList()).size() > 0)
                .collect(Collectors.toList());
        Map<Long, List<UserMomentsReview>> longListMap = CollectionUtil.toMapList(collect, UserMomentsReview::getMomentsId);
        return longListMap;
    }

    /**
     * 新增
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForPortal(PortalSessionUser sessionUser, UserMomentsAddParam param) {

        // 过滤非法请求字符
        if (!XssUtil.isValid(param.getContent())) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_ILLEGAL);
        }

        Long userId = sessionUser.getId();
        List<String> urls = param.getImgUrls();
        int maxCount = 9;
        // 只能发布图片或者视频
        if (CollectionUtil.isNotEmpty(urls) && StrUtil.isNotEmpty(param.getVideoUrls())) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        // 最多发布9张图片
        if (CollectionUtil.isNotEmpty(urls) && urls.size() > maxCount) {
            return RestResponse.failed(ResponseCode.USER_MOMENTS_EXCEED_IMG_COUNT);
        }

        // 保存朋友圈数据
        LocalDateTime now = LocalDateTime.now();
        UserMoments um = new UserMoments();
        Long id = IdWorker.getId();
        um.setId(id);
        um.setUserId(userId);
        um.setContent(param.getContent());
        um.setAddress(param.getAddress());
        String urlsStr = StrUtil.join(",", urls);
        um.setImgUrls(urlsStr);
        um.setVideoUrls(param.getVideoUrls());
        um.setTrendsType(param.getTrendsType());
        um.setIp(param.getIp());
        um.setCreateTime(now);
        um.setUpdateTime(now);
        boolean saved = save(um);
        if (!saved) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }

        // 保存朋友圈动态提醒
        List<UserMomentsTrends> umtList = new ArrayList<>();
        switch (param.getTrendsType()){
            case ALL_FRIEND:
                UserMomentsTrends umt = new UserMomentsTrends();
                umt.setUserId(0L);
                umt.setMomentsId(id);
                umt.setAllow(true);
                umt.setCreateTime(now);
                umt.setUpdateTime(now);
                umtList.add(umt);
                break;
            case ALLOW_FRIEND:
                for (Long allowId : param.getAllowUserIds()) {
                    UserMomentsTrends userMomentsTrends = new UserMomentsTrends();
                    userMomentsTrends.setUserId(allowId);
                    userMomentsTrends.setMomentsId(id);
                    userMomentsTrends.setAllow(true);
                    userMomentsTrends.setCreateTime(now);
                    userMomentsTrends.setUpdateTime(now);
                    umtList.add(userMomentsTrends);
                }
                if (CollectionUtil.isEmpty(umtList)) {
                    return RestResponse.failed(ResponseCode.USER_MOMENTS_ALLOW);
                }
                break;
            case REJECT_FRIEND:
                for (Long rejectId : param.getNotAllowUserIds()) {
                    UserMomentsTrends userMomentsTrends = new UserMomentsTrends();
                    userMomentsTrends.setUserId(rejectId);
                    userMomentsTrends.setMomentsId(id);
                    userMomentsTrends.setAllow(false);
                    userMomentsTrends.setCreateTime(now);
                    userMomentsTrends.setUpdateTime(now);
                    umtList.add(userMomentsTrends);
                }
                if (CollectionUtil.isEmpty(umtList)) {
                    return RestResponse.failed(ResponseCode.USER_MOMENTS_REJECT);
                }
                break;
            default:
                break;
        }

        boolean umtSaved = userMomentsTrendsService.saveBatch(umtList);
        if (!umtSaved) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }

        // 保存提醒谁看
        if (CollectionUtil.isNotEmpty(param.getCallUserIds())) {
            List<UserMomentsCallTrends> umcList = new ArrayList<>();
            for (Long callId : param.getCallUserIds()) {
                UserMomentsCallTrends umc = new UserMomentsCallTrends();
                umc.setUserId(callId);
                umc.setMomentsId(id);
                umc.setRead(false);
                umc.setCreateTime(now);
                umc.setUpdateTime(now);
                umcList.add(umc);
            }
            boolean umcSaved = userMomentsCallTrendsService.saveBatch(umcList);
            if (!umcSaved) {
                return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
            }
        }
        return RestResponse.OK;
    }

    /**
     * 删除
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForPortal(PortalSessionUser sessionUser, IdParam param) {
        UserMoments userMoments = getById(param.getId());
        if (!userMoments.getUserId().equals(sessionUser.getId())) {
            return RestResponse.failed(ResponseCode.SYS_NO_PERMISSION);
        }
        return deleteData(param.getId());
    }

    /**
     * 删除朋友圈
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        return deleteData(param.getId());
    }

    private RestResponse deleteData(Long id){
        // 删除提醒谁看
        userMomentsCallTrendsService.lambdaUpdate().eq(UserMomentsCallTrends::getMomentsId, id).remove();

        // 删除朋友圈动态提醒
        userMomentsTrendsService.lambdaUpdate().eq(UserMomentsTrends::getMomentsId, id).remove();

        // 删除评论
        userMomentsReviewService.lambdaUpdate().eq(UserMomentsReview::getMomentsId, id).remove();

        // 删除评论动态
        userMomentsReviewTrendsService.lambdaUpdate().eq(UserMomentsReviewTrends::getMomentsId, id).remove();

        // 删除朋友圈
        removeById(id);
        return RestResponse.OK;
    }

    @Autowired
    public void setUserMomentsTrendsService(UserMomentsTrendsService userMomentsTrendsService) {
        this.userMomentsTrendsService = userMomentsTrendsService;
    }

    @Autowired
    public void setUserMomentsCallTrendsService(UserMomentsCallTrendsService userMomentsCallTrendsService) {
        this.userMomentsCallTrendsService = userMomentsCallTrendsService;
    }

    @Autowired
    public void setUserMomentsReviewService(UserMomentsReviewService userMomentsReviewService) {
        this.userMomentsReviewService = userMomentsReviewService;
    }

    @Autowired
    public void setUserMomentsReviewTrendsService(UserMomentsReviewTrendsService userMomentsReviewTrendsService) {
        this.userMomentsReviewTrendsService = userMomentsReviewTrendsService;
    }

    @Autowired
    public void setTimFriendCache(TimFriendCache timFriendCache) {
        this.timFriendCache = timFriendCache;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }
}
