package richcoder.common.aspect.aop;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TrackTimeAspect {

  private static final Logger logger = LoggerFactory.getLogger(TrackTimeAspect.class);


  @Pointcut("@annotation(richcoder.common.aspect.annotation.TrackTime)")
  public void trackTime() {
    // 增加切面
  }

  @Around("trackTime()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long takeTime = System.currentTimeMillis() - startTime;
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    try {
      log.info("service.method_time", takeTime, "methodName:" + method.getName());
    } catch (Exception e) {
      logger.error("TrackTimeAspect error: {}", e);
    }
    return result;
  }
}
