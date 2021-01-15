package com.llj.mybatis.utils;

import java.util.Properties;
import org.apache.commons.lang.StringUtils;

public class GeneratorStringUtils {

  private GeneratorStringUtils() {

  }

  /**
   * format database type into java type, eg format "card_type" into "cardType"
   *
   * @param string
   * @return
   */
  public static String format(String string, Properties properties) {
    String columnPrefix = PropertiesUtils.getColumnPrefix(properties);
    if (StringUtils.isNotBlank(columnPrefix)) {
      if (StringUtils.startsWith(string, columnPrefix)) {
        string = string.replaceFirst(columnPrefix, "");
      }
    }
    if (!StringUtils.contains(string, "_")) {
      return string != null ? string.toLowerCase() : null;
    }

    StringBuilder sb = new StringBuilder();
    char[] cArr = string.trim().toLowerCase().toCharArray();
    for (int i = 0; i < cArr.length; i++) {
      char c = cArr[i];
      if (c == '_') {
        i++;
        sb.append(Character.toUpperCase(cArr[i]));
      } else {
        sb.append(c);
      }
    }

    return sb.toString();
  }

  public static String formatAndNoPrefix(String tableName, Properties properties) {
    String noPrefixTableName = tableName.toLowerCase()
        .replaceFirst(PropertiesUtils.getTablePrefix(properties), "")
        .replaceFirst(PropertiesUtils.getTableSuffix(properties), "");
    noPrefixTableName = format(noPrefixTableName, properties);
    return noPrefixTableName;
  }

  public static String firstUpper(String string, Properties properties) {
    String str = format(string, properties);
    str = str.substring(0, 1).toUpperCase() + str.substring(1);
    return str;
  }

  public static String firstUpperAndNoPrefix(String tableName, Properties properties) {
    String noPrefixTableName = tableName.toLowerCase()
        .replaceFirst(PropertiesUtils.getTablePrefix(properties), "")
        .replaceFirst(PropertiesUtils.getTableSuffix(properties), "");
    noPrefixTableName = firstUpper(noPrefixTableName, properties);
    return noPrefixTableName;
  }

  public static String firstUpperNoFormat(String string) {
    String str = string.substring(0, 1).toUpperCase() + string.substring(1);
    return str;
  }
}
