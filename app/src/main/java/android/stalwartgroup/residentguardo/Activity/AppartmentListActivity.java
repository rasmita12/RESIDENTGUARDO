package android.stalwartgroup.residentguardo.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.stalwartgroup.residentguardo.Adapter.ApartmentAdapter;
import android.stalwartgroup.residentguardo.Pojo.Apartmentlist;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.CheckInternet;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mobileapplication on 9/19/17.
 */

public class AppartmentListActivity extends AppCompatActivity {
    ListView lv_apartment;
    ApartmentAdapter adapter;
    ArrayList<Apartmentlist> apartmentlist;
    RelativeLayout linn;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appartment);

        lv_apartment=(ListView)findViewById(R.id.list_aprtment);
        linn=(RelativeLayout)findViewById(R.id.linn);
        apartmentlist=new ArrayList<>();

        getApartmentListData();

    }

    private void getApartmentListData() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            appartment_asyn checkin = new appartment_asyn();
            checkin.execute("");
        } else {
            showsnackbar("No Internet");
        }
    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private class appartment_asyn extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;
        String apartment_id,apartment_name;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(AppartmentListActivity.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL+Constants.Apartment_LIST;
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

                        .appendQueryParameter("user_id", "");
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

                /**
                 * {
                 "Location": [
                 {
                 "id": "3",
                 "name": "Esha Appartments",
                 "address": "BBSR",
                 "type": "Appartment",
                 "is_enable": "1",
                 "created": "2017-07-23 03:11:59",
                 "modified": "2017-07-23 03:11:59"
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONArray jarry=res.getJSONArray("Location");
                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        server_message = res.optString("message");
                        for(int i=0;i<jarry.length();i++){
                            JSONObject j_obj=jarry.getJSONObject(i);
                            apartment_id = j_obj.optString("id");
                            apartment_name = j_obj.optString("name");


                            Apartmentlist assigntoname = new Apartmentlist(apartment_id,apartment_name);
                            apartmentlist.add(assigntoname);
                        }
                    }
                   else {
                        server_message = "Invalid Credentials";
                    }
                }

                return null;

            } catch (SocketTimeoutException exception) {
                server_message = "Network Error";
                Log.e( "SynchMobnum : doInBackground", exception.toString());
            } catch (ConnectException exception) {
                server_message = "Network Error";
                Log.e("SynchMobnum : doInBackground", exception.toString());
            } catch (MalformedURLException exception) {
                server_message = "Network Error";
                Log.e( "SynchMobnum : doInBackground", exception.toString());
            } catch (IOException exception) {
                server_message = "Network Error";
                Log.e( "SynchMobnum : doInBackground", exception.toString());
            } catch (Exception exception) {
                server_message = "Network Error";
                Log.e( "SynchMobnum : doInBackground", exception.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            Collections.reverse(apartmentlist);
            if(server_status==1) {

                    adapter = new ApartmentAdapter(AppartmentListActivity.this, apartmentlist);
                    lv_apartment.setAdapter(adapter);
            }
            else{
                Snackbar snackbar = Snackbar
                        .make(linn, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            progressDialog.cancel();
        }
    }
}