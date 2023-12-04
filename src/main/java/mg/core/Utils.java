package mg.core;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import mg.exception.ScaffoldingException;

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
    final Dotenv dotenv = Dotenv.configure().systemProperties().directory(WD_PATH).load();
    final String value = dotenv.get(key);
    if (value == null) {
      throw new ScaffoldingException("Environment variable not set: " + key);
    }
    return value;
  }

  public static String getEnv(String key, String defaultValue) {
    final Dotenv dotenv = Dotenv.configure().systemProperties().directory(WD_PATH).load();
    final String value = dotenv.get(key);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  private static String getEnvPath(String key, String defaultValue) {
    String value = getEnv(key, defaultValue);
    if (value.isBlank() || value.isEmpty()) {
      System.out.println("Using default value for " + key + ": " + defaultValue);
      return value;
    }
    if (!value.endsWith("/")) {
      value += "/";
    }
    return value;
  }

  public static <T> T readFileJson(String path, Class<T> classMap) {
    final Gson gson = new Gson();
    return gson.fromJson(readFile(path), classMap);
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
    return firstUpper? camelCase: camelCase.substring(0, 1).toLowerCase() + camelCase.substring(1);
  }

  public static void writeFile(String fileName, Object object) {
    try {
      final File file = new File(fileName);
      if (file.getParentFile() != null && !file.getParentFile().exists() && !file.getParentFile().mkdirs())
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
    if(path == null || path.isEmpty())
      return "";
    path = getCorrectPath(path);
    if (path.startsWith(WD_PATH))
      path = path.substring(WD_PATH.length());
    return path.replace("/", ".").substring(0, path.length() - 1);
  }

    public static String[] getAvailableLanguages() {
      final File[] files = new File(DATA_LANG_PATH).listFiles();
      if (files == null)
        throw new ScaffoldingException("Error getting available languages");
      final String[] languages = new String[files.length];
      for (int i = 0; i < files.length; i++) {
        languages[i] = files[i].getName().replace(".json", "");
      }
      return languages;
    }

  public static void checkIfPathIsWritable(String path) {
    if (path == null)
      throw new ScaffoldingException("Path cannot be empty");
    if (path.startsWith(WD_PATH))
      path = path.substring(WD_PATH.length());
    if (path.endsWith("/"))
      path = path.substring(0, path.length() - 1);
    final String[] directories = path.split("/");
    String currentPath = WD_PATH;
    for (String directory : directories) {
      currentPath += directory + "/";
      File file = new File(currentPath);
      if (file.exists() && !file.isDirectory())
        throw new ScaffoldingException("Path is not a directory: " + currentPath);
      if (!file.exists() && !file.mkdir())
        throw new ScaffoldingException("Error creating directory: " + currentPath);
      file.delete();
    }
  }

  public static void checkIfPackageNameIsValid(String packageName) {
    if(packageName == null || packageName.isEmpty()) return;
    if (packageName.startsWith("."))
      packageName = packageName.substring(1);
    if (packageName.endsWith("."))
      packageName = packageName.substring(0, packageName.length() - 1);
    final String[] directories = packageName.split("\\.");
    for (String directory : directories) {
      if (!directory.matches("[a-z][a-z0-9]*"))
        throw new ScaffoldingException("Invalid package name: " + packageName);
    }
  }

  public static String[] getAvailableTemplates() {
    final File[] files = new File(DATA_TEMPLATES_PATH).listFiles();
    if (files == null)
      throw new ScaffoldingException("Error getting available templates");
    final String[] templates = new String[files.length];
    for (int i = 0; i < files.length; i++) {
      templates[i] = files[i].getName().replace(".template", "");
    }
    return templates;
  }
}
