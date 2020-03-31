package com.covid.nepal.config;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MongoDocumentObject {
    public static final String DOCUMENT_ID_KEY = "_id";
    public static final String CREATED_DATE_KEY = "created_date";
    public static final String UPDATE_KEY = "update_date";
    private static final String SUSPECTED_CASES = "SUSPECTED_CASES";
    private static final String BLACK_MARKETING = "BLACK_MARKETING";
    private static final String MISINFORMATION = "MISINFORMATION";
    private static final String NEPAL_DATA = "NEPAL_DATA";
    private static final String SAARC_DATA = "SAARC_DATA";


    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDocumentObject.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void createCollection(){
        if(!checkCollection(SUSPECTED_CASES)){
            createCollection(SUSPECTED_CASES);
            LOGGER.info("Creating the collection {}",SUSPECTED_CASES);
        }
        if(!checkCollection(BLACK_MARKETING)){
            createCollection(BLACK_MARKETING);
            LOGGER.info("Creating the collection {}",BLACK_MARKETING);
        }
        if(!checkCollection(MISINFORMATION)){
            createCollection(MISINFORMATION);
            LOGGER.info("Creating the collection {}",MISINFORMATION);
        }
        if(!checkCollection(NEPAL_DATA)){
            createCollection(NEPAL_DATA);
            LOGGER.info("Creating the collection {}",NEPAL_DATA);
        }
        if(!checkCollection(SAARC_DATA)){
            createCollection(SAARC_DATA);
            LOGGER.info("Creating the collection {}",SAARC_DATA);
        }
    }

    public boolean checkCollection(final String collectionName) {
        LOGGER.debug("Checking Collection Name exists");
        return mongoTemplate.collectionExists(collectionName);
    }

    public String saveMongoDocument(final JSONObject jsonDocument, final String collectionName) throws Exception {
        String documentId = null;
        if (null != jsonDocument && jsonDocument.has(DOCUMENT_ID_KEY)) {
            documentId = jsonDocument.getString(DOCUMENT_ID_KEY);
        } else {

            documentId = UUID.randomUUID().toString();
            jsonDocument.put(DOCUMENT_ID_KEY, documentId);
        }
        try{
            if (!jsonDocument.has(CREATED_DATE_KEY)) {
                jsonDocument.put(CREATED_DATE_KEY, new Date().toString());
            }
            mongoTemplate.save(Document.parse(jsonDocument.toString()), collectionName);
        }
        catch (Exception e){
            LOGGER.error("Exception while saving the json object -> {} in the collection -> {}",
                    jsonDocument.toString(), collectionName);
            LOGGER.error(e.getLocalizedMessage(), e);
            throw new Exception(e.getLocalizedMessage(),e);

        }
        return documentId;
    }
    public JSONArray find(final String collectionName) {
        JSONArray results = null;

        final List<Document> documents = mongoTemplate.findAll(Document.class, collectionName);

        if (null != documents && !documents.isEmpty()) {
            LOGGER.debug("Found Objects");
            results = new JSONArray();
            for (Document document : documents) {

                results.put(new JSONObject(document.toJson()));
            }
        } else {
            LOGGER.debug("Not Found Objects");
        }
        return results;
    }
    public JSONArray find(final Query filter, final String collectionName) {
        //LOGGER.debug("Find objects for filter:{}, pageNumber: {}, pageSize:{} in collection: {}", filter, collectionName);
        JSONArray results = null;

        final List<Document> documents = mongoTemplate.find(filter, Document.class, collectionName);

        if (null != documents && !documents.isEmpty()) {
            //LOGGER.debug("Found Objects");
            results = new JSONArray();
            for (Document document : documents) {

                results.put(new JSONObject(document.toJson()));
            }
        } else {
            LOGGER.debug("No Objects found for filter:{}, pageNumber: {}, pageSize:{} in collection: {}", filter, collectionName);
        }
        return results;
    }

    public JSONArray getMongoDocumentANDoperator(final String field1ToSearch,
                                                 final String field2ToSearch, final String valueToSearch1,
                                                 final String valueToSearch2, final String collectionName) {
        final Criteria criteria1 = Criteria.where(field1ToSearch)
                .is(valueToSearch1);
        final Criteria criteria2 = Criteria.where(field2ToSearch)
                .is(valueToSearch2);
        final Criteria criteria = new Criteria();
        criteria.andOperator(criteria1, criteria2);
        final Query searchQuery = new Query(criteria);

        return find(searchQuery, collectionName);
    }
    public JSONArray getMongoDocumentANUoperator(final String field1ToSearch,
                                                final boolean valueToSearch1,
                                                 final String collectionName) {
        final Criteria criteria1 = Criteria.where(field1ToSearch)
                .is(valueToSearch1);
        final Query searchQuery = new Query(criteria1);

        return find(searchQuery, collectionName);
    }
    public void deleteMongoDocument(final String documentId, final String collectionName) {
        LOGGER.debug("Finding the mongo document with id -> {} to remove from collection -> {}", documentId,
                collectionName);
        try {
            final Query idQuery = new Query();
            idQuery.addCriteria(Criteria.where(DOCUMENT_ID_KEY).is(documentId));
            final Document document = mongoTemplate.findById(documentId, Document.class, collectionName);
            mongoTemplate.remove(document, collectionName);
            LOGGER.debug("Found the mongo document with id -> {} and removed from collection -> {}", documentId,
                    collectionName);
        } catch (Exception exception) {
            LOGGER.debug("Unable to find the mongo document with id -> {} to remove from collection -> {}", documentId,
                    collectionName);
            LOGGER.error("Exception occured in deleteMongoDocument", exception);
        }
    }

    public void updateMongoDocument(final JSONObject jsonDocument, final String collection)
            throws Exception {

        try {
            final Document document = Document.parse(jsonDocument.toString());
            final Update dbUpdate = Update.fromDocument(document, "");
            dbUpdate.set(UPDATE_KEY, new Date().toString());

            final Query idQuery = new Query();
            idQuery.addCriteria(Criteria.where(DOCUMENT_ID_KEY).is(jsonDocument.getString(DOCUMENT_ID_KEY)));

            mongoTemplate.updateFirst(idQuery, dbUpdate, collection);

        } catch (Exception exception) {
            LOGGER.error("Exception while saving the json object -> {}, in the location -> {}", jsonDocument.toString(),
                    collection);
            LOGGER.error(exception.getLocalizedMessage(), exception);
            throw new Exception(
                    String.format("Exception while saving the json object -> %s, in the collection -> %s",
                            jsonDocument.toString(), collection),
                    exception);
        }
    }
    public void createCollection(final String collectionName) {

        LOGGER.debug("Create collection in Mongo -> {}", collectionName);
        try {

            mongoTemplate.createCollection(collectionName);
        } catch (Exception exception) {
            LOGGER.debug("Unable to create new collection in mongo -> {}",
                    collectionName);
            LOGGER.error("Exception occured in createCollection", exception);
        }
    }

    public void deleteMongoCollectionData(final String collectionName) {
        LOGGER.debug("Remove data from collection -> {}", collectionName);
        try {
            mongoTemplate.getCollection(collectionName).deleteMany(new Document());

        } catch (Exception exception) {
            LOGGER.debug("Unable to delete data in mongo for collection -> {}",
                    collectionName);
            LOGGER.error("Exception occured in deleteMongoCollectionData", exception);
        }
    }

    public JSONObject findOne(final Query filter, final String collectionName) {
        //LOGGER.debug("Find objects for filter:{}, pageNumber: {}, pageSize:{} in collection: {}", filter, collectionName);
        JSONObject result = null;

        final Document document = mongoTemplate.findOne(filter, Document.class, collectionName);
        final Optional<Document> documentOpt = Optional.ofNullable(document);

        if (documentOpt.isPresent()) {
            //LOGGER.debug("Found Object one");
            result = new JSONObject(document.toJson());
        } else {
            LOGGER.debug("No Objects found for filter:{}, pageNumber: {}, pageSize:{} in collection: {}", filter, collectionName);
        }
        return result;
    }

}
