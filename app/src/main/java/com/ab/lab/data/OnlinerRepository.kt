package com.ab.lab.data

import com.ab.lab.api.OnlinerService

class OnlinerRepository {

    private val service = OnlinerService.create()

    suspend fun getProductAsync(key: String) =
        service.getProduct(key)
}