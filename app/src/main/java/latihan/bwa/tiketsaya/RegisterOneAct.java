package latihan.bwa.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneAct extends AppCompatActivity {

    Button btn_continue;
    LinearLayout btn_back;
    EditText xusername, xpassword, xemail_address;
    DatabaseReference reference; /*tiketsaya-65251 adalah references, digunakan untuk menyimpan data
    pada https://console.firebase.google.com/u/0/project/tiketsaya-65251/database/tiketsaya-65251/data */

    //penyimpanan data secara lokal yang perlu disimpan untuk menampilkan data2 terkait (tiket,foto,dll)
    String USERNAME_KEY = "usernamekey";
    String username_key = ""; /*string untuk mendaftarkan string USERNAME_KEY, isinya
    kosong karena belum ada nilai, nilainya akan diambil pada EditText */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);


        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);
        xemail_address = findViewById(R.id.xemail_address);


        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();
                final String email_address = xemail_address.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Username Kosong !",Toast.LENGTH_SHORT).show();
                    //ubah state menjadi signin
                    btn_continue.setEnabled(true);
                    btn_continue.setText("SIGN IN");
                }
                else
                    if (password.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Password Kosong !",Toast.LENGTH_SHORT).show();
                        //ubah state menjadi signin
                        btn_continue.setEnabled(true);
                        btn_continue.setText("SIGN IN");
                }
                else
                    if (email_address.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Email Kosong !",Toast.LENGTH_SHORT).show();
                        //ubah state menjadi signin
                        btn_continue.setEnabled(true);
                        btn_continue.setText("SIGN IN");
                    }
                else {
                    //menyimpan data kepada local storage
                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(username_key, xusername.getText().toString());
                    editor.apply();

                    //simpan kepada database
                        reference = FirebaseDatabase.getInstance()
                                .getReference().child("Users").child(xusername.getText().toString());
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                                dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                                dataSnapshot.getRef().child("email_address").setValue(xemail_address.getText().toString());
                                dataSnapshot.getRef().child("user_balance").setValue(800);
                                /*data yang diisi pada EditText akan tersimpan pada database maka menggunakan getRef
                                sedangkan jika mengambil data yang sudah tersedia menggunakan setText */
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //berpindah acitivity
                        Intent gotoregistertwo = new Intent(RegisterOneAct.this,RegisterTwoAct.class);
                        startActivity(gotoregistertwo);
                    }

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backtosignin = new Intent(RegisterOneAct.this,SignInAct.class);
                startActivity(backtosignin);
            }
        });

    }
}
