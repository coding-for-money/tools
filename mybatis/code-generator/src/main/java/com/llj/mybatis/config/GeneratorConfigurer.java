package com.llj.mybatis.config;

import java.util.Properties;

/**
 * 功能描述：自动化生成配置接口
 */
public interface GeneratorConfigurer {

  /**
   * 注释
   */
  String GENERATOR_ANNOTATION = "false";

  /**
   * 代码生成包
   */
  String GENERATOR_BASE_PACKAGE = "com.llj";

  /**
   * 生成代码存放位置
   */
  String GENERATOR_LOCATION = "llj";

  /**
   * 所属系统项目
   */
  String GENERATOR_PROJECT_NAME = "llj";

  /**
   * 过滤表名前缀
   */
  String GENERATOR_TABLE_PREFIX = "";

  /**
   * 强类型转换，数据库float转换为BigDecimal，不配置转换为Double
   */
  String GENERATOR_PRECISION = "high";

  /**
   * 默认配置文件名称
   */
  String LOCAL_GENERATOR_PATH = "config-generator.properties";

  /**
   * project下配置的内容
   */
  String CONFIG_GENERATOR_PATH = "generator.properties";

  /**
   * Spring配置文件
   */
  String SPRING_CONFIG = "spring-generator.xml";

  /**
   * JAVA文件生成的所在包文件
   */
  String JAVA_SRC = "java";

  /**
   * 获取配置文件配置集
   *
   * @return 配置文件配置集
   */
  Properties getProperties();

  /**
   * 初始化默认参数
   */
  void initConfigParams();

}
