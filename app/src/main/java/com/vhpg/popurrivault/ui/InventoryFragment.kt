package com.vhpg.popurrivault.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.db.PopurriVaultDatabase
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.FragmentInventoryBinding
import com.vhpg.popurrivault.ui.adapters.ProductAdapter
import kotlinx.coroutines.launch



/**
 * A simple [Fragment] subclass.
 * Use the [InventoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InventoryFragment : Fragment() {
    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private var products: List<ProductEntity> = emptyList()
    private lateinit var repository: ProductRepository

    private lateinit var productAdapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root



    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_inventory, menu)
        /*
// Obtén la referencia al elemento de menú
        val menuItem = menu.findItem(R.id.action_add)

// Cambia el color del icono
        val icon: Drawable? = menuItem.icon
        if (icon != null) {
            DrawableCompat.setTint(icon, ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            menuItem.icon = icon
        }
        */

        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)

            // Cambia el color del icono
            val icon: Drawable? = menuItem.icon
            if (icon != null) {
                DrawableCompat.setTint(icon, ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                menuItem.icon = icon
            }
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

                    productAdapter.filterList(newText)

                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                // Maneja el clic en el botón btAdd aquí
                /*val dialog = ProductDialog(updateUI = {
                    updateUI()
                },message = {text->
                    message(text)

                })
                dialog.show(childFragmentManager,"dialog")*/

                /*requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_container, AddProductFragment(updateUI = {
                        updateUI()
                    },message = {text->
                        message(text)

                    }))
                    .addToBackStack(null)
                    .commit()*/
                findNavController().navigate(R.id.action_navigation_inventory_to_addProductFragment)
                //Toast.makeText(this@MainActivity, "Nuevo", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Ahora puedes acceder a las vistas usando binding.<nombreDeLaVista>
        //Toast.makeText(requireContext(),"Holoooo!",Toast.LENGTH_SHORT).show()

        repository = (requireActivity().application as PopurriVaultBDApp).productRepository

        productAdapter = ProductAdapter()

        /*productAdapter = ProductAdapter() { product ->
            productClicked(product)
        }*/

        binding.rvProducts.apply {
            adapter = productAdapter
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
            products = repository.getAllProducts()
            if(products.isNotEmpty()){
                binding.tvSinRegistros.visibility = View.INVISIBLE


            }else{
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            productAdapter.updateList(products)
        }
    }

    private fun productClicked(product: ProductEntity){
        //message()
        //Toast.makeText(this, "Click en el producto: ${product.name}", Toast.LENGTH_SHORT).show()
        val dialog = ProductDialog(newProduct = false, product = product, updateUI = {
            updateUI()
        },message = {id->
            message(id)
        })
        dialog.show(childFragmentManager, "dialog")

    }

    private fun message(id:Int){
        Snackbar.make(binding.cl, getString(id), Snackbar.LENGTH_SHORT)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .show()
    }


}