package com.example.uberbackend.service;

import com.example.uberbackend.model.Point;
import org.springframework.beans.factory.annotation.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
        String queryParams = new String();

        for(Point p : points){
            queryParams = queryParams.concat("point=").concat(p.getLat().toString()).concat(",")
                       .concat(p.getLng().toString()).concat("&");
        }

        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/route?".concat(queryParams).concat("points_encoded=false&profile=car&locale=de&key=").concat(graphhopperApiKey))
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }
}
