package com.xtagwgj.baseproject.utils;

import android.util.Log;

import com.xtagwgj.baseproject.constant.RegexConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具
 * Created by zy on 2016/6/30.
 */
public class RegexUtil extends RegexConstants {

    private RegexUtil() {
        super();
    }

    public static boolean valid(String regex, String text) {
        return !StringUtils.isEmpty(text) && Pattern.matches(regex, text);
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        return valid(REGEX_EMAIL, email);
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
        return valid(REGEX_MOBILE_EXACT, mobile);
    }

    /**
     * @param idCard 15位到18位 身份证验证
     * @return 是否是身份证号
     * 被IDCardValidate替换
     */
    @Deprecated
    public static boolean checkIdCard(String idCard) {
        if (StringUtils.isEmpty(idCard))
            return false;

        if (idCard.length() == 15) {
            return valid(REGEX_ID_CARD15, idCard);
        } else
            return idCard.length() == 18 && valid(REGEX_ID_CARD18, idCard);
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
        return valid(reg, str);
    }

    /**
     * 检查用户名是否合法
     *
     * @param name 用户名
     * @return true合法 false不合法
     */
    public static boolean checkUserNameValid(String name) {
        return valid(REGEX_USERNAME, name);
    }

    /**
     * 是否全是汉字
     *
     * @param text 文本
     */
    public static boolean isChinese(String text) {
        return valid(REGEX_ZH, text);
    }

    public static boolean checkZipCode(String code) {
        return valid(REGEX_ZIP_CODE, code);
    }

    /**
     * 判断是否全是数字
     *
     * @param str 字符串
     * @return true 全数字 false不是全部为数字
     */
    public static boolean isNumeric(String str) {
        return valid(REGEX_NUMERIC, str);
    }


    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str 时间字符串
     * @return true符合YYYY-MM-DD格式
     */
    public static boolean isDataFormat(String str) {
        return valid(REGEX_DATE, str);
    }

    /**
     * 验证是否是ip得知
     *
     * @param ipStr ip字符串
     */
    public static boolean isIp(String ipStr) {
        return valid(REGEX_IP, ipStr);
    }

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException 解析错误
     */
    public static boolean IDCardValidate(String IDStr) throws ParseException {
        String errorInfo;// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            Log.e("FormatUtil", "ID:" + "errorInfo=" + errorInfo);
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后一位都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (!isNumeric(Ai)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            Log.e("FormatUtil", "ID:" + "errorInfo=" + errorInfo);
            return false;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (!isDataFormat(strYear + "-" + strMonth + "-" + strDay)) {
            errorInfo = "身份证生日无效。";
            Log.e("FormatUtil", "ID:" + "errorInfo=" + errorInfo);
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                Log.e("FormatUtil", "ID:" + "errorInfo=" + errorInfo);
                return false;
            }
        } catch (NumberFormatException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            Log.e("FormatUtil", "ID:" + "errorInfo=" + errorInfo);
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            Log.e("FormatUtil", "ID:" + "errorInfo=" + errorInfo);
            return false;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            Log.e("FormatUtil", "ID:" + "errorInfo=" + errorInfo);
            return false;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (!Ai.equals(IDStr)) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                Log.e("FormatUtil", "ID:" + "errorInfo=" + errorInfo);
                return false;
            }
        } else {
            return true;
        }
        // =====================(end)=====================

        return true;
    }


    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }
}
