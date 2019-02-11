package com.yc.common.utils;

public class StringUtil {
    /**
     * <pre>
     * StringUtils.isBlank("null")    = true
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if ("null".equals(str) || str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <pre>
     * StringUtils.isBlank("null")    = false
     * StringUtils.isBlank(null)      = false
     * StringUtils.isBlank("")        = false
     * StringUtils.isBlank(" ")       = false
     * StringUtils.isBlank("bob")     = true
     * StringUtils.isBlank("  bob  ") = true
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

}
