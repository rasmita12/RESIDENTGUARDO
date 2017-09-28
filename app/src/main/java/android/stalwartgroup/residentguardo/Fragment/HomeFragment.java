package android.stalwartgroup.residentguardo.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.stalwartgroup.residentguardo.Adapter.VisitorsAdapter;
import android.stalwartgroup.residentguardo.Pojo.Visitors;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.CheckInternet;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mobileapplication on 9/26/17.
 */

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ViewPager viewPager;
    RelativeLayout home_rel;
    TextView visitor_n_f;
    ListView visitor_list;
    String user_id;
    int server_status;
    ArrayList<Visitors> list_visitors;
    String apartment_id,flat_name;
    VisitorsAdapter adpter;



    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main, container, false);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.REGISTER_USER_ID, null);
        apartment_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.APARTMENT_ID, null);
        flat_name = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_FLAT_NAME, null);
        visitor_list = (ListView) view.findViewById(R.id.list_home);
        visitor_n_f=(TextView)view.findViewById(R.id.visitor_notf);
        getVisiorsList();
        return view;
    }
    private void getVisiorsList() {
        if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
        // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String curr_date = dateFormatter.format(today);
        String datetosend=curr_date.substring(0, Math.min(curr_date.length(), 10));
        new VisiorsList().execute(apartment_id,flat_name,datetosend);
    }else {
        Snackbar snackbar = Snackbar
                .make(visitor_list, "No Internet", Snackbar.LENGTH_LONG);
        snackbar.show();        }


}


    /*
* GET VISIORS LIST ASYNTASK*/
    private class VisiorsList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Loading Visiorslist...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _locationId = params[0];
                String _flatname = params[1];
                String _intime = params[2];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL+Constants.VISITOR_LIST;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("location_id", _locationId)
                        .appendQueryParameter("flat_name", _flatname)
                        .appendQueryParameter("in_time", _intime);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

                /*
                *
                * {
   "visiter": [
       {
           "id": "2",
           "name": "Amaresh",
           "email_id": null,
           "address": null,
           "mobile": "7418529630",
           "visiter_type": "New",
           "coming_from": "Manyta",
           "vehicle_no": "KA 03 NA 9870",
           "location_id": "3",
           "appartment_name": "A-101",
           "pass_no": "Visitor ID 03",
           "photo": "",
           "added_by": "2",
           "in_time": "28-09-2017 10:34 AM",
           "exit_by": "",
           "out_time": "",
           "verification_status": "Not Verified",
           "overstay": 0,
           "created": "2017-09-28 11:41:39",
           "modified": "2017-09-28 11:41:39"
       }
   ],
   "vister": [
       {
           "added_by": "admin"
       }
   ],
   "status": 1,
   "message": "Successfully"
}*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    list_visitors=new ArrayList<>();
                    if(server_status==1) {
                        JSONArray visitoslist = res.getJSONArray("visiter");
                        for (int i = 0; i < visitoslist.length(); i++) {
                            JSONObject o_list_obj = visitoslist.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String name = o_list_obj.getString("name");
                            String mobile = o_list_obj.getString("mobile");
                            String visiter_type = o_list_obj.getString("visiter_type");
                            String coming_from = o_list_obj.getString("coming_from");
                            String vehicle_no = o_list_obj.getString("vehicle_no");
                            String appartment = o_list_obj.getString("appartment_name");
                            String pass_no = o_list_obj.getString("pass_no");
                            String photo = o_list_obj.getString("photo");
                            String in_time = o_list_obj.getString("in_time");
                            String out_time = o_list_obj.getString("out_time");
                            String exit_by = o_list_obj.getString("exit_by");
                            String overstay = o_list_obj.getString("overstay");
                            String verification = o_list_obj.getString("verification_status");
                            Visitors list1 = new Visitors(id,name,mobile,visiter_type,coming_from,vehicle_no,appartment,pass_no
                                    ,photo,in_time,out_time,exit_by,overstay,verification);
                            list_visitors.add(list1);
                        }
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if(server_status==1) {
                adpter = new VisitorsAdapter(getContext(), list_visitors);
                visitor_list.setAdapter(adpter);
            }
            else{
                visitor_list.setVisibility(View.GONE);
                visitor_n_f.setVisibility(View.VISIBLE);
            }

        }
    }
}

