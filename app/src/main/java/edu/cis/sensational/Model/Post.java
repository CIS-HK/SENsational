package edu.cis.sensational.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Nicole Xiang
 * Created on 23/03/2020.
 */

public class Post implements Parcelable {

    private String title;
    private String description;
    private String date_created;
    private String user_id;
    private String tag;
    private long likeCount;
    private ArrayList<String> likes;
    private ArrayList<String> unlikes;
    private ArrayList<Comment> comments;
    private boolean privatePost;
    private String postID;

    public Post()
    {
        title = "";
        description = "";
        date_created = "";
        user_id = "";
        tag = "";
        likes = new ArrayList<>();
        comments = new ArrayList<>();
        unlikes = new ArrayList<>();
        likeCount = 0;
        postID = "";
        privatePost = false;
    }

    public Post (String title, String description, String date_created, boolean privatePost,
                 String post_id, String user_id, String tags, ArrayList<String> likes,
                ArrayList<Comment> comments, ArrayList<String> unlikes, long likeCount){
        this.title = title;
        this.description = description;
        this.date_created = date_created;
        this.user_id = user_id;
        this.tag = tags;
        this.likes = likes;
        this.comments = comments;
        this.unlikes = unlikes;
        this.likeCount = likeCount;
        this.postID = post_id;
        this.privatePost = privatePost;
    }

    public Post(Parcel in) {
        title = in.readString();
        date_created = in.readString();
        description = in.readString();
        user_id = in.readString();
        postID = in.readString();
        tag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date_created);
        dest.writeString(description);
        dest.writeString(user_id);
        dest.writeString(postID);
        dest.writeString(tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public static Creator<Post> getCREATOR() {
        return CREATOR;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTags() {
        return tag;
    }

    public void setTags(String tags) {
        this.tag = tags;
    }

    public ArrayList<String> getUnLikes() {
        return unlikes;
    }

    public void setUnLikes(ArrayList<String> unlikes) {
        this.unlikes = unlikes;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public void setPrivate(boolean privatePost){
        this.privatePost = privatePost;
    }

    public boolean getPrivate(){
        return privatePost;
    }

    public void setPostID(String postID){
        this.postID = postID;
    }

    public String getPostID(){
        return postID;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", date_created='" + date_created + '\'' +
                ", description='" + description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", tags='" + tag + '\'' +
                ", likes=" + likes +
                ", public=" + privatePost +
                ", postID=" + postID +
                '}';
    }
}
