package com.llj.mybatis.utils;

import com.llj.mybatis.config.PackageConfigType;
import com.llj.mybatis.config.PackageConfigTypes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

public class GeneratorFileUtils {

  public static boolean write(String content, String path) {
    try {
      OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
      BufferedWriter writer = new BufferedWriter(writerStream);
      writer.write(content);
      writer.flush();
      writer.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static void createPackageDirectory(Properties properties,
      Set<PackageConfigTypes> packageConfigTypesSet) {

    String location = PropertiesUtils.getLocation(properties);
    String packageLayer = PropertiesUtils.getLayers(properties);
    String project = PropertiesUtils.getProject(properties);
    if (StringUtils.isNoneBlank(project)) {
      location = location + "/" + properties.get("java.src");
    }
    String packageDir = "/" + PropertiesUtils.getPackage(properties).replaceAll("[.]", "/");
    String[] packageLayerArr = packageLayer.split(",");
    Set<String> typeSet = new HashSet<>();
    typeSet.addAll(Arrays.asList(packageLayerArr));
    if (StringUtils.isNoneBlank(project)) {
      for (PackageConfigTypes packageConfigTypes : packageConfigTypesSet) {
        for (PackageConfigType packageConfigType : packageConfigTypes.getPackageConfigTypeSet()) {
          String targetDir = packageConfigType.getTargetDir();
          mkDir(location + packageDir + targetDir);
        }
      }
    }
  }

  private static void mkDir(String dir) {
    File file;
    file = new File(dir);
    if (!file.exists()) {
      file.mkdirs();
    }
  }

  public static String getPackageDirectory(String name, Properties properties) {
    String location = PropertiesUtils.getLocation(properties);
    String packageDir = "/" + PropertiesUtils.getPackage(properties).replaceAll("[.]", "/");
    String project = PropertiesUtils.getProject(properties);
    String directory;
    if (StringUtils.isNotBlank(project)) {
      directory = location + "/" + properties.get("java.src") + packageDir + "/" + name + "/";
    } else {
      directory = location + packageDir + "/" + name + "/";
    }
    return directory;
  }

  public static Map<String, String> getAllPackageName(Properties properties,
      Set<PackageConfigTypes> packageConfigTypesSet) {
    String basePackage = PropertiesUtils.getPackage(properties);
    Map<String, String> packageNameMap = new HashMap<>();
    for (PackageConfigTypes packageConfigTypes : packageConfigTypesSet) {
      for (PackageConfigType packageConfigType : packageConfigTypes.getPackageConfigTypeSet()) {
        String targetDir = packageConfigType.getTargetDir();
        StringBuilder packageNameStrb = new StringBuilder();
        packageNameStrb.append(basePackage);
        String packageNameValue = targetDir.replaceAll("/", ".");
        if (packageNameValue.startsWith(".")) {
          packageNameStrb.append(packageNameValue);
        } else {
          packageNameStrb.append(".").append(packageNameValue);
        }
        packageNameMap.put(packageConfigType.getAliasType(), packageNameStrb.toString());
      }
    }
    return packageNameMap;
  }

  public static Map<String, String> getAllPackageFileSuffix(
      Set<PackageConfigTypes> packageConfigTypesSet) {
    Map<String, String> packageFileSuffix = new HashMap<>();
    for (PackageConfigTypes packageConfigTypes : packageConfigTypesSet) {
      for (PackageConfigType packageConfigType : packageConfigTypes.getPackageConfigTypeSet()) {
        packageFileSuffix.put(packageConfigType.getAliasType() + "FileSuffix",
            packageConfigType.getFileNameSuffixAndNoFormat());
      }
    }
    return packageFileSuffix;
  }

  public static Map<String, String> getAllTypeAliasesMap(String tableName,
      Properties properties,
      Set<PackageConfigTypes> packageConfigTypesSet) {
    Map<String, String> typeAliasesMap = new TreeMap<>();
    for (PackageConfigTypes packageConfigTypes : packageConfigTypesSet) {
      if (PackageConfigTypes.ConfigType.MODEL.equals(packageConfigTypes.getType())) {
        for (PackageConfigType packageConfigType : packageConfigTypes.getPackageConfigTypeSet()) {
          String fileNameSuffix = packageConfigType.getFileNameSuffix();
          if (fileNameSuffix.endsWith(".java")) {
            String targetDir = packageConfigType.getTargetDir();
            targetDir = targetDir.replaceAll("/", ".");
            String basePackage = PropertiesUtils.getPackage(properties);
            String className = basePackage
                + targetDir
                + "."
                + GeneratorStringUtils.firstUpperAndNoPrefix(tableName, properties)
                + packageConfigType.getFileNameSuffixAndNoFormat();
            String alias = className.substring(className.lastIndexOf(".") + 1);
            typeAliasesMap.put(alias, className);
          }
        }
      }
    }
    return typeAliasesMap;
  }

  public static Map<String, String> getAllMappersMap(String tableName,
      Properties properties,
      Set<PackageConfigTypes> packageConfigTypesSet) {
    Map<String, String> mappersMap = new TreeMap<>();
    for (PackageConfigTypes packageConfigTypes : packageConfigTypesSet) {
      if (PackageConfigTypes.ConfigType.MAPPER.equals(packageConfigTypes.getType())) {
        for (PackageConfigType packageConfigType : packageConfigTypes.getPackageConfigTypeSet()) {
          String fileNameSuffix = packageConfigType.getFileNameSuffix();
          if (fileNameSuffix.endsWith(".xml")) {
            String targetDir = packageConfigType.getTargetDir();
            String basePackage = PropertiesUtils.getPackage(properties).replaceAll("[.]", "/");
            String fileName = basePackage
                + targetDir
                + "/"
                + GeneratorStringUtils.firstUpperAndNoPrefix(tableName, properties)
                + fileNameSuffix;
            mappersMap
                .put(GeneratorStringUtils.firstUpperAndNoPrefix(tableName, properties), fileName);
          }
        }
      }
    }
    return mappersMap;
  }
}