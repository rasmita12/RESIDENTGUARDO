package android.stalwartgroup.residentguardo.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.stalwartgroup.residentguardo.Util.MultipartUtility;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mobileapplication on 9/26/17.
 */

public class ResidentProfile extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String user_name,user_email,user_mobile,user_photo,user_apartment,user_flat,user_apartmentID;
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
    private static String[] STORAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};





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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // here it is checking whether the permission is granted previously or not
            if (!hasPermissions(getActivity(), STORAGE_PERMISSIONS)) {
                //Permission is granted
                ActivityCompat.requestPermissions(getActivity(), STORAGE_PERMISSIONS, 1);


            }
        }

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
        tv_profileName.setEnabled(false);
        add_phone.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);

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
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editfield();
            }
        });
        return v;
    }
    private void editfield() {
        tv_profileName.setEnabled(true);
        add_phone.setEnabled(true);
        email.setEnabled(true);
        phone.setEnabled(true);
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void validatefields() {

        if(email.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Email Address",Toast.LENGTH_SHORT).show();

        }
        else if(phone.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Valid Number",Toast.LENGTH_SHORT).show();

        }
        else{
            checkinserver();
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
             //imPath=picUri.getPath();
             //Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),picUri);
                picAvailable=true;
                resident_profileimg.setImageBitmap(photo);
                profileImage=String.valueOf(imageFile);



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
        user_apartmentID = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_APARTMENT_ID, null);
        user_flat = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_FLAT_NAME, null);

        tv_profileName.setText(user_name);
        String address=user_apartment+" "+" ,"+" "+user_flat;
        tv_address.setText(address);
        email.setText(user_email);
        phone.setText(user_mobile);
        if(!user_photo.isEmpty()) {
            Picasso.with(getContext()).load(user_photo).into(resident_profileimg);
        }
        else {
            resident_profileimg.setImageResource(R.drawable.logo_without_name);
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

            checkin.execute(username,emil,mobil,user_id,user_apartmentID,user_flat);
        } else {
            showsnackbar("No Internet");
        }
    }
    private class Checkin_resident extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Checkin_resident";
        private ProgressDialog progressDialog = null;
        int server_status;
        String id, otp_no, name;
        String server_message;
        String user_type;
        String photo;
        String charset = "UTF-8";
        String link = Constants.MAINURL+Constants.RESIDENT_PROFILE;
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
                String userid = params[3];
                String aprtid = params[4];
                String flatname = params[5];
                MultipartUtility multipart = new MultipartUtility(link, charset);
                        multipart.addFormField("name", userName);
                        multipart.addFormField("mobile", mobile);
                        multipart.addFormField("email",email);
                        multipart.addFormField("id",userid);
                        multipart.addFormField("location_id",aprtid);
                        multipart.addFormField("plot_no",flatname);
                if(imageFile!=null){
                    multipart.addFilePart("photo",imageFile );
                }
                List<String> response = multipart.finish();
                System.out.println("SERVER REPLIED:");
                String res = "";
                for (String line : response) {
                    res = res + line + "\n";
                }
                Log.i(TAG, res);


                /**
                 * {
                 "status": 1,
                 "message": "Successfully updated"
                 }
                 * */


                if (res != null && res.length() > 0) {
                    JSONObject ress = new JSONObject(res.trim());
                    server_status = ress.optInt("status");
                    if (server_status == 1) {
                        server_message = ress.optString("message");

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
            if (server_status == 1){
                showsnackbar(server_message);
                tv_profileName.setEnabled(false);
                add_phone.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(false);
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
}
