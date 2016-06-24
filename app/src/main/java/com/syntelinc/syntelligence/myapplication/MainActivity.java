package com.syntelinc.syntelligence.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView lv;
    private Spinner sp;
    private String[] catType = {"Homework", "Take Home Test", "Quiz"};
    public TextView titleV, descriptionV, dueDateV, catV, pointV, scaleV, fileV, imageV, videoV;
    int newSize, counter;

    ArrayList<ArrayList<String>> newAssignmentsList = new ArrayList<>();
    String[] current = new String[]{};
    ArrayList<String> test = new ArrayList<>();
    ArrayList<String[]> assignmentsList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        initializeViews();
        createAssignmentList();


        Intent in = getIntent();

        makeUpdateList();




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewFlipper flip = (ViewFlipper) findViewById(R.id.updateinfoflip);
                setViews(position);
                flip.setDisplayedChild(flip.indexOfChild(findViewById(R.id.assignView)));

            }
        });


    }
    public void setViews(int pos){
        current = assignmentsList.get(pos);


        titleV.setText(current[0]);
        descriptionV.setText(current[1]);
        dueDateV.setText("Due: " + current[2] +  "/" +current[3]+ "/" + current[4] );
        pointV.setText("Max points: " + current[5]);
        catV.setText(current[6]);
        scaleV.setText(current[7]);
        fileV.setText(current[8]);
        imageV.setText(current[9]);
        videoV.setText(current[10]);

        if(current[6].equals("Select One...")){
            catV.setVisibility(View.GONE);
        }
        if(current[7].equals("scale")){
            scaleV.setVisibility(View.GONE);
        }
        if(current[5].equals("0")){
            pointV.setVisibility(View.GONE);
        }
        if(current[8].equals("Files: \n")){
            fileV.setVisibility(View.GONE);
        }
        if(current[9].equals("Images: \n")){
            imageV.setVisibility(View.GONE);
        }
        if(current[10].equals("Videos: \n")){
            videoV.setVisibility(View.GONE);
        }
    }



    public void createAssignmentList(){
        try {
            FileInputStream fIn = openFileInput("assignmentData.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(new InputStreamReader(fIn));
            String strLine = "";
            ArrayList<String> tokenArr = new ArrayList<>();
            String[] tokens = strLine.split(", ");
            while ((strLine = br.readLine()) != null){
                if (strLine.contains("%%%")){
                    String replace = strLine.replaceAll("%%%", "\n");
                    strLine = replace;
                }
                tokens = strLine.split("%#%");
                Log.d("Strings", strLine);
                Log.d("Strings", "At token[0] " + tokens[0]);
                assignmentsList.add(tokens);

            }



        }catch (Exception e){
            Log.d("Input Stream", "Input stream has failed");
        }
    }


    public void initializeViews(){
        lv= (ListView) findViewById(R.id.updatesView);
        titleV = (TextView) findViewById(R.id.titleView);
        descriptionV = (TextView) findViewById(R.id.desciptionView);
        dueDateV = (TextView) findViewById(R.id.dateView);
        catV = (TextView) findViewById(R.id.catagoryView);
        pointV = (TextView) findViewById(R.id.maxPointsView);
        scaleV = (TextView) findViewById(R.id.scaleView);
        fileV  = (TextView) findViewById(R.id.filesView);
        imageV = (TextView) findViewById(R.id.imagesView);
        videoV = (TextView) findViewById(R.id.videosView);
    }

    public void makeUpdateList() {
        lv = (ListView) findViewById(R.id.updatesView);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> assignList = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter;
        if(assignmentsList.size() == 0){
            assignList.add("No recent updates");

        }
        else{
            for(String[] strList: assignmentsList){
                assignList.add(strList[0]+"\n" + strList[2] +  "/"+strList[3]+ "/" + strList[4]);
            }

        }
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                assignList );

        lv.setAdapter(arrayAdapter);
    }


    public void infoOnClick(View view){
        Button infoB = (Button) findViewById(R.id.info);
        TextView infoT = (TextView) findViewById(R.id.infoText);
        ViewFlipper flip = (ViewFlipper) findViewById(R.id.updateinfoflip);

        infoT.setText("Changed");
        flip.setDisplayedChild(flip.indexOfChild(findViewById(R.id.infoView)));


    }



    public void updateOnClick(View view){
        Button updateB = (Button) findViewById(R.id.updates);
        ListView updateV = (ListView) findViewById(R.id.updatesView);

        ViewFlipper flip = (ViewFlipper) findViewById(R.id.updateinfoflip);
        flip.setDisplayedChild(flip.indexOfChild(findViewById(R.id.updateView)));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
