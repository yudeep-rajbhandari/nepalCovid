package com.covid.nepal.scrap;


import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

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
        for (int i = 0; i <3 ; i++) {
            String[] mainValue =  row.select("li").get(i).text().split(" ",2);
            String key =  mainValue[1].replaceAll(" ","_");
            if(key.contains("Positive")){
                object.put("Positive",mainValue[0]);
                String str = key.replaceAll("[^0-9]+", " ");
                List number = Arrays.asList(str.trim().split(" "));
                object.put("Recovered",number.get(0));
                object.put("Isolation",number.get(1));
            }
            String value =  mainValue[0];
            object.put(key,value);

        }
        return object;
    }
}