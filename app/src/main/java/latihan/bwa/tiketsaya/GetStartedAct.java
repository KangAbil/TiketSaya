package latihan.bwa.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedAct extends AppCompatActivity {

    Animation ttb,btt;
    Button btn_signin, btn_new_account_create;
    ImageView emblem_app;
    TextView intro_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //load element
        btn_signin = findViewById(R.id.btn_signin);
        btn_new_account_create = findViewById(R.id.btn_new_account_create);
        emblem_app = findViewById(R.id.emblem_app);
        intro_app = findViewById(R.id.intro_app);


        //load animasi
        ttb = AnimationUtils.loadAnimation(this,R.anim.ttb);
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);

        //run animasi
        emblem_app.startAnimation(ttb);
        intro_app.startAnimation(ttb);
        btn_signin.startAnimation(btt);
        btn_new_account_create.startAnimation(btt);


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin = new Intent(GetStartedAct.this,SignInAct.class);
                startActivity(signin);
            }
        });

        btn_new_account_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(GetStartedAct.this,RegisterOneAct.class);
                startActivity(gotoregisterone);

            }
        });
    }
}
