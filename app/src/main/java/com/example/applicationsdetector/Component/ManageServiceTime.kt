package com.example.applicationsdetector.Component

import android.annotation.SuppressLint
import android.app.*
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.example.applicationsdetector.Model.ProcessesManager
import com.example.applicationsdetector.R
import java.util.*

public class ManageServiceTime : Service() {
    var runnable :Runnable? = null
    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "Play") {
            ShowNotification(intent)
            Execute()
        } else {
            stopForeground(true)
            stopSelf()

        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun ShowNotification(po: Intent?) {


        //var PlayPendingIntent :PendingIntent= PendingIntent.getService(this,15,),)
        var notification: NotificationCompat.Builder = NotificationCompat.Builder(this)
        notification.priority = Notification.PRIORITY_MAX
        notification.setContentTitle("Let`s Control the APP")
        notification.setSmallIcon(R.drawable.ic_timer_black_24dp)
        notification.setContentText("we can manage the Service by Closing the service and run it")
        //notification.addAction(R.drawable.ic_play_arrow_black_24dp,"Play",)
        //=================================================================================
        /* var playIntent :Intent = Intent(this,ManageServiceTime::class.java)
      playIntent.setAction("Play")
      var playPendingIntent :PendingIntent = PendingIntent.getService(this,15,playIntent,0)
      notification.addAction(android.R.drawable.ic_media_play,"Play",playPendingIntent)
        */
        var pauseIntent: Intent = Intent(this, ManageServiceTime::class.java)
        pauseIntent.setAction("Pause")
        var pausePendingIntent: PendingIntent =
            PendingIntent.getService(this, 15, pauseIntent, 0)
        notification.addAction(android.R.drawable.ic_media_pause, "Pause", pausePendingIntent)

        var notif: Notification = notification.build()
        startForeground(15, notif)
    }

    @SuppressLint("WrongConstant")
    fun Execute() {

        var handler : Handler = Handler()
            runnable = Runnable (){
            var processManager = ProcessesManager(baseContext)
            var processes :List<ActivityManager.RunningAppProcessInfo>? =
                processManager?.getProcesses()
            var sharedPreference : SharedPreferences = getSharedPreferences("Apps", Context.MODE_APPEND)
            var editor : SharedPreferences.Editor = sharedPreference.edit()
            val Length = processes!!.size
            editor.putInt("Count",Length)
            for (process in processes.orEmpty()) {
                Log.i("morse","The Process Name is "+process.processName + "and took time = ")
                val count = sharedPreference.getInt(process.processName,-1)
                if(count== -1){
                    editor.putInt(process.processName,0)
                    editor.commit()
                }
                else if(count == 0){
                    editor.putInt(process.processName,1)
                    editor.commit()
                }
                else{
                    var Value = sharedPreference.getInt(process.processName,-1)
                    editor.putInt(process.processName,  Value + 1)
                    editor.commit()
                }
            }
            handler.postDelayed(runnable ,1000)
        }
        handler.postDelayed(runnable,1000)


    }
}