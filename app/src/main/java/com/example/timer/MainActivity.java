package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    //Initiate Variables
    ImageButton stopButton;
    ImageButton playButton;
    ImageButton pauseButton;
    EditText enterText;
    TextView setText ;
    TextView updateText;
    TextView clock ;
    SharedPreferences sharedPreferences;
    String subject;
    String tempSubject;
    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking Variables to Items on in APP
        stopButton = findViewById(R.id.stopButton);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        enterText = findViewById(R.id.enterText);
        setText = findViewById(R.id.setText);
        updateText = findViewById(R.id.updateText);
        clock = findViewById(R.id.clock);
        sharedPreferences = getSharedPreferences("com.example.timer", MODE_PRIVATE);

        //Call function to check for saved data
        checkSharedPreferences();

        //if instance was destroyed, restore values
        if (savedInstanceState != null)
        {

            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        //Call function for Stopwatch
        runTimer();

        //If Play button is selected
        //Set the variable running to True
        //which will start/continue incrementing the clock in the runTimer function
        //also show a 'toast' that says 'Play'
        playButton.setOnClickListener(view ->
        {
            running = true;
            Toast toast = Toast.makeText(MainActivity.this, "Play", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,350);
            toast.show();
        });

        //If Pause button is selected
        //Set the variable running to False
        //which will pause incrementing the clock in the runTimer function
        //also show a 'toast' that says 'Pause'
        pauseButton.setOnClickListener(view ->
        {
            running = false;
            Toast toast = Toast.makeText(MainActivity.this, "Pause", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,350);
            toast.show();
        });

        //If Stop button is selected
        //Set the variable running to False
        //which will stop incrementing the clock in the runTimer function
        //and update the 'latest action' text at the top of the screen
        //also show a 'toast' that says 'Stop'
        //the latest action test will be saved for when the app is opened the next time
        //Finally, This will reset the stopwatch
        stopButton.setOnClickListener(view ->
        {
            running = false;
            tempSubject = ("You spent " + clock.getText().toString() + " on " + enterText.getText().toString() + " last time.");
            updateText.setText(tempSubject);
            Toast toast = Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,350);
            toast.show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(subject, "You spent " + clock.getText().toString() + " on " + enterText.getText().toString() + " last time.");
            editor.apply();

            seconds = 0;
        });


    }

        //Function that runs every second and increments the stopwatch if play button has been selected (running=ture)
        private void runTimer()
        {
            final Handler handler = new Handler();

            handler.post(new Runnable()
            {
                //set variables for each section of stopwatch
                //using value of 'seconds' variable
                //to give sec, min, and hour values
                @Override
                public void run()
                {
                    int hours = seconds / 3600;
                    int minutes = (seconds % 3600) /60;
                    int sec = seconds % 60;

                    //creates a string that looks like a stopwatch
                    //and inserts time values as above
                    String time = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, sec);
                    clock.setText(time);
                    //If stopwatch active
                    //add 1 second
                    if (running)
                    {
                        seconds++;
                    }
                    //Delay this code for 1 second
                    handler.postDelayed(this, 1000);
                }
            });
        }

    //reset the relevant values if app is paused for any reason
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    //save APP values on Pause
    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    //on resuming APP, set running to true if relevant
    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning)
        {
            running = true;
        }
    }

    //Set 'last action' text as per the previously saved shared preferences value
    public void checkSharedPreferences()
    {
        String statement = sharedPreferences.getString(subject,"");
        updateText.setText(statement);

    }
}

