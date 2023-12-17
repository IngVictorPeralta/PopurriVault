package com.vhpg.popurrivault.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.ProductElementBinding
import com.vhpg.popurrivault.databinding.ProductElementCompactBinding

class ProductCompactAdapter(
    private val typeCell: String,
    private var onProductClick: (ProductEntity,String) -> Unit
): RecyclerView.Adapter<ProductCompactAdapter.ViewHolder>() {
    private var products: List<ProductEntity> = emptyList()
    private var filteredProducts: List<ProductEntity> = emptyList()

    class ViewHolder(private val binding: ProductElementCompactBinding): RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon
        val btAccion = binding.btAccion
        fun bind(product: ProductEntity){



            binding.apply {
                tvName.text = product.name
                ivIcon.setImageURI(product.image.toUri())
                tvStock.text = product.stock.toString()

                tvPrice.text = "$ ${product.price.toString()}"


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductElementCompactBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredProducts.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredProducts[position])

        /*holder.itemView.setOnClickListener {
            onProductClick(products[position])
        }*/
        if(typeCell=="SEL"){
            holder.btAccion.apply {
                text = "+"
                setBackgroundResource(R.color.colorAccept)
            }
        }
        if(typeCell=="NEW"){
            holder.btAccion.apply {
                text = "-"
                setBackgroundResource(R.color.colorDelete)
            }
        }
        holder.ivIcon.setOnClickListener {

        }
        holder.btAccion.setOnClickListener {
            onProductClick(filteredProducts[position],typeCell)
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