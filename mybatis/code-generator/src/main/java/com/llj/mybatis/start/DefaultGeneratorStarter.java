package com.llj.mybatis.start;

import com.llj.mybatis.config.GeneratorConfigurer;
import com.llj.mybatis.config.GeneratorConfigurerFactory;
import com.llj.mybatis.config.PackageConfigTypes;
import com.llj.mybatis.connect.Connector;
import com.llj.mybatis.connect.MysqlConnector;
import com.llj.mybatis.generator.Generator;
import com.llj.mybatis.generator.context.GeneratorContext;
import com.llj.mybatis.utils.GeneratorFileUtils;
import com.llj.mybatis.utils.GeneratorStringUtils;
import com.llj.mybatis.utils.PropertiesUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 功能描述：自动化生成代码默认执行实现
 */
public class DefaultGeneratorStarter implements GeneratorStarter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGeneratorStarter.class);

  /**
   * 读取配置
   */
  private static Properties properties;

  /**
   * 读取数据连接
   */
  private static Connector connector;

  static {
    GeneratorConfigurer generatorConfigurer = GeneratorConfigurerFactory.getGeneratorConfigurer();
    properties = generatorConfigurer.getProperties();
    generatorConfigurer.initConfigParams();
    connector = new MysqlConnector(properties);
  }

  /**
   * 执行生成方法
   */
  public void start(Set<Generator> generatorSet) {
    try {
      generator(generatorSet);
    } catch (Exception e) {
      throw new RuntimeException("启动创建代码工具出现异常", e);
    }
  }

  /**
   * 自动化创建代码文件
   */
  protected void generator(Set<Generator> generatorSet) {
    LOGGER.info("**********代码生成工具,开始自动生成代码**********");

    List<String> tableNames = PropertiesUtils.getTableList(properties);
    if (CollectionUtils.isEmpty(tableNames)) {
      String tablePrefix = PropertiesUtils.getTablePrefix(properties);
      if (!StringUtils.isBlank(tablePrefix)) {
        List<String> tableList = connector.listTablesByTablePrefix(tablePrefix);
        if (CollectionUtils.isEmpty(tableList)) {
          throw new RuntimeException("无法获取配置代码中表前缀的数据表，请检查配置配置表前缀.");
        }
        tableNames.addAll(tableList);
      }
    }
    if (CollectionUtils.isEmpty(tableNames)) {
      throw new RuntimeException("配置代码表配置或者表前缀配置无法获取数据库表，请检查表配置和表前缀配置.");
    }

    Set<PackageConfigTypes> packageConfigTypesHashSet = new HashSet<>();
    for (Generator generator : generatorSet) {
      packageConfigTypesHashSet.add(generator.getPackageConfigTypes());
    }
    Map<String, String> allPackageNameMap = GeneratorFileUtils.getAllPackageName(properties,
        packageConfigTypesHashSet);
    for (Map.Entry<String, String> entry : allPackageNameMap.entrySet()) {
      LOGGER.info("**********包名映射[键:{} , 值:{}]",
          entry.getKey(),
          entry.getValue());
    }

    Map<String, String> packageFileSuffixMap = GeneratorFileUtils
        .getAllPackageFileSuffix(packageConfigTypesHashSet);
    for (Map.Entry<String, String> entry : packageFileSuffixMap.entrySet()) {
      LOGGER.info("**********包名对应文件后缀映射[键:{} , 值:{}]", entry.getKey(), entry.getValue());
    }

    // 自动化生成文件
    Map<String, String> allTypeAliasesMap = new TreeMap<>();
    Map<String, String> allMappersMap = new TreeMap<>();
    for (String tableName : tableNames) {
      LOGGER.info("**********代码生成工具,开始自动生成代码>>>{}", tableName);
      Map<String, String> primaryKeyMap = connector.getPrimaryKey(tableName);
      if (StringUtils.isBlank(primaryKeyMap.get("primaryKey"))) {
        throw new RuntimeException(tableName + " 表结构没有主键,请检查表结构,生成代码失败.");
      }
      // 要生成的模块分层
      String layerConfig = PropertiesUtils.getLayers(properties);
      String[] layers = StringUtils.split(layerConfig, ",");
      if (ArrayUtils.isEmpty(layers)) {
        throw new RuntimeException(tableName + " 读取配置文件分层结构为空,请检查配置是否按照逗号隔开.");
      }
      Set<String> typeSet = new HashSet<>(Arrays.asList(layers));
      LOGGER.info("**********代码生成工具,开始自动生成代码>>>{}", layerConfig);
      Set<PackageConfigTypes> packageConfigTypesSet = new HashSet<>();
      for (Generator generator : generatorSet) {
        if (typeSet.contains(generator.getPackageConfigTypes().getType().key)) {
          packageConfigTypesSet.add(generator.getPackageConfigTypes());
        }
      }

      // 创建目录
      GeneratorFileUtils.createPackageDirectory(properties, packageConfigTypesSet);

      Map<String, String> typeAliasesMap = GeneratorFileUtils.getAllTypeAliasesMap(tableName,
          properties,
          packageConfigTypesHashSet);

      for (Map.Entry<String, String> entry : typeAliasesMap.entrySet()) {
        LOGGER.info("**********MyBatis别名映射[键:{} , 值:{}]",
            entry.getKey(),
            entry.getValue());
      }
      Map<String, String> mappersMap = GeneratorFileUtils.getAllMappersMap(tableName,
          properties,
          packageConfigTypesHashSet);
      for (Map.Entry<String, String> entry : mappersMap.entrySet()) {
        LOGGER.info("**********MyBatis Mapper文件映射[键:{} , 值:{}]",
            entry.getKey(),
            entry.getValue());
      }
      allTypeAliasesMap.putAll(typeAliasesMap);
      allMappersMap.putAll(mappersMap);

      for (Generator generator : generatorSet) {
        PackageConfigTypes packageConfigTypes = generator.getPackageConfigTypes();
        if (!packageConfigTypesSet.contains(packageConfigTypes)) {
          continue;
        }

        try {
          GeneratorContext generatorContext = initBaseContext(tableName, allTypeAliasesMap,
              allMappersMap);

          for (Map.Entry<String, String> entry : packageFileSuffixMap.entrySet()) {
            generatorContext.addAttribute(entry.getKey(), entry.getValue());
          }

          doGeneratorService(generator, generatorContext, allPackageNameMap);
        } catch (Exception e) {
          LOGGER.error(String.format("Can not Generate tableName:%s,configTypes:%s",
              tableName,
              packageConfigTypes.getType()),
              e);
        }
      }
      LOGGER.info("**********代码生成工具,已经结束生成代码>>>{}", tableName);
    }
    LOGGER.info("**********代码生成工具,已经结束生成代码**********");
  }

  /**
   * 调用创建模板的方式
   *
   * @param generator         生成器
   * @param generatorContext  生成器上下文
   * @param allPackageNameMap 包名
   */
  protected void doGeneratorService(Generator generator,
      GeneratorContext generatorContext,
      Map<String, String> allPackageNameMap) {
    generator.defaultGenerator(generatorContext, allPackageNameMap);
  }

  /**
   * 初始化渲染模板基本参数上下文
   *
   * @param tableName 表名
   */
  protected GeneratorContext initBaseContext(String tableName,
      Map<String, String> allTypeAliasesMap,
      Map<String, String> allMappersMap) {
    Map<String, String> propMap = connector.getPrimaryKey(tableName);
    String authorName = PropertiesUtils.getAuthorName(properties);
    String upClassName = GeneratorStringUtils.firstUpperAndNoPrefix(tableName, properties);
    String lowClassName = GeneratorStringUtils.formatAndNoPrefix(tableName, properties);
    String packageName = PropertiesUtils.getPackage(properties);
    String primaryKeyType = propMap.get("primaryKeyType");
    String primaryKey = GeneratorStringUtils
        .firstUpperNoFormat(GeneratorStringUtils.format(propMap.get("primaryKey"), properties));
    String columnPrimaryKey = propMap.get("primaryKey");
    String normalPrimaryKey = GeneratorStringUtils.format(propMap.get("primaryKey"), properties);

    String tableRemark = connector.getTableRemark(tableName);
    GeneratorContext generatorContext = new GeneratorContext(authorName,
        tableName,
        upClassName,
        lowClassName,
        packageName,
        primaryKeyType,
        primaryKey,
        properties);
    generatorContext.addAttribute("connector", connector);
    generatorContext.addAttribute("properties", properties);
    generatorContext.addAttribute("columnPrimaryKey", columnPrimaryKey);
    generatorContext.addAttribute("normalPrimaryKey", normalPrimaryKey);
    generatorContext.addAttribute("typeAliases", allTypeAliasesMap);
    generatorContext.addAttribute("mappers", allMappersMap);
    generatorContext.addAttribute("tableRemark", tableRemark);
    return generatorContext;
  }
}
