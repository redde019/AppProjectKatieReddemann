package com.syntelinc.syntelligence.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class ViewAssignment extends AppCompatActivity {


    private ListView lv;
    private Spinner sp;
    private String[] catType = {"Homework", "Take Home Test", "Quiz"};
    TextView titleV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_assignment_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent in = getIntent();
        Bundle extra = in.getExtras();
        titleV = (TextView)findViewById(R.id.titleView);

            boolean exists = extra.getBoolean("title");
            if(exists){

                titleV.setText(extra.getString("title"));
            }

        else{
            titleV.setText("Bundle Was Null");
        }


    }


    public void setTextViews() {
        Bundle bundle = getIntent().getExtras();
        TextView titleV = (TextView) findViewById(R.id.titleView);
        String title = bundle.getString("titleV");
        titleV.setText(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        ViewFlipper flip = (ViewFlipper)findViewById(R.id.view);

        //noinspection SimplifiableIfStatement

        if (id == R.id.main_activity) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if(id == R.id.create_assign){
            startActivity(new Intent(this, CreateAssignment.class));

        }
        if(id == R.id.view_assign){
            startActivity(new Intent(this, ViewAssignment.class));

        }

        return super.onOptionsItemSelected(item);
    }
}