package com.zdzirinc323.finalprojectsandbox.ui.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zdzirinc323.finalprojectsandbox.DatabaseHelper;
import com.zdzirinc323.finalprojectsandbox.R;
import com.zdzirinc323.finalprojectsandbox.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;

    public static final String USER_PREFS = "USER";

    static ArrayList<Task> taskList;
    static SharedPreferences sharePref;
    DatabaseHelper database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)          {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        sharePref = this.getContext().getSharedPreferences(USER_PREFS, MODE_PRIVATE);


        taskList = new ArrayList<Task>();
        database = new DatabaseHelper(getContext());
        getTasksWithCoordinates();
        for (int i = 0; i < taskList.size(); i++) {
            Task currTask = taskList.get(i);
            new latLngToAddress(getContext(), currTask.getLat(), currTask.getLng(), i).execute();
        }

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);

        return v;
    }

    private void getTasksWithCoordinates() {

        Cursor c = database.getForMapView(getCurrentUser());

        while (c.moveToNext()) {

            String title = c.getString(0);
            String description = c.getString(1);
            Double lat = Double.parseDouble(c.getString(2));
            Double lng = Double.parseDouble(c.getString(3));

            taskList.add(new Task(title, description, lat, lng));

        }


    }

    public static int getCurrentUser() { return sharePref.getInt("USER_ID",0); }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        Toast.makeText(getContext(), "Readying map!", Toast.LENGTH_SHORT).show();

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);


        for (int i = 0; i < taskList.size(); i++) {

            Task curr = taskList.get(i);
            String title = curr.getTitle();
            Double lat = curr.getLat();
            Double lng = curr.getLng();

            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(title)).setTag(curr);

        }

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Task curr = (Task) marker.getTag();
                View v = View.inflate(getContext(), R.layout.info_window_layout, null);
                TextView name = v.findViewById(R.id.markerTitle);
                TextView address = v.findViewById(R.id.markerAddress);
                TextView description = v.findViewById(R.id.markerDescription);
                if (curr != null) {
                    name.setText(curr.getTitle());
                    address.setText(curr.getAddress());
                    description.setText(curr.getDescription());
                }


                return v;
            }
        });


        Toast.makeText(getContext(), "Map ready!", Toast.LENGTH_SHORT).show();

    }
    private static class latLngToAddress extends AsyncTask<Double, Void, String> {

        Geocoder geocoder;
        double lat, lng;
        int index;
        Context context;

        private latLngToAddress(Context context, double lat, double lng, int index) {
            geocoder = new Geocoder(context);
            this.lat = lat;
            this.lng = lng;
            this.index = index;
            this.context = context;
        }


        @Override
        protected String doInBackground(Double... doubles) {

            if (lat == 0 && lng == 0) return "Could not get address...";
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String result = address + ", " + city + ", " + state;
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Could not get address...";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(context, "Address added", Toast.LENGTH_SHORT).show();
            taskList.get(index).setAddress(s);

        }
    }



}