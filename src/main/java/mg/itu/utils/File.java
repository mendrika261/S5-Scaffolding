package mg.itu.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import mg.itu.display.Console;
import org.apache.logging.log4j.core.util.FileUtils;

public class File {
  public static String getFileContentAsString(java.io.File file) {
    try {
      return Files.readString(file.toPath());
    } catch (IOException ignored) {
      return null;
    }
  }

  public static String getFileContentAsString(String path) {
    return getFileContentAsString(new java.io.File(path));
  }

  public static void writeToFile(String fileNameWithPath, String content) {
    try {
      final java.io.File file = new java.io.File(fileNameWithPath);
      if (content == null)
        content = "";
      if (file.getParentFile() != null && !file.getParentFile().exists() &&
          !file.getParentFile().mkdirs())
        Console.LOGGER.error("Error creating directories: " +
                             file.getParentFile().getAbsolutePath());
      Files.writeString(Paths.get(fileNameWithPath), content);
    } catch (IOException ignored) {
      Console.LOGGER.error("Error writing to file: " + fileNameWithPath);
    }
  }
}
