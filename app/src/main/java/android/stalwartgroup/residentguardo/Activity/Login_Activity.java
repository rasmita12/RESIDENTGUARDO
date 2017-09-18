package android.stalwartgroup.residentguardo.Activity;

import android.Manifest;
import android.os.Bundle;
import android.stalwartgroup.residentguardo.R;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by mobileapplication on 9/18/17.
 */

public class Login_Activity extends AppCompatActivity{

        EditText et_phone,et_pass;
        Button bt_login,bt_reset;
/*    private static final int PERMISSION_ACCESS_CAMERA=101;
    private static final int PERMISSION_READ_STORAGE=103;
    private static final int PERMISSION_WRITE_STORAGE=104;*/
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_);
        }
}
