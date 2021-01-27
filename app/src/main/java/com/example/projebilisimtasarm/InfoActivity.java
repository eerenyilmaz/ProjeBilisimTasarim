// Erol Eren Yılmaz-200001678, Semih Sayın-200001703
package com.example.projebilisimtasarm;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Map;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    String email_come,name;
    String get_email, get_name,get_surname,lesson,get_status;
    TextView textView,textView2,textView3,textView8,textView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        firebaseFirestore = FirebaseFirestore.getInstance();
        email_come = getIntent().getStringExtra("mail2");
        name = getIntent().getStringExtra("name2");
        lesson = getIntent().getStringExtra("lesson_name");
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        textView8.setText(lesson);
        getDataFromFirestore();


    }

    public void getDataFromFirestore()
    {
        CollectionReference collectionReference = firebaseFirestore.collection("Student");
        collectionReference.whereEqualTo("useremail",email_come).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                if(error != null)
                {
                    Toast.makeText(InfoActivity.this, error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if(queryDocumentSnapshots != null)
                {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> data = snapshot.getData();
                        get_email = (String) data.get("useremail");
                        get_name = (String) data.get("name");
                        get_surname = (String) data.get("surname");
                        get_status = (String) data.get("status");
                        textView.setText(get_email);
                        textView2.setText(get_name);
                        textView3.setText(get_surname);
                        if(get_status == "1")
                        {
                            textView9.setText("Derse giriş uygun");
                        }
                    }
                }
            }
        });

    }
}
