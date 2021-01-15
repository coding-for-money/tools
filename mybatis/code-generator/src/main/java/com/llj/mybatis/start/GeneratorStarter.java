package com.llj.mybatis.start;

import com.llj.mybatis.generator.Generator;
import java.util.Set;

/**
 * 功能描述：自动化生成代码接口
 *
 * @author:llj
 */
public interface GeneratorStarter {

  /**
   * 启动创建代码
   */
  void start(Set<Generator> generatorSet);
}
