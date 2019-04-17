package com.iyika.root.goart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    public static final int RC_PHOTO_PICKER = 2;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mGoArtStorageReference;

    private static final String TAG = "MainActivity";

    private Uri uri;

    EditText nameText, descriptionText, phoneText, priceText;

    Button postAction;

    String name, phone, description, price, text, text2, text3, text4;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        /*imgView = findViewById(R.id.imageView);
        BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        final byte[] arr = bitmap.getNinePatchChunk();*/



        nameText = (EditText)findViewById(R.id.nameText);
        descriptionText = (EditText)findViewById(R.id.descriptionText);
        phoneText = (EditText)findViewById(R.id.phoneText);
        priceText = (EditText)findViewById(R.id.priceText);

        postAction = (Button)findViewById(R.id.postButton);

        imageView = (ImageView)findViewById(R.id.imageView);







        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("number", phone);
        user.put("description", description);
        user.put("price", price);


        //initialize firebase components
        mFirebaseStorage = FirebaseStorage.getInstance();


        //Here i am linking my storage reference to the storage. goart_photos is the name of a storage folder omline
        mGoArtStorageReference = mFirebaseStorage.getReference().child("goart_photos");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                //creating a storage for text data
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


                /*db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/


    }



    //end of onCreate method


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    } //this code is to activate the back button in the toolbar




    public void pickImage(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"),RC_PHOTO_PICKER);

                Log.i("imagePicker:", "ImagePicker has been opened!");

    }




    public void postAction(View view) {


        Intent intent = new Intent(this, Display.class);
        text = nameText.getText().toString();
        text2 = phoneText.getText().toString();
        text3 = descriptionText.getText().toString();
        text4 = priceText.getText().toString();
        intent.putExtra("Text", text);
        intent.putExtra("Phone", text2);
        intent.putExtra("Description", text3);
        intent.putExtra("Price",text4);

        //code for passing image to different activity
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        intent.putExtra("Picture", b);



        startActivity(intent);


        Log.i("nameText:", nameText.getText().toString());
        Log.i("phoneText:", phoneText.getText().toString());
        Log.i("descriptionText:", descriptionText.getText().toString());
        Log.i("priceText", priceText.getText().toString());


        Uri uploadedImageUri = uri;
        final StorageReference photoRef =
                mGoArtStorageReference.child(uploadedImageUri.getLastPathSegment());

        //code to enable/activate upload to firebase storage
        photoRef.putFile(uploadedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("downloadUrl", uri.toString());
                        Toast.makeText(UpdateActivity.this, "added", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        /*if (uploadedImageUri == null){
            Toast.makeText(this, "Please upload a Picture.", Toast.LENGTH_LONG).show();
        }*/

    }









    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Here i am creating an image picker to enable images to be uploaded onto the app
        //the request code in the if statement should be equal to the request code of the pickImage Intent code
        if (resultCode==RESULT_OK){
            if(requestCode==2){
                uri = data.getData();
                try {

                    InputStream stream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {

                    Toast.makeText(this, "Image Failed to Upload! Try Again.", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

        } else if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri uploadedImageUri = data.getData();
            StorageReference photoRef =
                    mGoArtStorageReference.child(uploadedImageUri.getLastPathSegment());

            //code to enable/activate upload to firebase storage
            photoRef.putFile(uploadedImageUri);


        }


    }
    //end of onActivity for results

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                //sign out
                AuthUI.getInstance().signOut(this);
                return true;


            case R.id.categories:
                Intent intent1 = new Intent(this, Categories.class);
                startActivity(intent1);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
