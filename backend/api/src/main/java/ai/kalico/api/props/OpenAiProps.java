package ai.kalico.api.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Bizuwork Melesse
 * created on 1/31/23
 */
@Primary
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAiProps {
    private String apiKey;
    private double temp = 0.5;
    private int maxTokens = 2000;
    private int chunkSize = 1400; //words
    private String model = "text-davinci-003";
    private String promptCluster = "Cluster the following text:";
    private String promptParagraph = "Break the following text into paragraphs and improve the writing style:";
//    private String promptGrammar = "Improve the writing style in this text.";
    private String promptTitle = "Generate a blog post title for the following text:";
    private String user = "kalico";
}
