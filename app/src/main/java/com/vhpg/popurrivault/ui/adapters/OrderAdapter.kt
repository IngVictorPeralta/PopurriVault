package com.vhpg.popurrivault.ui.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.data.db.model.ContactEntity
import com.vhpg.popurrivault.data.db.model.OrderEntity

import com.vhpg.popurrivault.databinding.ContactElementBinding
import com.vhpg.popurrivault.databinding.OrderElementBinding
import java.util.*

class OrderAdapter (
    //private var onSupplierClick: (SupplierEntity) -> Unit
): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private var orders: List<OrderEntity> = emptyList()
    private var filteredOrders: List<OrderEntity> = emptyList()
    private var today = Date().time
    class ViewHolder(private val binding: OrderElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon
        fun bind(order: OrderEntity){

            binding.apply {
                tvName.text = "${order.description}"
                ivIcon.setImageResource(R.drawable.cat5)
                if(order.arrived) {
                    progressBar.progress = 100
                    //val color = ContextCompat.getColor(progressBar.context, R.color.colorAccept)
                    //var cl = Color.parseColor(R.color.colorAccept.toString())
                    //progressBar.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                    //progressBar.progressTintList = ColorStateList.valueOf(cl)
                }else{
                    progressBar.progress =
                        (((Date().time - order.dateOrder ).toDouble() / (order.dateArrive - order.dateOrder).toDouble())*100).toInt()

                    Log.d("CALCULO","dateOrder: ${order.dateOrder}")
                    Log.d("CALCULO","today: ${Date().time}")
                    Log.d("CALCULO","arriba: ${Date().time - order.dateOrder}")
                    Log.d("CALCULO","arrived: ${order.dateArrive}")
                    Log.d("CALCULO","abajo :v : ${order.dateArrive - order.dateOrder}")
                    Log.d("CALCULO","div :  ${((Date().time - order.dateOrder ).toDouble() / (order.dateArrive - order.dateOrder).toDouble())}")

                    Log.d("CALCULO","procentaje: ${progressBar.progress}")
                }

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