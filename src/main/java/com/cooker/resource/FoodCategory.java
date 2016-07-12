package com.cooker.resource;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.CappedAt;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinhly on 7/6/16.
 */
//specifying that the collection should be capped with a maximum of 20 documents and a size of 4096 bytes.
@Entity(value="cappedCategories", cap=@CappedAt(count=20))//, value=4096))
@ApiModel( value = "FoodCategory", description = "Categories of food (include Text and Index)" )
public class FoodCategory {

    @Id private ObjectId id;
    @Property
    @ApiModelProperty( value = "Name of category. It can has many languages. The order is followed by Language Enum",
            required = true )
    private List<String> name = new ArrayList<>(Language.values().length);
    private int index;

    public FoodCategory(int index, String categoryName, int languageIndex) {
        this.index = index;
        for(int i = 0; i < Language.values().length; i++){
            if(i == languageIndex){
                name.add(categoryName);
            } else {
                name.add("");
            }
        }
    }

    public FoodCategory(List<String> name, int index) {
        this.name = name;
        this.index = index;
    }

    public FoodCategory() {

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
