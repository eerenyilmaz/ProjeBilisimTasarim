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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadPhotoActivity extends AppCompatActivity {

    private String url = "http://" + "10.0.2.2" + ":" + 5000 + "/user_email";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    Bitmap selectedImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStoragePP;
    EditText editTextEmail;
    String email,email58;
    String downloadUrl,name58,surname58;
    Uri imageData;
    private StorageReference storageReferencePP;
    ImageView imageView2;

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
            Intent intentToUpload = new Intent(UploadPhotoActivity.this, Lessons.class);
            startActivity(intentToUpload);
        }


        else if(item.getItemId() == R.id.home)
        {
            Intent intentToProfile = new Intent(UploadPhotoActivity.this, Lessons.class);
            startActivity(intentToProfile);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uoploadphoto);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.emailText);
        imageView2 = findViewById(R.id.imageView2);
        firebaseStoragePP = FirebaseStorage.getInstance();
        storageReferencePP = firebaseStoragePP.getReference();
        imageView2 = findViewById(R.id.imageView2);
        email58 = getIntent().getStringExtra("mail");
        name58 = getIntent().getStringExtra("name");
        surname58 = getIntent().getStringExtra("surname");

    }

    public void testButton(View v)
    {
        postRequest("smh@syn.com", url);

        //Intent intent = new Intent(UploadPhotoActivity.this,Lessons.class);
        //intent.putExtra("mail2", email58);
        //startActivity(intent);
    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

    private void postRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
       /* Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();*/
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/user_email?email="+email58)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(UploadPhotoActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();


                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(UploadPhotoActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
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
                    Toast.makeText(UploadPhotoActivity.this,"Photo uploaded. Please click the test button.",Toast.LENGTH_LONG).show();
                    //download URL

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imgName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            downloadUrl = uri.toString();


                            HashMap<String,Object> postData = new HashMap<>();

                            postData.put("downloadurl",downloadUrl);
                            postData.put("name",name58);
                            postData.put("surname",surname58);
                            postData.put("useremail",email58);
                            postData.put("status",0);

                            firebaseFirestore.collection("Student").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(UploadPhotoActivity.this,"Kayıt oluştu.",Toast.LENGTH_LONG).show();


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
                    Toast.makeText(UploadPhotoActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
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

    /*public void getDataFromFirestore()
    {

        email = getIntent().getStringExtra("EXTRA_SESSION_ID");

        CollectionReference collectionReference = firebaseFirestore.collection("Student");
        collectionReference.whereEqualTo("useremail",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                if(error != null)
                {
                    Toast.makeText(UploadPhotoActivity.this, error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
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

    }*/


}
