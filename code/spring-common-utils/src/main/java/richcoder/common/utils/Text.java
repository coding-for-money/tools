package richcoder.common.utils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Text {

  /**
   * Trim，如果trim后为空串，则变为null
   */
  public static final int TRIM_TO_NULL = 1;

  /**
   * Trim为空串
   */
  public static final int TRIM_TO_BLANK = 2;
  public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
  public static final String FORMAT_DATE = "yyyy-MM-dd";
  public static final String FORMAT_TIME = "HH:mm:ss";
  private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
  private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
      "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyyMMdd"};

  private Text() {
  }

  /**
   * 将日期（时间）格式化成指定格式的字符串
   *
   * @param date    日期(时间)对象
   * @param pattern 格式化模式
   * @return
   */
  public static String formatTime(Date date, String pattern) {
    return new SimpleDateFormat(pattern).format(date);
  }


  /**
   * 使用单个字符分割字符串
   *
   * @param value
   * @param delim
   * @return
   */
  public static String[] splitArray(String value, char delim) {
    List<String> res = split(value, delim);

    String[] ret = res.toArray(new String[res.size()]);
    for (int i = 0; i < ret.length; ++i) {
      ret[i] = trimEmpty(ret[i]);
    }
    return ret;
  }

  /**
   * @param value 源字符串
   * @param delim 分隔符
   * @return
   */
  public static List<String> split(String value, char delim) {
    int end = value.length();
    List<String> res = new ArrayList<>();

    int start = 0;
    for (int i = 0; i < end; i++) {
      if (value.charAt(i) == delim) {
        if (start == i) {
          res.add("");
        } else {
          res.add(value.substring(start, i));
        }
        start = i + 1;
      }
    }

    if (start == 0) {
      res.add(value);
    } else if (start != end) {
      res.add(value.substring(start, end));
    } else {
      for (int i = res.size() - 1; i >= 0; i--) {
        if (res.get(i).isEmpty()) {
          res.remove(i);
        } else {
          break;
        }
      }
    }

    return res;
  }

  /**
   * 对字符串进行MD5编码后转为16进制字符串
   *
   * @param src  源字符串
   * @param salt 混淆字符串
   * @return
   */
  public static String md5(String src, String salt) {
    byte[] md5s = digest("MD5", src, salt);
    if (md5s == null) {
      return "";
    }
    return toHexString(md5s);
  }

  /**
   * 对字符串进行SHA-256编码后转为16进制字符串
   *
   * @param src  源字符串
   * @param salt 混淆字符串
   * @return
   */
  public static String sha256(String src, String salt) {
    byte[] digest = digest("SHA-256", src, salt);
    if (digest == null) {
      return "";
    }
    return toHexString(digest);
  }

  /**
   * 合并两个字节数组
   *
   * @param x 第一个字节数组
   * @param y 第二个字节数组
   * @return
   */
  public static byte[] merge(byte[] x, byte[] y) {
    byte[] b = new byte[x.length + y.length];

    System.arraycopy(x, 0, b, 0, x.length);
    System.arraycopy(y, 0, b, x.length, y.length);

    return b;
  }

  /**
   * 转换字节数组为16进制字串
   *
   * @param b 字节数组
   * @return 16进制字串
   */
  public static String toHexString(byte[] b) {
    StringBuilder buf = new StringBuilder(b.length * 2);
    for (byte n : b) {
      int v = n < 0 ? n + 256 : n;
      buf.append(HEX_DIGITS[v >> 4]).append(HEX_DIGITS[v % 16]);
    }
    return buf.toString();
  }

  public static boolean isBlank(CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }

    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }

    return true;
  }

  public static boolean isBlank(CharSequence cs, int end) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }

    if (end > 0 && end < strLen) {
      strLen = end;
    }

    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isNotBlank(CharSequence cs) {
    return !isBlank(cs);
  }

  public static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  public static boolean startsWith(String s, char c) {
    return s.length() != 0 && s.charAt(0) == c;
  }

  public static boolean endsWith(String s, char c) {
    return s.length() != 0 && s.charAt(s.length() - 1) == c;
  }

  public static String trim(String cs) {
    return cs == null ? null : cs.trim();
  }

  public static String trimNull(String cs) {
    String s = trim(cs);
    return isEmpty(s) ? null : s;
  }

  public static String trimEmpty(String cs) {
    String s = trim(cs);
    return s == null ? "" : s;
  }

  public static String trimToFlag(String src, String flag) {
    int index = src.indexOf(flag);
    if (index == -1) {
      return src;
    }

    return src.substring(0, index);
  }

  /**
   * 字符串为null，或者长度等于指定长度，则返回true
   *
   * @param s      字符串
   * @param length 长度
   */
  public static boolean validLength(String s, int length) {
    if (s == null) {
      return true;
    }
    if (s.length() == length) {
      return true;
    }
    return false;
  }

  /**
   * 字符串为null，或者字符串长度在min和max范围内（包含），则返回true
   *
   * @param s   字符串
   * @param min 最小
   * @param max 最大
   */
  public static boolean validLength(String s, int min, int max) {
    if (s == null) {
      return true;
    }
    if (s.length() >= min && s.length() <= max) {
      return true;
    }
    return false;
  }

  private static byte[] digest(String algorithm, String src, String salt) {
    byte[] a = src.getBytes();

    try {
      if (salt != null && salt.length() > 0) {
        a = merge(a, salt.getBytes());
      }
      return MessageDigest.getInstance(algorithm).digest(a);
    } catch (Exception e) {
      return null;
    }
  }

  public static String upperCase(String s) {
    if (isBlank(s)) {
      return "";
    }
    return s.toUpperCase();
  }

  public static String toHtmlEntity(String text) {
    if (Text.isBlank(text)) {
      return "";
    }

    text = text
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#x27;")
        .replace("/", "&#x2f;");

    return text;
  }

  public static String replaceEmoji(String str) {
    String patternString = "[\uD83C\uDC04-\uD83C\uDE1A]|[\uD83D\uDC66-\uD83D\uDC69]|[\uD83D\uDC66\uD83C\uDFFB-\uD83D\uDC69\uD83C\uDFFF]|[\uD83D\uDE45\uD83C\uDFFB-\uD83D\uDE4F\uD83C\uDFFF]|[\uD83C\uDC00-\uD83D\uDFFF]|[\uD83E\uDD10-\uD83E\uDDC0]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEF6]";
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(str);
    return matcher.replaceAll("");
  }

  public static String get32UUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * 导出使用,判断为空返回 空字符串
   */
  public static String dataToString(Object o) {
    if (o == null) {
      return "";
    }
    if (o instanceof BigDecimal) {
      return ((BigDecimal) o).stripTrailingZeros().toPlainString();
    } else {
      return o.toString();
    }
  }

  /**
   * BigDecimal stripTrailingZeros toPlainString
   */
  public static String toStr(BigDecimal num) {
    if (num == null) {
      return "";
    }
    return num.stripTrailingZeros().toPlainString();
  }

  /**
   * 导出csv时，string类型增加引号，防止数据中有逗号，会产生串列的问题
   */
  public static String toCsvStr(String data) {
    if (isBlank(data)) {
      return "";
    }
    return String.format("\"%s\"", data);
  }
}
