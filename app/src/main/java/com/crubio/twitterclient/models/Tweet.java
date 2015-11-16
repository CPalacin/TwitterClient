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
    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String tweetId;
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
    @Column(name = "media")
    String mediaUrl;
    @Column(name = "retweet")
    Tweet retweet;

    // Make sure to always define this constructor with no arguments
    public Tweet() {
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public Tweet(JSONObject object){
        super();
        try {
//            Log.i(CLASS, object.toString().substring(0, object.toString().length()/4));
//            Log.i(CLASS, object.toString().substring(object.toString().length()/4, object.toString().length() / 2));
//            Log.i(CLASS, object.toString().substring(object.toString().length() / 2, 3*object.toString().length()/4));
//            Log.i(CLASS, object.toString().substring(3*object.toString().length()/4, object.toString().length()));
            JSONObject user = object.getJSONObject("user");
            this.tweetId = object.getString("id_str");
            this.userProfileImage = user.getString("profile_image_url");
            this.user = user.getString("screen_name");
            this.userName = user.getString("name");
            this.timestamp = object.getString("created_at");
            this.tweet = object.getString("text");
            this.retweets = object.getString("retweet_count");
            this.favourites = object.getString("favorite_count");
            this.userId = user.getString("id_str");

            JSONObject entities = object.getJSONObject("entities");
            if (entities.has("media")){
                mediaUrl = entities.getJSONArray("media")
                        .getJSONObject(0)
                        .getString("media_url");
            }

            checkRetweet(object);

        } catch (JSONException e) {
            Log.e(CLASS, "Error reading JSON", e);

        }
    }

    private void checkRetweet(JSONObject object) {
        if(object.has("retweeted_status")){

            try {
                retweet = new Tweet(object.getJSONObject("retweeted_status"));
            } catch (JSONException e) {
                Log.e(CLASS, "Error reading JSON", e);
            }
        }
    }

    public static List<Tweet> fromJson(JSONArray jsonArray) {
        List<Tweet> tweets = new ArrayList<>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson;
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

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
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

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Tweet getRetweet() {
        return retweet;
    }

    public void setRetweet(Tweet retweet) {
        this.retweet = retweet;
    }

    public boolean isRetweet(){
        return retweet != null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "tweetId='" + tweetId + '\'' +
                ", userId='" + userId + '\'' +
                ", userProfileImage='" + userProfileImage + '\'' +
                ", userName='" + userName + '\'' +
                ", user='" + user + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", tweet='" + tweet + '\'' +
                ", retweets='" + retweets + '\'' +
                ", favourites='" + favourites + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", retweet=" + retweet +
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
        dest.writeString(this.tweetId);
        dest.writeString(this.userId);
        dest.writeString(this.userProfileImage);
        dest.writeString(this.userName);
        dest.writeString(this.user);
        dest.writeString(this.timestamp);
        dest.writeString(this.tweet);
        dest.writeString(this.retweets);
        dest.writeString(this.favourites);
        dest.writeString(this.mediaUrl);
        dest.writeParcelable(this.retweet, 0);
    }

    protected Tweet(Parcel in) {
        this.tweetId = in.readString();
        this.userId = in.readString();
        this.userProfileImage = in.readString();
        this.userName = in.readString();
        this.user = in.readString();
        this.timestamp = in.readString();
        this.tweet = in.readString();
        this.retweets = in.readString();
        this.favourites = in.readString();
        this.mediaUrl = in.readString();
        this.retweet = in.readParcelable(Tweet.class.getClassLoader());
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}