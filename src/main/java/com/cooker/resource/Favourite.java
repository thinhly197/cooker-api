package com.cooker.resource;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.List;

/**
 * Created by thinhly on 7/13/16.
 */
@Entity
@ApiModel( value = "Favourite", description = "List of users's favourite recipes" )
public class Favourite {
    @Id
    private ObjectId id;
    @Property
    @ApiModelProperty( value = "Email of user", required = true )
    private String user;
    @Property
    @ApiModelProperty( value = "List of favourites", required = true )
    private List<Integer> listOfFavourites;

    public Favourite(String user, List<Integer> listOfFavourites) {
        this.user = user;
        this.listOfFavourites = listOfFavourites;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Integer> getListOfFavourites() {
        return listOfFavourites;
    }

    public void setListOfFavourites(List<Integer> listOfFavourites) {
        this.listOfFavourites = listOfFavourites;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
