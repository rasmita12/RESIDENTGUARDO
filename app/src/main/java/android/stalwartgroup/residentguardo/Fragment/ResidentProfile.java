package android.stalwartgroup.residentguardo.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

/**
 * Created by mobileapplication on 9/26/17.
 */

public class ResidentProfile extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String user_name,user_email,user_mobile,user_photo,user_apartment,user_flat;
    String user_id;
    TextView  tv_address;
    EditText email,add_phone,phone,tv_profileName;
    private static final int CAMERA_REQUEST = 1888;
    ImageView resident_profileimg,resident_profileimg_edit;
    String imPath;
    File imageFile;
    Uri picUri=null;
    Boolean picAvailable=false;
    String profileImage;
    Button save_button,edit_button;
    RelativeLayout linn;



    public ResidentProfile() {
        // Required empty public constructor
    }

    public static ResidentProfile newInstance(String param1, String param2) {
        ResidentProfile fragment = new ResidentProfile();
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
        View v = inflater.inflate(R.layout.resident_profile, container, false);

        tv_profileName=(EditText)v.findViewById(R.id.tv_profileName);
        tv_address=(TextView)v.findViewById(R.id.tv_address);
        email=(EditText)v.findViewById(R.id.user_email);
        phone=(EditText)v.findViewById(R.id.user_phone);
        add_phone=(EditText)v.findViewById(R.id.user_phone1);
        resident_profileimg=(ImageView)v.findViewById(R.id.resident_profileimg);
        resident_profileimg_edit=(ImageView)v.findViewById(R.id.profile_img_edit);
        save_button=(Button) v.findViewById(R.id.save_button);
        edit_button=(Button) v.findViewById(R.id.edit_button);
        linn=(RelativeLayout)v.findViewById(R.id.linn);

        getUserdetaildata();

        resident_profileimg_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureimage();
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatefields();
            }
        });
        return v;
    }

    private void validatefields() {

        if(email.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Email Address",Toast.LENGTH_SHORT).show();

        }
        else if(phone.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Valid Number",Toast.LENGTH_SHORT).show();

        }
        else if(add_phone.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Valid Number",Toast.LENGTH_SHORT).show();

        }
        
        else{
            checkinserver();
        }
    }

    private void checkinserver() {
        if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
            Checkin_resident checkin = new Checkin_resident();
            String username=tv_profileName.getText().toString();
            String emil=email.getText().toString();
            String mobil=phone.getText().toString();
            String addphone=add_phone.getText().toString();
            String address=tv_address.getText().toString();

            checkin.execute(username,emil,mobil,profileImage,user_id);
        } else {
            showsnackbar("No Internet");
        }
    }

    private void captureimage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
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
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),picUri);
                picAvailable=true;
                resident_profileimg.setImageBitmap(photo);
                profileImage=resident_profileimg.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void getUserdetaildata() {
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.REGISTER_USER_ID, null);
        user_name = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_NANE, null);
        user_email = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_EMAIL, null);
        user_mobile = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_MOBILE, null);
        user_photo = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_PHOTO, null);
        user_apartment = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_APARTMENT_NAME, null);
        user_flat = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_FLAT_NAME, null);

        tv_profileName.setText(user_name);
        String address=user_apartment+" "+" ,"+" "+user_flat;
        tv_address.setText(address);
        email.setText(user_email);
        phone.setText(user_mobile);
        if(user_photo!=null) {
            Picasso.with(getContext()).load(user_photo).into(resident_profileimg);
        }
        else {
            resident_profileimg.setImageResource(R.drawable.logo_without_name);
        }
    }

    private class Checkin_resident extends AsyncTask<String, Void, Void> {

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
                progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String userName = params[0];
                String email = params[1];
                String mobile = params[2];
                String photo = params[3];
                String userid = params[4];
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
                        .appendQueryParameter("user_type",email)
                        .appendQueryParameter("plot_no",photo)
                        .appendQueryParameter("firebase_reg_id",userid);

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
                 "PreRegistration_id": "2",
                 "mobile": "9460932771",
                 "otp": "9723",
                 "status": 1,
                 "message": "Inserted details but mobile number verfication is pending."
                 }
                 * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        id = res.optString("PreRegistration_id");
                        otp_no = res.optString("otp");
                        server_message = res.optString("message");

                        //showsnackbar(server_message);

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
            progressDialog.cancel();
            if (server_status == 0){
                showsnackbar(server_message);
            }

        }
    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
