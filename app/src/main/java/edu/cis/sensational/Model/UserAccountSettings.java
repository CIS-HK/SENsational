package edu.cis.sensational.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Nicole Xiang
 * Created on 23/03/2020.
 */

public class UserAccountSettings implements Parcelable{

    private String username;
    private String location;
    private String child_age;
    private String child_gender;
    private String email;
    private long posts;
    private String user_id;

    public UserAccountSettings(String username, String email, long posts, String user_id) {
        this.username = username;
        this.email = email;
        this.posts = posts;
        this.user_id = user_id;
    }

    public UserAccountSettings() {

    }

    protected UserAccountSettings(Parcel in) {
        location = in.readString();
        child_age = in.readString();
        child_gender = in.readString();
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getChild_age() {
        return child_age;
    }

    public void setChild_age(String age) {
        this.child_age = age;
    }

    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String gender) {
        this.child_gender = gender;
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
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                ", child_age='" + child_age + '\'' +
                ", child_profile'" + child_gender + '\'' +
                ", posts=" + posts +
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
        dest.writeString(location);
        dest.writeString(child_gender);
        dest.writeString(user_id);
        dest.writeString(child_age);
    }
}
