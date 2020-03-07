package com.zdzirinc323.finalprojectsandbox.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zdzirinc323.finalprojectsandbox.DatabaseHelper;
import com.zdzirinc323.finalprojectsandbox.DeleteDialog;
import com.zdzirinc323.finalprojectsandbox.LocationBackgroundService;
import com.zdzirinc323.finalprojectsandbox.R;
import com.zdzirinc323.finalprojectsandbox.RecyclerTouchListener;
import com.zdzirinc323.finalprojectsandbox.Task;
import com.zdzirinc323.finalprojectsandbox.TaskAdapter;
import com.zdzirinc323.finalprojectsandbox.addTaskDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {


    public static final String USER_PREFS = "USER";

    TabLayout tabLayout;
    PagerNoSwiping viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.tab_home_layout, container, false);

        TabPagerAdapter adapter = new TabPagerAdapter(getContext(), getChildFragmentManager());

        getActivity().startService(new Intent(getActivity(), LocationBackgroundService.class));

        viewPager = root.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }


}