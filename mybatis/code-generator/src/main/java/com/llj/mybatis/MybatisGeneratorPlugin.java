package com.llj.mybatis;

import com.llj.mybatis.config.GeneratorConfigurer;
import com.llj.mybatis.generator.base.GeneratorFactoryImpl;
import java.net.URL;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @goal create
 */
public class MybatisGeneratorPlugin extends AbstractMojo {

  /**
   * 打印MybatisGeneratorPlugin.java日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(MybatisGeneratorPlugin.class);
  /**
   * 上下文
   */
  private static ApplicationContext context;

  static {
    try {
      String filePath = System.getProperties().getProperty("user.dir");
      context = new FileSystemXmlApplicationContext(
          filePath + System.getProperty("file.separator") + GeneratorConfigurer.SPRING_CONFIG);
      LOGGER.info("加载配置文件:" + filePath + System.getProperty("file.separator")
          + GeneratorConfigurer.SPRING_CONFIG);
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      if (context == null) {
        URL url = Thread.currentThread().getContextClassLoader()
            .getResource(GeneratorConfigurer.SPRING_CONFIG);
        if (url == null) {
          LOGGER.info("配置文件不再类资源文件路径下:" + GeneratorConfigurer.SPRING_CONFIG);
        } else {
          String filePath = url.getPath();
          context = new ClassPathXmlApplicationContext(GeneratorConfigurer.SPRING_CONFIG);
          LOGGER.info("加载配置文件:" + filePath);
        }
      }
    }
  }

  public void execute() throws MojoExecutionException, MojoFailureException {
    GeneratorFactoryImpl generatorFactory = (GeneratorFactoryImpl) context
        .getBean("generatorFactory");
    generatorFactory.pluginGeneratorStarter();
    LOGGER.info("auto plugin Generator code finish...");
  }
}
