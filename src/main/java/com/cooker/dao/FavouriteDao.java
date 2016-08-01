package com.cooker.dao;

import com.cooker.resource.Favourite;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Created by thinhly on 7/8/16.
 */
@Repository
public class FavouriteDao extends BasicDAO<Favourite, ObjectId> {

    public FavouriteDao(MongoClient mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
    }

    public Favourite findByUser(String userEmail) {
        Query<Favourite> query = createQuery().filter("user", userEmail).maxTime(2, TimeUnit.SECONDS);
        return findOne(query);
    }

    public Favourite add(String userEmail, long recipeIndex) {
        Favourite favourite = findByUser(userEmail);
        Query<Favourite> updateQuery = createQuery().field(Mapper.ID_KEY).equal(favourite.getId());
        UpdateOperations<Favourite> ops;
        ops = ds.createUpdateOperations(Favourite.class).add("listOfFavourites", recipeIndex);
        ds.update(updateQuery, ops, true);
        return favourite;
    }

}
