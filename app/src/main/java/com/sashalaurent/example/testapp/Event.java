package com.sashalaurent.example.testapp;

import java.util.Date;

/**
 * Created by Sasha on 01/03/2017.
 */

public class Event {

    private Date startDate;
    private Date endDate;
    private String creator;
    private String description;

    public Event(Date startDate, Date endDate, String creator, String description){
        this.startDate = startDate;
        this.endDate = endDate;
        this.creator = creator;
        this.description = description;
    }

    private Date getStartDate(){
        return this.startDate;
    }

    private Date getEndDate(){
        return this.endDate;
    }

    private String getCreator(){
        return this.creator;
    }

    private String getDescription(){
        return this.description;
    }
}
