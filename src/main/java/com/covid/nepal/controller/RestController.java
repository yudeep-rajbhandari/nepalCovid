package com.covid.nepal.controller;


import com.covid.nepal.config.MongoDocumentObject;
import com.covid.nepal.models.*;
import com.covid.nepal.scrap.CoronaFearController;
import com.covid.nepal.scrap.NepalCrawler;
import com.covid.nepal.scrap.Scrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private MongoDocumentObject mObject;

    @Autowired
    private NepalCrawler crawler;

    @Autowired
    private CoronaFearController coronaFearController;

    private static final String SUSPECTED_CASES = "SUSPECTED_CASES";
    private static final String BLACK_MARKETING = "BLACK_MARKETING";
    private static final String MISINFORMATION = "MISINFORMATION";
    private static final String STATUS = "status";
    private static final String NEPAL_DATA = "NEPAL_DATA";

    @Autowired
    private Scrapper scrapper;

    @RequestMapping(value = "/allUpdate", method = RequestMethod.GET)
    @ResponseBody
    public String allcountryUpdate() {

        JSONArray array = scrapper.getdocument();
        if(array != null){
            return  array.toString();
        }
        else{
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/update/country/{country}", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin()
    public String updatebyCountry(@PathVariable("country") final String country) {

        JSONObject array = scrapper.getdocumentbyCountry(country);
        if(array != null){
            return  array.toString();
        }
        else{
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/coronaFear", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin()
    public String getCoronaFear() {

        JSONArray array = coronaFearController.getCoronaFear();
        if(array != null){
            return  array.toString();
        }
        else{
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/update/SAARC", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin()
    public String updatebySAARCCountry() {

        JSONArray array = scrapper.getdocumentbySAARC();
        if(array != null){
            return  array.toString();
        }
        else{
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/update/currentTotalcases", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public String totalupdate() {

        JSONObject array = scrapper.getTotalcases();
        if(array != null){
            return  array.toString();
        }
        else{
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/update/Nepal", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin()
    public String updateNepal() {

        JSONObject array = null;
        String api = null;
        try {
//            final Query idQuery = new Query();
//            idQuery.with(Sort.by(Sort.DEFAULT_DIRECTION,"created_date"));
//            array = mObject.findOne(idQuery,NEPAL_DATA);
             array = crawler.getfromAPI();
            System.out.println(array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(array != null){
            return  array.toString();
        }
        else{
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome() {

        return "Welcome to covid-19-data-centre. \n \nVisit Github repo: https://github.com/yudeep-rajbhandari/covid-19-api";
    }

    @RequestMapping(value = "/cases", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    @ResponseBody
    @CrossOrigin
    public CovidResponseBody saveSuspectedcase(@RequestBody final SuspectedCase obj) {
            try {
                obj.setStatus(false);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(obj);
                String documentId = mObject.saveMongoDocument(new JSONObject(json),SUSPECTED_CASES);
                return setSuccessResponse(documentId,obj);

            } catch (Exception e) {
                return setFailureResponse("Could not process",null);
            }
    }

    @RequestMapping(value = "/cases", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public String getSuspectedcase() {
        try {
             return mObject.getMongoDocumentANUoperator(STATUS,true,SUSPECTED_CASES).toString();
        } catch (Exception e) {
            return new JSONArray().toString();
        }
    }
    @RequestMapping(value = "/misinformation", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public String getMisinformation() {
        try {
            System.out.println(mObject.getMongoDocumentANUoperator(STATUS,true,MISINFORMATION).toString());
            return mObject.getMongoDocumentANUoperator(STATUS,true,MISINFORMATION).toString();

        } catch (Exception e) {
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/blackMarketing", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public String getBlackMarketing() {
        try {
            System.out.println(mObject.getMongoDocumentANUoperator(STATUS,true,BLACK_MARKETING).toString());
            return mObject.getMongoDocumentANUoperator(STATUS,true,BLACK_MARKETING).toString();

        } catch (Exception e) {
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/provinceData", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public String getProvinceData() {
        try {
            System.out.println(crawler.getJSONDataforProvince().toString());
            return crawler.getJSONDataforProvince().toString();

        } catch (Exception e) {
            return new JSONArray().toString();
        }
    }

    @RequestMapping(value = "/misinformation", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public CovidResponseBody saveMisinformation(@RequestBody final Misinformation obj) {
        try {
            obj.setStatus(false);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(obj);
            String documentId = mObject.saveMongoDocument(new JSONObject(json),MISINFORMATION);
            return setSuccessResponse(documentId,obj);
        } catch (Exception e) {
            return setFailureResponse("Could not process",null);
        }

    }


    @RequestMapping(value = "/coronaFear", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public CovidResponseBody saveCoronaFear(@RequestBody final CoronaFear obj) {
        try {
            String documentId = coronaFearController.saveCoronaFear(obj);
            return setSuccessResponse(documentId,obj);
        } catch (Exception e) {
            return setFailureResponse("Could not process",null);
        }

    }
    @RequestMapping(value = "/blackmarketing", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    @ResponseBody
    @CrossOrigin
    public CovidResponseBody saveBlackMarketing(@RequestBody final BlackMarketing obj) {
        try {
            obj.setStatus(false);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(obj);
            String documentId = mObject.saveMongoDocument(new JSONObject(json),BLACK_MARKETING);
            return setSuccessResponse(documentId,obj);
        } catch (Exception e) {
            return setFailureResponse("Could not process",null);
        }

    }

    private CovidResponseBody setSuccessResponse(String message,Object obj){
        CovidResponseBody responseBody = new CovidResponseBody();
        responseBody.setMessage(message);
        responseBody.setStatus(HttpStatus.ACCEPTED);
        responseBody.setObj(obj);
        return responseBody;
    }

    private CovidResponseBody setFailureResponse(String message,Object obj){
        CovidResponseBody responseBody = new CovidResponseBody();
        responseBody.setMessage(message);
        responseBody.setStatus(HttpStatus.NOT_FOUND);
        responseBody.setObj(obj);
        return responseBody;
    }

    private GetCovidResponseBody setGetSuccessResponse(String message,JSONArray obj){
        GetCovidResponseBody responseBody = new GetCovidResponseBody();
        responseBody.setMessage(message);
        responseBody.setStatus(HttpStatus.ACCEPTED);
        responseBody.setArray(obj);
        return responseBody;
    }

    private GetCovidResponseBody setGetFailureResponse(String message,JSONArray obj){
        GetCovidResponseBody responseBody = new GetCovidResponseBody();
        responseBody.setMessage(message);
        responseBody.setStatus(HttpStatus.NOT_FOUND);
        responseBody.setArray(null);
        return responseBody;
    }


}
