package android.stalwartgroup.residentguardo.Pojo;

/**
 * Created by mobileapplication on 9/19/17.
 */

public class Apartmentlist {

    String apartment_id,apartment_name;

    public Apartmentlist(String apartment_id, String apartment_name) {
        this.apartment_id=apartment_id;
        this.apartment_name=apartment_name;
    }

    public String getApartment_id() {
        return apartment_id;
    }

    public void setApartment_id(String apartment_id) {
        this.apartment_id = apartment_id;
    }

    public String getApartment_name() {
        return apartment_name;
    }

    public void setApartment_name(String apartment_name) {
        this.apartment_name = apartment_name;
    }
}
