package com.example.forage

import android.app.Application
import com.example.forage.data.ForageDatabase


class BaseApplication : Application() {

    // TODO: provide a ForageDatabase value by lazy here
    val database : ForageDatabase by lazy { ForageDatabase.getDatabase(this) }
}