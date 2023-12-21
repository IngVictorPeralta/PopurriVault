package com.vhpg.popurrivault.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import com.vhpg.popurrivault.data.RestockRepository
import com.vhpg.popurrivault.data.SaleRepository
import com.vhpg.popurrivault.data.db.model.OrderEntity
import com.vhpg.popurrivault.data.db.model.SaleEntity
import com.vhpg.popurrivault.databinding.FragmentOrdersBinding
import com.vhpg.popurrivault.databinding.FragmentSalesBinding
import com.vhpg.popurrivault.ui.adapters.OrderAdapter
import com.vhpg.popurrivault.ui.adapters.SaleAdapter
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [SalesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SalesFragment : Fragment() {
    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!

    private var sales: List<SaleEntity> = emptyList()
    private lateinit var repository: SaleRepository

    private lateinit var saleAdapter: SaleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSalesBinding.inflate(inflater, container, false)
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

                saleAdapter.filterList(newText)

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
                val typeSel = "SALE"
                val action = SalesFragmentDirections.actionNavigationSalesToSelectProductsFragment(typeSel)
                findNavController().navigate(action)




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

        repository = (requireActivity().application as PopurriVaultBDApp).saleRepository

        saleAdapter = SaleAdapter()

        /*productAdapter = ProductAdapter() { product ->
            productClicked(product)
        }*/

        binding.rvOrders.apply {
            adapter = saleAdapter
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
            sales = repository.getAllSales()
            if(sales.isNotEmpty()){
                binding.tvSinRegistros.visibility = View.INVISIBLE


            }else{
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            saleAdapter.updateList(sales)
        }
    }

    /*private fun productClicked(product: ProductEntity){
        //message()
        //Toast.makeText(this, "Click en el producto: ${product.name}", Toast.LENGTH_SHORT).show()
        val dialog = ProductDialog(newProduct = false, product = product, updateUI = {
            updateUI()
        },message = {id->
            message(id)
        })
        dialog.show(childFragmentManager, "dialog")

    }*/

    private fun message(id:Int){
        Snackbar.make(binding.cl, getString(id), Snackbar.LENGTH_SHORT)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .show()
    }
}