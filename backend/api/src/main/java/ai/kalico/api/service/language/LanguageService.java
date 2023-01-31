package ai.kalico.api.service.language;

/**
 * @author Biz Melesse created on 1/30/23
 */
public interface LanguageService {

  /**
   * Apply GPT-3 or equivalent ML model/s to generate the content from raw
   * data gathered so far.
   * @param mediaId
   */
  void generateContent(String mediaId);

  /**
   * Cleanup text by extracting texts and number without any special characters
   *
   * @param input the text to clean up
   * @return
   */
  String cleanup(String input);

}
