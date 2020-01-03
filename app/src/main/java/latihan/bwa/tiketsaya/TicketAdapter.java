package latihan.bwa.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*digunakan untuk mengambil atau melisting sebuah konten yang dimiliki pada firebase */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    Context context; //dibutuhkan sebuah content(variabel) dengan nama Context
    ArrayList<MyTicket> myTicket; //data-nya berupa array yang didapatkan pada myTicket

    public TicketAdapter(Context c, ArrayList<MyTicket> p){
        context = c;
        myTicket = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //LayoutInflater untuk mereplace/mengganti/merubah konten yang sudah ada
        return new MyViewHolder(LayoutInflater
                .from(context).inflate(R.layout.item_myticket,
                        viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //mendapatkan data tersebut dari firebase

        myViewHolder.xnama_wisata.setText(myTicket.get(i).getNama_wisata());
        myViewHolder.xlokasi.setText(myTicket.get(i).getLokasi());
        myViewHolder.xjumlah_tiket.setText(myTicket.get(i).getJumlah_tiket() + " Tickets");

        final String getNamaWisata = myTicket.get(i).getNama_wisata();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*menyimpan intent pada "nama_wisata" yang akan digunakan sebagai referensi
                 ketika mengambil sebuah data  pada firebase*/
                Intent gotomyticketdetails = new Intent(context, MyTicketDetailAct.class);
                gotomyticketdetails.putExtra("nama_wisata", getNamaWisata);
                context.startActivity(gotomyticketdetails);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myTicket.size(); //item berdasarkan ukuran dari myTicket misal ada 5 ya 5, misal ada 10 ya 10
    }

    /*class yang mendefinisikan komponen-komponen yang kita punya nantinya*/
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView xnama_wisata, xlokasi, xjumlah_tiket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //setelah didaftarkan nanti akan ditimban dengan data baru dari firebase
            xnama_wisata = itemView.findViewById(R.id.xnama_wisata);
            xlokasi = itemView.findViewById(R.id.xlokasi);
            xjumlah_tiket = itemView.findViewById(R.id.xjumlah_tiket);

        }
    }

}

