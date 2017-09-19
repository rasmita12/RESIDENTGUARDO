package android.stalwartgroup.residentguardo.Pojo;

/**
 * Created by mobileapplication on 9/19/17.
 */

public class Flatlist {

    String plot_no_id,flat_name;

    public Flatlist(String plot_no_id, String flat_name) {
        this.plot_no_id=plot_no_id;
        this.flat_name=flat_name;
    }

    public String getPlot_no_id() {
        return plot_no_id;
    }

    public void setPlot_no_id(String plot_no_id) {
        this.plot_no_id = plot_no_id;
    }

    public String getFlat_name() {
        return flat_name;
    }

    public void setFlat_name(String flat_name) {
        this.flat_name = flat_name;
    }
}
