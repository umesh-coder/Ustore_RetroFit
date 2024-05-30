package com.example.ustore

import Adapter.CustomProductsAdapter
import Client.RetrofitInstance
import Model.ProductItem
import Utility.ApiService.ProductService
import android.app.ProgressDialog

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ustore.databinding.ActivityMainBinding
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val productAdapter = CustomProductsAdapter()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView adapter
        binding.productListRecyclerView.adapter = productAdapter
        binding.productListRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        // Show progress dialog
        progressDialog.show()

        // Create the ProductService instance
        val productService: ProductService = RetrofitInstance.getRetrofitInstance().create(ProductService::class.java)

        // LiveData for the API response
        val responseLiveData: LiveData<Response<List<ProductItem>>> = liveData {
            val response: Response<List<ProductItem>> = productService.getProducts()
            emit(response)
        }

        // Observe the LiveData for changes
        responseLiveData.observe(this, Observer { response ->
            if (response.isSuccessful) {
                progressDialog.dismiss()
                // Extract the product list from the response body
                val productList: List<ProductItem>? = response.body()

                // Update the adapter with the new product list
                productList?.let {
                    productAdapter.setProducts(productList)
                }
            } else {
                Log.e("MainActivity", "Error: ${response.errorBody()?.string()}")
            }
        })
    }
}
