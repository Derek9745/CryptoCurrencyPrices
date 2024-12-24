package com.derekhehn.cryptocurrencyprices

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.asStateFlow


class CryptoViewModel: ViewModel(){
    private val _cryptoList = MutableStateFlow<List<CryptoCurrencyDataClass>>(emptyList())
    val cryptoList: StateFlow<List<CryptoCurrencyDataClass>> = _cryptoList.asStateFlow()

    fun updateCryptoList(newCryptoObject:CryptoCurrencyDataClass){
        _cryptoList.value = _cryptoList.value + newCryptoObject

    }

    fun clearCryptoList() {
        _cryptoList.value = emptyList()

    }


}
