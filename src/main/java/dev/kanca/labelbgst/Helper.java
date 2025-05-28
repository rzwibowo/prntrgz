package dev.kanca.labelbgst;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dev
 */
public class Helper {
      public Map<String, String> getSettings(String filename) {
        try {
            return readSettingsFromFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Map<String, String> readSettingsFromFile(String filename) throws Exception {
        Map<String, String> settings = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                settings.put(parts[0], parts[1]);
            }
        }
        reader.close();
        return settings;
    }
    
    public byte[] decodeB64(String b64String) {
        return Base64.getDecoder().decode(b64String);
    }
}