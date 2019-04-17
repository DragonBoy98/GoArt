package com.iyika.root.goart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

import org.w3c.dom.Text;

public class Display extends AppCompatActivity {

    TextView textView;
    TextView displayPhone, displayDesc, displayPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);



        textView = (TextView)findViewById(R.id.displayText);
        textView.setText(getIntent().getStringExtra("Text"));
        Log.i("NAME", textView.getText().toString());

        displayPhone = (TextView)findViewById(R.id.phoneText);
        displayPhone.setText(getIntent().getStringExtra("Phone"));
        Log.i("PHONE",displayPhone.getText().toString());

        displayDesc = (TextView)findViewById(R.id.descriptionText);
        displayDesc.setText(getIntent().getStringExtra("Description"));
        Log.i("DESCRIPTION",displayDesc.getText().toString());

        displayPrice = (TextView)findViewById(R.id.priceText);
        displayPrice .setText(getIntent().getStringExtra("Price"));
        Log.i("PRICE",displayPrice.getText().toString());


        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("Picture");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        ImageView image = (ImageView) findViewById(R.id.imgDisplay);
        image.setImageBitmap(bmp);




    }

    public void newPost(View view){
        Intent intent = new Intent(this, UpdateActivity.class);
        startActivity(intent);
    }

    public void categoriesFunction(View view){
        Intent intent = new Intent(this, Categories.class);
        startActivity(intent);
    }

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
