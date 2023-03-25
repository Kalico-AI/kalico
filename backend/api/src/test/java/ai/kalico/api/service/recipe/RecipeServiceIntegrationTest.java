package ai.kalico.api.service.recipe;

import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.utils.migration.FlywayMigration;
import com.kalico.model.CreateRecipeResponse;
import com.kalico.model.StringDto;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * @author Bizuwork Melesse
 * created on March 24, 2023
 */
@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class RecipeServiceIntegrationTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private FlywayMigration flywayMigration;

    @Autowired
    private RecipeService recipeService;

    @BeforeClass
    public void setup() {
    }

    @AfterTest
    public void teardown() {
    }

    @BeforeMethod
    public void beforeEachTest(Method method) {
        log.info("  Testcase: " + method.getName() + " has started");
        flywayMigration.migrate(true);
    }

    @AfterMethod
    public void afterEachTest(Method method) {
        log.info("  Testcase: " + method.getName() + " has ended");
    }

    @Test
    public void createRecipe() {
      StringDto stringDto = new StringDto().value("https://www.youtube.com/watch?v=U-0JCdjkREU");
      CreateRecipeResponse response = recipeService.createRecipe(stringDto);
      assertNotNull(response);
      assertNotNull(response.getStatus());
    }
}
