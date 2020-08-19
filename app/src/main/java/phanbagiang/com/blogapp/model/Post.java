package phanbagiang.com.blogapp.model;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    private String title;
    private String des;
    private String userName;
    private String picture;
    private String userId;
    private String userPhoto;
    private Object timeStamp;

    public Post() {
    }

    public Post(String title, String des, String picture, String userId, String userPhoto, String name) {
        this.title = title;
        this.des = des;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto=userPhoto;
        this.timeStamp=ServerValue.TIMESTAMP;
        this.userName=name;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
