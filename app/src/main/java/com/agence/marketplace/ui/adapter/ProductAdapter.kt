package com.agence.marketplace.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.agence.marketplace.R
import com.agence.marketplace.databinding.ItemProductBinding
import com.agence.marketplace.model.Product
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

class ProductAdapter(private val list: List<Product>, private val listener: OnProductClickListener): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: ItemProductBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class ProductViewHolder(private val binding: ItemProductBinding, private val onProductClickListener: OnProductClickListener) : RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.name.text = product.name
            binding.price.text = product.price
            Glide.with(binding.root)
                .load(product.thumbnailUrl)
                .placeholder(CircularProgressDrawable(binding.root.context).apply { start() })
                .signature(ObjectKey(product))
                .into(binding.thumbnail.findViewById(R.id.thumbnail))
            binding.root.setOnClickListener {
                onProductClickListener.apply {
                    onProductClick(product)
                }
            }
        }
    }

    interface OnProductClickListener{
        fun onProductClick(product: Product)
    }
}