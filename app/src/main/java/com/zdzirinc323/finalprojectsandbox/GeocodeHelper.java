package com.zdzirinc323.finalprojectsandbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class GeocodeHelper extends AsyncTask<String, Void, Coordinate> {

    Geocoder geocoder;
    DatabaseHelper database;
    Context context;
    int task_id;

    public GeocodeHelper(Context context, DatabaseHelper database) {
        geocoder = new Geocoder(context);
        this.database = database;
        this.context = context;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public Coordinate getCoordinatesFromAddress(String address) throws IOException {
        List<Address> addressList = geocoder.getFromLocationName(address, 1);
        if (!addressList.isEmpty()) {
            Address currAdd = addressList.get(0);
            Log.i("CHECK", currAdd.getLocality());
            return new Coordinate(currAdd.getLatitude(), currAdd.getLongitude());
        } else {
            return new Coordinate(0,0);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Coordinate doInBackground(String... strings) {

        try {
            return getCoordinatesFromAddress(strings[0]);
        } catch (IOException e) {
            Log.i("CHECK", "Error in doInBackground");
            e.printStackTrace();
        }
        return new Coordinate(0,0);
    }

    @Override
    protected void onPostExecute(Coordinate coordinate) {
        super.onPostExecute(coordinate);

        Toast.makeText(context, "Added coordinates!", Toast.LENGTH_SHORT).show();
        database.updateCoordinates(task_id, coordinate);
    }
}
