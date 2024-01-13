package com.progi.ostecenja.server.service;


import com.progi.ostecenja.server.Utils.JsonManager;
import com.progi.ostecenja.server.Utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class JsonService {
    private static final String FILE_PATH = "data.json";
    JsonManager jsonManager = new JsonManager(FILE_PATH);

    public Map<Pair<Double, Double>, String> readJsonFile() {
        return jsonManager.getDataMap();
    }

    public void addToJsonFile(Pair<Double, Double> location, String address){
        jsonManager.addData(location, address);
    }
}
