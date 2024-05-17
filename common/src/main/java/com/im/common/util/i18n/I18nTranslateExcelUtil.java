package com.im.common.util.i18n;

import com.im.common.constant.CommonConstant;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;

/**
 * @author Barry
 * @date 2020-04-02
 */
public class I18nTranslateExcelUtil {
    public static void main(String[] args) {
        // 定义语言列表
        String[] languages = new String[]{"zh-CN", "en", "th"};

        // 假设这是从excel读取的内容对象
        List<I18nJsEntity> jsList = generateJsList(languages);

        // <语言, <组, <Key, Value>>>>
        Map<String, Map<String, Map<String, String>>> translateMap = new HashMap<>();
        for (I18nJsEntity entity : jsList) {
            Map<String, String> languageMap = entity.getValueMap();

            languageMap.forEach((language, translateValue) -> {
                if (!translateMap.containsKey(language)) {
                    translateMap.put(language, new HashMap<>());
                }
                if (!translateMap.get(language).containsKey(entity.getGroup())) {
                    translateMap.get(language).put(entity.getGroup(), new HashMap<>());
                }
                translateMap.get(language).get(entity.getGroup()).put(entity.getKey(), translateValue);
            });
        }

        List<I18nJsFile> fileList = new ArrayList<>();
        translateMap.forEach((language, groupMap) -> {
            // JS文件
            I18nJsFile jsFile = new I18nJsFile();
            jsFile.setLanguage(language);

            // 文件里面的项
            List<I18nJsItem> itemList = new ArrayList<>();

            groupMap.forEach((group, keyMap) -> {
                I18nJsItem groupItem = new I18nJsItem();
                groupItem.setKey(group);

                List<I18nJsItem> groupItemList = new ArrayList<>();

                Set<String> groupProcessKey = new HashSet<>();
                keyMap.forEach((key, value) -> {
                    if (!groupProcessKey.contains(key)) {
                        if (key.indexOf(CommonConstant.POINT_EN) > -1) {
                            // 多级
                            I18nJsItem item = resolveMultipleItem(new ArrayList<>(), key, keyMap, groupProcessKey);

                            groupItemList.add(item);
                        } else {
                            // 单级
                            I18nJsItem item = new I18nJsItem();
                            item.setKey(key);
                            item.setValue(value);
                            groupItemList.add(item);
                        }
                    }
                });

                groupItem.setSubItem(groupItemList);
                itemList.add(groupItem);
            });

            jsFile.setItemList(itemList);
            fileList.add(jsFile);
        });

        try {
            System.out.println(generateJsFile(fileList.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static I18nJsItem resolveMultipleItem(List<String> preGroups, String subKey, Map<String, String> groupKeyMap, Set<String> groupProcessKey) {
        // 组common
        String[] subKeyArr = StrUtil.splitToArray(subKey, CommonConstant.POINT_EN);
        final String subKeyGroup = subKeyArr[0];

        I18nJsItem item = new I18nJsItem();
        item.setKey(subKeyGroup);

        if (!CollectionUtil.anyMatch(preGroups, preGroup -> preGroup.equals(subKeyGroup))) {
            preGroups.add(subKeyGroup);
        }

        // 所有相似数据进行分组 autoRefresh这一组全部找出来
        // autoRefresh.status.status1=状态1
        // autoRefresh.status.status2=状态2
        // autoRefresh.status.status3=状态3
        // autoRefresh.open=开
        // autoRefresh.close=关
        String preGroupKey = CollectionUtil.join(preGroups, CommonConstant.POINT_EN) + CommonConstant.POINT_EN;
        Map<String, String> similarKeyMap = new HashMap<>(4);
        groupKeyMap.forEach((groupKey, groupKeyValue) -> {
            if (groupKey.indexOf(CommonConstant.POINT_EN) > -1 && groupKey.startsWith(preGroupKey)) {
                similarKeyMap.put(groupKey, groupKeyValue);
            }
        });

        // 找到该组下所有相同的key
        List<I18nJsItem> subItem = new ArrayList<>();
        similarKeyMap.forEach((groupKey, groupKeyValue) -> {
            if (!groupProcessKey.contains(groupKey)) {
                String groupKeyRestKey = groupKey;
                for (int i = 0; i < preGroups.size(); i++) {
                    groupKeyRestKey = StrUtil.subSuf(groupKeyRestKey, preGroups.get(i).length() + 1);
                }

                // 后面的Key还有没有多级
                I18nJsItem jsItem;
                if (groupKeyRestKey.indexOf(CommonConstant.POINT_EN) > -1) {
                    jsItem = resolveMultipleItem(preGroups, groupKeyRestKey, groupKeyMap, groupProcessKey);
                    subItem.add(jsItem);
                } else {

                    jsItem = new I18nJsItem();
                    String[] groupKeyArr = StrUtil.splitToArray(groupKey, CommonConstant.POINT_EN);
                    jsItem.setKey(groupKeyArr[groupKeyArr.length - 1]);
                    jsItem.setValue(groupKeyValue);
                    subItem.add(jsItem);
                    groupProcessKey.add(groupKey);
                }
            }
        });

        item.setSubItem(subItem);

        return item;
    }

    private static String generateJsFile(I18nJsFile jsFile) throws IOException {
        StringBuffer jsFileStr = new StringBuffer();
        jsFileStr.append("export default {\n");

        List<I18nJsItem> itemList = jsFile.getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            // 第1级
            I18nJsItem item = itemList.get(i);
            jsFileStr.append("\t").append(item.getKey()).append(": {\n");

            List<I18nJsItem> subItemList = item.getSubItem();
            for (int i1 = 0; i1 < subItemList.size(); i1++) {
                I18nJsItem subItem = subItemList.get(i1);

                if (CollectionUtil.isNotEmpty(subItem.getSubItem())) {
                    List<I18nJsItem> multipleSubItemList = subItem.getSubItem();
                    String multiple = generateMultipleSubItemList(2, subItem, multipleSubItemList);
                    jsFileStr.append(multiple);
                } else {
                    // 第2级
                    jsFileStr.append("\t\t").append(subItem.getKey()).append(": \'").append(subItem.getValue()).append("\'");
                }

                if (i1 < subItemList.size() - 1) {
                    jsFileStr.append(',');
                }
                jsFileStr.append('\n');
            }

            jsFileStr.append("\t}");
            if (i < itemList.size() - 1) {
                jsFileStr.append(',');
            }
            jsFileStr.append('\n');
        }

        jsFileStr.append("}");

        return jsFileStr.toString();
    }

    private static String generateMultipleSubItemList(int between, I18nJsItem subItem, List<I18nJsItem> multipleSubItemList) {
        StringBuffer jsFileStr = new StringBuffer();

        String betweenStr = StrUtil.repeat("\t", between);

        // 无限层级
        jsFileStr.append(betweenStr).append(subItem.getKey()).append(": {\n");

        for (int i = 0; i < multipleSubItemList.size(); i++) {
            I18nJsItem multipleSubItem = multipleSubItemList.get(i);

            if (CollectionUtil.isNotEmpty(multipleSubItem.getSubItem())) {
                jsFileStr.append(generateMultipleSubItemList((between + 1), multipleSubItem, multipleSubItem.getSubItem()));
            } else {
                String subBetweenStr = StrUtil.repeat("\t", between + 1);
                jsFileStr.append(subBetweenStr).append(multipleSubItem.getKey()).append(": \'").append(multipleSubItem.getValue()).append("\'");
            }
            if (i < multipleSubItemList.size() - 1) {
                jsFileStr.append(',');
            }
            jsFileStr.append('\n');
        }

        jsFileStr.append(betweenStr).append("}");

        return jsFileStr.toString();
    }

    private static List<I18nJsEntity> generateJsList(String[] languages) {
        List<I18nJsEntity> jsList = new ArrayList<>();

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("confirmTitle");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".确认");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("copySuccess");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".复制成功");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }


        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("autoRefresh.close");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".关闭自动刷新");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("autoRefresh.open");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".{second}秒自动刷新");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("autoRefresh.status.status1");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".状态1");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("autoRefresh.status.status2");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".状态2");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("autoRefresh.status.status3");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".状态3");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("issueTimeType.real");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".实时");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("common");
            js.setKey("issueTimeType.fixed");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".定时");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("button");
            js.setKey("confirm");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".确认");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("button");
            js.setKey("cancel");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".取消");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("lotteryOpenTimeConfig");
            js.setKey("openTimeTip");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".官彩（为预计官方开奖时间，程序无法控制，实际是封盘后只要有开奖号码系统立即开奖）；自营彩（为生成号码和计算注单并派奖的时间）");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("lotteryOpenTimeConfig");
            js.setKey("addDayFormat.yesterday");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".前1天");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("lotteryOpenTimeConfig");
            js.setKey("addDayFormat.today");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".当天");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }

        {
            I18nJsEntity js = new I18nJsEntity();
            js.setGroup("lotteryOpenTimeConfig");
            js.setKey("addDayFormat.secondDay");
            Map<String, String> valueMap = new HashMap<>();
            for (String language : languages) {
                valueMap.put(language, language + ".次日");
            }
            js.setValueMap(valueMap);
            jsList.add(js);
        }


        return jsList;
    }

    @Data
    @NoArgsConstructor
    public static class I18nJsEntity {
        private String group;
        private String key;

        // <zh, 翻译值>
        // <en, 翻译值>
        // <th, 翻译值>
        private Map<String, String> valueMap;
    }

    @Data
    @NoArgsConstructor
    public static class I18nJsFile {
        private String language;
        private List<I18nJsItem> itemList;
    }

    @Data
    @NoArgsConstructor
    public static class I18nJsItem {
        private String key;
        private String value;
        private List<I18nJsItem> subItem;
    }
}
