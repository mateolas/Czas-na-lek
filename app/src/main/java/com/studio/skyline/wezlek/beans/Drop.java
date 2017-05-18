package com.studio.skyline.wezlek.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by aneimat on 06.04.2017.
 */

public class Drop extends RealmObject {

    private String what;
    @PrimaryKey
    private long added;
    private long when;
    private long timer;
    private boolean completed;
    private boolean paused;
    private long timeSet;
    private int quantity;
    private long timeEnded;

    public Drop(){

    }

    public long getTimeSet() {
        return timeSet;
    }

    public void setTimeSet(long timeSet) {
        this.timeSet = timeSet;
    }

    public Drop(String what, long added, long when, long timer, boolean completed, long timeSet,int quantity, long timeEnded, boolean paused) {

        this.what = what;
        this.added = added;
        this.when = when;
        this.completed = completed;
        this.timer = timer;
        this.timeSet = timeSet;
        this.quantity = quantity;
        this.timeEnded = timeEnded;
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public String getWhat() {
        return what;
    }

    public long getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(long timeEnded) {
        this.timeEnded = timeEnded;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }
}
