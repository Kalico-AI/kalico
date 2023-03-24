package ai.kalico.api.service.recipe;

import com.kalico.model.CreateRecipeResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;
import com.kalico.model.StringDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 3/24/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

  @Override
  public CreateRecipeResponse createRecipe(StringDto stringDto) {
    return new CreateRecipeResponse().status(stringDto.getValue());
  }

  @Override
  public PageableRecipeResponse getAllRecipes(Integer page, Integer size) {
    return new PageableRecipeResponse();
  }

  @Override
  public RecipeFull getFullRecipe(String slug) {
    return new RecipeFull();
  }
}
