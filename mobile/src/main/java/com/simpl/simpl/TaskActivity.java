package com.simpl.simpl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

import resources.Task;

public class TaskActivity extends ActionBarActivity
            implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    //Global variables
    public Context mainContext = this;
    public int theme = 1;

    //All of our important views in our dialog
    private EditText nameEdit;
    private EditText noteEdit;
    private EditText locationEdit;
    private CheckBox locationCheck;
    private CheckBox timeCheck;
    private CheckBox repeatCheck;
    private CheckBox dueCheck;
    private TimePickerDialog timePickActive;
    private TimePickerDialog timePickDue;
    private DatePickerDialog datePickActive;
    private DatePickerDialog datePickDue;
    private RadioButton dailyButton;
    private RadioButton weeklyButton;

    //Task List Views
    private ObservableScrollView scrollView;
    private LinearLayout taskLayout;
    private FloatingActionButton fab;

    //Our holder of tasks
    ArrayList<Task> tasks;

    //TODO: Load color programmatically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        taskLayout = (LinearLayout) findViewById(R.id.taskLayout);

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });
        fab.attachToScrollView(scrollView);

        tasks = new ArrayList<Task>();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    /**
     * Starts the dialog to create a new task
     */
    public void createNewTask(){

        final Resources re = getResources();

        boolean wrapInScrollView = true;
        MaterialDialog.Builder dialogB = new MaterialDialog.Builder(this)
                .title(re.getString(R.string.create_title))
                .positiveText(re.getString(R.string.create_create))
                .neutralText(re.getString(R.string.create_clear))
                .negativeText(re.getString(R.string.create_cancel))
                .autoDismiss(false)
                .customView(R.layout.create_task_view, wrapInScrollView);

        dialogB.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {

                //Check to make sure the necessary views are available
                boolean good = checkCreateOptions();

                if(good){
                    getTaskFromDialog();
                    updateListView();
                    dialog.dismiss();
                }
                else {

                }

            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onNeutral(MaterialDialog dialog) {

                nameEdit.setText(""); noteEdit.setText(""); locationEdit.setText("");
                timeCheck.setChecked(false); locationCheck.setChecked(false); timeCheck.setChecked(false);
                repeatCheck.setChecked(false); dueCheck.setChecked(false);

            }
        });

        MaterialDialog dialog = dialogB.build();

        dialog.show();

        nameEdit = (EditText) dialog.getCustomView().findViewById(R.id.taskNameEdit);
        noteEdit = (EditText) dialog.getCustomView().findViewById(R.id.noteEdit);
        locationEdit = (EditText) dialog.getCustomView().findViewById(R.id.taskLocationEdit);
        locationCheck = (CheckBox) dialog.getCustomView().findViewById(R.id.locationCheck);
        timeCheck = (CheckBox) dialog.getCustomView().findViewById(R.id.timeCheck);
        repeatCheck = (CheckBox) dialog.getCustomView().findViewById(R.id.repeatCheck);
        dailyButton = (RadioButton) dialog.getCustomView().findViewById(R.id.dailyButton);
        weeklyButton = (RadioButton) dialog.getCustomView().findViewById(R.id.weeklyButton);
        dueCheck = (CheckBox) dialog.getCustomView().findViewById(R.id.dueCheck);

        //TODO: Temp fix until appcompat setPadding is fixed ---------------------------------------------------------
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int namePX = (int) (64 * (metrics.densityDpi / 160f));
        int notePX = (int) (126 * (metrics.densityDpi / 160f));
        int locationPX = (int) (105 * (metrics.densityDpi / 160f));
        nameEdit.setPadding(namePX ,nameEdit.getPaddingTop(),nameEdit.getPaddingRight(),nameEdit.getPaddingBottom());
        noteEdit.setPadding(notePX ,noteEdit.getPaddingTop(),noteEdit.getPaddingRight(),noteEdit.getPaddingBottom());
        locationEdit.setPadding(locationPX, locationEdit.getPaddingTop(), locationEdit.getPaddingRight(), locationEdit.getPaddingBottom());
        //-------------------------------------------------------------------------------------------------------------

        //Set disable/enable for repeat button
        repeatCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dailyButton.setEnabled(true);
                    weeklyButton.setEnabled(true);
                    dailyButton.setChecked(true);
                }
                else {
                    dailyButton.setChecked(false);
                    weeklyButton.setChecked(false);
                    dailyButton.setEnabled(false);
                    weeklyButton.setEnabled(false);
                }
            }
        });

        timeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //TODO: Show time and date picker
                    Calendar now = Calendar.getInstance();
                    datePickActive = DatePickerDialog.newInstance(
                            TaskActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickActive.show(getFragmentManager(), "Datepickerdialog");
                }
            }
        });

        dueCheck.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //TODO: Show time and date picker
                    Calendar now = Calendar.getInstance();
                    datePickDue = DatePickerDialog.newInstance(
                            TaskActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickDue.show(getFragmentManager(), "Datepickerdialog");
                }
            }
        });

    }

    /**
     * Update the list view of tasks
     */
    public void updateListView(){

        taskLayout.removeAllViews();

        for(Task t : tasks){

            LayoutInflater linf = LayoutInflater.from(this);
            final View root = linf.inflate(R.layout.insertable_task, null);

            TextView text = (TextView) root.findViewById(R.id.textName);
            text.setText(t.getTaskName());

            taskLayout.addView(root, 0);
        }

    }

    /**
     * Use the dialog form to create the task
     */
    public void getTaskFromDialog(){
        String name = (nameEdit.getText().toString().trim().equals("") ? null : nameEdit.getText().toString().trim());
        String note = (noteEdit.getText().toString().trim().equals("") ? null : noteEdit.getText().toString().trim());
        String loca = (locationEdit.getText().toString().trim().equals("") ? null : locationEdit.getText().toString().trim());


        Task newTask = new Task(name, note, loca, null, null, false, false, null, false);

        tasks.add(newTask);

    }

    /**
     * Check items that need to be filled out
     * @return false if there is an error
     */
    public boolean checkCreateOptions(){

        if(nameEdit.getText().toString().trim().equals("")){
            nameEdit.setError("Need to enter a name");
            return false;
        }
        else {
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){

            case R.id.action_settings:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i2, int i3) {

        if(datePickerDialog == datePickActive){
            Calendar now = Calendar.getInstance();
            timePickActive = TimePickerDialog.newInstance(
                    TaskActivity.this,
                    now.get(Calendar.HOUR),
                    now.get(Calendar.MINUTE),
                    false
            );

            timePickActive.show(getFragmentManager(), "Timepickerdialog");
        }
        else{
            Calendar now = Calendar.getInstance();
            timePickDue = TimePickerDialog.newInstance(
                    TaskActivity.this,
                    now.get(Calendar.HOUR),
                    now.get(Calendar.MINUTE),
                    false
            );
            timePickDue.show(getFragmentManager(), "Timepickerdialog");
        }

    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {



    }

    public void loadColors(){

    }

}
