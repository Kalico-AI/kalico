package ai.kalico.api.service.language;


import ai.kalico.api.props.OpenAiProps;
import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.utils.migration.FlywayMigration;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.FileCopyUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author Bizuwork Melesse
 * created on 1/30/23
 */
@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class LanguageServiceIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private LanguageService languageService;

//    @Autowired
//    private ServiceTestHelper testHelper;

    @Autowired
    private FlywayMigration flywayMigration;

    @Autowired
    private OpenAiProps openAiProps;


    @AfterTest
    public void teardown() {
    }

    @SneakyThrows
    @BeforeMethod
    public void beforeEachTest(Method method) {
        log.info("  Testcase: " + method.getName() + " has started");
//        flywayMigration.migrate(true);
//        userService.getOrCreateUserprofile();
//        cookieHelper.loadFromFile();
    }

    @AfterMethod
    public void afterEachTest(Method method) {
        log.info("  Testcase: " + method.getName() + " has ended");
    }

    @Test
    public void chunkTextTest() {
        String text = loadFromFile("text/recipe.txt");
        List<String> response = languageService.chunkTranscript(text, 100);
        assertNotNull(response);
        assertEquals(5, response.size());
    }

    @SneakyThrows
    private String loadFromFile(String path) {
        InputStream resource = new ClassPathResource(path).getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource)) ) {
            return FileCopyUtils.copyToString(reader);
        }
    }
    @Test(enabled = false)
    public void onScreenTextCleanupTest() {
      String onScreenText = "\n"
          + "\n"
          + " \n"
          + "   \n"
          + "\n"
          + "WVIAMS OeInt Mnlinestel\n"
          + "\n"
          + "*\n"
          + "\f\n"
          + "0003.jpg\n"
          + "----------\n"
          + "Eien part thinly sliced\n"
          + "\n"
          + " \n"
          + "\f\n"
          + "0005.jpg\n"
          + "----------\n"
          + "oS\n"
          + "©\n"
          + "&\n"
          + "\n"
          + "S\n"
          + "q\n"
          + "@\n"
          + ">\n"
          + "©\n"
          + "©)\n"
          + "Bo)\n"
          + "TS\n"
          + "©\n"
          + "fo)\n"
          + "Ke)\n"
          + "\n"
          + " \n"
          + "\f\n"
          + "0006.jpg\n"
          + "----------\n"
          + "2 eggs and set aside\n"
          + "\n"
          + " \n"
          + "\f\n"
          + "1-2 tosp red chilli flakes\n"
          + "\n"
          + " \n"
          + "\f\n"
          + "0009.jpg\n"
          + "----------\n"
          + "Stir fry on high flame\n"
          + "\n"
          + " \n"
          + "\f\n"
          + "0010.jpg\n"
          + "----------\n"
          + "a)\n"
          + "@\n"
          + "&\n"
          + "©\n"
          + "®\n"
          + "—\n"
          + "iS\n"
          + "i)\n"
          + "=\n"
          + "=\n"
          + "Ne\n"
          + "S\n"
          + "o)\n"
          + "eS\n"
          + "RS\n"
          + "(ep)\n"
          + ")\n"
          + "©\n"
          + "=y\n"
          + "oy\n"
          + "co\n"
          + "ie\n"
          + "ie\n"
          + "<¢\n"
          + "\n"
          + " \n"
          + "\f\n"
          + "0011.jpg\n"
          + "----------\n"
          + "Add sauces (check written recipe)\n"
          + "\n"
          + " \n"
          + "\f\n"
          + "0014.jpg\n"
          + "----------\n"
          + "Switch off the flame and mix\n"
          + "\n"
          + " \n"
          + "\f\n";
      String cleaned = languageService.cleanup(onScreenText);
      assertThat(cleaned, is(notNullValue()));
    }

    @Test(enabled = false)
    public void descriptionCleanupTest() {
        String description = "Spicy egg fried rice \uD83D\uDD25 Recipe \uD83D\uDC47<br><br>INGREDIENTS:<br>5-6 green onions<br>6 cloves of garlic<br>1-2 tbsp crushed chilli flakes (add based on spice tolerance)<br>2 servings cold rice<br>2 eggs<br>Cooking oil as needed<br>—Sauce—<br>1 tbsp light/regular soy sauce<br>1 tbsp dark soy sauce<br>1 tbsp water<br>1 tsp sugar<br>Note: Add salt if needed<br><br>PROCESS:<br>1. Clean and trim 5-6 green onions. Mince the white parts and thinly slice the green parts for garnish<br>2. Mince 6 cloves of garlic. Add more or less depending on size of the cloves or taste<br>3. Take 2 servings of cold, day old rice in a bowl and separate the clumps. This helps in even stir frying. Cold, slightly dried out rice is essential for the perfect fried rice texture<br>4. Mix the sauce ingredients in a bowl and set aside<br>5. To a heated wok/pan add some oil and scramble 2 eggs with a pinch of salt. Set aside<br>6. Set flame to medium and add 2 tbsp neutral cooking oil. When the oil is hot, sauté the garlic and green onion for 30 seconds<br>7. Reduce flame to low and sauté 1-2 tbsp crushed red chilli flakes for 30 seconds or until fragrant. Chilli flakes will burn if the flame is too high<br>8. Increase flame to medium high, add rice and stir fry until mixed well with the aromatics<br>9. Add the prepared sauce and stir fry for 2-3 minutes. The rice needs to fry well so keep tossing and turning<br>10. When the rice is fried well, add the previously scrambled eggs. Mix everything well and adjust salt if needed<br>11. Add the green part of green onions and switch off the flame. You don’t want to overcook them<br>12. Serve the spicy egg fried rice with a crispy fried egg, a side of your choice or on its own<br><br>#easyrecipes #asmrcooking #recipes #friedrice";
    }

    @Test(enabled = false)
    public void transcriptCleanupTest() {
        String transcript = "";
    }
}
