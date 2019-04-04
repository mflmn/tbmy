package org.thingsboard.server.transport.goldcard.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author rym
 */
public class StringUtil extends StringUtils {
    public static String balance(String hex) {
        String preStr = "00";
        if (hex.contains("-")) {
            preStr = "01";
            hex = hex.replace("-", "").substring(hex.indexOf("-"));
        }
        if (hex.contains(".")) {
            String balanceInt = hex.substring(0, hex.indexOf("."));
            balanceInt = leftPad(balanceInt, 8, "0");//左补0 按照指定的长度
            String balanceFloat = hex.substring(hex.indexOf(".") + 1);
            if (balanceFloat.length() > 2) {
                balanceFloat = balanceFloat.substring(0, 2);
            }
            balanceFloat = rightPad(balanceFloat, 2, "0");//后补0 按照指定的长度
            return preStr + balanceInt + balanceFloat;
        } else {
            return preStr + leftPad(hex, 8, "0") + "00";
        }
    }

}
