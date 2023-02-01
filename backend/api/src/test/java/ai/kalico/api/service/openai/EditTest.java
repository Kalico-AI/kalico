package ai.kalico.api.service.openai;

import static org.testng.AssertJUnit.assertNotNull;

import ai.kalico.api.props.OpenAiProps;
import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.openai.edit.EditRequest;
import ai.kalico.api.service.openai.edit.EditResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class EditTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private OpenAiProps openAiProps;
    private OpenAiService service;

    @BeforeClass
    public void setup() {
        service = new OpenAiService(openAiProps.getApiKey());
    }


    @Test
    void edit() {
        EditRequest request = EditRequest.builder()
                .model("text-davinci-edit-001")
                .input("What day of the wek is it?")
                .instruction("Fix the spelling mistakes")
                .build();

        EditResult result = service.createEdit( request);

        assertNotNull(result.getChoices().get(0).getText());
    }

    @Test
    void editDeprecated() {
        EditRequest request = EditRequest.builder()
                .input("What day of the wek is it?")
                .instruction("Fix the spelling mistakes")
                .build();

        EditResult result = service.createEdit("text-davinci-edit-001", request);

        assertNotNull(result.getChoices().get(0).getText());
    }
}
