package com.cooker.services;

import com.cooker.dao.RecipeDao;
import com.cooker.resource.Ingredient;
import com.cooker.resource.Language;
import com.cooker.resource.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by thinhly on 7/12/16.
 */
@Service
public class RecipeService {
    @Autowired private RecipeDao recipeDao;

    public Recipe create(int languageIndex, String recipeName, String ingredients, String preparations,
                         List<Integer> categories, String author) {
        long count = recipeDao.count() + 1;
        Recipe recipe = new Recipe(count, recipeName, ingredients, preparations, categories, author, languageIndex);
        recipeDao.save(recipe);
        return recipe;
    }
}
