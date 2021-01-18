package richcoder.common.utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

/**
 * 对称加密解密
 */
@Slf4j
public class CryptUtils {

  private final static int GCM_IV_LENGTH = 12;
  private final static int GCM_TAG_LENGTH = 16;

  public static String encrypt(String key, String value) {
    try {
      SecretKey sk = new SecretKeySpec(Arrays.copyOfRange(key.getBytes(), 0, GCM_TAG_LENGTH),
          "AES");

      byte[] iv = new byte[GCM_IV_LENGTH];
      (new SecureRandom()).nextBytes(iv);
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      if (cipher == null) {
        return "";
      }
      GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
      cipher.init(Cipher.ENCRYPT_MODE, sk, ivSpec);
      byte[] cipherText = cipher.doFinal(value.getBytes("UTF8"));
      byte[] encrypted = new byte[iv.length + cipherText.length];
      System.arraycopy(iv, 0, encrypted, 0, iv.length);
      System.arraycopy(cipherText, 0, encrypted, iv.length, cipherText.length);

      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      log.error("CryptUtils encrypt  error : {}", e.getCause());
    }
    return "";
  }

  public static String decrypt(String key, String value) {
    try {
      SecretKey sk = new SecretKeySpec(Arrays.copyOfRange(key.getBytes(), 0, GCM_TAG_LENGTH),
          "AES");

      byte[] decoded = Base64.getDecoder().decode(value);
      byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      if (cipher == null) {
        return "";
      }
      GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
      cipher.init(Cipher.DECRYPT_MODE, sk, ivSpec);
      byte[] cipherText = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

      return new String(cipherText, "UTF8");
    } catch (Exception e) {
      log.error("CryptUtils encrypt  error : {}", e.getCause());
    }
    return "";
  }
}
