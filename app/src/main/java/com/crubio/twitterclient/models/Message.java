package com.crubio.twitterclient.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by carlos on 11/16/2015.
 */
public class Message {

    private String message;
    private String username;
    private String userImageUrl;

    public Message(JSONObject jsonMessage) {
        try{
            message = jsonMessage.getString("text");
            JSONObject sender = jsonMessage.getJSONObject("sender");
            username = sender.getString("name");
            userImageUrl = sender.getString("profile_image_url");

        } catch (JSONException e) {
            Log.e("Message", "Error reading JSON", e);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImageUrl() {
        return userImageUrl.replace("_normal", "");
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                '}';
    }

    public static List<Message> fromJson(JSONArray jsonArray) {
        List<Message> messages = new ArrayList<>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Message message = new Message(tweetJson);
            messages.add(message);
        }

        return messages;
    }
}
