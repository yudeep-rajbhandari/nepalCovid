package com.covid.nepal.scrap;


import com.covid.nepal.config.MongoDocumentObject;
import com.covid.nepal.models.NepalInformation;
import com.covid.nepal.models.ProvinceModelData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Component
@EnableScheduling
@EnableAsync
public class NepalCrawler {
    @Autowired
    private MongoDocumentObject mObject;

    @Autowired
    private WebClientConfig clientConfig;
    Document doc;
    private static final String NEPAL_DATA = "NEPAL_DATA";
    private static final String NEPAL_DATA_API = "NEPAL_DATA_API";
    private static final String PROVINCE_WISE_STATS = "PROVINCE_WISE_STATS";
    private static final String PROVINCE_WISE_STATS_CRON = "PROVINCE_WISE_STATS_CRON";



    Document doc1 = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(NepalCrawler.class);


    private  JSONObject prepareJSONNepaldata() throws SSLException, JsonProcessingException {
        WebClient client = clientConfig.NepalWebclient();
        WebClient.RequestBodySpec uri1 = client
                .method(HttpMethod.GET)
                .uri("/stats/?format=json");
        String response2 = uri1.exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        JSONObject object = new JSONObject(response2);
        NepalInformation nepalInformation = new NepalInformation();
        nepalInformation.setTotal_Samples_Tested(object.getInt("tested"));
        nepalInformation.setIsolation(object.getInt("isolation"));
        nepalInformation.setNegative(object.getInt("tested") -object.getInt("confirmed") );
        nepalInformation.setRecovered(object.getInt("confirmed")-object.getInt("isolation"));
        nepalInformation.setPositive(object.getInt("confirmed"));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(nepalInformation);

        return new JSONObject(json);
    }


    public JSONObject getfromAPI() throws SSLException, JsonProcessingException {

        return  prepareJSONNepaldata();
    }
    @Scheduled(fixedRate = 21600000)
    public void storefromAPI() throws Exception {
     JSONObject object = prepareJSONNepaldata();
        String documentId = mObject.saveMongoDocument(object,NEPAL_DATA);
        LOGGER.info("Data stored in mongo with documentId {}",documentId);

    }


//    @Scheduled(fixedRate = 4000)
    public JSONObject call() throws Exception {
        int ii = 0;
        while (doc1 == null) {
            try {
                ii++;
//                doc1 = SSLHelper.getConnection("https://heoc.mohp.gov.np/").timeout(0).userAgent(USER_AGENT).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Element row = doc1.selectFirst("div.emergency-box");
        JSONObject object = new JSONObject();

        ArrayList<String> str= new ArrayList<>();
        String[] value1 = row.select("li").get(0).text().split(" ",2);
        object.put(value1[0],value1[1]);
        row.select("li").forEach(element -> {
           element.select("span").forEach(elem->{
               str.add(elem.text());
           });
        });

        Map<String,Integer> map = null ;

        IntStream.range(0,str.size()).forEach(elem->{
            if(elem % 2 ==0){
               object.put(str.get(elem+1).contains(" ")?str.get(elem+1).replaceAll(" ","_"):str.get(elem+1),str.get(elem));
            }
        });
NepalInformation nepalInformation = new NepalInformation();
nepalInformation.setTotal_Samples_Tested(object.getInt("Total_Samples_Tested"));
nepalInformation.setIsolation(object.getInt("Isolation"));
nepalInformation.setNegative(object.getInt("Negative"));
nepalInformation.setRecovered(object.getInt("Recovered"));
nepalInformation.setPositive(object.getInt("Positive"));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(nepalInformation);
        mObject.deleteMongoCollectionData(NEPAL_DATA);
        String documentId = mObject.saveMongoDocument(new JSONObject(json),NEPAL_DATA);
        return object;
    }

    public JSONArray getJSONDataforProvince(){
        JSONArray array = mObject.find(PROVINCE_WISE_STATS);
        return array;
    }

    @Scheduled(fixedRate = 7200000)
    private void getJSONDataForProvince() throws Exception {
        WebClient client = clientConfig.NepalWebclient();
        WebClient.RequestBodySpec uri1 = client
                .method(HttpMethod.GET)
                .uri("/stats/?format=json&id=&province=all");
        String response2 = uri1.exchange()

                .block()
                .bodyToMono(String.class)
                .block();
        JSONArray array = new JSONArray(response2);
        mObject.deleteMongoCollectionData(PROVINCE_WISE_STATS);
        for(Object object:array){
            ProvinceModelData modelData =  new ProvinceModelData();
            JSONObject obj = (JSONObject)object;
            modelData.setProvince_name(obj.getString("province_name"));
            modelData.setFacility_count(obj.getInt("facility_count"));
            modelData.setNum_of_isolation_bed(obj.getInt("num_of_isolation_bed"));
            modelData.setOccupied_isolation_bed(obj.getInt("occupied_isolation_bed"));
            modelData.setTotal_death(obj.getInt("total_death"));
            modelData.setTotal_negative(obj.getInt("total_negative"));
            modelData.setTotal_in_isolation(obj.getInt("total_in_isolation"));
            modelData.setTotal_positive(obj.getInt("total_positive"));
            modelData.setTotal_recovered(obj.getInt("total_recovered"));
            modelData.setUpdate_date(obj.getString("update_date"));
            modelData.setTotal_tested(obj.getInt("total_tested"));
            modelData.setProvince_id(obj.getInt("province_id"));
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(modelData);
            String documentId = mObject.saveMongoDocument(new JSONObject(json),PROVINCE_WISE_STATS);
            LOGGER.debug("Data stored for province wise data for province {} with documentId {}",obj.getString("province_name"),documentId);
        }
    }
    @Scheduled(cron =  "0 58 23 * * *",zone = "UTC")
    private void getJSONDataForProvinceCron() throws Exception {
        WebClient client = clientConfig.NepalWebclient();
        WebClient.RequestBodySpec uri1 = client
                .method(HttpMethod.GET)
                .uri("/stats/?format=json&id=&province=all");
        String response2 = uri1.exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        JSONArray array = new JSONArray(response2);
        for(Object object:array) {
            String documentId = mObject.saveMongoDocument(new JSONObject(object),PROVINCE_WISE_STATS_CRON);
        }
        }

    }
