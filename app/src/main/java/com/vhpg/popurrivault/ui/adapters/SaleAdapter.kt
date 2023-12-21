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
import com.vhpg.popurrivault.data.db.model.SaleEntity

import com.vhpg.popurrivault.databinding.ContactElementBinding
import com.vhpg.popurrivault.databinding.OrderElementBinding
import com.vhpg.popurrivault.databinding.SaleElementBinding
import java.util.*

class SaleAdapter (
    //private var onSupplierClick: (SupplierEntity) -> Unit
): RecyclerView.Adapter<SaleAdapter.ViewHolder>() {
    private var sales: List<SaleEntity> = emptyList()
    private var filteredSales: List<SaleEntity> = emptyList()
    private var today = Date().time
    class ViewHolder(private val binding: SaleElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon
        fun bind(sale: SaleEntity){

            binding.apply {
                tvName.text = "${sale.description}"
                ivIcon.setImageResource(R.drawable.cat5)
                if(sale.arrived) {
                    progressBar.progress = 100
                    //val color = ContextCompat.getColor(progressBar.context, R.color.colorAccept)
                    //var cl = Color.parseColor(R.color.colorAccept.toString())
                    //progressBar.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                    //progressBar.progressTintList = ColorStateList.valueOf(cl)
                }else{
                    progressBar.progress =
                        (((Date().time - sale.dateOrder ).toDouble() / (sale.dateArrive - sale.dateOrder).toDouble())*100).toInt()

                    Log.d("CALCULO","dateOrder: ${sale.dateOrder}")
                    Log.d("CALCULO","today: ${Date().time}")
                    Log.d("CALCULO","arriba: ${Date().time - sale.dateOrder}")
                    Log.d("CALCULO","arrived: ${sale.dateArrive}")
                    Log.d("CALCULO","abajo :v : ${sale.dateArrive - sale.dateOrder}")
                    Log.d("CALCULO","div :  ${((Date().time - sale.dateOrder ).toDouble() / (sale.dateArrive - sale.dateOrder).toDouble())}")

                    Log.d("CALCULO","procentaje: ${progressBar.progress}")
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SaleElementBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredSales.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredSales[position])

        /*holder.itemView.setOnClickListener {
            onProductClick(products[position])
        }*/

        holder.ivIcon.setOnClickListener {

        }
    }
    fun updateList(list: List<SaleEntity>) {
        sales = list
        filterList("")
    }

    fun filterList(query: String) {
        Log.d("Filtro","entrando al filtro con $query")
        filteredSales = if (query.isEmpty()) {
            sales
        } else {

            sales.filter { sale ->
                // Puedes personalizar la lógica de filtrado según tus necesidades
                sale.description.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}