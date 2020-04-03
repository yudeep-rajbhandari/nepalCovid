package com.covid.nepal.scrap;

import com.covid.nepal.config.MongoDocumentObject;
import com.covid.nepal.models.CoronaFear;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoronaFearController {
    @Autowired
    private MongoDocumentObject mObject;

    private static final String CORONA_FEAR = "CORONA_FEAR";
    private static final String STATUS = "status";


    public String saveCoronaFear(CoronaFear coronaFear) throws Exception {
        coronaFear.setStatus(false);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(coronaFear);

        String documentId = mObject.saveMongoDocument(new JSONObject(json),CORONA_FEAR);
        return  documentId;

    }
    public JSONArray getCoronaFear(){
        JSONArray array = mObject.getMongoDocumentANUoperator(STATUS,true,CORONA_FEAR);
        return array;

    }

}
