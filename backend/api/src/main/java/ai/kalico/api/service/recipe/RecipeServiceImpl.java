package ai.kalico.api.service.recipe;

import ai.kalico.api.data.postgres.entity.RecipeEntity;
import ai.kalico.api.data.postgres.projection.UserProjectProjection;
import ai.kalico.api.data.postgres.repo.RecipeRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.ContentItem;
import com.kalico.model.CreateRecipeResponse;
import com.kalico.model.PageableRecipeResponse;
import com.kalico.model.RecipeFull;
import com.kalico.model.RecipeLite;
import com.kalico.model.StringDto;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 3/24/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
  private final RecipeRepo recipeRepo;
  private final ObjectMapper objectMapper;
  
  @Override
  public CreateRecipeResponse createRecipe(StringDto stringDto) {
    RecipeEntity recipeEntity = new RecipeEntity();
    recipeEntity.setTitle("Untitled");
    recipeEntity.setCanonicalUrl(stringDto.getValue());
    recipeRepo.save(recipeEntity);
    return new CreateRecipeResponse().status(stringDto.getValue());
  }

  @Override
  public PageableRecipeResponse getAllRecipes(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size,
        Sort.by("created_at").descending());
    Page<RecipeEntity> pageResult = recipeRepo.findAllRecipes(pageable);
    PageableRecipeResponse response = new PageableRecipeResponse();
    response.setTotalRecords(pageResult.getTotalElements());
    response.setNumPages(pageResult.getTotalPages());
    response.setRecords(pageResult
        .getContent()
        .stream()
        .map(this::getLiteRecipe)
        .collect(Collectors.toList()));
    return response;
  }

  @Override
  public RecipeFull getFullRecipe(String slug) {
    Optional<RecipeEntity> entityOpt = recipeRepo.findBySlug(slug);
    if (entityOpt.isPresent()) {
      RecipeEntity entity = entityOpt.get();
      return new RecipeFull()
          .summary(entity.getSummary())
          .ingredients(stringToList(entity.getIngredients()))
          .instructions(stringToList(entity.getInstructions()))
          .recipeLite(getLiteRecipe(entity));
    }
    return null;
  }

  private List<String> stringToList(String s) {
    TypeReference<List<String>> typeRef = new TypeReference<>() {};
    try {
      return objectMapper.readValue(s, typeRef);
    } catch (JsonProcessingException e) {
      log.error(e.getLocalizedMessage());
    }
    return new ArrayList<>();
  }

  @Override
  public PageableRecipeResponse getMostRecentRecipes(Integer page, Integer size) {
    return getAllRecipes(page, size);
  }

  private RecipeLite getLiteRecipe(RecipeEntity entity) {
    return new RecipeLite()
        .cookingTime(entity.getCookingTimeMinutes())
        .description(entity.getDescription())
        .slug(entity.getSlug())
        .numIngredients(entity.getNumIngredients())
        .numSteps(entity.getNumSteps())
        .title(entity.getTitle())
        .thumbnail(entity.getThumbnail())
        .createdAt(entity
            .getCreatedAt()
            .toEpochSecond(ZoneOffset.UTC));
  }

  private List<String> toStringList(Object[] objects) {
    return Arrays
        .stream(objects)
        .map(it -> (String) it)
        .collect(Collectors.toList());
  }
}
