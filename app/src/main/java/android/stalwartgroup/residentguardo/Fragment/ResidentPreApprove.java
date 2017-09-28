package android.stalwartgroup.residentguardo.Fragment;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

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
    public  static EditText visitor_name,visitor_phone,visitor_comingfrm,visitor_vehicle;
    ImageView add_contact;
    static final int PICK_CONTACT=1;
    private static final int REQUEST_CODE = 1;
    Context context =this.getActivity();
    Uri uriContact;
    String contactID;
    ImageView imageView;
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
        visitor_name=(EditText)v.findViewById(R.id.visitor_name);
        visitor_phone=(EditText)v.findViewById(R.id.visitor_phone);
        visitor_comingfrm=(EditText)v.findViewById(R.id.visitor_comingfrom);
        visitor_vehicle=(EditText)v.findViewById(R.id.visitor_vehicle);
        number_of_visitor=(TextView)v.findViewById(R.id.visitor_expctd_guest);
        tv_profileName=(TextView)v.findViewById(R.id.tv_profileName);
        tv_address=(TextView)v.findViewById(R.id.tv_address);
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContact();
            }
        });

        getuserdetails();

        return v;
    }

    private void getuserdetails() {
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
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();

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

}