package com.llj.mybatis.connect;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：连接数据库接口
 */
public interface Connector {

  /**
   * 获取表的键值类型
   *
   * @param tableName
   */
  Map<String, String> getPrimaryKey(String tableName);

  /**
   * 获取表的备注信息
   *
   * @param tableName
   * @return
   */
  String getTableRemark(String tableName);

  /**
   * 获取类型
   *
   * @param tableName
   */
  Map<String, String> mapColumnNameType(String tableName);

  /**
   * 获取备注
   *
   * @param tableName
   */
  Map<String, String> mapColumnRemark(String tableName);

  /**
   * 获取所有的索引信息
   *
   * @param tableName
   */
  List<String> listAllIndex(String tableName);

  List<String> listTablesByTablePrefix(String tablePrefix);
}
