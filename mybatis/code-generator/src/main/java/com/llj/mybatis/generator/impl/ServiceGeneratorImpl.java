package com.llj.mybatis.generator.impl;

import com.google.common.collect.Lists;
import com.llj.mybatis.connect.Connector;
import com.llj.mybatis.generator.base.BaseGeneratorImpl;
import com.llj.mybatis.generator.context.GeneratorContext;
import com.llj.mybatis.utils.GeneratorStringUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import org.apache.velocity.VelocityContext;


/**
 * 功能描述：Service代码生成
 */
public class ServiceGeneratorImpl extends BaseGeneratorImpl {

  private static String LINE = System.getProperty("line.separator");

  @Override
  public void initVelocityContext(VelocityContext velocityContext,
      GeneratorContext generatorContext) {
    super.initVelocityContext(velocityContext, generatorContext);
    velocityContext
        .put("SerialVersionUID", String.valueOf(UUID.randomUUID().getLeastSignificantBits()));

    String tableName = generatorContext.getTableName();
    Connector connector = (Connector) generatorContext.getAttribute("connector");

    Map<String, String> colMap = connector.mapColumnNameType(tableName);
    Map<String, String> columnRemarkMap = connector.mapColumnRemark(tableName);
    Set<String> keySet = colMap.keySet();
    Set<String> importSets = new HashSet<>();
    for (String key : keySet) {
      String value = colMap.get(key);
      if ("BigDecimal".equals(value) && !importSets.contains("BigDecimal")) {
        importSets.add("import java.math.BigDecimal;" + LINE);
      } else if ("Date".equals(value) && !importSets.contains("Date")) {
        importSets.add("import java.util.Date;" + LINE);
      } else if ("Timestamp".equals(value) && !importSets.contains("Timestamp")) {
        importSets.add("import java.sql.Timestamp;" + LINE);
      }
    }

    Properties properties = generatorContext.getProperties();

    velocityContext
        .put("methods", generateGetAndSetMethods(colMap, generatorContext.getProperties()));
    velocityContext
        .put("fields", generateFields(colMap, columnRemarkMap, generatorContext.getProperties()));
    velocityContext.put("importSets", importSets);
    velocityContext
        .put("convertDomains", getCovertDomainFields(colMap, generatorContext.getProperties()));
    velocityContext.put("converts", getCovertFields(colMap, properties));
  }

  protected List<String> getCovertDomainFields(Map<String, String> map, Properties properties) {
    Set<String> keySet = map.keySet();
    List<String> converts = Lists.newArrayList();
    for (String key : keySet) {
      StringBuilder sb = new StringBuilder();
      String field = GeneratorStringUtils.format(key, properties);
      sb.append(velocityContext.get("lowClassName"))
          .append("Convert")
          .append(".set" + GeneratorStringUtils.firstUpperNoFormat(field) + "(")
          .append(velocityContext.get("lowClassName"))
          .append(".get" + GeneratorStringUtils.firstUpperNoFormat(field) + "())");
      converts.add(sb.toString());
    }
    return converts;
  }

  protected List<String> getCovertFields(Map<String, String> map, Properties properties) {
    Set<String> keySet = map.keySet();
    List<String> converts = Lists.newArrayList();
    for (String key : keySet) {
      StringBuilder sb = new StringBuilder();
      String field = GeneratorStringUtils.format(key, properties);
      sb.append(velocityContext.get("lowClassName"))
          .append(".set" + GeneratorStringUtils.firstUpperNoFormat(field) + "(")
          .append(velocityContext.get("lowClassName"))
          .append(properties.get("generator.domain.suffix"))
          .append(".get" + GeneratorStringUtils.firstUpperNoFormat(field) + "())");
      converts.add(sb.toString());
    }
    return converts;
  }
}
