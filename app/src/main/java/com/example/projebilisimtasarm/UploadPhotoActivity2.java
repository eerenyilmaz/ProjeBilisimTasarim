// Erol Eren Yılmaz-200001678, Semih Sayın-200001703

package com.example.projebilisimtasarm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UploadPhotoActivity2 extends AppCompatActivity {

    Bitmap selectedImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStoragePP;
    EditText editTextEmail;

    TextView textView;
    String email,email58,email588;
    String downloadUrl,name58,surname58;
    Uri imageData;
    private StorageReference storageReferencePP;
    ImageView imageView2;
    String name_get,surname_get;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.camera)
        {
            Intent intentToUpload = new Intent(UploadPhotoActivity2.this, Lessons.class);//Camera sayfası eklenecek
            startActivity(intentToUpload);
        }
        else if(item.getItemId() == R.id.home)
        {
            Intent intentToProfile = new Intent(UploadPhotoActivity2.this, Lessons.class);
            startActivity(intentToProfile);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo2);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.emailText);
        imageView2 = findViewById(R.id.imageView2);
        firebaseStoragePP = FirebaseStorage.getInstance();
        storageReferencePP = firebaseStoragePP.getReference();
        imageView2 = findViewById(R.id.imageView2);
        email58 = getIntent().getStringExtra("sign_in");
        name58 = getIntent().getStringExtra("name");
        surname58 = getIntent().getStringExtra("surname");
    }

    public void testButton1(View view){

    }

    public void okButton(View v)
    {
        if(imageData != null)
        {
            UUID uuid = UUID.randomUUID();
            final String imgName = "images/" + uuid + ".jpg";

            storageReferencePP.child(imgName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UploadPhotoActivity2.this,"Photo uploaded. Please click the test button.",Toast.LENGTH_LONG).show();
                    //download URL
                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imgName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            downloadUrl = uri.toString();

                            HashMap<String,Object> postData = new HashMap<>();
                            postData.put("downloadurl",downloadUrl);
                            postData.put("useremail",email58);
                            postData.put("name",name_get);
                            postData.put("surname",surname_get);
                            postData.put("status",0);

                            firebaseFirestore.collection("Student").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(UploadPhotoActivity2.this,"Kayıt oluştu.",Toast.LENGTH_LONG).show();
                                    String email = getIntent().getStringExtra("EXTRA_SESSION_ID");
                                    Intent intent = new Intent(UploadPhotoActivity2.this,Lessons.class);
                                    intent.putExtra("mail2", email58);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadPhotoActivity2.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
            CollectionReference collectionReference = firebaseFirestore.collection("Student");
            collectionReference.whereEqualTo("useremail",email58).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                    if(error != null)
                    {
                        Toast.makeText(UploadPhotoActivity2.this, error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }

                    if(queryDocumentSnapshots != null)
                    {
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                            Map<String,Object> data = snapshot.getData();
                            name_get = (String) data.get("name");
                            surname_get = (String) data.get("surname");
                            //System.out.println(name_get + surname_get);

                        }
                    }

                }
            });
        }
    }


    public void selectProfilePhoto(View v)
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else
        {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null ) {

            imageData = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView2.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    imageView2.setImageBitmap(selectedImage);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getDataFromFirestore()
    {

        email = getIntent().getStringExtra("EXTRA_SESSION_ID");

        CollectionReference collectionReference = firebaseFirestore.collection("Student");
        collectionReference.whereEqualTo("useremail",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                if(error != null)
                {
                    Toast.makeText(UploadPhotoActivity2.this, error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if(queryDocumentSnapshots != null)
                {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                        Map<String,Object> data = snapshot.getData();

                        //Casting
                        // String userEmail = (String) data.get("useremail");
                        downloadUrl = (String) data.get("downloadurl");
                        System.out.println(email);

                        //userEmailFromFB.add(userEmail);
                        //userImageFromFB.add(downloadUrl);


                    }
                }

            }
        });

    }


}
