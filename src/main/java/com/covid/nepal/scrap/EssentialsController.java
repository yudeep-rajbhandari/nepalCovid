package com.covid.nepal.scrap;

import com.covid.nepal.config.MongoDocumentObject;
import com.covid.nepal.models.EssentialsModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EssentialsController {
    @Autowired
    private MongoDocumentObject mObject;
    private static final String ESSENTIALS_DB = "ESSENTIALS_GOODS";
    private static final Logger LOGGER = LoggerFactory.getLogger(EssentialsController.class);

    private String saveEssentials(EssentialsModel essentialsModel) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(essentialsModel);
        String docId = mObject.saveMongoDocument(new JSONObject(json),ESSENTIALS_DB);
        return docId;

    }
    public JSONArray getEssentials() throws Exception {
        JSONArray array = mObject.find(ESSENTIALS_DB);
        return array;

    }
    public void saveBulkEssentials(List<EssentialsModel> essentialsModel) throws Exception {
        essentialsModel.forEach(model->{
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(model);
                String docId = mObject.saveMongoDocument(new JSONObject(json),ESSENTIALS_DB);
            }
            catch (Exception e){
                LOGGER.error("Error uploading model {}",model);
            }

        });

    }
}
