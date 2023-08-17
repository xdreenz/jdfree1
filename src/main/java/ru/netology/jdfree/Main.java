package ru.netology.jdfree;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        System.out.print("Sending a request to API...");
        CloseableHttpResponse response = httpClient.execute(new HttpGet(URI));
        System.out.println(" OK");

        NasaObject nasaObject = mapper.readValue(response.getEntity().getContent(), NasaObject.class);

        System.out.print("Sending a request for image...");
        CloseableHttpResponse pictureResponse = httpClient.execute(new HttpGet(nasaObject.getUrl()));
        System.out.println(" OK");

        String[] arr = nasaObject.getUrl().split("/");
        String fileName = arr[arr.length - 1];

        HttpEntity entity = pictureResponse.getEntity();

        System.out.print("Saving the image...");
        FileOutputStream fos = new FileOutputStream(fileName);
        entity.writeTo(fos);
        fos.close();
        System.out.println(" OK");

    }
}