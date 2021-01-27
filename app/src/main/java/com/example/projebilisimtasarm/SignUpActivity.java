// Erol Eren Yılmaz-200001678, Semih Sayın-200001703
package com.example.projebilisimtasarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    EditText name,surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        imageView = findViewById(R.id.imageView);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.nameText);
        surname = findViewById(R.id.surNameText);

    }

    public void upload(View v)
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String userEmail = firebaseUser.getEmail();
        final String post_name = name.getText().toString();
        final String post_surname = surname.getText().toString();

        HashMap<String,Object> postData = new HashMap<>();

        String email = getIntent().getStringExtra("EXTRA_SESSION_ID");
        Intent intent = new Intent(SignUpActivity.this, UploadPhotoActivity.class);
        intent.putExtra("mail", userEmail);
        intent.putExtra("name", post_name);
        intent.putExtra("surname", post_surname);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
