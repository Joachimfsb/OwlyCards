package com.example.owlycards

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.owlycards.data.Config

class SharedViewModel(context: Context) : ViewModel() {
    val config = Config(context)
}