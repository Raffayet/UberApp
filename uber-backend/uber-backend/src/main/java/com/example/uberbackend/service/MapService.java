package com.example.uberbackend.service;

import com.example.uberbackend.model.Point;
import org.springframework.beans.factory.annotation.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hibernate.cfg.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class MapService {

    @Value("${graphhopper.api.key}")
    private String graphhopperApiKey;
    
    public String getRoute(List<Point> points) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/route?point=45.2413851,19.8253233&point=45.2759048,19.8012658&points_encoded=false&profile=car&locale=de&key=" + graphhopperApiKey)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }
}
