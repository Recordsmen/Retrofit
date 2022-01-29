package com.example.retrofit

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapter.MyMovieAdapter
import com.example.Common.Common
import com.example.`interface`.RetrofitServices
import com.example.model.Movie
import com.example.retrofit.databinding.ActivityMainBinding

import dmax.dialog.SpotsDialog
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mService: RetrofitServices
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MyMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mService = Common.retrofitServices
        binding.recyclerMovieList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        binding.recyclerMovieList.layoutManager = layoutManager
        scope()
    }
    private fun scope() = runBlocking {
        getAllMovieListCorutine()

    }
    suspend fun getAllMovieListCorutine() = coroutineScope {
            launch {
                mService.getMovieList().enqueue(object : Callback<MutableList<Movie>> {
                    override fun onFailure(call: Call<MutableList<Movie>>, t: Throwable) {
                        Log.i("Fail", "Failure getting movie list")
                    }
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<MutableList<Movie>>,
                        response: Response<MutableList<Movie>>
                    ) {
                        adapter = MyMovieAdapter(baseContext, response.body() as MutableList<Movie>)
                        adapter.notifyDataSetChanged()
                        binding.recyclerMovieList.adapter = adapter
                    }
                })
            }
    }
}