package android.stalwartgroup.residentguardo.Pojo;

/**
 * Created by User on 16-07-2017.
 */

public class Visitors {
    String id,name,mobile,visiter_type,coming_from,vehicle_no,appartment,pass_no
            ,photo,in_time,out_time,exit_by,overstay,verification;

    public String getOverstay() {
        return overstay;
    }

    public void setOverstay(String overstay) {
        this.overstay = overstay;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Visitors(String id, String name, String mobile, String visiter_type, String coming_from,
                    String vehicle_no, String appartment, String pass_no, String photo, String in_time, String out_time, String exit_by, String overstay, String verification) {
        this.id=id;
        this.name=name;
        this.mobile=mobile;
        this.visiter_type=visiter_type;
        this.coming_from=coming_from;
        this.vehicle_no=vehicle_no;
        this.appartment=appartment;
        this.pass_no=pass_no;
        this.photo=photo;
        this.in_time=in_time;
        this.exit_by=exit_by;
        this.out_time=out_time;
        this.overstay=overstay;
        this.verification=verification;



    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVisiter_type() {
        return visiter_type;
    }

    public void setVisiter_type(String visiter_type) {
        this.visiter_type = visiter_type;
    }

    public String getComing_from() {
        return coming_from;
    }

    public void setComing_from(String coming_from) {
        this.coming_from = coming_from;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getPass_no() {
        return pass_no;
    }

    public String getExit_by() {
        return exit_by;
    }

    public void setExit_by(String exit_by) {
        this.exit_by = exit_by;
    }

    public void setPass_no(String pass_no) {
        this.pass_no = pass_no;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }
}
