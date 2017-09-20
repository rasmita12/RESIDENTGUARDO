package android.stalwartgroup.residentguardo.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.stalwartgroup.residentguardo.Adapter.FlatAdapter;
import android.stalwartgroup.residentguardo.Pojo.Flatlist;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.CheckInternet;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

/**
 * Created by mobileapplication on 9/19/17.
 */

public class FlatListActivity extends AppCompatActivity {

    ListView lv_assign;
    FlatAdapter assignAdapter;
    ArrayList<Flatlist> flatlist;
    RelativeLayout linn;
    String data;
    String apartment_id;
    TextView flat_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat);

        flat_txt=(TextView)findViewById(R.id.flat_txt);
        lv_assign=(ListView)findViewById(R.id.list_flat);
        linn=(RelativeLayout)findViewById(R.id.linn);
        flatlist=new ArrayList<>();
        apartment_id =FlatListActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.APARTMENT_ID, null);

        getFlatListData();

    }

    private void getFlatListData() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            getFlatList_asyn checkin = new getFlatList_asyn();
            checkin.execute(apartment_id);
        } else {
            showsnackbar("No Internet");
        }
    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private class getFlatList_asyn extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String flat_name, plot_no_id;
        String server_message;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(FlatListActivity.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String apartment_id=params[0];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL+Constants.FLAT_LIST;
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

                        .appendQueryParameter("unit_id", apartment_id);
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
                 "LocationDetail": [
                 {
                 "id": "8",
                 "location_id": "Salarpuria sattva",
                 "person_name": "Shivan",
                 "mobile": "9460932777",
                 "email": "s@gmail.com",
                 "password": "",
                 "plot_no": "110",
                 "is_enable": "1",
                 "app_status": "1",
                 "photo": "photo1505294690.png",
                 "created": "2017-09-13 10:31:00",
                 "modified": "2017-09-13 11:24:50"
                 }
                 ],
                 "status": 1,
                 "message": "Successfully"
                 }
                 * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONArray jarry=res.getJSONArray("LocationDetail");

                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        server_message = res.optString("message");

                        for(int i=0;i<jarry.length();i++){
                            JSONObject j_obj=jarry.getJSONObject(i);
                            plot_no_id = j_obj.optString("id");
                            flat_name = j_obj.optString("plot_no");

                            Flatlist assigntoname = new Flatlist(plot_no_id,flat_name);
                            flatlist.add(assigntoname);
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
            if(server_status==1) {
                assignAdapter = new FlatAdapter(FlatListActivity.this, flatlist);
                    lv_assign.setAdapter(assignAdapter);
            }
            else{
                lv_assign.setVisibility(View.GONE);
                flat_txt.setVisibility(View.VISIBLE);
                Snackbar snackbar = Snackbar
                        .make(linn, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            progressDialog.cancel();
        }
    }
}
