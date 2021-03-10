package com.example.caloriecalc.models

import android.text.Editable

data class User(
        val id: String = "",
        var age: Int = 0,
        var weight: Int = 0,
        var height: Int = 0,
        var gender: String = "",
        val BMR: Int = 0
)