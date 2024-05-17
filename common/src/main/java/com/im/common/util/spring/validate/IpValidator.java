package com.im.common.util.spring.validate;

import cn.hutool.core.util.StrUtil;
import com.im.common.constant.CommonConstant;
import com.im.common.exception.CommonException;
import com.im.common.util.ip.IpAddressUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 实现{@link IpValidate}逻辑，不需要定义为Spring Bean，Spring会自动扫描，可以在类中直接注入Spring类
 *
 * @author Barry
 * @date 2019/2/15
 */
public class IpValidator implements ConstraintValidator<IpValidate, String> {
    private IpValidate annotation;
    private String message;

    @Override
    public void initialize(IpValidate constraintAnnotation) {
        this.annotation = constraintAnnotation;
        if (StrUtil.isBlank(annotation.message())) {
            this.message = "RSP_MSG.IP_FORMATTING_INCORRECT#I18N";
        } else {
            this.message = annotation.message();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String ip = value;

        if (StrUtil.isBlank(ip)) {
            if (annotation.required()) {
                throw new CommonException(message, new Object[]{ip});
            }
            return true;
        } else {
            String[] ipArr = annotation.multiple() ? StrUtil.splitToArray(ip, CommonConstant.DOT_EN) : new String[]{ip};

            for (String singleIp : ipArr) {
                boolean legal = false;
                // IPV4
                if (legal == false && annotation.supportIpv4() && IpAddressUtil.isIpv4(singleIp)) {
                    legal = true;
                }
                // IPV4掩码
                if (legal == false && annotation.supportIpv4Mask() && IpAddressUtil.isIpv4Mask(singleIp)) {
                    legal = true;
                }
                // IPV4段
                if (legal == false && annotation.supportIpv4Range() && IpAddressUtil.isIpv4Range(singleIp)) {
                    legal = true;
                }
                // IPV6
                if (legal == false && annotation.supportIpv6() && IpAddressUtil.isIpv6(singleIp)) {
                    legal = true;
                }

                if (!legal) {
                    throw new CommonException(message, new Object[]{singleIp});
                }
            }

            return true;
        }
    }
}