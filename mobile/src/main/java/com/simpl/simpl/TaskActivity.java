package com.simpl.simpl;

import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

public class TaskActivity extends ActionBarActivity {

    //All of our important views
    private EditText nameEdit;
    private EditText noteEdit;
    private CheckBox locationCheck;
    private CheckBox timeCheck;
    private CheckBox repeatCheck;
    private RadioButton dailyButton;
    private RadioButton weeklyButton;

    //TODO: Load color programmatically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });

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

        Resources re = getResources();

        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(re.getString(R.string.create_title))
                .positiveText(re.getString(R.string.create_create))
                .neutralText(re.getString(R.string.create_clear))
                .negativeText(re.getString(R.string.create_cancel))
                .customView(R.layout.create_task_view, wrapInScrollView)
                .build();

        dialog.show();

        nameEdit = (EditText) dialog.getCustomView().findViewById(R.id.taskNameEdit);
        noteEdit = (EditText) dialog.getCustomView().findViewById(R.id.noteEdit);
        locationCheck = (CheckBox) dialog.getCustomView().findViewById(R.id.locationCheck);
        timeCheck = (CheckBox) dialog.getCustomView().findViewById(R.id.timeCheck);
        repeatCheck = (CheckBox) dialog.getCustomView().findViewById(R.id.repeatCheck);
        dailyButton = (RadioButton) dialog.getCustomView().findViewById(R.id.dailyButton);
        weeklyButton = (RadioButton) dialog.getCustomView().findViewById(R.id.weeklyButton);

        //TODO: Temp fix until appcompat setPadding is fixed ---------------------------------------------------------
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int namePX = (int) (64 * (metrics.densityDpi / 160f));
        int notePX = (int) (126 * (metrics.densityDpi / 160f));
        nameEdit.setPadding(namePX ,nameEdit.getPaddingTop(),nameEdit.getPaddingRight(),nameEdit.getPaddingBottom());
        noteEdit.setPadding(notePX ,noteEdit.getPaddingTop(),noteEdit.getPaddingRight(),noteEdit.getPaddingBottom());
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
                }
                else{

                }
            }
        });

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
}
