package com.covid.nepal.scrap;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig{

    @Value("${nepal.url.value:https://covidapi.mohp.gov.np/api/v1}")
    private String NepalUrl;

    @Value("${world.url.value:https://corona.lmao.ninja/countries}")
    private String WorldUrl;


    public ClientHttpConnector getConnector() throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        HttpClient httpClient = HttpClient
                .create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        return  connector;

    }

    @Bean
    public WebClient NepalWebclient() throws SSLException {
        WebClient client2 = WebClient.builder().clientConnector(getConnector()).baseUrl(NepalUrl).build();
        return client2;
    }
    @Bean
    public WebClient WorldWebclient() throws SSLException {
        WebClient client2 = WebClient.builder().clientConnector(getConnector()).baseUrl(WorldUrl).build();
        return client2;
    }
//    public CloseableHttpClient getHttpClient(){
//        final CloseableHttpClient httpclient = HttpClients.custom()
//                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
//                .build();
//        return httpclient;
//    }

}
