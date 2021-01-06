package com.richcoder.code.generator.db.code.impl;

import com.richcoder.code.generator.IGenerator;
import com.richcoder.code.generator.db.code.GeneratoraAdapter;
import com.richcoder.code.generator.db.code.conf.GenerateConfig;
import com.richcoder.code.generator.db.code.vo.TableInfo;

public class GeneratorModifyDTOFreeMark extends AbstractGeneratorFreeMark implements IGenerator {

  @Override
  public void generator(final GenerateConfig config) {
    generator(config, new GeneratoraAdapter() {
      @Override
      public String getTemplateName() {
        return "modifyDTO.ftl";
      }

      @Override
      public String getPackageName(GenerateConfig config, TableInfo tableInfo) {
        return config.getDtoPackageName() + ".request." + tableInfo.getClassPackageName();
      }

      @Override
      public String getClassName(String modelName) {
        return modelName + "ModifyDTO";
      }
    });
  }
}
