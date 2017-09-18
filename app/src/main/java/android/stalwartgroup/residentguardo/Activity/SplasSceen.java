package android.stalwartgroup.residentguardo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.stalwartgroup.residentguardo.R;
import android.support.v7.app.AppCompatActivity;

public class SplasSceen extends AppCompatActivity {
    private static final int SPLASH_INTERVAL_TIME= 3000;
    String user_id,user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sceen);

        //user_id = SplasSceen.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_ID, null);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplasSceen.this, Register_Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        }, SPLASH_INTERVAL_TIME);

    }
}
