/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risksense.controller;

import com.risksense.converters.XMLJSONConverterI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Elcot
 */
@RestController
@RequestMapping("/api")
public class XMLJSONConverter implements XMLJSONConverterI {

    @GetMapping("/test")
    public String test() {
        return new Date().toString();
    }

    @Override
    @GetMapping("/convert")
    public void convertJSONtoXML(File json, File xml) throws IOException {
        String jsonStr = new String(Files.readAllBytes(json.toPath()));
        StringBuilder sb = printMap(buildXML(jsonStr));
        try (FileWriter fw = new FileWriter(xml)) {
            fw.write(sb.toString());
        }
    }

    public static Map<Key, Object> buildXML(Object value, String... name) {
        Map<Key, Object> map = new HashMap<>();

        value = checkForObjectType(value);

        Key k = new Key(Key.Type.Object);

        if (value instanceof String) {
            k = new Key(Key.Type.String, name);

        } else if (value instanceof Integer) {
            k = new Key(Key.Type.Integer, name);

        } else if (value instanceof Boolean) {
            k = new Key(Key.Type.Boolean, name);

        } else if (value instanceof JSONObject) {
            k = new Key(Key.Type.Object, name);

            JSONObject jsonObj = (JSONObject) value;
            List<Map<Key, Object>> jsonObjFields = new ArrayList<>();

            for (String key : jsonObj.keySet()) {
                jsonObjFields.add(buildXML(jsonObj.get(key), key));
            }
            value = jsonObjFields;

        } else if (value instanceof JSONArray) {
            k = new Key(Key.Type.Array, name);
            JSONArray array = (JSONArray) value;
            List<Map<Key, Object>> jsonArrFields = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                jsonArrFields.add(buildXML(array.get(i)));
            }
            value = jsonArrFields;
        }
        map.put(k, value);
        return map;
    }

    public static Object checkForObjectType(Object object) {
        try {
            object = new JSONObject(object.toString());
        } catch (JSONException ex) {
            try {
                object = new JSONArray(object);
            } catch (JSONException ex1) {
                try {
                    object = Integer.parseInt(object.toString());
                } catch (NumberFormatException e) {
                    return object;
                }
            }
        }
        return object;
    }

    public static StringBuilder printMap(Object value) {
        StringBuilder sb = new StringBuilder();
        if (value instanceof Map) {
            Map<Key, Object> map = (Map<Key, Object>) value;

            for (Map.Entry<Key, Object> e : map.entrySet()) {
                sb.append("<").append(e.getKey().getType()).append(e.getKey().getName() != null ? " name=\"" + e.getKey().getName() + "\"" : "").append(">");

                if (e.getValue() instanceof List) {
                    for (Map<Key, Object> m : (List<Map<Key, Object>>) e.getValue()) {

                        for (Map.Entry<Key, Object> entry : m.entrySet()) {
                            Key k = entry.getKey();
                            sb.append("<").append(k.getType()).append(k.getName() != null ? " name=\"" + k.getName() + "\"" : "").append(">");

                            if (Key.Type.Array.equals(k.getType()) || Key.Type.Object.equals(k.getType())) {
                                sb.append(printMap(entry.getValue()));
                            } else {
                                sb.append(entry.getValue());
                            }
                            sb.append("</").append(k.getType()).append(">");
                        }
                    }
                } else {
                    sb.append(e.getValue());
                }
                sb.append("</").append(e.getKey().getType()).append(">");

            }
        } else if (value instanceof List) {
            List<Map<Key, Object>> list = (List<Map<Key, Object>>) value;

            for (Map<Key, Object> lMap : list) {
                sb.append(printMap(lMap));
            }
        }
        return sb;
    }
}
