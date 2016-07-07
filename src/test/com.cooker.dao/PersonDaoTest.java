package com.cooker.dao;

import com.cooker.resource.Person;
import com.cooker.util.MongoDBConnector;
import com.mongodb.MongoClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonDaoTest {
    private final static Logger logger = LoggerFactory.getLogger(PersonDaoTest.class);

    private MongoClient mongo;
    private Morphia morphia;
    private PersonDao personDao;

    private final String DB_NAME = "cooker";

    @Before
    public void initiate() {
        mongo = MongoDBConnector.getMongo();
        morphia = new Morphia();
        morphia.map(Person.class);
        personDao = new PersonDao(mongo, morphia, DB_NAME);
    }

    @Test
    public void test() {
        long counter = personDao.count();
        logger.debug("The count is [" + counter + "]");

        Person p = new Person();
        p.setFirstName("Thinh");
        p.setMiddleName("Hung");
        p.setLastName("Ly");
        p.setEmail("lyhungthinh@gmail.com");
        p.setPassword("123456");
        p.setRole("master");
        personDao.save(p);

        long newCounter = personDao.count();
        logger.debug("The new count is [" + newCounter + "]");
        Assert.assertTrue((counter + 1) == newCounter);

        personDao.delete(personDao.findByEmail("lyhungthinh@gmail.com"));
        newCounter = personDao.count();
        Assert.assertTrue(counter == newCounter);
    }
}