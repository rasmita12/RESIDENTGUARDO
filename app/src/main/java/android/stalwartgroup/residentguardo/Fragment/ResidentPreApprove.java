package android.stalwartgroup.residentguardo.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.CheckInternet;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mobileapplication on 9/27/17.
 */

public class ResidentPreApprove extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String user_name, user_email, user_mobile, user_photo, user_apartment, user_flat;
    String user_id;
    TextView tv_profileName, tv_address,number_of_visitor;
    public  static EditText visitor_name,visitor_phone,visitor_comingfrm,visitor_vehicle,visitor_arrival,visitor_leaving;
    ImageView add_contact;
    static final int PICK_CONTACT=1;
    private static final int REQUEST_CODE = 1;
    Context context =this.getActivity();
    Uri uriContact;
    Spinner adult,child;
    String contactID;
    ImageView imageView,edit_img;
    Button generate_pass;
    Calendar myCalendar;
    RelativeLayout linn;
    String adult_value,child_value;
    String imPath;
    File imageFile;
    Uri picUri=null;
    Boolean picAvailable=false;
    String profileImage;
    private static final int CAMERA_REQUEST = 1888;
    private static String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};



    public ResidentPreApprove() {
        // Required empty public constructor
    }

    public static ResidentPreApprove newInstance(String param1, String param2) {
        ResidentPreApprove fragment = new ResidentPreApprove();
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
        View v = inflater.inflate(R.layout.preapprove, container, false);



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // here it is checking whether the permission is granted previously or not
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                //Permission is granted
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 1);

            }
        }
         imageView = (ImageView)v.findViewById(R.id.resident_profileimg);
        add_contact=(ImageView)v.findViewById(R.id.add_phone_name);
        edit_img=(ImageView)v.findViewById(R.id.approve_img);
        visitor_name=(EditText)v.findViewById(R.id.visitor_name);
        visitor_phone=(EditText)v.findViewById(R.id.visitor_phone);
        visitor_comingfrm=(EditText)v.findViewById(R.id.visitor_comingfrom);
        visitor_vehicle=(EditText)v.findViewById(R.id.visitor_vehicle);
        visitor_arrival=(EditText)v.findViewById(R.id.visitor_arrival_date);
        visitor_leaving=(EditText)v.findViewById(R.id.visitor_leaving_date);
        number_of_visitor=(TextView)v.findViewById(R.id.visitor_expctd_guest);
        tv_profileName=(TextView)v.findViewById(R.id.tv_profileName);
        tv_address=(TextView)v.findViewById(R.id.tv_address);
        adult=(Spinner)v.findViewById(R.id.adult_spin);
        child=(Spinner)v.findViewById(R.id.child_spin);
        linn=(RelativeLayout)v.findViewById(R.id.linn);
        generate_pass=(Button) v.findViewById(R.id.generate_pass);
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContact();
            }
        });
        myCalendar= Calendar.getInstance();
        visitor_arrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datepickerDialog= new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datepickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datepickerDialog.show();
            }
        });
        visitor_leaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datepickerDialog= new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datepickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datepickerDialog.show();
            }
        });
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureimage();
            }
        });
        generate_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatefield();
            }
        });
        adult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 adult_value = adult.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 child_value = adult.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        getuserdetails();

        return v;
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



    private void validatefield() {
        if(visitor_name.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Visitor Name",Toast.LENGTH_SHORT).show();
        }
        else if(visitor_phone.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Valid Number",Toast.LENGTH_SHORT).show();

        }
        else if(visitor_arrival.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Arrival Date",Toast.LENGTH_SHORT).show();

        }
        else if(visitor_leaving.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Leaving Date",Toast.LENGTH_SHORT).show();

        }
        else if(visitor_comingfrm.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Coming From",Toast.LENGTH_SHORT).show();

        }
       else if(visitor_vehicle.getText().toString().trim().length()<=0){
            Toast.makeText(getContext(),"Enter Vehicle Number",Toast.LENGTH_SHORT).show();

        }

        else{
            checkinserver();
        }
    }

    private void checkinserver() {
        if (CheckInternet.getNetworkConnectivityStatus(getContext())) {
            Checkin_preapprove checkin = new Checkin_preapprove();
            String username=visitor_name.getText().toString();
            String phone=visitor_phone.getText().toString();
            String arival_date=visitor_arrival.getText().toString();
            String leave_date=visitor_leaving.getText().toString();
            String cmngfm=visitor_comingfrm.getText().toString();
            String vehicleno=visitor_vehicle.getText().toString();

            checkin.execute(username,phone,arival_date,leave_date,cmngfm,vehicleno,profileImage,user_id);
        } else {
            showsnackbar("No Internet");
        }
    }

    private void showsnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(linn, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
            updateLabel1();
        }

    };

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        visitor_arrival.setText(sdf.format(myCalendar.getTime()));
        //incident_date=(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        visitor_leaving.setText(sdf.format(myCalendar.getTime()));
        //incident_date=(sdf.format(myCalendar.getTime()));
    }

    private void getuserdetails() {
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.REGISTER_USER_ID, null);
        user_name = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_NANE, null);
        user_apartment = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_APARTMENT_NAME, null);
        user_flat = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_FLAT_NAME, null);
        tv_profileName.setText(user_name);
        String address=user_apartment+" "+" ,"+" "+user_flat;
        tv_address.setText(address);
    }

    private void getContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            switch(requestCode){
                case CAMERA_REQUEST:
                    try {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),picUri);
                        picAvailable=true;
                        imageView.setImageBitmap(photo);
                        profileImage=imageView.toString();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }                    break;  //optional
                case PICK_CONTACT:
                    uriContact = data.getData();

                    retrieveContactName();
                    retrieveContactNumber();
                    retrieveContactPhoto();
                    break;  //optional

                default:
                   // code to be executed if all cases are not matched;
            }

        }
    }

    public Bitmap retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(photo);

            }

            assert inputStream != null;
            if(inputStream!=null)
            inputStream.close();


        } catch (MalformedURLException e) {
            System.out.println("Bad ad URL");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not get remote ad image");
            e.printStackTrace();
        }
        return photo;

    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getActivity().getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d("phoe", "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            visitor_phone.setText(contactNumber);
        }

        cursorPhone.close();

        Log.d("PHONE", "Contact Phone Number: " + contactNumber);
    }

    private void retrieveContactName() {
        String contactName = null;

        // querying contact data store
        Cursor cursor = getActivity().getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            visitor_name.setText(contactName);
        }

        cursor.close();

        Log.d("Contactname", "Contact Name: " + contactName);
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

    private class Checkin_preapprove extends AsyncTask<String, Void, Void> {

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
}