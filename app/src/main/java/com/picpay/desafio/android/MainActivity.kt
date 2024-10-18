package com.picpay.desafio.android

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.junit.runner.RunWith
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserListAdapter
    private val service: PicPayService by inject()


    override fun onResume() {
        super.onResume()

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.user_list_progress_bar)

        adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
        service.getUsers()
            .enqueue(object : Callback<List<User>> {
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    val message = getString(R.string.error)

                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE

                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    progressBar.visibility = View.GONE
                    val code = response.code()
                    if(code == 200) {
                        val body = response.body()
                        if(body == null){
                            adapter.users = listOf()
                            recyclerView.visibility = View.GONE
                        }else{
                            adapter.users = body
                        }
                    } else{
                        recyclerView.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "A requisição falhou com código: $code", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        }
    }
}
