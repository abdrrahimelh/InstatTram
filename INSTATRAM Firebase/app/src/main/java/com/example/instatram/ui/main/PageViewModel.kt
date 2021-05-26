package com.example.instatram.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.example.instatram.data.TramRepository

class PageViewModel(val app: Application) : AndroidViewModel(app) {
    private val dataRepo = TramRepository(app)
    val TramData = dataRepo.TramData


    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"

    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}