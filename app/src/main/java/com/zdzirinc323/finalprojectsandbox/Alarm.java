package com.zdzirinc323.finalprojectsandbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub
        Toast.makeText(k1, "com.zdzirinc323.finalprojectsandbox.Alarm received!", Toast.LENGTH_LONG).show();
        System.out.println("com.zdzirinc323.finalprojectsandbox.Alarm received!!!!!!!!!!!!!!!!!!!!!!");

        Intent intent = new Intent(k1.getApplicationContext(),AlarmPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        k1.startActivity(intent);
    }
}