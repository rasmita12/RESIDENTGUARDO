package android.stalwartgroup.residentguardo.Pojo;

/**
 * Created by mobileapplication on 9/22/17.
 */

public class User {
    String user_id,user_name,user_email_id,user_mobile,user_photo,user_aprtment,user_flatname;
    public User(){

    }

    public User(String user_id, String user_name, String user_email_id, String user_mobile, String user_photo,
                String user_aprtment,String user_flatname) {
        this.user_id=user_id;
        this.user_name=user_name;
        this.user_email_id=user_email_id;
        this.user_mobile=user_mobile;
        this.user_photo=user_photo;
        this.user_aprtment=user_aprtment;
        this.user_flatname=user_flatname;
    }

    public String getUser_aprtment() {
        return user_aprtment;
    }

    public void setUser_aprtment(String user_aprtment) {
        this.user_aprtment = user_aprtment;
    }

    public String getUser_flatname() {
        return user_flatname;
    }

    public void setUser_flatname(String user_flatname) {
        this.user_flatname = user_flatname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email_id() {
        return user_email_id;
    }

    public void setUser_email_id(String user_email_id) {
        this.user_email_id = user_email_id;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }
}
