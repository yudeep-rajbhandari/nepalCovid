package com.covid.nepal.scrap;


import com.covid.nepal.models.NepalInformation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.USER_AGENT;


public class NepalCrawler implements Callable {
    Document doc;

    Document doc1 = null;

    @Override
    public JSONObject call() throws Exception {
        int ii = 0;
        while (doc1 == null) {
            try {
//            Connection connection = Jsoup.connect("http://heoc.mohp.gov.np").timeout(30000);
//            Document document = connection.get();
//            Elements metas =  document.getElementsByTag("#emergency-box");
                    ii++;
                doc1 = SSLHelper.getConnection("https://heoc.mohp.gov.np/").timeout(0).userAgent(USER_AGENT).get();


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
               // map.put(str.get(elem+1).contains(" ")?str.get(elem+1).replaceAll(" ","_"):str.get(elem+1),str.get(elem));
            }
        });

//        object.put(str.get(1),str.get(0));
//        object.put(str.get(3),str.get(2));
//        object.put(str.get(5),str.get(4));
//        object.put(str.get(7),str.get(6));
//        object.put(str.get(9),str.get(8));
//
//
//
//
//
//
//        for (int i = 0; i <3 ; i++) {
//            String[] mainValue =
//            if(i==0){
//                String key =  mainValue[1].replaceAll(" ","_");
//                object.put(key,mainValue[0]);
//            }
//            else{
//                Elements key = row.select("li").get(i).select("div");
//                key.forEach(elem->{
//                    String
//                });
//            }
//
//            if(key.contains("Positive")){
//                object.put("Positive",mainValue[0]);
//                String str = key.replaceAll("[^0-9]+", " ");
//                List number = Arrays.asList(str.trim().split(" "));
//                object.put("Recovered",number.get(0));
//                object.put("Isolation",number.get(1));
//            }
//            String value =  mainValue[0];
//            object.put(key,value);
//
//        }
        return object;
    }
}