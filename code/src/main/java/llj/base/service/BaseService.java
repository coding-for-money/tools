package llj.base.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.stereotype.Service;

/**
 * base class of service
 */
@Service
public class BaseService {


  /**
   * check is row exists by id in pointed table
   *
   * @param mapper
   * @param id
   * @param <T>
   * @return
   */
  public <T, M> T checkExist(M mapper, Long id) {
    return (T) "hehe";
  }


  /**
   * set adminName by adminId
   *
   * @param vo  include adminId , adminName
   * @param <T> dto ,include adminId , adminName
   */
  public <T> void setAdminName(T vo) {
    if (null == vo) {
      return;
    }
    try {
      Class<?> aClass = vo.getClass();
      Field[] fields = aClass.getDeclaredFields();
      for (Field field : fields) {
        if ("adminId".equals(field.getName())) {
          field.setAccessible(true);
          Object o = field.get(vo);
          if (null != o) {
            String name = "to do something..";
            Method setAdminName = aClass.getMethod("setAdminName", String.class);
            setAdminName.invoke(vo, name);
            return;
          }
        }
      }
    } catch (SecurityException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException e) {
      e.printStackTrace();
    }
  }

}
