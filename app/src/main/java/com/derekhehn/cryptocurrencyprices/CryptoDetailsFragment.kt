package com.derekhehn.cryptocurrencyprices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBindings
import com.derekhehn.cryptocurrencyprices.databinding.ActivityMainBinding
import com.derekhehn.cryptocurrencyprices.databinding.FragmentBlankBinding
import kotlinx.coroutines.launch


    class CryptoDetailsFragment : Fragment(R.layout.fragment_blank) {

        private var _binding: FragmentBlankBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            _binding = FragmentBlankBinding.inflate(inflater, container, false)

            // Retrieve the selected crypto data from the bundle
            val selectedCrypto = arguments?.getSerializable("cryptoData") as? CryptoCurrencyDataClass

            selectedCrypto?.let {
                updateUI(it)
            }

            return binding.root
        }


         fun updateUI(crypto: CryptoCurrencyDataClass) {
             // Format the price and percent change to ensure correct formatting
             val formattedPrice = String.format("%.2f", crypto.price)
             val formattedChange = String.format("%.2f", crypto.changePercent24Hr)
             val formattedSupply = String.format("%.2f",crypto.supply)

            // Update the UI with selected cryptocurrency data
            binding.textViewCryptoName.text = crypto.currencyName
            binding.textViewSymbol.text = "Symbol: ${crypto.symbol}"
            binding.textViewPrice.text = "Price: $${formattedPrice}"
            binding.textViewSupply.text = "Supply: ${formattedSupply}"
            binding.textViewPercentChange.text = "% Change: ${formattedChange}"
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

