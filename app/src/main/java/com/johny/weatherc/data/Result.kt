package com.johny.weatherc.data

import java.lang.Exception

sealed class Result<out R> {

    data class Ok<out T>(val data: T): Result<T>()

    data class Error(val e: Exception): Result<Nothing>()

    object Loading: Result<Nothing>()

    override fun toString(): String {
        return when(this) {
            is Ok<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$e]"
            is Loading -> "Loading..."
        }
    }

}
