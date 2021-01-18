package richcoder.common.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * 随机字符生成工具
 */
@Slf4j
public class RandomCharsUtils {

    private static Random rand;

    static {
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.error("RandomCharsUtils error : " + e.getMessage());
        }
    }

    public static String generateUUid() {

        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateSerialNum() {
        return System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateSerialNumUniq() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String ranEight = String.format("%08d", new Random().nextInt(99999999));
        return System.currentTimeMillis() + uuid + ranEight;
    }


    public static String generateRandomStr(int len) {
        final int maxNum = 62;
        StringBuilder sb = new StringBuilder();
        int rdGet;//取得随机数
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int count = 0;
        while (count < len) {
            rdGet = Math.abs(rand.nextInt(maxNum));//生成的数最大为62-1
            if (rdGet >= 0 && rdGet < str.length) {
                sb.append(str[rdGet]);
                count++;
            }
        }
        return sb.toString();
    }

}
