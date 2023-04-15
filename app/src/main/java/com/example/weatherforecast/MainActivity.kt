package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL




class MainActivity : AppCompatActivity() {

    //объявили переменые
    private var user_field: EditText? = null
    private var main_btn: Button? = null
    private var result_info: TextView? = null
    private var weather_icon: ImageView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_field = findViewById(R.id.user_field)
        main_btn = findViewById(R.id.main_btn)
        result_info = findViewById(R.id.result_info)
        weather_icon = findViewById(R.id.weather_icon)



        //Обработчик события кнопки, trim убираем лишние знаки
        main_btn?.setOnClickListener {
            if(user_field?.text?.toString()?.trim().equals(""))
                Toast.makeText(this, "Введите город", Toast.LENGTH_LONG).show()
            else{
                val city: String = user_field?.text.toString().capitalize()
                val key: String = "fc9328bfefe0818995833a91eecbee45"
                val url: String = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric&lang=ru"

                GlobalScope.launch {
                    try {
                        val apiResponse = URL(url).readText()
                        val weather = JSONObject(apiResponse).getJSONArray("weather")
                        val desc = weather.getJSONObject(0).getString("description")
                        val iconCode = weather.getJSONObject(0).getString("icon")
                        val main = JSONObject(apiResponse).getJSONObject("main")
                        val temp = main.getString("temp")
                        val iconUrl = "https://openweathermap.org/img/w/$iconCode.png"

                        withContext(Dispatchers.Main) {
                            result_info?.text = "Температура: $temp\n$desc"
                            Picasso.get().load(iconUrl).into(weather_icon)
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "Ошибка получения данных о погоде",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

    }
}