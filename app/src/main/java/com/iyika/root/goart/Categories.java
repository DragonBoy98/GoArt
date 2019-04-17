package com.iyika.root.goart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

public class Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    } //this code is to activate the back button in the toolbar


    public void paintingsFeed(View view){
        Intent intent = new Intent(this, Paintings.class);
        startActivity(intent);
    }

    public void drawingsFeed(View view){
        Intent intent = new Intent(this, Drawings.class);
        startActivity(intent);
    }

    public void handicraftsFeed(View view){
        Intent intent = new Intent(this, Handicrafts.class);
        startActivity(intent);
    }

    public void fashionFeed(View view){
        Intent intent = new Intent(this, Fashion.class);
        startActivity(intent);
    }

    public void digitalFeed(View view){
        Intent intent = new Intent(this,Digital.class);
        startActivity(intent);
    }

    public void moreOptions(View view){
            Intent intent = new Intent(this, Options.class);
            startActivity(intent);
    }

    public void sellFunction(View view){
        Intent intent = new Intent(this, UpdateActivity.class);
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
