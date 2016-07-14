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
    private long index;

    @Indexed(value= IndexDirection.ASC, name="recipe_author_indx", background=true, unique=true,
            dropDups=true, sparse = false, expireAfterSeconds = -1)
    @Property @ApiModelProperty( value = "The name of the recipe", required = true )
    private List<String> name = new ArrayList<>(Language.values().length);

    @Property @ApiModelProperty( value = "The Ingredient of the recipe", required = true )
    //private List<Ingredient> ingredientList;
    private List<String> ingredients = new ArrayList<>(Language.values().length);

    @Property @ApiModelProperty( value = "The Preparation of the recipe", required = true )
    //private List<Preparation> preparationList;
    private List<String> preparations = new ArrayList<>(Language.values().length);

    @Property @ApiModelProperty( value = "The category of the recipe", required = true )
    private List<Integer> categoryIndex;

    @Property @ApiModelProperty( value = "The author of the recipe", required = true )
    private String personEmail;

    @Property @ApiModelProperty( value = "The number of the recipe's view", required = false )
    private long view;

    //private List<String> comments;


    public Recipe() {}

    public Recipe(long index, String recipeName, String ingredientList, String preparationList,
                  List<Integer> categoryIndex, String personEmail, int languageIndex) {
        this.index = index;
        for(int i = 0; i < Language.values().length; i++){
            if(i == languageIndex){
                this.name.add(recipeName);
                this.ingredients.add(ingredientList);
                this.preparations.add(preparationList);
            } else {
                this.name.add("");
                this.ingredients.add("");
                this.preparations.add("");
            }
        }
        this.categoryIndex = categoryIndex;
        this.personEmail = personEmail;
        this.view = 0;
    }

    public Recipe(long index, String recipeName, String ingredientList, String preparationList,
                  List<Integer> categoryIndex, String personEmail, long view, int languageIndex) {
        this.index = index;
        for(int i = 0; i < Language.values().length; i++){
            if(i == languageIndex){
                this.name.set(i, recipeName);
                this.ingredients.set(i, ingredientList);
                this.preparations.set(i, preparationList);
            } else {
                this.name.set(i, "");
                this.ingredients.set(i, "");
                this.preparations.set(i, "");
            }
        }
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

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
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

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getPreparations() {
        return preparations;
    }

    public void setPreparations(List<String> preparations) {
        this.preparations = preparations;
    }
}
