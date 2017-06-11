package com.xtagwgj.baseproject.utils;

import com.xtagwgj.baseproject.constant.RegexConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具
 * Created by zy on 2016/6/30.
 */
public class RegexUtil extends RegexConstants {
    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               中国移动：134（不含1349）、135、136、137、138、139、147、150、151、152、157、158、 159、182、183、184、187、188、178
     *               中国联通：130、131、132、145（上网卡）、155、156、185、186、176、175
     *               中国电信：133、1349（卫星通信）、153、180、181、189、177、173、149
     *               4G号段：176/175(联通)、173/177(电信)、178(移动)
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkCellPhone(String mobile) {
        return Pattern.matches(REGEX_MOBILE_EXACT, mobile);
    }

    /**
     * @param idCard 15位到18位 身份证验证
     * @return
     */
    public static boolean checkIdCard(String idCard) {
        if (StringUtils.isEmpty(idCard))
            return false;

        if (idCard.length() == 15) {
            return Pattern.matches(REGEX_ID_CARD15, idCard);
        } else
            return Pattern.matches(REGEX_ID_CARD18, idCard);
    }

    /**
     * 验证号码 手机号 固话均可
     *
     * @param mobile phoneNumber
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhoneNumber(String mobile) {
        String regex = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}d{1}-?d{8}$)|"
                + "(^0[3-9] {1}d{2}-?d{7,8}$)|"
                + "(^0[1,2]{1}d{1}-?d{8}-(d{1,4})$)|"
                + "(^0[3-9]{1}d{2}-? d{7,8}-(d{1,4})$))";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 从短信字符窜提取验证码
     *
     * @param body       短信内容
     * @param codeLength 验证码的长度 一般6位或者4位
     * @return 接取出来的验证码
     */
    public static String getEcode(String body, int codeLength) {

        if (StringUtils.isEmpty(body))
            return "";

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{" + codeLength + "}");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 匹配非表情符号
     *
     * @param str
     * @return
     */
    public static boolean matchNotEmojo(String str) {
        String reg = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }
}
