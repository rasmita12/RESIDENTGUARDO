package android.stalwartgroup.residentguardo.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.CheckInternet;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mobileapplication on 9/18/17.
 */

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static EditText user_name,apart_name,flat_name,email,phno_number;
    public static String apartment_id,flat_id;
    Button reset_btn;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        apart_name=(EditText)findViewById(R.id.et_aprt_name);
        flat_name=(EditText)findViewById(R.id.et_flat_name);
        email=(EditText)findViewById(R.id.et_email);
        phno_number=(EditText)findViewById(R.id.et_mobile);
        reset_btn=(Button)findViewById(R.id.register_submit);
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

    }

    private void captureImage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (cameraIntent.resolveActivity(RegisterActivity.this.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(RegisterActivity.this,
                            "androidapp.com.stalwartsecurity",
                            photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        }
        else{
            imPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
            imageFile = new File(imPath);
            picUri = Uri.fromFile(imageFile); // convert path to Uri
            cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, picUri );
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = RegisterActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imPath = image.getAbsolutePath();
        imageFile = new File(imPath);
        picUri = Uri.fromFile(image); // convert path to Uri
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // imPath=picUri.getPath();
            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(),picUri);
                picAvailable=true;
                profile_image.setImageBitmap(photo);
                 profileImage=profile_image.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void validatefield() {
        String username=user_name.getText().toString();
        String aprtment=apart_name.getText().toString();
        String flat=flat_name.getText().toString();
        String emil=email.getText().toString();
        String mobil=phno_number.getText().toString();
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

    private class Checkin_register extends AsyncTask<String, Void, Void> {

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
            if (server_status == 2 || server_status==1) {
                showsnackbar(server_message);
                Intent intent=new Intent(RegisterActivity.this,SendOtpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            else {
                showsnackbar(server_message);

            }
        }
    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /*private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }*/
}
