package ai.kalico.api.service.openai;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import ai.kalico.api.props.OpenAiProps;
import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.openai.engine.Engine;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class EngineTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private OpenAiProps openAiProps;
    private OpenAiService service;

    @BeforeClass
    public void setup() {
        service = new OpenAiService(openAiProps.getApiKey());
    }


    @Test
    void getEngines() {
        List<Engine> engines = service.getEngines();

        assertFalse(engines.isEmpty());
    }

    @Test
    void getEngine() {
        Engine ada = service.getEngine("ada");

        assertEquals("ada", ada.id);
    }
}
