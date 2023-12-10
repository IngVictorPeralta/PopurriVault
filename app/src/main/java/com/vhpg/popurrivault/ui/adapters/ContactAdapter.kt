package com.vhpg.popurrivault.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.data.db.model.ContactEntity

import com.vhpg.popurrivault.databinding.ContactElementBinding

class ContactAdapter (
    //private var onSupplierClick: (SupplierEntity) -> Unit
): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    private var contacts: List<ContactEntity> = emptyList()
    private var filteredContacts: List<ContactEntity> = emptyList()

    class ViewHolder(private val binding: ContactElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon
        fun bind(contact: ContactEntity){

            binding.apply {
                tvName.text = "${contact.name}"
                ivIcon.setImageResource(R.drawable.cat4)
                tvPhone.text = "${contact.phoneNumber}"
                tvEmail.text = "${contact.email}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContactElementBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredContacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredContacts[position])

        /*holder.itemView.setOnClickListener {
            onProductClick(products[position])
        }*/

        holder.ivIcon.setOnClickListener {

        }
    }
    fun updateList(list: List<ContactEntity>) {
        contacts = list
        filterList("")
    }

    fun filterList(query: String) {
        Log.d("Filtro","entrando al filtro con $query")
        filteredContacts = if (query.isEmpty()) {
            contacts
        } else {

            contacts.filter { supplier ->
                // Puedes personalizar la lógica de filtrado según tus necesidades
                supplier.name.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}