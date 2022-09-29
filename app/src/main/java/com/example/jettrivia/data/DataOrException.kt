package com.example.jettrivia.data

data class DataOrException<T, Loading:Boolean, E : Exception>(
    var data: T? =null,
    var loading: Loading? = null,
    var exception: E? = null
)
