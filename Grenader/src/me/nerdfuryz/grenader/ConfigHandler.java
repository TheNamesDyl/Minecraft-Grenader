package me.nerdfuryz.grenader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler
{
  public static String name = "[Grenader] ";

  public static void checkForConfig(File file) {
    if (!file.exists()) {
      file.getParentFile().mkdirs();
      copy(Grenader.getInstance().getResource(file.getName()), file);
      Grenader.logger.info(name + file.getName() + " generated!");
    }
  }

  public static void loadConfig(FileConfiguration config, File file) {
    try {
      config.load(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void saveConfig(FileConfiguration config, File file) {
    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void copy(InputStream inputStream, File file) {
    try {
      OutputStream out = new FileOutputStream(file);
      byte[] buf = new byte[1024];
      int len;
      while ((len = inputStream.read(buf)) > 0)
      {
        out.write(buf, 0, len);
      }
      out.close();
      inputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}