package com.example.covid19.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covid19.R
import com.example.covid19.model.CountriesItem
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.list_country.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterCountry(private val country:ArrayList<CountriesItem>, private val clicklistener: (CountriesItem)-> Unit):
    RecyclerView.Adapter<CountryViewHolder>(), Filterable {

    var countryCount = ArrayList<CountriesItem>()
    init {
        countryCount = country
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_country,parent,false)
        return CountryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countryCount.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countryCount[position],clicklistener)
    }

    override fun getFilter():Filter{

        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                countryCount = if (charSearch.isEmpty()){
                    country
            }else{
                val result = ArrayList<CountriesItem>()
                for (row in country){
                    val search = row.country?.toLowerCase(Locale.ROOT)?:""
                if (search.contains(charSearch.toLowerCase(Locale.ROOT))){
                    result.add(row)
                        }
                    }
                    result
                }
                val filterResult = FilterResults()
                filterResult.values = countryCount
                return filterResult
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                countryCount = p1?.values as ArrayList<CountriesItem>
                notifyDataSetChanged()
            }

        }
    }
}

class CountryViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
    fun bind(country:CountriesItem, clickLinstener: (CountriesItem)-> Unit){
        val name_country:TextView = itemView.tv_country_list
        val flag_negara:CircleImageView = itemView.circle_Image
        val country_totalCase:TextView = itemView.tv_kasus_list
        val country_recovered:TextView = itemView.tv_recovered_list
        val country_deaths_:TextView = itemView.tv_death_list

        val formater:NumberFormat = DecimalFormat("#,###")

        name_country.tv_country_list.text = country.country
        country_totalCase.tv_kasus_list.text = formater.format(country.totalConfirmed?.toDouble())
        country_recovered.tv_recovered_list.text = formater.format(country.totalRecovered?.toDouble())
        country_deaths_.tv_death_list.text = formater.format(country.totalDeaths?.toDouble())

        //masukin gambar
        Glide.with(itemView)
            .load("https://www.countryflags.io/" + country.countryCode + "/flat/64.png")
            .into(flag_negara)

        //ketika di klik
        name_country.setOnClickListener {clickLinstener(country) }
        flag_negara.setOnClickListener { clickLinstener(country) }
        country_totalCase.setOnClickListener { clickLinstener(country) }
        country_recovered.setOnClickListener { clickLinstener(country) }
        country_deaths_.setOnClickListener { clickLinstener(country) }
    }
}
