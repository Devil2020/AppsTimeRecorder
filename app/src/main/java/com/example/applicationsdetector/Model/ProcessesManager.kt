package com.example.applicationsdetector.Model

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import kotlin.math.log

public class  ProcessesManager{
    var context : Context ?=null;
    constructor(context: Context){
        this.context = context;
    }

    public fun getProcesses() : List<ActivityManager.RunningAppProcessInfo> ?{
            var serviceManager : ActivityManager =context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
           return serviceManager?.runningAppProcesses;
    }

    public fun DisplayThem(processInfos: List<ActivityManager.RunningAppProcessInfo>?){

        for ( processInfo in processInfos.orEmpty()) {
           Log.i("morse", processInfo?.processName);
       }
    }
}