package com.vhpg.popurrivault.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.ProductElementBinding

class ProductAdapter(
    //private var onProductClick: (ProductEntity) -> Unit
): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private var products: List<ProductEntity> = emptyList()
    private var filteredProducts: List<ProductEntity> = emptyList()

    class ViewHolder(private val binding: ProductElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon
        fun bind(product: ProductEntity){

            binding.apply {
                tvName.text = product.name
                ivIcon.setImageURI(product.image.toUri())
                tvDesc.text = product.description
                tvStock.text = product.stock.toString()
                tvPrice.text = "$ ${product.price.toString()}"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductElementBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredProducts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredProducts[position])

        /*holder.itemView.setOnClickListener {
            onProductClick(products[position])
        }*/

        holder.ivIcon.setOnClickListener {

        }
    }
    fun updateList(list: List<ProductEntity>) {
        products = list
        filterList("")
    }

    fun filterList(query: String) {
        Log.d("Filtro","entrando al filtro con $query")
        filteredProducts = if (query.isEmpty()) {
            products
        } else {

            products.filter { product ->
                // Puedes personalizar la lógica de filtrado según tus necesidades
                product.name.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}