package com.example.a7minworkout

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.a7minworkout.databinding.ActivityFinishBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FinishActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        val toolbar_finish_activity= findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_finish_activity)
        setSupportActionBar(toolbar_finish_activity)
        val actionbar = supportActionBar
        if(actionbar!= null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val btnFinish= findViewById<Button>(R.id.btnFinish)
        btnFinish.setOnClickListener{
            finish()
        }
        addDateToDatabase()


    }

    private fun addDateToDatabase(){
        val calendar = Calendar.getInstance()
        val dateTime= calendar.time
        Log.i("Date:",""+dateTime)

        val sdf=SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date=sdf.format(dateTime)
        val dbHandler = SqliteOpenHelper(this,null)
        dbHandler.addDate(date)
        Log.i("Date:","Added")


    }

}