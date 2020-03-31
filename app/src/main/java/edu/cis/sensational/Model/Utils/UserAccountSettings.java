package edu.cis.sensational.Model.Utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 6/29/2017.
 */

public class UserAccountSettings implements Parcelable{

    private String username;
    private long posts;

    private String user_id;

    public UserAccountSettings(String display_name, String email, String location, String user_id) {
        this.posts = posts;
        this.username = username;
        this.user_id = user_id;
    }

    public UserAccountSettings() {

    }

    protected UserAccountSettings(Parcel in) {
        posts = in.readLong();
        username = in.readString();
        user_id = in.readString();
    }

    public static final Creator<UserAccountSettings> CREATOR = new Creator<UserAccountSettings>() {
        @Override
        public UserAccountSettings createFromParcel(Parcel in) {
            return new UserAccountSettings(in);
        }

        @Override
        public UserAccountSettings[] newArray(int size) {
            return new UserAccountSettings[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "UserAccountSettings{" +
                ", posts=" + posts +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(posts);
        dest.writeString(username);
        dest.writeString(user_id);
    }
}
