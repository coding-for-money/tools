package com.llj.mybatis.config;

/**
 * 功能描述：获取配置类工厂
 */
public class GeneratorConfigurerFactory {

  /**
   * 自动化生成代码配置器
   */
  private static GeneratorConfigurer generatorConfigurer;

  /**
   * 获取配置
   *
   * @return
   */
  public synchronized static GeneratorConfigurer getGeneratorConfigurer() {
    if (null == generatorConfigurer) {
      generatorConfigurer = new DefaultGeneratorConfigurer();
    }
    return generatorConfigurer;
  }

}
