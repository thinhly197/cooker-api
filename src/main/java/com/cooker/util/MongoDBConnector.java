package com.cooker.util;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by thinhly on 7/6/16.
 */
public class MongoDBConnector {
    private final static Logger logger = LoggerFactory.getLogger(MongoDBConnector.class);

    private static final int port = 27017;
    private static final String host = "localhost";
    private static MongoClient mongo = null;

    public static MongoClient getMongo() {
        if (mongo == null) {
            try {
                mongo = new MongoClient(host, port);
                logger.debug("New MongoClient created with [" + host + "] and [" + port + "]");
            } catch (UnknownHostException | MongoException e) {
                logger.error(e.getMessage());
            }
        }
        return mongo;
    }
}
