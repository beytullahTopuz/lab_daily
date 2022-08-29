package com.example.lab_dailiy_selfi.model;

import android.graphics.Bitmap;

import com.example.lab_dailiy_selfi.R;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//DATA CLASS
public class PictureModel {

    @JsonProperty("images")
    private List<String> images = new ArrayList<>();
    @JsonProperty("texts")
    private List<String> texts = new ArrayList<>();



    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    @Override
    public String toString() {
        return "PictureModel{" +
                "images=" + images +
                ", texts=" + texts +
                '}';
    }

    public PictureModel stringToModel(String str){
        ObjectMapper mapper = new ObjectMapper();
        PictureModel pm = null;
        try{
             pm = mapper.readValue(str,PictureModel.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pm;
    }
    public String modelToString() {
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(this);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }





   /* public String mapToJsonString() {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> myMap = new HashMap<>();
        String jsonString = "";

        myMap.put(String.valueOf(R.string.image), images);
        myMap.put(String.valueOf(R.string.text),texts);

        try {
            jsonString = mapper.writeValueAsString(myMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
    */
}
