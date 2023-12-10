package com.vhpg.popurrivault.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.data.db.model.ContactEntity
import com.vhpg.popurrivault.data.db.model.OrderEntity

import com.vhpg.popurrivault.databinding.ContactElementBinding
import com.vhpg.popurrivault.databinding.OrderElementBinding

class OrderAdapter (
    //private var onSupplierClick: (SupplierEntity) -> Unit
): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private var orders: List<OrderEntity> = emptyList()
    private var filteredOrders: List<OrderEntity> = emptyList()

    class ViewHolder(private val binding: OrderElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon
        fun bind(order: OrderEntity){

            binding.apply {
                tvName.text = "${order.id}"
                ivIcon.setImageResource(R.drawable.cat5)

                progressBar.progress = 20


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderElementBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredOrders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredOrders[position])

        /*holder.itemView.setOnClickListener {
            onProductClick(products[position])
        }*/

        holder.ivIcon.setOnClickListener {

        }
    }
    fun updateList(list: List<OrderEntity>) {
        orders = list
        filterList("")
    }

    fun filterList(query: String) {
        Log.d("Filtro","entrando al filtro con $query")
        filteredOrders = if (query.isEmpty()) {
            orders
        } else {

            orders.filter { order ->
                // Puedes personalizar la lógica de filtrado según tus necesidades
                order.description.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}