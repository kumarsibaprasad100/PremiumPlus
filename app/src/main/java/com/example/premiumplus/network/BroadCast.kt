package com.example.premiumplus.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadCast :
    BroadcastReceiver() {

    override fun onReceive(context: Context?, p1: Intent?) {
        var status = NetWorkUtill.getConnectivityStatusString(context!!);
        if(status?.isEmpty() == true) {
            status="No Internet Connection";
        }
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }
}