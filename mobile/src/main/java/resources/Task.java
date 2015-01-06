package resources;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class that represents our tasks
 * Created by Vontell on 1/5/15.
 */
public class Task {

    private String taskName;
    private String taskNote;
    private String taskLocation;
    private LatLng actingLocation;
    private Date actingTime;
    private boolean repeatDaily;
    private boolean repeatWeekly;
    private Date dueDate;
    private ArrayList<Task> subTasks;
    private boolean isSubtask;

    /**
     * Creates our task
     */
    public Task(String name, String note, String location, LatLng actingL,
                Date time, boolean repDay, boolean repWeek, Date due, boolean subbed){

        //Maybe need if else statement to differentiate between subbed or not
        taskName = name;
        taskNote = note;
        taskLocation = location;
        actingLocation = actingL;
        actingTime = time;
        repeatDaily = repDay;
        repeatWeekly = repWeek;
        dueDate = due;
        isSubtask = subbed;

        subTasks = new ArrayList<Task>();

    }

    /**
     * Adds a subtask to this task
     * @param t The task to add
     */
    public void addTask(Task t){
        subTasks.add(t);
    }


    public String getTaskNote() {
        return taskNote;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public LatLng getActingLocation() {
        return actingLocation;
    }

    public Date getActingTime() {
        return actingTime;
    }

    public boolean isRepeatWeekly() {
        return repeatWeekly;
    }

    public boolean isRepeatDaily() {
        return repeatDaily;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isSubtask() {
        return isSubtask;
    }

    public ArrayList<Task> getSubTasks() {
        return subTasks;
    }
}
