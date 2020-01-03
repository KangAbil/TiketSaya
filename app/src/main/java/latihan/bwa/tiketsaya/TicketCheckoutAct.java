package latihan.bwa.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_buy_ticket, btnplus, btnmines;
    TextView textjumlahtiket, texttotalharga, textmybalance, nama_wisata, lokasi, ketentuan;
    Integer valuejumlahtiket = 1;
    Integer mybalance = 200;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    ImageView notice_uang;
    Integer sisa_balance = 0;

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";

    //membuat nomer transaksi secara random (generate nomoer transaksi secara random agar unik)
    Integer nomor_transaksi = new Random().nextInt();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        //deklarasi penghubung variabel dengan id pada XML
        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btnplus = findViewById(R.id.btnplus);
        btnmines = findViewById(R.id.btnmines);
        textjumlahtiket = findViewById(R.id.textjumlahtiket);
        texttotalharga = findViewById(R.id.texttotalharga);
        textmybalance = findViewById(R.id.textmybalance);
        notice_uang = findViewById(R.id.notice_uang);

        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);


        //setting value baru untuk beberapa komponen
        textjumlahtiket.setText(valuejumlahtiket.toString());


        //secara default, kita hide btnmines
        btnmines.animate().alpha(0).setDuration(300).start();
        btnmines.setEnabled(false);
        notice_uang.setVisibility(View.GONE); //JADI SI NOTICE UANG HILANG DULU

        //mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textmybalance.setText("US$ "+mybalance+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //mengambil data dari firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());

                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                valuehargatiket=Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                //operasi aritmatik total . hargatiket . jumlahtiket
                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("US$ "+valuetotalharga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //event pada saat di klik pada button +
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahtiket+=1; //jumlah tiket akan terus bertambah 1

                textjumlahtiket.setText(valuejumlahtiket.toString());//textjumlahtiket hanya text, yang berubah adalah valuejumlahtiket karena integer
                if (valuejumlahtiket > 1){
                    btnmines.animate().alpha(1).setDuration(300).start();
                    btnmines.setEnabled(true);
                }
                //operasi aritmatika total harga yang perlu dibayar berarti harga tiket * jumlahtiket
                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("US$ "+valuetotalharga+"");
                //kondisi disaat harga(jumlahtiket) lebih dari uang yang dimiliki
                if (valuetotalharga > mybalance) {
                    btn_buy_ticket.animate().alpha(0).translationY(250).setDuration(350).start();
                    btn_buy_ticket.setEnabled(false);
                    textmybalance.setTextColor(Color.parseColor("#D1206B")) ; //ubah warna jadi merah
                    notice_uang.setVisibility(View.VISIBLE);

                }
            }
        });

        //event pada saat di klik pada button -
        btnmines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahtiket-=1; //jumlah tiket akan terus berkurang 1
                textjumlahtiket.setText(valuejumlahtiket.toString());//textjumlahtiket hanya text, yang berubah adalah valuejumlahtiket karena integer
                if (valuejumlahtiket < 2){
                    btnmines.animate().alpha(0).setDuration(300).start();
                    btnmines.setEnabled(false);
                }
                //operasi aritmatika total harga yang perlu dibayar berarti harga tiket * jumlahtiket
                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("US$ "+valuetotalharga+"");
                //kondisi disaat harga(jumlahtiket) kurang dari uang yang dimiliki
                if (valuetotalharga < mybalance) {
                    btn_buy_ticket.animate().alpha(1).translationY(0).setDuration(350).start();
                    btn_buy_ticket.setEnabled(true);
                    textmybalance.setTextColor(Color.parseColor("#203DD1")); //ubah warna jadi biru
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });

        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ubah state menjadi loading
                btn_buy_ticket.setEnabled(false);
                btn_buy_ticket.setText("Loading...");

                //menyimpan data user kepada firebase dan membuat tabel baru "MyTickets"
                reference3 = FirebaseDatabase.getInstance()
                        .getReference().child("MyTickets").child(username_key_new)
                        .child(nama_wisata.getText().toString() + nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(valuejumlahtiket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);

                        Intent gotosuccessticket = new Intent(TicketCheckoutAct.this,SuccessBuyTicketAct.class);
                        startActivity(gotosuccessticket);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //update data balance kepada users (yang saat ini login)
              reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
              reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      sisa_balance = mybalance - valuetotalharga;
                      reference4.getRef().child("user_balance").setValue(sisa_balance);
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
