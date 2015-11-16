package com.crubio.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    private static final String CLASS = User.class.getSimpleName();
    private String id;
    private String userName;
    private String user;
    private String description;
    private String location;
    private String profileImageUrl;
    private String profileBannerUrl;
    private String backgroundColor;
    private String followersCount;
    private String friendsCount;
    private String tweetsCount;

    public User() {
        super();
    }

    public User(JSONObject object) {
        super();
        Log.i(CLASS, ("" + object).substring(0, ("" + object).length() / 2));
        Log.i(CLASS, ("" + object).substring(("" + object).length()/2));
        try {
            this.id = object.getString("id_str");
            this.profileImageUrl = object.getString("profile_image_url");
            if(object.has("profile_banner_url")) {
                this.profileBannerUrl = object.getString("profile_banner_url");
            }
            this.backgroundColor = object.getString("profile_link_color");
            this.followersCount = object.getString("followers_count");
            this.friendsCount = object.getString("friends_count");
            this.tweetsCount = object.getString("statuses_count");
            this.userName = object.getString("name");
            this.user = "@"+object.getString("screen_name");
            this.location = object.getString("location");
            this.description = object.getString("description");
        } catch (JSONException e) {
            Log.e(CLASS, "Error reading JSON", e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl.replace("_normal", "");
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String bacgroundColor) {
        this.backgroundColor = bacgroundColor;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(String friendsCount) {
        this.friendsCount = friendsCount;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public String getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(String tweetsCount) {
        this.tweetsCount = tweetsCount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", user='" + user + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", profileBannerUrl='" + profileBannerUrl + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", followersCount='" + followersCount + '\'' +
                ", friendsCount='" + friendsCount + '\'' +
                ", tweetsCount='" + tweetsCount + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userName);
        dest.writeString(this.user);
        dest.writeString(this.description);
        dest.writeString(this.location);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.profileBannerUrl);
        dest.writeString(this.backgroundColor);
        dest.writeString(this.followersCount);
        dest.writeString(this.friendsCount);
        dest.writeString(this.tweetsCount);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.userName = in.readString();
        this.user = in.readString();
        this.description = in.readString();
        this.location = in.readString();
        this.profileImageUrl = in.readString();
        this.profileBannerUrl = in.readString();
        this.backgroundColor = in.readString();
        this.followersCount = in.readString();
        this.friendsCount = in.readString();
        this.tweetsCount = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
