package com.zdzirinc323.finalprojectsandbox;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    private String title, description, duedate, duetime, address;
    private int id, user_id, image;


    private Date date;

    private double lat, lng;

    public Task(int id, String title, String description, String duedate, String duetime) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.duedate = duedate;
        this.duetime = duetime;
        image = 0;


        try {
            this.date = StringToDate(duedate);
        } catch (ParseException e) {
            Log.i("CHECK", "Incorrect date format for task: " + title);
            e.printStackTrace();
        }
    }

    public Task(String title, String description, Double lat, Double lng) {
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.address = "Could not get address...";
    }


    public static Date StringToDate(String dob) throws ParseException {
        //Instantiating the SimpleDateFormat class
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        //Parsing the given String to Date object
        Date date = formatter.parse(dob);
        System.out.println("Date object value: "+date);
        return date;
    }

    public void setAddress(String address) { this.address = address; }

    public String getAddress() { return address; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDuedateStr() {return duedate; }

    public Date getDuedate() {
        return date;
    }

    public String getDuetime() {
        return duetime;
    }

    public int getId() {
        return id;
    }

    public double getLat() { return lat; }

    public double getLng() { return lng; }
}
