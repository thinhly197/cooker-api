package com.cooker.dao;

import com.cooker.resource.FoodCategory;
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
 * Created by thinhly on 7/8/16.
 */
@Repository
public class FoodCategoryDao extends BasicDAO<FoodCategory, ObjectId> {

    public FoodCategoryDao(MongoClient mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
//        Datastore ds = getDatastore();
//        ds.ensureCaps();
    }

    public FoodCategory findByIndex(int index) {
        Query<FoodCategory> query = createQuery().filter("index", index).maxTime(2, TimeUnit.SECONDS);
        return findOne(query);
    }

    public FoodCategory addByLanguage(int languageIndex, String categoryName) {
        int count = (int) count() + 1;
        FoodCategory ct = new FoodCategory(count, categoryName, languageIndex);
        save(ct);
        return ct;
    }

    public void updateByLanguage(int languageIndex, String categoryName, int index) {
        FoodCategory ct = findByIndex(index);
        List<String> values = ct.getName();
        values.set(languageIndex, categoryName);
        ct.setName(values);

        Query<FoodCategory> updateQuery = createQuery().field(Mapper.ID_KEY).equal(ct.getId());
        UpdateOperations<FoodCategory> ops;
        ops = createUpdateOperations().set("name", ct.getName());
        update(updateQuery, ops);
    }
}
