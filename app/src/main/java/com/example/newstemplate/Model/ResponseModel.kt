package com.example.newstemplate.Model

class ResponseModel<T> {
    var success : Boolean = false
    var message: String? = null
    var data:T? = null
}