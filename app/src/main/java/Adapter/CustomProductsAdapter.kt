package Adapter

import Model.ProductItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ustore.databinding.ItemProductBinding

class CustomProductsAdapter : RecyclerView.Adapter<CustomProductsAdapter.ProductViewHolder>() {

    private var products: List<ProductItem> = listOf()

    fun setProducts(products: List<ProductItem>) {
        this.products = products
        notifyDataSetChanged()
    }

    /**
     * @onCreateViewHolder
     * Hold binding with recycler view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.binding.productNameTextView.text = product.title
        Glide.with(holder.binding.productImageView.context)
            .load(product.image)
            .into(holder.binding.productImageView)
        holder.binding.productPriceTextView.text = product.price.toString()+" ₹"
        holder.binding.ratingBar1.rating = product.rating.rate.toFloat()
        holder.binding.categoryTextView.text = product.category
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}
