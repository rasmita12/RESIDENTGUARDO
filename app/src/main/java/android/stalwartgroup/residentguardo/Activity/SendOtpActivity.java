package android.stalwartgroup.residentguardo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.stalwartgroup.residentguardo.R;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by mobileapplication on 9/19/17.
 */

public class SendOtpActivity extends AppCompatActivity {

    Button otp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp_btn=(Button)findViewById(R.id.otp_submit);
        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SendOtpActivity.this,PendingActivity.class);
                startActivity(i);
            }
        });
    }
}
