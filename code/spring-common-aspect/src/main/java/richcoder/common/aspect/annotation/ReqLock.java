package richcoder.common.aspect.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import javax.validation.Payload;

/**
 * 重复执行锁定
 *
 * @author richcoder
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface ReqLock {

  String message() default "{资源被锁定稍后再试结束}";

  //锁定缓存key
  String key() default "customRedisKey";

  long waitTime() default 300;

  long leaseTime() default 1000;

  TimeUnit unit() default TimeUnit.MILLISECONDS;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  @Target({METHOD})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    ReqLock[] value();
  }
}