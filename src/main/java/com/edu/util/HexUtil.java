package com.edu.util;

/**
 * @Author: tangzh
 * @Date: 2019/5/16$ 7:41 PM$
 **/
public class HexUtil {
    final static char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '+', '/',
    };

    public static String _10_to_64(String num) {
        return _10_to_64(Long.parseLong(num));
    }

    public static String _10_to_64(long num) {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << 6;
        long mask = radix - 1;
        do {
            buf[--charPos] = digits[(int) (num & mask)];
            num >>>= 6;
        } while (num != 0);
        return new String(buf, charPos, (64 - charPos));
    }

    public static long _64_to_10(String str) {
        long result = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (i == str.length() - 1) {
                result += getCharIndexNum(str.charAt(i));
                continue;
            }
            for (int j = 0; j < digits.length; j++) {
                if (str.charAt(i) == digits[j]) {
                    result += ((long) j) << 6 * (str.length() - 1 - i);
                }
            }
        }
        return result;
    }

    private static long getCharIndexNum(char ch) {
        int num = ((int) ch);
        if (num >= 48 && num <= 57) {
            return num - 48;
        } else if (num >= 97 && num <= 122) {
            return num - 87;
        } else if (num >= 65 && num <= 90) {
            return num - 29;
        } else if (num == 43) {
            return 62;
        } else if (num == 47) {
            return 63;
        }
        return 0;
    }
}
