package android.stalwartgroup.residentguardo.Fragment;

import android.os.Bundle;
import android.stalwartgroup.residentguardo.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView tv_profileName, tv_address;
    EditText email, add_phone, phone;
    ImageView resident_profileimg;


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

        return v;
    }
}