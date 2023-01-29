package ai.kalico.api.service.utils;

import java.io.File;
import lombok.SneakyThrows;

/**
 * @author Biz Melesse created on 1/2/23
 */
public class FWUtils {

  @SneakyThrows
  public static String getCanonicalPath(String path) {
    String cPath = new File(path).getCanonicalPath();
    if (cPath.startsWith("/private")) {
      return cPath.replace("/private", "");
    }
    return cPath;
  }

}
