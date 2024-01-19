package com.example.a7minworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration : Long = 1


    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration: Long=1
    private lateinit var llRestView: LinearLayout
    private lateinit var llExerciseView: LinearLayout

    private var exerciseList: ArrayList<ExerciseModel>? =null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player:MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        llRestView = findViewById(R.id.llRestView)
        llExerciseView = findViewById(R.id.llExerciseView)


        val toolbar: Toolbar = findViewById(R.id.toolbar_exercise_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        tts = TextToSpeech(this,this)
        exerciseList = Constants.defaultExerciseList()
        setupRestView()
        setupExerciseStatusRecyclerView()

    }

    override fun onDestroy() {
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }
        if(exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }

        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!=null)
        {
            player!!.stop()
        }

        super.onDestroy()
    }

    private fun setRestProgressBar(){
        val progressBar= findViewById<ProgressBar>(R.id.progressBar)
        val tvTimer=findViewById<TextView>(R.id.tvTimer)
        progressBar.progress=restProgress
        restTimer = object : CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress=10-restProgress
                tvTimer.text=(10-restProgress).toString()

            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()



                setupExerciseView()

            }
        }.start()


    }

    private fun setExerciseProgressBar(){
        val progressBarExercise= findViewById<ProgressBar>(R.id.progressBarExercise)
        val tvExerciseTimer=findViewById<TextView>(R.id.tvExerciseTimer)
        progressBarExercise.progress=exerciseProgress
        exerciseTimer = object : CountDownTimer(30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarExercise.progress=30-exerciseProgress
                tvExerciseTimer.text=(30-exerciseProgress).toString()

            }

            override fun onFinish() {
                if(currentExercisePosition< exerciseList?.size!!-1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()


    }

    private fun setupExerciseView(){
        val ivImage=findViewById<ImageView>(R.id.ivImage)
        val tvExerciseName=findViewById<TextView>(R.id.tvExerciseName)
        llRestView.visibility=View.GONE
        llExerciseView.visibility=View.VISIBLE

        if(exerciseTimer!=null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        setExerciseProgressBar()
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()

    }

    private fun setupRestView(){
        try{
            player = MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping=false
            player!!.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

        val tvUpcomingExerciseName=findViewById<TextView>(R.id.tvUpcomingExerciseName)
        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE

        if(restTimer!=null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        val upcomingExerciseName = exerciseList!![currentExercisePosition+1].getName()
        val spannableString = SpannableString("Exercițiul următor:\n$upcomingExerciseName")
        spannableString.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            "Exercițiul următor:\n".length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvUpcomingExerciseName.text = spannableString

        setRestProgressBar()

    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language specified is not supported!")
            }
        }else{
            Log.e("TTS","Initialization failed!")
        }
    }
    private fun speakOut(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setupExerciseStatusRecyclerView(){
        val rvExerciseStatus=findViewById<RecyclerView>(R.id.rvExerciseStatus)
        rvExerciseStatus.layoutManager=LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter = exerciseAdapter


    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        val tvYes = customDialog.findViewById<Button>(R.id.tvYes)
        val tvNo = customDialog.findViewById<Button>(R.id.tvNo)
        tvYes.setOnClickListener{
            finish()
            customDialog.dismiss()
        }
        tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()

    }
}