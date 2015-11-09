package com.crubio.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Table(name = "Tweets")
public class Tweet extends Model implements Parcelable {
    private static String CLASS = "Tweet";
    // Define database columns and associated fields
    @Column(name = "userId")
    String userId;
    @Column(name = "userProfileImage")
    String userProfileImage;
    @Column(name = "userName")
    String userName;
    @Column(name = "user")
    String user;
    @Column(name = "timestamp")
    String timestamp;
    @Column(name = "tweet")
    String tweet;
    @Column(name = "retweets")
    String retweets;
    @Column(name = "favourites")
    String favourites;

    // Make sure to always define this constructor with no arguments
    public Tweet() {
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public Tweet(JSONObject object){
        super();
        try {
            JSONObject user = object.getJSONObject("user");
            this.userId = user.getString("id_str");
            this.userProfileImage = user.getString("profile_image_url");
            this.user = user.getString("screen_name");
            this.userName = user.getString("name");
            this.timestamp = object.getString("created_at");
            this.tweet = object.getString("text");
            this.retweets = object.getString("retweet_count");
            this.favourites = object.getString("favorite_count"); //TODO Not real favs change
        } catch (JSONException e) {
            Log.e(CLASS, "Error reading JSON", e);
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            tweets.add(tweet);
        }

        return tweets;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTimestamp() {
        return getRelativeTimeAgo(timestamp);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getRetweets() {
        return retweets;
    }

    public void setRetweets(String retweets) {
        this.retweets = retweets;
    }

    public String getFavourites() {
        return favourites;
    }

    public void setFavourites(String favourites) {
        this.favourites = favourites;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "userId='" + userId + '\'' +
                ", userProfileImage='" + userProfileImage + '\'' +
                ", userName='" + userName + '\'' +
                ", user='" + user + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", tweet='" + tweet + '\'' +
                ", retweets='" + retweets + '\'' +
                ", favourites='" + favourites + '\'' +
                '}';
    }

    private String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        relativeDate = relativeDate.replace(" ago", "");

        return relativeDate;
    }

    public static List<Tweet> getAll(){
        return new Select().from(Tweet.class).execute();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.userProfileImage);
        dest.writeString(this.userName);
        dest.writeString(this.user);
        dest.writeString(this.timestamp);
        dest.writeString(this.tweet);
        dest.writeString(this.retweets);
        dest.writeString(this.favourites);
    }

    protected Tweet(Parcel in) {
        this.userId = in.readString();
        this.userProfileImage = in.readString();
        this.userName = in.readString();
        this.user = in.readString();
        this.timestamp = in.readString();
        this.tweet = in.readString();
        this.retweets = in.readString();
        this.favourites = in.readString();
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}