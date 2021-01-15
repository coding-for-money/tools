package com.llj.mybatis.generator.impl;

import com.llj.mybatis.generator.base.BaseGeneratorImpl;
import com.llj.mybatis.generator.context.GeneratorContext;
import java.util.UUID;
import org.apache.velocity.VelocityContext;

/**
 * 功能描述：Result代码生成
 */
public class ResultGeneratorImpl extends BaseGeneratorImpl {

  @Override
  public void initVelocityContext(VelocityContext velocityContext,
      GeneratorContext generatorContext) {
    super.initVelocityContext(velocityContext, generatorContext);
    velocityContext
        .put("SerialVersionUID", String.valueOf(UUID.randomUUID().getLeastSignificantBits()));
  }
}
