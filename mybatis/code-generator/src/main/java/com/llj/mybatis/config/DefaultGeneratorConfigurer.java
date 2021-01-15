package com.llj.mybatis.config;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 功能描述：基本的配置类实现
 */
public class DefaultGeneratorConfigurer implements GeneratorConfigurer {

  /*private static ConcurrentHashMap<String, Properties> propsMap = new ConcurrentHashMap();*/

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGeneratorConfigurer.class);
  private static Properties properties;

  private static List<String> listProjectDirAllFiles(String projectPath) {
    List<String> fileNames = Lists.newArrayList();
    Vector<String> vector = new Vector<>();
    vector.add(projectPath);
    while (vector.size() > 0) {
      File[] files = new File(vector.get(0)).listFiles();
      vector.remove(0);
      int len = files.length;
      for (int i = 0; i < len; i++) {
        String tmpDir = files[i].getAbsolutePath();
        if (files[i].isDirectory()) {
          vector.add(tmpDir);
        } else if (tmpDir.endsWith(".properties") && !tmpDir.contains("class")) {
          fileNames.add(tmpDir);
        }
      }
    }
    return fileNames;
  }

  public Properties getProperties() {
    if (null == properties) {
      loadProperties();
    }
    return properties;
  }

  public void initConfigParams() {
    initPackage();
    initProjectName();
    initTablePrefix();
    initPrecision();
    initLayers();
    initLocation();
    initJavaSrc();
    initAnnotation();
  }

  private void initAnnotation() {
    String value = (String) properties.get("generator.annotation");
    if (StringUtils.isNotBlank(value)) {
      return;
    }
    properties.setProperty("generator.annotation", GENERATOR_ANNOTATION);
  }

  protected void initPackage() {
    String value = (String) properties.get("generator.basePackage");
    if (StringUtils.isNotBlank(value)) {
      return;
    }
    properties.setProperty("generator.basePackage", GENERATOR_BASE_PACKAGE);
  }

  protected void initProjectName() {
    String value = (String) properties.get("generator.project.name");
    if (StringUtils.isNotBlank(value)) {
      return;
    }
    properties.setProperty("generator.project.name", GENERATOR_PROJECT_NAME);
  }

  protected void initTablePrefix() {
    String value = (String) properties.get("generator.table.prefix");
    if (StringUtils.isNotBlank(value)) {
      return;
    }
    properties.setProperty("generator.table.prefix", GENERATOR_TABLE_PREFIX);
  }

  protected void initPrecision() {
    String value = (String) properties.get("generator.precision");
    if (StringUtils.isNotBlank(value)) {
      return;
    }
    properties.setProperty("generator.precision", GENERATOR_PRECISION);
  }

  protected void initLayers() {
    String value = (String) properties.get("generator.layers");
    if (StringUtils.isNotBlank(value)) {
      properties.setProperty("generator.layers", value);
    } else {
      properties.setProperty("generator.layers", "");
    }
  }

  protected void initLocation() {
    String value = (String) properties.get("generator.location");
    if (StringUtils.isNotBlank(value)) {
      return;
    }
    properties.setProperty("generator.location", GENERATOR_LOCATION);
  }

  protected void initJavaSrc() {
    String value = (String) properties.get("java.src");
    if (StringUtils.isNotBlank(value)) {
      return;
    }
    properties.setProperty("java.src", JAVA_SRC);
  }

  protected void loadProperties() {
    String filePath = System.getProperties().getProperty("user.dir");
    List<String> dirAllFiles = listProjectDirAllFiles(filePath);
    if (CollectionUtils.isEmpty(dirAllFiles)) {
      throw new RuntimeException("读取工程目录下的文件为空.");
    }

    String configFilePath = null;
    for (String dirAllFile : dirAllFiles) {
      if (dirAllFile.endsWith(CONFIG_GENERATOR_PATH)) {
        configFilePath = dirAllFile;
        break;
      }
    }

    DefaultGeneratorConfigurer.properties = new Properties();
    InputStream input = null;
    try {
      LOGGER.info("加载配置文件:" + configFilePath);
      input = new FileInputStream(configFilePath);
      DefaultGeneratorConfigurer.properties.load(input);
    } catch (Exception e) {
      LOGGER.error("加载配置文件出现异常，读取默认配置:" + LOCAL_GENERATOR_PATH);
      try {
        DefaultGeneratorConfigurer.properties = PropertiesLoaderUtils
            .loadAllProperties(LOCAL_GENERATOR_PATH);
      } catch (IOException ex) {
        throw new RuntimeException("读取配置文件失败.", e);
      }
    } finally {
      IOUtils.closeQuietly(input);
    }
  }
}
