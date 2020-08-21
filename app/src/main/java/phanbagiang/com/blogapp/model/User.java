package phanbagiang.com.blogapp.model;

public class User {
    private String userId;
    private String userName;
    private String photoImage;
    private String email;
    private String pass;
    private boolean check;

    public User() {
    }

    public User(String userId,String userName, String photoImage, String email, String pass) {
        this.userId=userId;
        this.userName = userName;
        this.photoImage = photoImage;
        this.email = email;
        this.pass = pass;
        this.check=false;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(String photoImage) {
        this.photoImage = photoImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
