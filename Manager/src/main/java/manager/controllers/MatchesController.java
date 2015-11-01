package manager.controllers;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by joao on 9/7/15.
 */
@RestController
public class MatchesController {
    @RequestMapping("/api/matches")
    public String home() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("heraclitus");
        List<Document> data = new ArrayList<>();
        FindIterable<Document> iterable = db.getCollection("matches").find().sort(new BasicDBObject("timelog",-1)).limit(1000);
        iterable.forEach((Block<Document>) document -> {
            data.add(document);
        });
        String test = JSON.serialize(data);
        return JSON.serialize(data);
    }

}
