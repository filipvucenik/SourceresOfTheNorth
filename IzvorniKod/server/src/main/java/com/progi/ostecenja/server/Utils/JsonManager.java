package com.progi.ostecenja.server.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonManager {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File jsonFile;
    @Getter
    private Map<Pair<Double, Double>, String> dataMap;

    public JsonManager(String filePath) {
        this.jsonFile = new File(filePath);
        this.dataMap = readMapFromJsonFile();
    }

    public void addData(Pair<Double, Double> key, String value) {
        dataMap.put(key, value);
        saveMapToJsonFile();
    }

    private Map<Pair<Double, Double>, String> readMapFromJsonFile() {
        try {
            if (jsonFile.exists()) {
                return objectMapper.readValue(jsonFile, new TypeReference<Map<Pair<Double, Double>, String>>() {});
            }
        } catch (IOException e) {
            throw new JsonException("can't read from file");
        }
        return new HashMap<>();
    }

    private void saveMapToJsonFile() {
        try {
            objectMapper.writeValue(jsonFile, dataMap);
        } catch (IOException e) {
            throw new JsonException("can write to file");
        }
    }

}
