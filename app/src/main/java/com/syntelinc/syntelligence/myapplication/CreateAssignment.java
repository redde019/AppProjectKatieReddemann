package com.syntelinc.syntelligence.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.content.Intent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateAssignment extends AppCompatActivity {


    private ListView lv;
    private Spinner sp, catSpin;
    private String[] catType = {"Select One...", "Homework", "Take Home Test", "Quiz"};


    private static int TAKE_PICTURE = 1;
    private int ACTIVITY_START_CAMERA_APP = 0;
    private SeekBar seekPoints;
    ImageButton button, vidButton, fileB;
    private Uri imageUri;


    RadioGroup scaleG;
    Date date;
    DatePicker dateP;
    TextView savedMess, maxPoints;
    EditText titleT, descriptionT;
    int seekBarProgress;
    ArrayList<String> imagesList;
    String files ="Files: \n";
    String images= "Images: \n";
    String videos = "Videos: \n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_assignment_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        makeCatTypeList();
        initializeViews();

        seekPoints.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxPoints = (TextView) findViewById(R.id.maxPointsText);
                seekBarProgress = progress;
                maxPoints.setText("Max Points:  " + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        vidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callVideoAppIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File file = getFile(ACTIVITY_START_CAMERA_APP);
                callVideoAppIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                callVideoAppIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(callVideoAppIntent, ACTIVITY_START_CAMERA_APP);
            }
        });

    }

    //Use fragment to populate list
    //Can only extend one thing, but need listFragment and ListActivity
//    public void fileButtonOnClick(){
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.add(R.id.fileFragment, new FileDirectory());
//        ft.commit();
//
//    }


    public void initializeViews() {
        seekBarProgress = 0;
        fileB = (ImageButton) findViewById(R.id.fileButton);
        button = (ImageButton) findViewById(R.id.cameraButton);

        vidButton = (ImageButton) findViewById(R.id.videoButton);
        savedMess = (TextView) findViewById(R.id.savedMessage);

        seekPoints = (SeekBar) findViewById(R.id.maxPointsBar);
        imagesList = new ArrayList<>();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Video Has Been Saved", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == TAKE_PICTURE && requestCode == RESULT_OK) {

            Toast.makeText(getApplicationContext(), "Picture Has Been Saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void cameraOnClick(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getFile(TAKE_PICTURE);
        imageUri = Uri.fromFile(file);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_intent, TAKE_PICTURE);
    }

    public File getFile(int type) {

        File folder = new File("sdcard/Assignment");
        if (!folder.exists()) {
            folder.mkdir();
        }
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        if (type == TAKE_PICTURE) {
            File image_file = new File(folder, ts + ".jpg");
            images.concat(ts+".jpg\n");

            return image_file;
        }
        if (type == ACTIVITY_START_CAMERA_APP) {
            File video_file = new File(folder, ts + ".mp4");
            videos = videos + ts+ ".mp4\n";
            return video_file;
        }
        return null;
    }


    private void makeCatTypeList() {
        sp = (Spinner) findViewById(R.id.catTypeSpinner);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                catType);

        sp.setAdapter(arrayAdapter);
    }

    public void getDataFromForm() {
        titleT = (EditText) findViewById(R.id.title);
        descriptionT = (EditText) findViewById(R.id.description);
        catSpin = (Spinner) findViewById(R.id.catTypeSpinner);
        scaleG = (RadioGroup) findViewById(R.id.scaleRadioGroup);

        dateP = (DatePicker) findViewById(R.id.dueDatePicker);
        date = new Date(dateP.getYear() - 1900, dateP.getMonth(), dateP.getDayOfMonth());
    }

    public void sendAssignOnClick(View view) {
        getDataFromForm();
        String titleString = titleT.getText().toString();
        String descpition = descriptionT.getText().toString();

        if (titleString.contains("\n")){
            String replace = titleString.replaceAll("\n", "%%%");
            titleString = replace;
        }
        if (descpition.contains("\n")){
            String replace = descpition.replaceAll("\n", "%%%");
            descpition = replace;
        }
        if (files.contains("\n")){
            String replace = files.replaceAll("\n", "%%%");
            files = replace;
        }
        if (images.contains("\n")){
            String replace = images.replaceAll("\n", "%%%");
            images = replace;
        }

        if (videos.contains("\n")){
            String replace = videos.replaceAll("\n", "%%%");
            videos = replace;
        }

        if (titleT.length() == 0 || descriptionT.length() == 0) {
            Toast.makeText(getApplicationContext(), "Input Title and Description", Toast.LENGTH_SHORT).show();
        } else {

            String scaleText = "scale";
            if (scaleG.getCheckedRadioButtonId() != -1) {
                int id = scaleG.getCheckedRadioButtonId();
                View radioButton = scaleG.findViewById(id);
                int radioId = scaleG.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) scaleG.getChildAt(radioId);
                scaleText = (String) btn.getText();
            }


            String catText = catSpin.getSelectedItem().toString();
            int month = dateP.getMonth() + 1;


            ArrayList<String> details = new ArrayList<String>();

            details.add(titleString);
            details.add(descpition);
            details.add(String.valueOf(month));
            details.add(String.valueOf(dateP.getDayOfMonth()));
            details.add(String.valueOf(dateP.getYear()));
            details.add(String.valueOf(seekBarProgress));
            details.add(catText);
            details.add(scaleText);
            details.add(files);
            details.add(images);
            details.add(videos);

            addToFile(details);

            startActivity(new Intent(this, MainActivity.class));


        }

    }
    public void addToFile(ArrayList<String> arr){


        String between = "%#%";
        String text= arr.get(0) + between + arr.get(1) + between  + arr.get(2) + between + arr.get(3) + between + arr.get(4) + between + arr.get(5) + between
                + arr.get(6) + between  + arr.get(7)+ between  + arr.get(8)+ between  + arr.get(9)+ between  + arr.get(10);
        Log.d("add", "adding to file    " + text);
        try{
            Log.d("Adding", "Trying to add");

            FileOutputStream fOut = openFileOutput("assignmentData.txt", MODE_APPEND);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(text);
            myOutWriter.append("\n");
            myOutWriter.flush();
            myOutWriter.close();
            fOut.close();
        }
        catch (Exception e){
            Log.d("Adding", "Failed to add");
        }


    }
    public void showMoreOnClick(View view) {
        Button showB = (Button) findViewById(R.id.showMoreButton);
        RelativeLayout relShow = (RelativeLayout) findViewById(R.id.relativeShowMore);


        if (relShow.getVisibility() == View.INVISIBLE) {
            relShow.setVisibility(View.VISIBLE);
        } else {
            relShow.setVisibility(View.INVISIBLE);
        }


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
        ViewFlipper flip = (ViewFlipper) findViewById(R.id.view);

        //noinspection SimplifiableIfStatement

        if (id == R.id.main_activity) {

            startActivity(new Intent(this, MainActivity.class));
        }
        if (id == R.id.create_assign) {
            startActivity(new Intent(this, CreateAssignment.class));

        }
        if (id == R.id.view_assign) {
            startActivity(new Intent(this, ViewAssignment.class));

        }

        return super.onOptionsItemSelected(item);
    }
}
