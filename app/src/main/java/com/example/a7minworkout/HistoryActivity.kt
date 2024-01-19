package com.example.a7minworkout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minworkout.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHistoryActivity)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Istoric"
        }

        binding.toolbarHistoryActivity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        getAllCompletedDates()
    }

    private fun getAllCompletedDates() {
        val dbHandler = SqliteOpenHelper(this, null)
        val allCompletedDatesList = dbHandler.getAllCompletedDatesList()

        if (allCompletedDatesList.size > 0) {
            binding.tvHistory.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.VISIBLE
            binding.tvNoDataAvailable.visibility = View.GONE

            binding.rvHistory.layoutManager = LinearLayoutManager(this)
            val historyAdapter = HistoryAdapter(this, allCompletedDatesList)
            binding.rvHistory.adapter = historyAdapter
        } else {
            binding.tvHistory.visibility = View.GONE
            binding.rvHistory.visibility = View.GONE
            binding.tvNoDataAvailable.visibility = View.VISIBLE
        }
    }
}
