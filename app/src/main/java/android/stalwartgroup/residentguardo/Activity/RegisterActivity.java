package android.stalwartgroup.residentguardo.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.stalwartgroup.residentguardo.Interface.OTPListener;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.CheckInternet;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mobileapplication on 9/18/17.
 */

public class RegisterActivity extends AppCompatActivity implements OTPListener {

    Toolbar toolbar;
    public static EditText user_name,apart_name,flat_name,email,phno_number,phone,et_otp;
    public static String apartment_id,flat_id;
    Button reset_btn,otp_button;
    RelativeLayout linn;
    CircleImageView profile_image;
    private static final int CAMERA_REQUEST = 1888;
    String imPath;
    File imageFile;
    Uri picUri=null;
    Boolean picAvailable=false;
    String profileImage;
    String resident_type;
    RadioGroup radiogroup;
    RadioButton residenttype,tenattype;
    String resident_data,tenant_data;
    TextView resend_otp;
    RelativeLayout relative_lay_register,relative_otp;
    String otp;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION =100;
    private static final int PERMISSION_ACCESS_MESSAGE =101;
    private static final int PERMISSION_ACCESS_CALL =102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                PERMISSION_ACCESS_MESSAGE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                PERMISSION_ACCESS_CALL);
        resend_otp=(TextView)findViewById(R.id.resend_otp);
        relative_lay_register=(RelativeLayout)findViewById(R.id.relative_lay_register);
        relative_otp=(RelativeLayout)findViewById(R.id.relative_otp);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register_Login_Activity.class));
                finish();
            }
        });
        radiogroup=(RadioGroup)findViewById(R.id.resident_type);
        residenttype=(RadioButton) findViewById(R.id.resident);
        tenattype=(RadioButton) findViewById(R.id.tenant);
        profile_image=(CircleImageView) findViewById(R.id.profile_image);
        linn=(RelativeLayout)findViewById(R.id.linn);
        user_name=(EditText)findViewById(R.id.et_username);
        phone=(EditText)findViewById(R.id.et_phone);
        apart_name=(EditText)findViewById(R.id.et_aprt_name);
        flat_name=(EditText)findViewById(R.id.et_flat_name);
        email=(EditText)findViewById(R.id.et_email);
        et_otp=(EditText)findViewById(R.id.et_otp);
        phno_number=(EditText)findViewById(R.id.et_mobile);
        reset_btn=(Button)findViewById(R.id.register_submit);
        otp_button=(Button)findViewById(R.id.otp_submit);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatefield();
            }
        });
        apart_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this,AppartmentListActivity.class);
                startActivity(i);
            }
        });
        flat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this,FlatListActivity.class);
                startActivity(i);
            }
        });
        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpvalidate();
            }
        });
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatefield();

            }
        });

    }

    private void otpvalidate() {
       if(et_otp.getText().toString().trim().length()<=0){
           showsnackbar("Enter OTP");
       }
       else{
            OtpsendToserver();
       }
    }

    private void OtpsendToserver() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            Checkin_otp checkin = new Checkin_otp();
            String mobil=phno_number.getText().toString();
             otp=et_otp.getText().toString();

            checkin.execute(mobil,otp);
        } else {
            showsnackbar("No Internet");
        }
    }

    private void validatefield() {
        if (residenttype.isChecked()) {
            resident_data = residenttype.getText().toString();
        } else if (tenattype.isChecked()) {
            resident_data = tenattype.getText().toString();
        }
        if(user_name.getText().toString().trim().length()<0){
            Toast.makeText(RegisterActivity.this,"Enter Username",Toast.LENGTH_SHORT).show();

        }
        else if(apart_name.getText().toString().trim().length()<0){
            Toast.makeText(RegisterActivity.this,"Enter Your Apartment name",Toast.LENGTH_SHORT).show();
        }

        else if(flat_name.getText().toString().trim().length()<0){
            Toast.makeText(RegisterActivity.this,"Enter Your Flat name",Toast.LENGTH_SHORT).show();
        }
       else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Toast.makeText(RegisterActivity.this,"Invalid Email Address",Toast.LENGTH_SHORT).show();
        }
        else if(phno_number.getText().toString().trim().length()<0){
            Toast.makeText(RegisterActivity.this,"Enter Mobile Number",Toast.LENGTH_SHORT).show();
        }
        else{
            CheckinServer();
        }
    }

    private void CheckinServer() {
        OtpReader.bind(RegisterActivity.this);
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            Checkin_register checkin = new Checkin_register();
            String username=user_name.getText().toString();
            String emil=email.getText().toString();
            String mobil=phno_number.getText().toString();
            String flatname=flat_name.getText().toString();
            if (residenttype.isChecked()) {
                resident_data = residenttype.getText().toString();
            } else if (tenattype.isChecked()) {
                resident_data = tenattype.getText().toString();
            }
            checkin.execute(mobil,username,emil,resident_data,apartment_id,flatname);
        } else {
            showsnackbar("No Internet");
        }
    }

    @Override
    public void otpReceived(String messageText) {

         otp=messageText.substring(messageText.lastIndexOf(".") -4,messageText.length()-11);
            et_otp.setText(otp);
            otp_button.performClick();
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int i) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int i, int i1) {
        return null;
    }

    private class Checkin_register extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String id, otp_no, name;
        String server_message;
        String user_type;
        String photo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(RegisterActivity.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String mobile = params[0];
                String userName = params[1];
                String email = params[2];
                String residentType = params[3];
                String apartname = params[4];
                String flat = params[5];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL+Constants.REGISTRATION;
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
                        .appendQueryParameter("name", userName)
                        .appendQueryParameter("mobile", mobile)
                        .appendQueryParameter("location_id", apartname)
                        .appendQueryParameter("user_type",residentType)
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("flat_no",flat);

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
                 "locationDetail_id": "5",
                 "status": 1,
                 "message": "Successfully inserted wait for the approval."
                 }
                 * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if (server_status == 2) {
                        id = res.optString("locationDetail_id");
                        otp_no = res.optString("otp");
                        server_message = res.optString("message");

                        //showsnackbar(server_message);

                    }
                    else if(server_status==1) {
                        server_message = res.optString("message");

                    }else {
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
            progressDialog.cancel();
            if (server_status == 0){
                showsnackbar(server_message);
            }
            else {

                SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.REGISTER_LOCATIONDETAIL_ID, id);
                editor.commit();
                showsnackbar(server_message);
                showsnackbar(otp_no);
                calltoOtpservice();
            }
        }
    }

    private void calltoOtpservice() {
        relative_lay_register.setVisibility(View.GONE);
        relative_otp.setVisibility(View.VISIBLE);
    }
    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private class Checkin_otp extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String id, mobile, name;
        String server_message;
        String user_type;
        String photo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(RegisterActivity.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String mobile = params[0];
                String otp = params[1];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL+Constants.VERIFY_OTP;
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
                        .appendQueryParameter("mobile", mobile)
                        .appendQueryParameter("otp", otp);

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
                 "locationDetail_id": "5",
                 "status": 1,
                 "message": "Successfully inserted wait for the approval."
                 }
                 * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        server_message = res.optString("message");
                    }else {
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
            progressDialog.cancel();
            if (server_status == 0){
                showsnackbar(server_message);
            }
            else {
                Intent i=new Intent(RegisterActivity.this,PendingActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        }
    }

}
