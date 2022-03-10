package net.thumbtack.school.servlet.v2;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class MyHttpClient {
    public static void main(String[] args) throws IOException {
        String url = "https://hr-vector.com/java/oshibka-java-lang-noclassdeffounderror";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        System.out.println(response.getStatusLine());
    }
}
