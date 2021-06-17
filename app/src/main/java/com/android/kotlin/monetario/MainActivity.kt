package com.android.kotlin.monetario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView
    private var key: String = "PEGAR A SUA CHAVE NO SITE https://www.currencyconverterapi.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txt_result)

        val buttonConverter = findViewById<Button>(R.id.btn_converter)

        buttonConverter.setOnClickListener{
           converter()
        }
    }

    private fun converter() {
        val selectedCurrency = findViewById<RadioGroup>(R.id.radio_group)

        val checked = selectedCurrency.checkedRadioButtonId

        val currency = when(checked) {
            R.id.radio_usd -> "USD"
            R.id.radio_eur -> "EUR"
            else ->  "CLP"
        }

        val ediField = findViewById<EditText>(R.id.edit_field)
        val value = ediField.text.toString()

        if (value.isEmpty()) return

        result.text = value
        result.visibility = View.VISIBLE

        Thread {
            // aqui acontece em paralelo

            val url = URL("https://free.currconv.com/api/v7/convert?q=${currency}_BRL&compact=ultra&apiKey=${key}")

            val conn = url.openConnection() as HttpsURLConnection

            try {

                val data = conn.inputStream.bufferedReader().readText()


                val obj = JSONObject(data)

                runOnUiThread{
                    val res = obj.getDouble("${currency}_BRL")

                    result.text = "R$ ${"%.4f".format(value.toDouble()  * res)}"
                    result.visibility = View.VISIBLE
                }



            } finally {
                conn.disconnect()
            }

        }.start()


    }
}
