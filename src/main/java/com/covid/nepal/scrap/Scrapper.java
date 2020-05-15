package com.covid.nepal.scrap;


import com.covid.nepal.config.MongoDocumentObject;
import com.covid.nepal.models.CountryData;
import com.covid.nepal.models.NepalInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Component
@EnableScheduling
@EnableAsync
public class Scrapper {
    Document doc;
    List header;
    List<Elements> list;
    private static final String SAARC_DATA = "SAARC_DATA";


    @Autowired
    private WebClientConfig clientConfig;

    @Autowired
    private MongoDocumentObject mObject;


    public JSONArray getdocument() {
        getRows();
        JSONArray array = getJSONArray(list, header);
        return array;
    }

    public JSONObject getdocumentbyCountry(String country) {
        getRows();
        JSONObject array = getJSONObject(list, header, country);
        return array;
    }

    public JSONArray getdocumentbySAARC() {
        getRows();
        JSONArray array = getSAARCJSONObject(list, header);
        return array;
    }

    private void getRows() {
        try {
            doc = SSLHelper.getConnection("https://www.worldometers.info/coronavirus/").userAgent(USER_AGENT).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements table = doc.select("#main_table_countries_today");
        Elements rows = table.select("tr");
        Elements headers = rows.get(0).select("tr").select("th").select("th");
        header = headers.stream().map(i -> i.text().replaceAll(" ", "_")).collect(Collectors.toList());
        list = rows.stream().map(i -> i.select("td").select("td")).collect(Collectors.toList());
    }

    private JSONArray getJSONArray(List<Elements> list, List header) {
        JSONArray array = new JSONArray();
        ArrayList<String> lim = new ArrayList();
        if (list != null) {
            for (Elements li : list) {
                if (li.size() != 0) {
                    int i = 0;
                    JSONObject obj = new JSONObject();
                    for (Element l : li) {
                        obj.put(header.get(i).toString(), l.text());
                        i = i + 1;
                    }
                    if (list.indexOf(li) != list.size() - 1) {
                        array.put(obj);
                    }

                }
            }
        }
        return array;
    }

    private JSONObject getJSONObject(List<Elements> list, List header, String country) {
        JSONObject obj = new JSONObject();
        if (list != null) {
            List<Elements> dataCountry = list.stream().filter(i -> i.size() != 0).filter(i -> i.select("td").get(0).text().replaceAll(" ", "_").equalsIgnoreCase(country)).collect(Collectors.toList());
            for (Elements li : dataCountry) {
                int i = 0;
                for (Element l : li) {
                    obj.put(header.get(i).toString(), l.text());
                    i = i + 1;
                }
            }
        }

        return obj;
    }


    private JSONArray getSAARCJSONObject(List<Elements> list, List header) {
        JSONArray array = new JSONArray();
        List<String> saarcCountry = Arrays.asList("India","Nepal","Pakistan","Bangladesh","Sri_lanka","Bhutan");
        for(String country:saarcCountry) {
            JSONObject obj = new JSONObject();
            if (list != null) {

                List<Elements> dataCountry = list.stream().filter(i -> i.size() != 0).filter(i -> i.select("td").get(1).text().replaceAll(" ", "_").equalsIgnoreCase(country)).collect(Collectors.toList());
                for (Elements li : dataCountry) {
                    int i = 0;
                    for (Element l : li) {
                        obj.put(header.get(i).toString().equalsIgnoreCase("Tests/_1M_pop")?"TestIn1M":header.get(i).toString(), l.text());
                        obj.put(header.get(i).toString().equalsIgnoreCase("Country,_Other")?"country":header.get(i).toString(), l.text());
                        obj.put(header.get(i).toString().equalsIgnoreCase("Tot_Cases/_1M_pop")?"TotIn1M":header.get(i).toString(), l.text());
                        obj.put(header.get(i).toString().equalsIgnoreCase("Deaths/_1M_pop")?"DIn1M":header.get(i).toString(), l.text());

                        i = i + 1;

                    }
                }
            }
            array.put(obj);
        }

        return array;
    }
    private  JSONObject prepareJSONSAARCdata(WebClient client,String country) throws SSLException, JsonProcessingException {

        JSONArray array = new JSONArray();

            WebClient.RequestBodySpec uri1 = client
                    .method(HttpMethod.GET)
                    .uri("/"+country);
            String response2 = uri1.exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();
            JSONObject object = new JSONObject(response2);
            CountryData countryData = new CountryData();
            countryData.setActive_Cases(object.getInt("active"));
            countryData.setCountry(object.getString("country"));
            try{
                countryData.setDIn1M(object.getDouble("deathsPerOneMillion"));
            }
            catch (Exception e){
                countryData.setDIn1M(null);
            }
            countryData.setTotal_Deaths(object.getInt("deaths"));
        countryData.setTotIn1M(object.getDouble("casesPerOneMillion"));
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(countryData);
        return new JSONObject(json);
    }

    public JSONArray getSAARCJSONObjectAPI() throws SSLException, JsonProcessingException {
        JSONArray array = new JSONArray();
        List<String> saarcCountry = Arrays.asList("India","Nepal","Pakistan","Bangladesh","Sri Lanka","Bhutan");
        WebClient client = clientConfig.WorldWebclient();
        for(String country:saarcCountry){
            array.put(prepareJSONSAARCdata(client,country));
        }
        return array;
    }
    @Scheduled(cron =  "0 17 13 ? * MON,TUE,WED,THU,FRI*",zone = "UTC")
    private JSONArray getSAARCJSONObjectAPICron() throws Exception {
        JSONArray array = new JSONArray();
        List<String> saarcCountry = Arrays.asList("India","Nepal","Pakistan","Bangladesh","Sri Lanka","Bhutan");
        WebClient client = clientConfig.WorldWebclient();
        for(String country:saarcCountry){
            mObject.saveMongoDocument(prepareJSONSAARCdata(client,country),SAARC_DATA);
        }
        return array;
    }



    public JSONObject getTotalcases() {
        getRows();
        JSONObject array = getlastJSONObject(list, header);
        return array;
    }

    private JSONObject getlastJSONObject(List<Elements> list, List header) {
        JSONObject obj = new JSONObject();

        if (list != null) {
            List<Elements> dataCountry = Collections.singletonList(list.stream().reduce((a, b) -> b).orElse(null));;

            for (Elements li : dataCountry) {
                int i = 0;
                for (Element l : li) {
                    obj.put(header.get(i).toString(), l.text());
                    i = i + 1;
                }
            }
            System.out.println("abcd");
        }
        return obj;
    }
}
