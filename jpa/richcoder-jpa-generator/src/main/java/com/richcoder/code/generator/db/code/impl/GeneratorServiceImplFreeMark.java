package com.richcoder.code.generator.db.code.impl;

import com.richcoder.code.generator.IGenerator;
import com.richcoder.code.generator.db.code.GeneratoraAdapter;
import com.richcoder.code.generator.db.code.conf.GenerateConfig;

public class GeneratorServiceImplFreeMark extends AbstractGeneratorFreeMark implements IGenerator {

  @Override
  public void generator(GenerateConfig config) {
    super.generator(config, new GeneratoraAdapter() {
      @Override
      public String getTemplateName() {
        return "serviceImpl.ftl";
      }

      @Override
      public String getPackageName(GenerateConfig config) {
        return config.getServiceImplPackageName();
      }

      @Override
      public String getClassName(String modelName) {
        return modelName + "ServiceImpl";
      }
    });
  }
}
