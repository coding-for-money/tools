package com.richcoder.code.generator;


import com.richcoder.code.generator.db.code.GeneratoraAdapter;
import com.richcoder.code.generator.db.code.conf.GenerateConfig;

/**
 * @author richcoder
 */
public interface IGenerator {

  default void generator(GenerateConfig config) {
  }

  default void generator(GenerateConfig config, GeneratoraAdapter adapter) {
  }
}
