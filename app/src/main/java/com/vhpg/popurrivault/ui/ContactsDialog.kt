package com.vhpg.popurrivault.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import com.vhpg.popurrivault.data.ContactRepository
import com.vhpg.popurrivault.data.db.model.ContactEntity
import com.vhpg.popurrivault.databinding.ContactsDialogBinding
import com.vhpg.popurrivault.ui.adapters.ContactAdapter
import kotlinx.coroutines.launch

class ContactsDialog(
    private val typeContact: String,
    private val linkContact: (Long?,ContactEntity) -> Unit
): DialogFragment() {
    private var _binding: ContactsDialogBinding? = null
    private val binding get() = _binding!!

    private var contacts: List<ContactEntity> = emptyList()
    private lateinit var contactAdapter: ContactAdapter

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private lateinit var repository: ContactRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ContactsDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as PopurriVaultBDApp).contactRepository

        builder = AlertDialog.Builder(requireContext())

        contactAdapter = ContactAdapter()
        binding.rvContacts.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }
        /*binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Aquí puedes realizar acciones cuando se envía la búsqueda (pulsar Enter)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Aquí puedes realizar acciones mientras se está escribiendo la búsqueda
                // Actualiza la lista según la nueva consulta (newText)

                supplierAdapter.filterList(newText)

                return true
            }
        })*/
        val searchView: SearchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Manejar la acción de enviar búsqueda si es necesario
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Manejar cambios en el texto de búsqueda
                // Actualiza tu RecyclerView según la búsqueda aquí...
                contactAdapter.filterList(newText)
                return true
            }
        })

        updateUI()
        dialog = buildDialog("Agregar","Cancelar",{
            val dialog2 = AddContactDialog(typeContact="SUPPLIER", linkContact = { keySupp,supp ->
                linkContact(keySupp,supp)
            })
            dialog2.show(parentFragmentManager,"addSupplier")
        },{
            val dialog2 = AddContactDialog(typeContact="SUPPLIER", linkContact = { keySupp,supp ->
                linkContact(keySupp,supp)
            })
            dialog2.show(parentFragmentManager,"addSupplier")
            //Cancelar
        })
        return dialog
    }


    override fun onResume() {
        super.onResume()
        updateUI()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun updateUI(){
        lifecycleScope.launch{
            contacts = repository.getAllContactsByType(typeContact)
            /*contacts = listOf(
                /*SupplierEntity(name = "Proveedor 1", email = "proveedor1@example.com", phoneNumber = "123456789", address = "Dirección 1"),
                SupplierEntity(name = "Proveedor 2", email = "proveedor2@example.com", phoneNumber = "987654321", address = "Dirección 2"),
                SupplierEntity(name = "Proveedor 3", email = "proveedor3@example.com", phoneNumber = "555555555", address = "Dirección 3")*/
            )*/

            if(contacts.isNotEmpty()){
                binding.tvSinRegistros.visibility = View.INVISIBLE


            }else{
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            contactAdapter.updateList(contacts)
        }
    }
    private fun message(id:Int){
        Snackbar.make(binding.cl, getString(id), Snackbar.LENGTH_SHORT)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .show()
    }


    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.Suppliers))
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener { dialog, which ->
                positiveButton()
            })
            .setNegativeButton(btn2Text){_, _ ->
                negativeButton()
            }.create()
}