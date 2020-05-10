package edu.cis.sensational.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 6/26/2017.
 */

public class User implements Parcelable{

    private String user_id;
    private String user_name;
    private String user_email;
    private String user_location;
    private String user_gender;
    private String user_password;
    private String user_child_gender;
    private int user_child_age;
    private String user_child_profile;

    public User()
    {
        user_id = "";
        user_name = "";
        user_email = "";
        user_location = "";
        user_gender = "";
        user_password = "";
        user_child_gender = "";
        user_child_age = 0;
    }

    public User(String id, String n, String e, String l)
    {
        user_id = id;
        user_name = n;
        user_email = e;
        user_location = l;
    }

    protected User(Parcel in)
    {
        user_id = in.readString();
        user_email = in.readString();
        user_name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return user_email;
    }

    public void setEmail(String email) {
        this.user_email = email;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String username) {
        this.user_name = username;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String location) {
        this.user_location = location;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String gender) {
        this.user_gender = gender;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String password) {
        this.user_password = password;
    }

    public String getUser_child_gender() {
        return user_child_gender;
    }

    public void setUser_child_gender(String child_gender) {
        this.user_child_gender = child_gender;
    }

    public int getUser_child_age() {
        return user_child_age;
    }

    public void setUser_child_age(int age) {
        this.user_child_age = age;
    }

    public String getUser_child_profile() {
        return user_child_profile;
    }

    public void setUser_child_profile(String profile) {
        this.user_child_profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + user_email + '\'' +
                ", username='" + user_name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(user_email);
        dest.writeString(user_name);
    }
}