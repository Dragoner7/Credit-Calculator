package bme.creditcalc.io

import bme.creditcalc.model.Leckekonyv
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class SaveLoadCommon {
    companion object{
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter(Leckekonyv::class.java)
    }
}