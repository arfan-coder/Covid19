package com.example.covid19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid19.adapter.AdapterCountry
import com.example.covid19.detail.Detail
import com.example.covid19.model.CountriesItem
import com.example.covid19.model.ResponseCountry
import com.example.covid19.network.ApiService
import com.example.covid19.network.RetrofitBuilder.retrofit
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private var ascending = true
    companion object{
        lateinit var  adaptersX : AdapterCountry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        SearchView
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptersX.filter.filter(newText)
                return false
            }

        })

//        refresh
        refresh.setOnRefreshListener {
            getNegara()
            refresh.isRefreshing = false
        }


        intializedView()
//        untuk dapetin data
        getNegara()
    }

    private fun intializedView() {
        button_refresh.setOnClickListener {
            sequenzeWithoutInternet(ascending)
            ascending = !ascending
        }
    }

    private fun sequenzeWithoutInternet(ascending: Boolean) {
        rv_country.apply {
            setHasFixedSize(true)
            //here is a.... =
            layoutManager  = LinearLayoutManager(this@MainActivity)
            if (ascending){
                (layoutManager as LinearLayoutManager).reverseLayout = true
                (layoutManager as LinearLayoutManager).stackFromEnd = true
                Toast.makeText(this@MainActivity,"Z-A", Toast.LENGTH_SHORT)
            }else{
                (layoutManager as LinearLayoutManager).reverseLayout = false
                (layoutManager as LinearLayoutManager).stackFromEnd = false
                Toast.makeText(this@MainActivity,"A-Z", Toast.LENGTH_SHORT)
            }
            adapter = adapter
        }
    }

    private fun getNegara(){
//        retro here
        val api = retrofit.create(ApiService::class.java)
        api.getAllNegara().enqueue(object:Callback<ResponseCountry>{
            override fun onFailure(call: Call<ResponseCountry>, t: Throwable) {
                progresbar.visibility = View.GONE
            }

            override fun onResponse(call: Call<ResponseCountry>, response: Response<ResponseCountry>) {
                if (response.isSuccessful){
                    val getlistDataCorona = response.body()!!.global
                    val formater : NumberFormat = DecimalFormat("#,###")
                    txt_confirmed.text=formater.format(getlistDataCorona?.totalConfirmed?.toDouble())
                    txt_confirmed_recovered.text=formater.format(getlistDataCorona?.totalRecovered?.toDouble())
                    txt_confirmed_deaths.text=formater.format(getlistDataCorona?.totalDeaths?.toDouble())
                    rv_country.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        progresbar.visibility = View.GONE
                        adaptersX = AdapterCountry(response.body()!!.countries as ArrayList<CountriesItem>)
                        {negara -> clicked(negara)}
                        adapter = adaptersX
                    }
                }else{
                    progresbar?.visibility = View.GONE
                }
            }

        })
    }

//    Ke ActivityDetail
    private fun clicked(negara: CountriesItem) {
        val moveWithData = Intent(this@MainActivity, Detail::class.java)
        moveWithData.putExtra(Detail.extra_country, negara)
        startActivity(moveWithData)
    }
}