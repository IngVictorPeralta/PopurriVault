/*package com.vhpg.popurrivault.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.ContactRepository
import com.vhpg.popurrivault.data.db.dao.ContactDao

import com.vhpg.popurrivault.data.db.model.ContactEntity

import com.vhpg.popurrivault.databinding.FragmentSuppliersBinding

import com.vhpg.popurrivault.ui.adapters.ContactAdapter
import kotlinx.coroutines.launch


class SuppliersFragment : Fragment() {
    private var _binding: FragmentSuppliersBinding? = null
    private val binding get() = _binding!!

    private var contacts: List<ContactEntity> = emptyList()
    private lateinit var repository: ContactRepository

    private lateinit var contactAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSuppliersBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_inventory, menu)
// Obtén la referencia al elemento de menú
        val menuItem = menu.findItem(R.id.action_add)

// Cambia el color del icono
        val icon: Drawable? = menuItem.icon
        if (icon != null) {
            DrawableCompat.setTint(icon, ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            menuItem.icon = icon
        }

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Aquí puedes realizar acciones cuando se envía la búsqueda (pulsar Enter)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Aquí puedes realizar acciones mientras se está escribiendo la búsqueda
                // Actualiza la lista según la nueva consulta (newText)

                contactAdapter.filterList(newText)

                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                // Maneja el clic en el botón btAdd aquí
                val dialog = ProductDialog(updateUI = {
                    updateUI()
                },message = {text->
                    message(text)

                })
                dialog.show(childFragmentManager,"dialog")
                //Toast.makeText(this@MainActivity, "Nuevo", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Ahora puedes acceder a las vistas usando binding.<nombreDeLaVista>
        Toast.makeText(requireContext(),"Holoooo!", Toast.LENGTH_SHORT).show()

        repository = (requireActivity().application as PopurriVaultBDApp).contactRepository
            //(requireActivity().application as PopurriVaultBDApp).repository

        contactAdapter = ContactAdapter()

        /*productAdapter = ProductAdapter() { product ->
            productClicked(product)
        }*/

        binding.rvContacts.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }

        /*binding.btAdd.setOnClickListener {
            val dialog = ProductDialog(updateUI = {
                updateUI()
            },message = {text->
                message(text)

            })
            dialog.show(childFragmentManager,"dialog")
            //Toast.makeText(this@MainActivity, "Nuevo", Toast.LENGTH_SHORT).show()
        }*/
        updateUI()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun updateUI(){
        lifecycleScope.launch{
            suppliers = repository.getAllSuppliers()
            if(suppliers.isNotEmpty()){
                binding.tvSinRegistros.visibility = View.INVISIBLE


            }else{
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            supplierAdapter.updateList(suppliers)
        }
    }



    private fun message(id:Int){
        Snackbar.make(binding.cl, getString(id), Snackbar.LENGTH_SHORT)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .show()
    }

}*/