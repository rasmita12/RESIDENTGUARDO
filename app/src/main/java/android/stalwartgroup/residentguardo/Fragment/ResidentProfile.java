package android.stalwartgroup.residentguardo.Fragment;

import android.os.Bundle;
import android.stalwartgroup.residentguardo.R;
import android.stalwartgroup.residentguardo.Util.Constants;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    TextView tv_profileName, tv_address;
    EditText email,add_phone,phone;
    ImageView resident_profileimg;



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

        tv_profileName=(TextView)v.findViewById(R.id.tv_profileName);
        tv_address=(TextView)v.findViewById(R.id.tv_address);
        email=(EditText)v.findViewById(R.id.user_email);
        phone=(EditText)v.findViewById(R.id.user_phone);
        add_phone=(EditText)v.findViewById(R.id.user_phone1);
        resident_profileimg=(ImageView)v.findViewById(R.id.resident_profileimg);
        getUserdetaildata();

        return v;
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
}
