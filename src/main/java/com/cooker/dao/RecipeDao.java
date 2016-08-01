package com.cooker.dao;

import com.cooker.resource.FoodCategory;
import com.cooker.resource.Ingredient;
import com.cooker.resource.Preparation;
import com.cooker.resource.Recipe;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by thinhly on 7/12/16.
 */
@Repository
public class RecipeDao extends BasicDAO<Recipe, ObjectId> {

    public RecipeDao(MongoClient mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
        ensureIndexes();
    }

    public Recipe findByIndex(int index) {
        Query<Recipe> query = createQuery().filter("index", index).maxTime(2, TimeUnit.SECONDS);
        return findOne(query);
    }
}
