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
    private String timer;
    private boolean completed;

    public Drop(){

    }

    public Drop(String what, long added, long when, String timer, boolean completed) {

        this.what = what;
        this.added = added;
        this.when = when;

        this.completed = completed;
        this.timer = timer;
    }

    public String getWhat() {
        return what;
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

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
