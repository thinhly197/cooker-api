package com.cooker.resource;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.utils.IndexDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinhly on 7/12/16.
 */
@Entity
@ApiModel( value = "Recipe", description = "Recipe for food" )
public class Recipe {
    @Id
    private ObjectId id;

    @Property @ApiModelProperty( value = "The index of the recipe", required = true )
    private int index;

    @Indexed(value= IndexDirection.ASC, name="recipe_author_indx", background=true, unique=true,
            dropDups=true, sparse = false, expireAfterSeconds = -1)
    @Property @ApiModelProperty( value = "The name of the recipe", required = true )
    private List<String> name = new ArrayList<>(Language.values().length);

    @Property @ApiModelProperty( value = "The Ingredient of the recipe", required = true )
    private List<Ingredient> ingredientList;

    @Property @ApiModelProperty( value = "The Preparation of the recipe", required = true )
    private List<Preparation> preparationList;

    @Property @ApiModelProperty( value = "The category of the recipe", required = true )
    private List<Integer> categoryIndex;

    @Property @ApiModelProperty( value = "The author of the recipe", required = true )
    private String personEmail;

    @Property @ApiModelProperty( value = "The number of the recipe's view", required = false )
    private long view;

    public void addIngredient(Ingredient ingredient) {
        ingredientList.add(ingredient);
    }

    public void addPreparation(Preparation preparation) {
        preparationList.add(preparation);
    }

    public Recipe() {}

    public Recipe(int index, List<String> name, List<Ingredient> ingredientList,
                  List<Preparation> preparationList, List<Integer> categoryIndex, String personEmail) {
        this.index = index;
        this.name = name;
        this.ingredientList = ingredientList;
        this.preparationList = preparationList;
        this.categoryIndex = categoryIndex;
        this.personEmail = personEmail;
        this.view = 0;
    }

    public Recipe(int index, List<String> name, List<Ingredient> ingredientList,
                  List<Preparation> preparationList, List<Integer> categoryIndex, String personEmail, long view) {
        this.index = index;
        this.name = name;
        this.ingredientList = ingredientList;
        this.preparationList = preparationList;
        this.categoryIndex = categoryIndex;
        this.personEmail = personEmail;
        this.view = view;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<Integer> getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(List<Integer> categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public long getView() {
        return view;
    }

    public void setView(long view) {
        this.view = view;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<Preparation> getPreparationList() {
        return preparationList;
    }

    public void setPreparationList(List<Preparation> preparationList) {
        this.preparationList = preparationList;
    }
}
