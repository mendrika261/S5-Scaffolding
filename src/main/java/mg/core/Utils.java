package mg.core;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.extern.java.Log;
import lombok.val;
import mg.exception.ScaffoldingException;

@Log
public class Utils {
  public static final String WD_PATH = System.getProperty("user.dir") + "/";

  public static final String DATA_PATH = WD_PATH + "data/";
  public static final String DATA_DB_PATH = DATA_PATH + "db/";
  public static final String DATA_LANG_PATH = DATA_PATH + "lang/";
  public static final String DATA_TEMPLATES_PATH = DATA_PATH + "templates/";

  public static String readFile(File file) {
    try {
      return Files.readString(file.toPath());
    } catch (IOException ignored) {
      throw new ScaffoldingException("Error reading file: " + file.getAbsolutePath());
    }
  }

  public static String readFile(String path) {
    return readFile(new File(path));
  }

  public static String getEnv(String key) {
    Dotenv dotenv = Dotenv.load();
    String value = dotenv.get(key);
    if (value == null) {
      throw new ScaffoldingException("Environment variable not set: " + key);
    }
    return value;
  }

  public static String getEnv(String key, String defaultValue) {
    Dotenv dotenv = Dotenv.load();
    String value = dotenv.get(key);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  private static String getEnvPath(String key, String defaultValue) {
    String value = getEnv(key, defaultValue);
    if (value.isBlank() || value.isEmpty()) {
      log.info("Using default value for " + key + ": " + defaultValue);
      return value;
    }
    if (!value.endsWith("/")) {
      value += "/";
    }
    return value;
  }

  public static <T> T readFileJson(String path, Class<T> classMap) {
    Gson gson = new Gson();
    return gson.fromJson(readFile(path), classMap);
  }

  public static String toCamelCase(String string) {
    StringBuilder stringBuilder = new StringBuilder();
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
    String camelCase = toCamelCase(string);
    return firstUpper? camelCase: camelCase.substring(0, 1).toLowerCase() + camelCase.substring(1);
  }

  public static void writeFile(String fileName, Object object) {
    try {
      val file = new File(fileName);
      if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
        throw new ScaffoldingException("Error creating directories: " +
                                 file.getParentFile().getAbsolutePath());
      Files.writeString(file.toPath(), object.toString());
    } catch (IOException ignored) {
      throw new ScaffoldingException("Error writing file: " + fileName);
    }
  }

    public static String getCorrectPath(String path) {
      if (path == null || path.equals("."))
        return "";
      if (!path.isEmpty() && !path.endsWith("/"))
        path += "/";
      return path;
    }

  public static String getPackageFromPath(String path) {
    path = getCorrectPath(path);
    if (path.startsWith(WD_PATH))
      path = path.substring(WD_PATH.length());
    return path.replace("/", ".").substring(0, path.length() - 1);
  }
}
