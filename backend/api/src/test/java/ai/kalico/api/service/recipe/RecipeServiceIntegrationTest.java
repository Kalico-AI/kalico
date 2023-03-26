package ai.kalico.api.service.recipe;

import ai.kalico.api.data.postgres.entity.RecipeEntity;
import ai.kalico.api.data.postgres.repo.RecipeRepo;
import ai.kalico.api.dto.GptResponse;
import ai.kalico.api.dto.RecipeGptResponse;
import ai.kalico.api.props.AWSProps;
import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.language.LanguageService;
import ai.kalico.api.service.openai.completion.CompletionChoice;
import ai.kalico.api.utils.migration.FlywayMigration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.CreateRecipeResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeLite;
import com.kalico.model.StringDto;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

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

    @Autowired
    private RecipeRepo recipeRepo;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private LanguageService languageService;

    @Autowired
    private AWSProps awsProps;

    private final int numRecipes = 10;

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

    @Test(enabled = false)
    public void createRecipeDirectTest() {
        RecipeEntity entity = new RecipeEntity();
        entity.setContentId("U-0JCdjkREU-" + UUID.randomUUID());
        entity.setCanonicalUrl("https://www.youtube.com/watch?v=U-0JCdjkREU");
        entity.setThumbnail(String.format("%s/%s/%s.jpg",
            awsProps.getCdn(),
            awsProps.getImageFolder(),
            entity.getContentId()));
        recipeRepo.save(entity);
        Optional<RecipeEntity> entityOpt = recipeRepo.findByContentId(entity.getContentId());
        assertTrue(entityOpt.isPresent());
        RecipeEntity savedEntity = entityOpt.get();
        assertThat(savedEntity.getThumbnail(),
            startsWith("https://d229njkjc1dgnt.cloudfront.net/image/" + entity.getContentId() + ".jpg"));
    }

    @SneakyThrows
    @Test(enabled = false)
    public void createRecipeFullWorkflowTest() {
        StringDto stringDto = new StringDto().value("https://www.youtube.com/watch?v=U-0JCdjkREU");
        CreateRecipeResponse response = recipeService.createRecipe(stringDto);
        assertNotNull(response);
        assertTrue(response.getStatus().contains("processing"));
        Thread.sleep(60000);
    }

    @Test(enabled = false)
    public void getMostRecentRecipesTest() {
        recipeRepo.deleteAll();
        createRecipes();
        assertRecipeLiteResponse(recipeService.getMostRecentRecipes(0, 5));
    }

    @Test(enabled = false)
    public void getAllRecipesTest() {
        recipeRepo.deleteAll();
        createRecipes();
        assertRecipeLiteResponse(recipeService.getAllRecipes(0, 5));
    }

    @Test
    public void extractRecipeGptResponseTest() {
        var completionChoice = new CompletionChoice();
        completionChoice.setText("\n"
            + "Blog Title: \"Korean Braised Tofu: An Easy and Flavorful Meal Prep Recipe\"\n"
            + "\n"
            + "Summary: This Korean Braised Tofu is a delicious and easy meal prep recipe that is packed full of flavor and spice. It's perfect for a hearty and complete meal and can be easily stored in the fridge for future meals. The recipe includes extra firm tofu, water, rice vinegar, soy sauce, mirin, sugar, gochugaru or chili powder, sesame oil, garlic, and scallions. The tofu is cooked in batches in a wok with neutral oil until golden brown and then the braising liquid is added. The sauce is simmered for a few minutes and then the green parts of the scallions are added. Enjoy immediately or store for future meals!\n"
            + "\n"
            + "Recipe Steps:\n"
            + "\n"
            + "1. Start by washing off any sliminess from the extra firm or firm tofu and wrapping it in paper towels or a kitchen or dish towel. Use bananas in a basket for weight or a few pans or boards to press.\n"
            + "\n"
            + "2. Slice the tofu into 3-4 inch thick slices.\n"
            + "\n"
            + "3. Prepare the braising liquid or sauce by combining water, rice vinegar, soy sauce, mirin, sugar, gochugaru or chili powder, sesame oil, garlic, and scallions.\n"
            + "\n"
            + "4. Heat a wok with some neutral oil and add the tofu slices. Cook until the sides are golden brown and then flip to cook the other side.\n"
            + "\n"
            + "5. Use a paper towel to soak up any excess oil.\n"
            + "\n"
            + "6. Add the braising liquid or sauce to the pan and simmer for a few minutes.\n"
            + "\n"
            + "7. Allow the sauce to reduce and then add the green parts of the scallions.\n"
            + "\n"
            + "Ingredients:\n"
            + "\n"
            + "-Extra firm or firm tofu\n"
            + "-Water\n"
            + "-Rice vinegar\n"
            + "-Soy sauce\n"
            + "-Mirin\n"
            + "-Sugar\n"
            + "-Gochugaru or chili powder\n"
            + "-Sesame oil\n"
            + "-Garlic\n"
            + "-Scallions");
        GptResponse gptResponse = new GptResponse(0, List.of(completionChoice), null);
        RecipeGptResponse extracted = languageService.extractRecipeGptResponse(gptResponse);
        assertNotNull(extracted);
    }

    private void assertRecipeLiteResponse(PageableRecipeResponse response) {
        assertNotNull(response);
        assertEquals(2, response.getNumPages().intValue());
        assertEquals(5, response.getRecords().size());
        assertThat(response.getTotalRecords().intValue(), is(equalTo(numRecipes)));
        assertRecipeLite(response.getRecords());
    }

    private void assertRecipeLite(List<RecipeLite> records) {
        for (RecipeLite record : records) {
            assertThat(record.getTitle(), is(notNullValue()));
            assertThat(record.getSlug(), is(notNullValue()));
            assertThat(record.getThumbnail(), is(notNullValue()));
            assertThat(record.getCreatedAt(), is(notNullValue()));
            assertThat(record.getNumIngredients(), is(equalTo(20)));
            assertThat(record.getNumSteps(), is(equalTo(12)));
            assertThat(record.getCookingTime(), is(equalTo(40)));
        }
    }

    @SneakyThrows
    private void createRecipes() {
        List<RecipeEntity> entityList = new ArrayList<>();
        for (int i = 0; i < numRecipes; i++) {
            RecipeEntity entity = new RecipeEntity();
            entity.setTitle("How to Cook Frozen Pork Chops in the Oven");
            entity.setCanonicalUrl("https://www.youtube.com/watch?v=U-0JCdjkREU?a=" + i);
            entity.setContentId("U-0JCdjkREU-" + UUID.randomUUID());
            entity.setSummary("This Ground Beef Stir Fry Recipe is one you’ll add to your dinner rotation over and over! It uses simple ingredients and there are so many ways to customize it! Ground beef and veggies are coated in a delicious sweet…");
            entity.setCookingTimeMinutes((int) Math.round(Math.random() * 100));
            entity.setNumIngredients((int) Math.round(Math.random() * 100));
            entity.setNumSteps((int) Math.round(Math.random() * 100));
            entity.setProcessed(true);
            entity.setSlug("how-to-cook-chicken-well-" + UUID.randomUUID());
            entity.setThumbnail("https://picsum.photos/800/800");
            entity.setIngredients(objectMapper.writeValueAsString(List.of(
            "2 lbs boneless, skinless chicken chunks",
                "2 tablespoons freshly grated ginger root",
                "2 cloves garlic, finely minced",
                "2 tablespoons light brown sugar",
                "2 tablespoons rice vinegar",
                "2 tablespoons fish sauce",
                "1 tablespoon soy sauce",
                "Hot sauce, to taste",
                "1/4 cup chopped cilantro",
                "2 green onions, chopped",
                "1/4 cup roasted peanuts",
                "2 peppers (green jalapeno and red Jimmy dolo), sliced",
                "1 tablespoon vegetable oil")));
            entity.setInstructions(objectMapper.writeValueAsString(List.of("1. In a mixing bowl, combine grated ginger root, minced garlic, light brown sugar, rice vinegar, fish sauce, soy sauce and hot sauce. Whisk until combined.",
                "2. Place chicken chunks in a bowl and add 1/4 cup of the sauce mixture. Mix until all the chicken chunks are coated. Let sit for 15 minutes.",
                "3. Heat a heavy, large cast iron skillet over high heat. Add vegetable oil and let it smoke.",
                "4. Carefully add the chicken chunks to the skillet, spreading them out so they are in contact with the pan. Cook, stirring, until the chicken is caramelized.",
                "5. Reduce heat to medium and add the peppers, roasted peanuts and green onions. Cook for 1 minute.",
                "6. Add the remaining sauce and cook, stirring, until the sauce is thick and caramelized.",
                "7. Turn off the heat and stir in the chopped cilantro.",
                "8. Serve over rice and garnish with additional green onion, if desired. Enjoy!")));
            entityList.add(entity);
        }
        recipeRepo.saveAll(entityList);
    }
}
