package com.vhpg.popurrivault.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.FragmentSelectProductsBinding
import com.vhpg.popurrivault.ui.adapters.ProductCompactAdapter
import kotlinx.coroutines.launch



/**
 * A simple [Fragment] subclass.
 * Use the [SelectProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectProductsFragment: Fragment() {


    private var _binding: FragmentSelectProductsBinding? = null
    private val binding get() = _binding!!



    private lateinit var typeSel:String


    private var showCar = true
    private var products: List<ProductEntity> = emptyList()
    private var selectedProducts: MutableList<ProductEntity> = mutableListOf()

    private lateinit var repository: ProductRepository

    private lateinit var productAdapter: ProductCompactAdapter
    private lateinit var selectedProductAdapter: ProductCompactAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectProductsBinding.inflate(inflater, container, false)

        val args: SelectProductsFragmentArgs by navArgs()
        typeSel = args.typeSel
        setHasOptionsMenu(true)
        return binding.root



    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_select_products, menu)
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
            R.id.action_see -> {
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
                //findNavController().navigate(R.id.action_navigation_inventory_to_addProductFragment)

                //Toast.makeText(this@MainActivity, "Nuevo", Toast.LENGTH_SHORT).show()
                if(showCar){
                    binding.rvSelectedProducts.visibility = View.GONE
                }else{
                    binding.rvSelectedProducts.visibility = View.VISIBLE
                }
                showCar = showCar.not()
                Log.d("showCar","${showCar}")
                return true
            }
            R.id.finish -> {
                val arrayProductList: Array<ProductEntity> = selectedProducts.toTypedArray()

                val action = SelectProductsFragmentDirections.actionSelectProductsFragmentToAddOrderFragment(arrayProductList)
                findNavController().navigate(action)

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

        productAdapter = ProductCompactAdapter("SEL"){product,typeCell ->
            productEditList(product,typeCell)
        }

        selectedProductAdapter = ProductCompactAdapter("NEW"){product,typeCell ->
            productEditList(product,typeCell)
        }
        /*productAdapter = ProductAdapter() { product ->
            productClicked(product)
        }*/

        binding.rvProducts.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            //LinearLayoutManager(requireContext())

        }

        binding.rvSelectedProducts.apply {
            adapter = selectedProductAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
                //LinearLayoutManager(requireContext())

        }

        /*selectedProducts.add(ProductEntity(id = 1, name = "a",
            description = "b",
            cost = 0.0,
            price = 0.0,
            category = 0,
            stock = 0,
            supplier = null))
       val a = ProductEntity(id=2,name = "ab",
           description = "b",
           cost = 0.0,
           price = 0.0,
           category = 0,
           stock = 0,
           supplier = null)
        selectedProducts.add(a)

        selectedProducts += ProductEntity(id=3,name = "ac",
            description = "b",
            cost = 0.0,
            price = 0.0,
            category = 0,
            stock = 0,
            supplier = null)
        selectedProducts += ProductEntity(id=4,name = "ad",
            description = "b",
            cost = 0.0,
            price = 0.0,
            category = 0,
            stock = 0,
            supplier = null)*/
        selectedProductAdapter.updateList(selectedProducts)


        //productEditList(a,"NEW")
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

    private fun productEditList(productClicked: ProductEntity, typeCell: String){
        //val idOnChange:Long = 1
        var positiveText: String = "Aceptar"
        var negativeText: String = "Eliminar"
        var titleText: String = "Agregar producto"
        val initialStock: Int? = productClicked.stock
        val editText = EditText(context).apply {
            // Configura el tipo de entrada como número entero
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        val stockEditText = EditText(context).apply {
            // Configura el tipo de entrada como número entero
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            hint = "Stock"
            if(typeCell == "NEW"){
                initialStock?.let { setText(it.toString()) }
            }
        }
        val costEditText = EditText(context).apply {
            // Configura el tipo de entrada como número decimal
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

            hint = "Costo"


        }
        val layoutIns = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(stockEditText)
            if(typeSel == "ORDER"){
                addView(costEditText)
            }
        }

        if(typeCell == "SEL"){
            titleText = "Agregar producto"
            positiveText = "Ingresar"
            negativeText = "Cancelar"
        }
        if(typeCell == "NEW"){
            titleText = "editar producto"
            positiveText = "Actualizar"
            negativeText = "Eliminar"
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(titleText)
            .setView(layoutIns)
            .setPositiveButton(positiveText) { _, _ ->
                // Acción a realizar cuando se hace clic en Aceptar
                val stock = stockEditText.text.toString().toIntOrNull()
                val cost = editText.text.toString().toDoubleOrNull()
                if (stock != null) {
                    // Realiza la acción con el número ingresado
                    // Puedes manejar el número (enteredNumber) aquí
                    if(typeCell == "NEW") {
                        if(cost!=null) {
                            selectedProducts.filter { product ->
                                if (product.id == productClicked.id) {
                                    product.stock = stock
                                    product.cost = cost
                                    //selectedProducts.remove(productClicked)
                                }
                                true
                            }
                        }else{
                            Toast.makeText(requireContext(), "El costo no es valido :)", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if(typeCell == "SEL") {
                        productClicked.stock = stock
                        if(cost!=null) {
                            productClicked.cost = cost
                        }else{
                            Toast.makeText(requireContext(), "El costo no es valido :)", Toast.LENGTH_SHORT).show()
                        }
                        selectedProducts.add(productClicked)
                    }
                    selectedProductAdapter.updateList(selectedProducts)
                } else {
                    // Manejar el caso en que la entrada no sea un número válido
                    Toast.makeText(requireContext(), "La cantidad de inventario no es valido :)", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(negativeText){ _ ,_ ->

                Toast.makeText(requireContext(), "Eliminando ${productClicked.name}", Toast.LENGTH_SHORT).show()
                if(typeCell == "NEW"){
                    selectedProducts.remove(productClicked)
                }
                selectedProductAdapter.updateList(selectedProducts)
            }
            .create()

        dialog.show()



    }
    /*private fun productClicked2(product: ProductEntity){
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