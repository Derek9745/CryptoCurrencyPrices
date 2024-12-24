package com.derekhehn.cryptocurrencyprices


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.derekhehn.cryptocurrencyprices.databinding.ActivityMainBinding
import com.derekhehn.cryptocurrencyprices.CryptoCurrencyDataClass
import com.derekhehn.cryptocurrencyprices.CryptoDetailsFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding
    private var BASE_URL: String = "https://api.coincap.io/v2/assets"
    private var mRequestQueue: RequestQueue? = null
    private val viewModel: CryptoViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView((binding.root))
        val spinner: Spinner = binding.spinner

        //add list to spinner options
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            mutableListOf<CryptoCurrencyDataClass>()
        )
        spinner.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cryptoList.collect { cryptoList ->
                    //adapter.clear()
                    (adapter as ArrayAdapter<CryptoCurrencyDataClass>).clear()
                    // Add new items to the adapter
                    adapter.addAll(cryptoList)
                    adapter.notifyDataSetChanged()

                }
            }
        }

        getData()


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedCrypto = viewModel.cryptoList.value[position]
                showCryptoDetails(selectedCrypto)
                //load details into current fragment


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun getData() {
        mRequestQueue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET, BASE_URL,
            { response ->
                try {
                    viewModel.clearCryptoList()

                    val jsonResponse = JSONObject(response)
                    val dataArray = jsonResponse.getJSONArray("data")

                    //loop through JSON data and add objects to the cryptoList
                    for (i in 0 until dataArray.length()) {
                        val item: JSONObject = dataArray.getJSONObject(i)

                        val cryptoObject = CryptoCurrencyDataClass(
                            currencyName = item.getString("name"),
                            symbol = item.getString("symbol"),
                            supply = item.getString("supply").toDoubleOrNull()?:0.0,
                            price = item.getString("priceUsd").toDoubleOrNull()?:0.0,
                            changePercent24Hr = item.getString("changePercent24Hr").toDoubleOrNull()?: 0.0,

                        )

                        viewModel.updateCryptoList(cryptoObject)

                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },

            { error ->
                error.printStackTrace()
                Log.e("TAG", "RESPONSE IS $error")
                Toast.makeText(this@MainActivity, "Fail to get response", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        mRequestQueue?.add(stringRequest)
    }


    private fun showCryptoDetails(crypto: CryptoCurrencyDataClass) {
        val fragment = CryptoDetailsFragment()

        // Pass data to the fragment using a bundle
        val bundle = Bundle().apply { putSerializable("cryptoData", crypto) }
        fragment.arguments = bundle

        // Replace the current fragment with the CryptoDetailsFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView5, fragment)
            .commit()

    }

}

