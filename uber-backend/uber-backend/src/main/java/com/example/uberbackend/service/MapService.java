package com.example.uberbackend.service;

import com.example.uberbackend.model.Point;
import org.springframework.beans.factory.annotation.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.json.*;
import java.io.IOException;
import java.util.*;

@Service
public class MapService {

    @Value("${graphhopper.api.key}")
    private String graphhopperApiKey;
    
    public List<Point> getOptimalRoute(List<Point> points) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String queryParams = new String();

        for(Point p : points){
            queryParams = insertPointString(p, queryParams);
        }

        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/route?".concat(queryParams).concat("points_encoded=false&profile=car&locale=de&key=").concat(graphhopperApiKey))
                .get()
                .build();
        Response response = client.newCall(request).execute();
        List<Point> retList = getCoordinatesFromGraphhoperResponse(response, 0);

        return retList;
    }

    private List<Point> getCoordinatesFromGraphhoperResponse(Response response, int index) throws IOException {
        JSONObject obj = new JSONObject(response.body().string());
        JSONArray paths = (JSONArray) obj.get("paths");
        JSONArray coordinates = paths.getJSONObject(index).getJSONObject("points").getJSONArray("coordinates");
        List<Point> retList = new ArrayList<>();

        for(int i = 0; i < coordinates.length(); i++){
            JSONArray v = (JSONArray) (coordinates.get(i));
            retList.add(new Point(v.get(0).toString(), v.get(1).toString()));
        }
        return retList;
    }

    private String insertPointString(Point p, String queryParams){
        queryParams = queryParams.concat("point=").concat(p.getLat().toString()).concat(",")
                .concat(p.getLng().toString()).concat("&");
        return queryParams;
    }

    public List<Point> getAlternativeRoute(List<Point> points) throws IOException {
        OkHttpClient client = new OkHttpClient();
        List<Point> retList = new ArrayList<>();
        int i = 0;

        while(i < points.size() - 1){
            String queryParams = new String();
            queryParams = insertPointString(points.get(i), queryParams);
            queryParams = insertPointString(points.get(i + 1), queryParams);

            Request request = new Request.Builder()
                    .url("https://graphhopper.com/api/1/route?".concat(queryParams).concat("points_encoded=false&algorithm=alternative_route&alternative_route.max_weight_factor=7&key=").concat(graphhopperApiKey))
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            retList.addAll(getCoordinatesFromGraphhoperResponse(response, 1));
            i++;
        }
        return retList;
    }
}
