package android.stalwartgroup.residentguardo.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.stalwartgroup.residentguardo.Pojo.User;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.CheckInternet;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

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

public class SplasSceen extends AppCompatActivity {

    private static final int SPLASH_INTERVAL_TIME= 3000;
    String user_id,user_type,register_id,fcm_id;
    RelativeLayout linn;
    ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sceen);


        linn=(RelativeLayout)findViewById(R.id.linn);
       // CheckApprove();

        user_id = SplasSceen.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.REGISTER_USER_ID, null);
        fcm_id = SplasSceen.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.FCM_ID, null);
        register_id = SplasSceen.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).
                getString(Constants.REGISTER_ID, null);


        if(register_id==null || register_id.trim().length()<0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplasSceen.this, Register_Login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_INTERVAL_TIME);
        }
        else if(user_id==null|| user_id.trim().length()<0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            CheckApprove();
                }
            }, SPLASH_INTERVAL_TIME);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplasSceen.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(intent);

                    finish();
                }
            }, SPLASH_INTERVAL_TIME);

        }


    }

    private void CheckApprove() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            Checkin_user_approve checkin = new Checkin_user_approve();


            checkin.execute(register_id);
        } else {
            showsnackbar("No Internet");
        }
    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private class Checkin_user_approve extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
       // private ProgressDialog progressDialog = null;
        int server_status;
        String user_email_id, user_mobile,user_id, user_name,IS_ENABLE,user_flatname,user_aprtment;
        String server_message;
        String user_type;
        String user_photo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*if (progressDialog == null) {
                progressDialog = ProgressDialog.show(SplasSceen.this, "Loading", "Please wait...");
            }*/
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String register_id = params[0];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL+Constants.REGISTER_USER_LIST;
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
                        .appendQueryParameter("preregistration_id", register_id);

                //.appendQueryParameter("deviceid", deviceid);
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
                 "User": {
                 "id": "49",
                 "name": "Rasmita",
                 "email_id": "rasmi@gmail.com",
                 "mobile": "8594938936",
                 "photo": "http://stalwartsecurity.in/admin/files/photo/",
                 "address": null,
                 "user_type": "Owner",
                 "firebase_reg_id": "5",
                 "location_name": "Brigade Metropolis",
                 "flat_name": "L-1906"
                 },
                 "is_approved": 1,
                 "status": 1,
                 "message": "Approved User"
                 }
                 // for reject
                 {
                 "is_approved": 3,
                 "status": 2,
                 "message": "Rejeted by the admin"
                 }
                 * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");

                    if (server_status == 1) {
                        server_message = res.optString("message");

                            JSONObject jobj=res.getJSONObject("User");
                            user_id = jobj.optString("id");
                            user_name = jobj.optString("name");
                            user_email_id = jobj.optString("email_id");
                            user_mobile = jobj.optString("mobile");
                            user_photo = jobj.optString("photo");
                            IS_ENABLE = jobj.optString("is_enable");
                            String login_status = jobj.optString("status");
                            login_status = jobj.optString("status");
                            user_aprtment = jobj.optString("location_name");
                            user_flatname = jobj.optString("flat_name");
                            User ulist = new User(user_id, user_name, user_email_id, user_mobile, user_photo,user_aprtment,user_flatname);
                            userArrayList.add(ulist);


                    }
                    else if(server_status==2){
                        server_message=res.optString("message");

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
            //progressDialog.cancel();
            if (server_status == 1){
                showsnackbar(server_message);
                SharedPreferences sharedPreferences = SplasSceen.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.REGISTER_USER_ID, user_id);
                editor.putString(Constants.USER_NANE, user_name);
                editor.putString(Constants.USER_MOBILE, user_mobile);
                editor.putString(Constants.USER_EMAIL, user_email_id);
                editor.putString(Constants.USER_PHOTO, user_photo);
                editor.putString(Constants.USER_TYPE, user_type);
                editor.putString(Constants.USER_FLAT_NAME, user_flatname);
                editor.putString(Constants.USER_APARTMENT_NAME, user_aprtment);
                editor.commit();
                Intent i=new Intent(SplasSceen.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
            else if(server_status==2){
                showsnackbar(server_message);
                Intent i=new Intent(SplasSceen.this,RejectActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
            else if(server_status==3){
                showsnackbar(server_message);
                Intent i=new Intent(SplasSceen.this,PendingActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }

            else {
                showsnackbar(server_message);

            }
        }
    }

}
