package org.example.cronoplanv2.controler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.ItemsDAO.SettingsDAO;
import org.example.cronoplanv2.model.ItemsDAO.TaskDAO;
import org.example.cronoplanv2.model.Settings;
import org.example.cronoplanv2.model.Task;

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
    private TextView textViewTime,tvTaskId;
    private ImageView ivSettings,imageViewReset,imageViewStartStop,ivEdit,ivDeleteTask;
    private CountDownTimer countDownTimer;
    private EditText etTitleTimer,etDescripTimer;
    private Spinner cbStatusTimer;
    private ConstraintLayout clTimerTask;
    boolean editFlag = true;
    private Task currentTask;
    private final TaskDAO TASKDATA = new TaskDAO();


    public TimerFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            currentTask = (Task) args.getSerializable("task");
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
        clTimerTask = view.findViewById(R.id.clTimerTask);
        progressBarCircle = (ProgressBar) view.findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) view.findViewById(R.id.tvTime);
        setTimerValues();
        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
        imageViewReset = (ImageView) view.findViewById(R.id.ivReset);
        imageViewStartStop = (ImageView) view.findViewById(R.id.ivStartStop);
        ivSettings = (ImageView) view.findViewById(R.id.ivSettings);
        tvTaskId=(TextView) view.findViewById(R.id.tvTaskIdTimer);
        ivEdit=(ImageView) view.findViewById(R.id.ivEditTask);
        ivDeleteTask=(ImageView) view.findViewById(R.id.ivDeleteTask);

        // create a ConstraintSet object
        ConstraintSet constraintSet = new ConstraintSet();
        // connect the constraintSet with the ConstraintLayout
        constraintSet.clone(clTimerTask);
        if(currentTask!=null){
            tvTaskId.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.VISIBLE);
            ivDeleteTask.setVisibility(View.VISIBLE);
            etTitleTimer = (EditText) view.findViewById(R.id.etTitleTimer);
            etTitleTimer.setText(currentTask.getTitle());
            etDescripTimer = (EditText) view.findViewById(R.id.etDescripTimer);
            etDescripTimer.setText(currentTask.getDescription());
            cbStatusTimer=(Spinner) view.findViewById(R.id.cbStatusTimer);
            cbStatusTimer.setEnabled(false);
            cbStatusTimer.setSelection(currentTask.getStatus());
            tvTaskId.setText("ID: "+String.valueOf(currentTask.getId()));


        }else{
            clTimerTask.setVisibility(View.INVISIBLE);
            tvTaskId.setVisibility(View.INVISIBLE);
            ivEdit.setVisibility(View.INVISIBLE);
        }
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
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editFlag) {
                    ivEdit.setImageResource(R.drawable.salvar);
                    cbStatusTimer.setEnabled(true);
                    etTitleTimer.setEnabled(true);
                    etDescripTimer.setEnabled(true);
                    editFlag=false;
                } else {
                    ivEdit.setImageResource(R.drawable.editar);
                    cbStatusTimer.setEnabled(false);
                    etTitleTimer.setEnabled(false);
                    etDescripTimer.setEnabled(false);
                    editFlag=true;
                    TASKDATA.updateTask(new Task(etTitleTimer.getText().toString(),etDescripTimer.getText().toString(),cbStatusTimer.getSelectedItemPosition(),Integer.parseInt(tvTaskId.getText().toString().split(" ")[1])));
                }

            }
        });
        ivDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete this task?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Perform the delete operation here
                        deleteTask();
                    }
                });
                builder.setNegativeButton("No", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void deleteTask(){
        TASKDATA.deleteTask(Integer.parseInt(tvTaskId.getText().toString().split(" ")[1]));
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
        insertTime(false);

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
            insertTime(false);


        } else if (timerStatus == TimerStatus.STARTED){

            // hiding the reset icon
            imageViewReset.setVisibility(View.GONE);
            // changing stop icon to start icon
                            //imageViewStartStop.setImageResource(R.drawable.icon_start);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
            insertTime(false);
        } else {
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            //imageViewStartStop.setImageResource(R.drawable.icon_stop);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();
            insertTime(false);
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
    private void insertTime(boolean terminated){
        String taskId = (String) tvTaskId.getText();
        if(!taskId.equals("")){
        int id = Integer.parseInt(taskId.split(" ")[1]); // Extract the second part after splitting on whitespace
        TASKDATA.insertTime(id,terminated);}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            insertTime(true);
        }

    /**
     * set the settings in case they have changed
     */
}}