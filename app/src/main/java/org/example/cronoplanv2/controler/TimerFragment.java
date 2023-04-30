package org.example.cronoplanv2.controler;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.ItemsDAO.SettingsDAO;
import org.example.cronoplanv2.model.Settings;

import java.util.concurrent.TimeUnit;


public class TimerFragment extends Fragment {
    private long timeCountInMilliSeconds;

    private enum TimerStatus {
        STARTED,
        STOPPED,
        UNSTARTED

    }
    private final SettingsDAO SETTINGS = new SettingsDAO();
    private TimerStatus timerStatus = TimerStatus.UNSTARTED;

    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private ImageView ivSettings;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;


    public TimerFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        // method call to initialize the views
        initViews(view);
        // method call to initialize the listeners
        initListeners(view);

        return view;
    }
    /**
     * method to initialize the views
     */
    private void initViews(View view) {
        progressBarCircle = (ProgressBar) view.findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) view.findViewById(R.id.tvTime);
        setTimerValues();
        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
        imageViewReset = (ImageView) view.findViewById(R.id.ivReset);
        imageViewStartStop = (ImageView) view.findViewById(R.id.ivStartStop);
        ivSettings = (ImageView) view.findViewById(R.id.ivSettings);
    }

    /**
     * method to initialize the click listeners
     */
    private void initListeners(View view) {
        imageViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        imageViewStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * method to reset count down timer
     */
    private void reset() {
        stopCountDownTimer();
        // call to initialize the timer values
        setTimerValues();
        // call to initialize the progress bar values
        setProgressBarValues();

        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
        // hiding the reset icon
        imageViewReset.setVisibility(View.GONE);
        // changing stop icon to start icon
        //imageViewStartStop.setImageResource(R.drawable.icon_start);
        // changing the timer status to stopped
        timerStatus = TimerStatus.UNSTARTED;
    }


    /**
     * method to start and stop count down timer
     */
    private void startStop() {
        if (timerStatus == TimerStatus.UNSTARTED) {

            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
                         //imageViewStartStop.setImageResource(R.drawable.icon_stop);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else if (timerStatus == TimerStatus.STARTED){

            // hiding the reset icon
            imageViewReset.setVisibility(View.GONE);
            // changing stop icon to start icon
                            //imageViewStartStop.setImageResource(R.drawable.icon_start);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        } else {
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            //imageViewStartStop.setImageResource(R.drawable.icon_stop);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();
        }

    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues() {
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = SETTINGS.getSettings().getTime()*60*1000;;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeCountInMilliSeconds=millisUntilFinished;

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {
                reset();
            }
        }.start();
        countDownTimer.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }

    /**
     * set the settings in case they have changed
     */
}