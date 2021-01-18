package richcoder.common.utils;

import java.math.BigInteger;

public class Ip2Number {

    public static long ipAllToNumber(String ip) {
        try {
            if (ip.contains(":")) {
                return ipv6ToNumber(ip).longValue();
            } else {
                return ipv4ToNumber(ip);
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static long ipv4ToNumber(String ipv4) {
        if (ipv4 == null || ipv4.trim().equals("")) {
            return 0;
        }
        String[] splits = ipv4.split("\\.");
        long l = 0;
        l = l + (Long.valueOf(splits[0], 10)) << 24;
        l = l + (Long.valueOf(splits[1], 10) << 16);
        l = l + (Long.valueOf(splits[2], 10) << 8);
        l = l + (Long.valueOf(splits[3], 10));
        return l;
    }

    public static String numberToIpv4(long l) {
        String ip = "";
        ip = ip + (l >>> 24);
        ip = ip + "." + ((0x00ffffff & l) >>> 16);
        ip = ip + "." + ((0x0000ffff & l) >>> 8);
        ip = ip + "." + (0x000000ff & l);
        return ip;
    }

    public static BigInteger ipv6ToNumber(String ipv6) {
        if (ipv6 == null || ipv6.equals("")) {
            return BigInteger.ZERO;
        }
        int compressIndex = ipv6.indexOf("::");
        if (compressIndex != -1) {
            String part1s = ipv6.substring(0, compressIndex);
            String part2s = ipv6.substring(compressIndex + 1);
            BigInteger part1 = ipv6ToNumber(part1s);
            BigInteger part2 = ipv6ToNumber(part2s);
            int part1hasDot = 0;
            char ch[] = part1s.toCharArray();
            for (char c : ch) {
                if (c == ':') {
                    part1hasDot++;
                }
            }
            return part1.shiftLeft(16 * (7 - part1hasDot)).add(part2);
        }

        String[] str = ipv6.split(":");
        BigInteger bigInteger = BigInteger.ZERO;
        for (int i = 0; i < str.length; i++) {
            if (str[i].isEmpty()) {
                str[i] = "0";
            }
            bigInteger = bigInteger.add(
                BigInteger.valueOf(Long.valueOf(str[i], 16)).shiftLeft(16 * (str.length - i - 1)));
        }
        return bigInteger;
    }

    public static String numberToIpv6(BigInteger bigInteger) {
        if (bigInteger == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        BigInteger ff = BigInteger.valueOf(0xffff);
        for (int i = 0; i < 8; i++) {
            str.insert(0, bigInteger.and(ff).toString(16) + ":");
            bigInteger = bigInteger.shiftRight(16);
        }
        str = new StringBuilder(str.substring(0, str.length() - 1));
        return str.toString().replaceFirst("(^|:)(0+(:|$)){2,8}", "::");
    }
}
