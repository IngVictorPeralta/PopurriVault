package com.vhpg.popurrivault.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.data.db.model.SupplierEntity
import com.vhpg.popurrivault.databinding.SupplierElementBinding

class SupplierAdapter (
    //private var onSupplierClick: (SupplierEntity) -> Unit
): RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {
    private var suppliers: List<SupplierEntity> = emptyList()
    private var filteredSuppliers: List<SupplierEntity> = emptyList()

    class ViewHolder(private val binding: SupplierElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon
        fun bind(supplier: SupplierEntity){

            binding.apply {
                tvName.text = "${supplier.name}"
                ivIcon.setImageResource(R.drawable.cat4)
                tvPhone.text = "${supplier.phoneNumber}"
                tvEmail.text = "$ ${supplier.email}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SupplierElementBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredSuppliers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredSuppliers[position])

        /*holder.itemView.setOnClickListener {
            onProductClick(products[position])
        }*/

        holder.ivIcon.setOnClickListener {

        }
    }
    fun updateList(list: List<SupplierEntity>) {
        suppliers = list
        filterList("")
    }

    fun filterList(query: String) {
        Log.d("Filtro","entrando al filtro con $query")
        filteredSuppliers = if (query.isEmpty()) {
            suppliers
        } else {

            suppliers.filter { supplier ->
                // Puedes personalizar la lógica de filtrado según tus necesidades
                supplier.name.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}