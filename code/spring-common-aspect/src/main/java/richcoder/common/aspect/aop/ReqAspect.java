package richcoder.common.aspect.aop;


import java.lang.reflect.Method;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import richcoder.common.aspect.annotation.ReqLock;

@Aspect
@Slf4j
@Service
public class ReqAspect {

  @Resource
  private RedissonClient redissonClient;
  @Value("${info.env.name:no_set}")
  private String envName;

  @Pointcut("@annotation(richcoder.common.aspect.annotation.ReqLock)")
  public void reqLockAop() {
  }

  @Before("reqLockAop()&&@annotation(reqLock)")
  public void before(JoinPoint joinPoint, ReqLock reqLock) {
  }


  @Around("reqLockAop()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    if (envName.contains("dev") || envName.contains("test") || envName.contains("local")) {
      //redis不稳定
      return pjp.proceed();
    }

    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    ReqLock req = method.getAnnotation(ReqLock.class);
    String key = "key...";
    String value = "value...";
    //如果value不为空 ， 去入参里找到对应的值
    if (StringUtils.isNotEmpty(value)) {
      Object[] args = pjp.getArgs();
    }
    RLock lock = redissonClient.getLock(key);
    if (!lock.tryLock(req.waitTime(), req.leaseTime(), req.unit())) {
      log.info("get lock failed : " + key);
      return null;
    }
    log.info("get lock success : " + key);
    try {
      //执行方法
      return pjp.proceed();
    } catch (Exception e) {
      log.info("execute locked method occured an exception", e);
    } finally {
      lock.unlock();  //释放分布式锁
      log.info("release lock :" + key);
    }
    return null;
  }
}
