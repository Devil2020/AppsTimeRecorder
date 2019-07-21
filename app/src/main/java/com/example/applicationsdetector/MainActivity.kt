package com.example.applicationsdetector

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.view.ViewPropertyAnimatorCompatSet
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import com.example.applicationsdetector.Component.ManageServiceTime
import com.example.applicationsdetector.Model.ProcessesManager
import com.example.applicationsdetector.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    var layout_Binding : ActivityMainBinding ?= null
    var sharedPreference :SharedPreferences?= null
    var processManager : ProcessesManager?=null ;
    var count =0
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        processManager = ProcessesManager(this)
        sharedPreference = getSharedPreferences("Apps", Context.MODE_APPEND)
        layout_Binding = DataBindingUtil.
        setContentView(this,R.layout.activity_main)
        var isExist :Boolean=CheckSharedPreference()
        if(isExist == true){
            layout_Binding?.FirstMainView?.visibility = View.INVISIBLE
            layout_Binding?.SecondMainView?.visibility = View.VISIBLE
            DisplayProcessData()
        }
        else{
            layout_Binding?.FirstMainView?.visibility = View.VISIBLE
            layout_Binding?.SecondMainView?.visibility = View.INVISIBLE
        }

    }

    private fun DisplayProcessData() {
        var TotalString :String = String()
        val Size = sharedPreference?.getInt("Count",-1)
        var processes :List<ActivityManager.RunningAppProcessInfo> = processManager?.getProcesses()!!
        for (process in processes){
            var data = sharedPreference?.getInt(process.processName,-1)
            TotalString =process.processName + " >> " + data + " sec" + "/n"
        }
        layout_Binding?.AppsInfo?.setText(TotalString)
    }

    public fun AnimateFab(x:View){
        if(count % 2 == 0){
            layout_Binding?.PauseService?.animate()?.
                translationY(-resources.getDimension(R.dimen.Pause))
            layout_Binding?.StartService?.animate()?.translationY(-resources.getDimension(R.dimen.Play))
            count++
        }
        else {
            layout_Binding?.PauseService?.animate()?.translationY(0F)
            layout_Binding?.StartService?.animate()?.translationY(0F)
            count++
        }

    }
    public fun PlayService(x:View){
        AnimateFab(x)
        var playIntent :Intent = Intent(this,ManageServiceTime::class.java)
        playIntent.setAction("Play")

        startService(playIntent)
        Toast.makeText(this,"Play service in background",Toast.LENGTH_LONG).show()
    }
    public fun PauseService(x:View){
        AnimateFab(x)
        var pauseIntent :Intent = Intent(this,ManageServiceTime::class.java)
        pauseIntent.setAction("Pause")

        startService(pauseIntent)
        Toast.makeText(this,"Pause service from background",Toast.LENGTH_LONG).show()

    }
    private fun CheckSharedPreference():Boolean{

        val count = sharedPreference?.getInt("Count",0)
        if(count == 0)
            return false
        else
            return true
    }
}
