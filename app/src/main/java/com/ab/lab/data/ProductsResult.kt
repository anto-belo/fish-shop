package com.ab.lab.data

import com.ab.lab.api.Product

sealed class ProductsResult {
    data class Success(val data: List<Product>) : ProductsResult()
    data class Error(val error: Exception) : ProductsResult()
}

sealed class ProductResult {
    data class Success(val data: Product) : ProductResult()
    data class Error(val error: Exception) : ProductResult()
}
