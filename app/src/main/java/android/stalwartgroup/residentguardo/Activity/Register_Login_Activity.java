package android.stalwartgroup.residentguardo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.stalwartgroup.residentguardo.R;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by mobileapplication on 9/18/17.
 */

public class Register_Login_Activity extends AppCompatActivity   {

    Button register_btn,login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        register_btn=(Button)findViewById(R.id.register);
        login_btn=(Button)findViewById(R.id.login);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Register_Login_Activity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Register_Login_Activity.this,Login_Activity.class);
                startActivity(i);
            }
        });


    }
}
