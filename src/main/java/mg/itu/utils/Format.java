package mg.itu.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mg.itu.utils.gsonAdapter.LocalDateAdapter;
import mg.itu.utils.gsonAdapter.LocalDateTimeAdapter;

public class Format {
  public static Gson mapper;

  static Gson getMapper() {
    if (mapper == null) {
      final GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(java.time.LocalDate.class,
                                  new LocalDateAdapter());
      builder.registerTypeAdapter(java.time.LocalDateTime.class,
                                  new LocalDateTimeAdapter());
      mapper = builder.create();
    }
    return mapper;
  }

  public static String toJson(Object object) {
    try {
      return getMapper().toJson(object);
    } catch (Exception e) {
      return null;
    }
  }

  public static <T> T fromJson(String json, Class<T> classOfT) {
    try {
      return getMapper().fromJson(json, classOfT);
    } catch (Exception e) {
      // Json is escaped from resultSet (because of getString() method)
      json = unescape(json.substring(1, json.length() - 1));
      return getMapper().fromJson(json, classOfT);
    }
  }

  public static String unescape(String string) {
    return string.replaceAll("\\\\\"", "");
  }

  public static String toCamelCase(String string) {
    final StringBuilder stringBuilder = new StringBuilder();
    boolean nextUpper = true;
    for (char c : string.toCharArray()) {
      if (c == '_') {
        nextUpper = true;
      } else {
        if (nextUpper) {
          stringBuilder.append(Character.toUpperCase(c));
          nextUpper = false;
        } else {
          stringBuilder.append(c);
        }
      }
    }
    return stringBuilder.toString();
  }

  public static String toCamelCase(String string, boolean firstUpper) {
    final String camelCase = toCamelCase(string);
    return firstUpper
        ? camelCase
        : camelCase.substring(0, 1).toLowerCase() + camelCase.substring(1);
  }

  public static String toPathNameWithTrail(String path) {
    if (path == null || path.equals("."))
      return "";
    if (!path.isEmpty() && !path.endsWith("/"))
      path += "/";
    return path;
  }

  public static String packageToPath(String packageName) {
    return toPathNameWithTrail(packageName.replace(".", "/"));
  }

  public static String joinPackageName(String... packageName) {
    StringBuilder builder = new StringBuilder();
    for (String name : packageName) {
      if (!name.isEmpty()) {
        builder.append(name);
        builder.append("$.");
      }
    }
    // if ends with $., remove it
    if (!builder.isEmpty())
      builder.delete(builder.length() - 2, builder.length());
    return builder.toString();
  }
}
