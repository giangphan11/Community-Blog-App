package phanbagiang.com.blogapp.model;

import com.google.firebase.database.ServerValue;

public class Comment {
    private String content;
    private String userName;
    private String userId;
    private String userImage;
    private Object timeStamp;

    public Comment() {
    }

    public Comment(String content, String userName, String userId, String userImage) {
        this.content = content;
        this.userName = userName;
        this.userId = userId;
        this.userImage = userImage;
        this.timeStamp= ServerValue.TIMESTAMP;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
