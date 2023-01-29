package ai.kalico.api.service.ocr;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Biz Melesse created on 11/27/22
 */
@Getter
@Setter
public class OcrRequest {
  private String videoPath;
  private String ocrPath;
  private String language;
  private String videoId;
  private String ocrTesseractPath;
}
