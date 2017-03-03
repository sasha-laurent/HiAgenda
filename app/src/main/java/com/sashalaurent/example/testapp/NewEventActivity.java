package com.sashalaurent.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Sasha on 01/03/2017.
 */

public class NewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        setTitle("Nouvel événement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent();
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                myIntent = new Intent(NewEventActivity.this, MainActivity.class);
                break;
        }

        startActivity(myIntent);
        return super.onOptionsItemSelected(item);
    }
}
