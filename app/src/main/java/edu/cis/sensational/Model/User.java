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
    private String user_child_age;
    private String user_child_profile;

    public User()
    {
        user_id = "";
        user_name = "";
        user_email = "";

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

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeLong(phone_number);
        dest.writeString(email);
        dest.writeString(username);
    }
}