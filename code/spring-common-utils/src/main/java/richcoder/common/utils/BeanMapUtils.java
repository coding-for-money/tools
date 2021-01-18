package richcoder.common.utils;


import java.beans.FeatureDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Map 与 对象的互相转换
 */
public class BeanMapUtils {

  public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) {
    return anyMapToObject(map, beanClass);
  }

  public static <T> T mapStrToObject(Map<String, String> map, Class<T> beanClass) {
    return anyMapToObject(map, beanClass);
  }

  public static <T> T mapStrToObjectOrDefault(Map<String, String> map, Class<T> beanClass) {
    T t = anyMapToObject(map, beanClass);
    try {
      return t == null ? beanClass.newInstance() : t;
    } catch (Exception e) {
    }
    return null;
  }

  private static <T> T anyMapToObject(Map map, Class<T> beanClass) {
    if (map == null || map.size() <= 0) {
      return null;
    }
    try {
      T obj = beanClass.newInstance();
      //获取关联的所有类，本类以及所有父类
      Class oo = obj.getClass();
      List<Class> clazzs = new ArrayList<>();
      while (true) {
        clazzs.add(oo);
        oo = oo.getSuperclass();
        if (oo == null || oo == Object.class) {
          break;
        }
      }

      for (Class clazz : clazzs) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
          int mod = field.getModifiers();
          if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
            continue;
          }
          //由字符串转换回对象对应的类型
          field.setAccessible(true);
          field.set(obj, map.get(field.getName()));
        }
      }
      return obj;
    } catch (Exception ignored) {
    }
    return null;
  }

  public static Map<String, Object> objectToMap(Object obj) {
    if (obj == null) {
      return null;
    }
    try {
      //获取关联的所有类，本类以及所有父类
      Class oo = obj.getClass();
      List<Class> clazzs = new ArrayList<>();
      while (true) {
        clazzs.add(oo);
        oo = oo.getSuperclass();
        if (oo == null || oo == Object.class) {
          break;
        }
      }
      Map<String, Object> map = new HashMap<>();

      for (Class clazz : clazzs) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
          int mod = field.getModifiers();
          //过滤 static 和 final 类型
          if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
            continue;
          }
          field.setAccessible(true);
          map.put(field.getName(), field.get(obj));
        }
      }
      return map;
    } catch (Exception ignored) {
    }
    return null;
  }

  public static Map<String, String> objectToMapStr(Object obj) {
    Map<String, Object> map = objectToMap(obj);
    if (map == null) {
      return null;
    }
    Map<String, String> strMap = new HashMap<>();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() != null) {
        try {
          strMap.put(entry.getKey(), entry.getValue().toString());
        } catch (Exception ignored) {

        }
      }
    }
    return strMap;
  }

  public static Map<String, String> objectNoParentToMapStr(Object obj) {
    if (obj == null) {
      return null;
    }
    try {
      Map<String, String> map = new HashMap<>();
      Field[] declaredFields = obj.getClass().getDeclaredFields();
      for (Field field : declaredFields) {
        int mod = field.getModifiers();
        //过滤 static 和 final 类型
        if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
          continue;
        }
        field.setAccessible(true);
        if (Objects.nonNull(field.get(obj))) {
          map.put(field.getName(), String.valueOf(field.get(obj)));
        }

      }
      return map;
    } catch (Exception ignored) {
    }
    return null;
  }

  public static String[] getNullPropertyNames(Object source) {
    BeanWrapper wrappedSource = new BeanWrapperImpl(source);
    return Stream.of(wrappedSource.getPropertyDescriptors())
        .map(FeatureDescriptor::getName)
        .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
        .toArray(String[]::new);
  }
}
