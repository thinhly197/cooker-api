package com.cooker.dao;

import com.cooker.resource.Person;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by thinhly on 7/6/16.
 */
public class PersonDao extends BasicDAO<Person, ObjectId> {

    // TODO: make singleton for DAO object

    public PersonDao(MongoClient mongo, Morphia morphia, String dbName) {
        super(mongo, morphia, dbName);
        Datastore ds = getDatastore();
        ds.ensureIndexes(); //creates all defined with @Indexed
        ds.ensureCaps(); //creates all collections for @Entity(cap=@CappedAt(...))
    }

    public Person findByEmail(String email){
        Query<Person> query = createQuery().filter("email", email).maxTime(2, TimeUnit.SECONDS);
        return findOne(query);
    }

    public List<Person> listAllPerson(int offset, int pageSize){
        return createQuery().offset(offset).limit(pageSize).asList();
    }
}