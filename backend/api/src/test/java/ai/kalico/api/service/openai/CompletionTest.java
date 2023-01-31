package ai.kalico.api.service.openai;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import ai.kalico.api.props.OpenAiProps;
import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.openai.completion.CompletionChoice;
import ai.kalico.api.service.openai.completion.CompletionRequest;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class CompletionTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private OpenAiProps openAiProps;
    private OpenAiService service;

    @BeforeClass
    public void setup() {
        service = new OpenAiService(openAiProps.getApiToken());
    }

    @Test
    void createCompletion() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .n(5)
                .maxTokens(50)
                .user("testing")
                .logitBias(new HashMap<>())
                .build();

        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        assertEquals(5, choices.size());
    }

    @Test
    void createCompletionDeprecated() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing")
                .build();

        List<CompletionChoice> choices = service.createCompletion("ada", completionRequest).getChoices();
        assertFalse(choices.isEmpty());
    }
}
