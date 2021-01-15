package com.llj.mybatis.config;

import org.apache.commons.lang3.StringUtils;

/**
 * Title: <br/> Create Date: 2015/11/27 23:04 <br/> author: llj <br/>
 */
public class PackageConfigType {

  /**
   * 别名
   */
  private String aliasType;
  /**
   * 生成目标文件夹
   */
  private String targetDir;

  /**
   * 生成文件后缀
   */
  private String fileNameSuffix = ".java";

  /**
   * 生成文件模板
   */
  private String template;

  public String getTargetDir() {
    return targetDir;
  }

  public void setTargetDir(String targetDir) {
    this.targetDir = targetDir;
  }

  public String getFileNameSuffix() {
    return fileNameSuffix;
  }

  public void setFileNameSuffix(String fileNameSuffix) {
    this.fileNameSuffix =
        StringUtils.isNotBlank(fileNameSuffix) ? fileNameSuffix : this.fileNameSuffix;
  }

  public String getFileNameSuffixAndNoFormat() {
    if (StringUtils.isBlank(fileNameSuffix)) {
      return "";
    }
    return StringUtils.substring(fileNameSuffix, 0, fileNameSuffix.indexOf("."));
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getAliasType() {
    return aliasType;
  }

  public void setAliasType(String aliasType) {
    this.aliasType = aliasType;
  }
}
