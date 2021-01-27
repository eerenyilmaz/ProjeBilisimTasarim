// Erol Eren Yılmaz-200001678, Semih Sayın-200001703
package com.example.projebilisimtasarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Lessons extends AppCompatActivity {
    ListView list;

    String[] maintitle ={
            "Bulut Bilişim","Bilgisayar Sistemler Lab.",
            "Veri Madenciliği","Computer Networks",
            "Paralel Bilgisayarlar",
    };

    String[] subtitle ={
            "Dr.Öğr.Üyesi Alper ÖZPINAR","Dr.Öğr.Üyesi Mustafa Cem KASAPBAŞI",
            "Dr. Arzu Kakışım","Prof.Dr. Abdül Halim ZAİM",
            "Doç.Dr. TURGAY ALTILAR",
    };

    Integer[] imgid={
            R.drawable.dunermiflin,R.drawable.dunermiflin,
            R.drawable.dunermiflin,R.drawable.dunermiflin,
            R.drawable.dunermiflin,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        MyListAdapter adapter=new MyListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);

        final String email41 = getIntent().getStringExtra("mail2");
        final String name41 = getIntent().getStringExtra("name2");
        final String surname41 = getIntent().getStringExtra("surname2");
        final String email411 = getIntent().getStringExtra("sign_in");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    Intent intent = new Intent(Lessons.this,InfoActivity.class);
                    intent.putExtra("mail2", email41);
                    intent.putExtra("name2", name41);
                    intent.putExtra("surname2", surname41);
                    intent.putExtra("mail22", email411);
                    intent.putExtra("lesson_name",maintitle[0]);
                    startActivity(intent);
                }
                else if(position == 1) {
                    Intent intent = new Intent(Lessons.this,InfoActivity.class);
                    intent.putExtra("mail2", email41);
                    intent.putExtra("name2", name41);
                    intent.putExtra("surname2", surname41);
                    intent.putExtra("mail22", email411);
                    intent.putExtra("lesson_name",maintitle[1]);
                    startActivity(intent);
                }

                else if(position == 2) {
                    Intent intent = new Intent(Lessons.this,InfoActivity.class);
                    intent.putExtra("mail2", email41);
                    intent.putExtra("name2", name41);
                    intent.putExtra("surname2", surname41);
                    intent.putExtra("mail22", email411);
                    intent.putExtra("lesson_name",maintitle[2]);
                    startActivity(intent);
                }
                else if(position == 3) {
                    Intent intent = new Intent(Lessons.this,InfoActivity.class);
                    intent.putExtra("mail2", email41);
                    intent.putExtra("name2", name41);
                    intent.putExtra("surname2", surname41);
                    intent.putExtra("mail22", email411);
                    intent.putExtra("lesson_name",maintitle[3]);
                    startActivity(intent);
                }
                else if(position == 4) {
                    Intent intent = new Intent(Lessons.this,InfoActivity.class);
                    intent.putExtra("mail2", email41);
                    intent.putExtra("name2", name41);
                    intent.putExtra("surname2", surname41);
                    intent.putExtra("mail22", email411);
                    intent.putExtra("lesson_name",maintitle[4]);
                    startActivity(intent);
                }

            }
        });
    }
}