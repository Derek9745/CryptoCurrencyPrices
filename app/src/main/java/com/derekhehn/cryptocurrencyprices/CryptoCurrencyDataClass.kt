package com.derekhehn.cryptocurrencyprices

import java.io.Serializable

class CryptoCurrencyDataClass (
    var currencyName: String = "",
    var symbol: String = "",
    var supply: Double = 0.0,
    var price: Double = 0.0,
    var changePercent24Hr: Double = 0.0

):Serializable {


    override fun toString(): String {


        return "$currencyName ($symbol)"
    }
}