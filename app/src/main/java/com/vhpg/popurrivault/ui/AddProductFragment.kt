package com.vhpg.popurrivault.ui

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
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
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.FragmentAddProductBinding
import com.vhpg.popurrivault.databinding.ProductDialogBinding
import com.vhpg.popurrivault.ui.adapters.ProductAdapter
import kotlinx.coroutines.launch
import java.io.IOException

class AddProductFragment(


): Fragment() {
    private val newProduct: Boolean = true
    private var product: ProductEntity = ProductEntity(
        name = "",
        description = "",
        cost = 0,
        price = 0,
        category = 0,
        stock = 0
    )
    var spinnerData = 0
    /*private val updateUI: () -> Unit
    private val message: (Int) -> Unit*/

    //private lateinit var cameraHelper: CameraHelper
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!


    private var saveButton: Button? = null

    private lateinit var repository: ProductRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_form, menu)
// Obtén la referencia al elemento de menú
        //val menuItem = menu.findItem(R.id.action_save)
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)

            // Cambia el color del icono
            val icon: Drawable? = menuItem.icon
            if (icon != null) {
                DrawableCompat.setTint(icon, ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                menuItem.icon = icon
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                // Maneja el clic en el botón btAdd aquí
                if(validateFields()){
                    InsertProduct()
                    findNavController().navigateUp()
                }
                //Toast.makeText(requireContext(), "Nuevo producto", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Ahora puedes acceder a las vistas usando binding.<nombreDeLaVista>
        Toast.makeText(requireContext(),"Holoooo!", Toast.LENGTH_SHORT).show()

        repository = (requireActivity().application as PopurriVaultBDApp).productRepository


        val spinner = binding.spCat
        //findViewById<MaterialAutoCompleteTextView>(R.id.materialSpinner)
        val datos = arrayListOf(
            getString(R.string.notCategory),
            getString(R.string.cap),
            getString(R.string.pants),
            getString(R.string.shoes),
            getString(R.string.socks),
            getString(R.string.sweater),
            getString(R.string.tshirt),

            )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,datos)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val itemSelected = position
                val imageResource = when (position) {
                    0-> R.drawable.cat0
                    1 -> R.drawable.cat1
                    2 -> R.drawable.cat2
                    3 -> R.drawable.cat3
                    4 -> R.drawable.cat4
                    5 -> R.drawable.cat5
                    6 -> R.drawable.cat6
                    7 -> R.drawable.cat7
                    else -> R.drawable.cat0
                }
                binding.apply {
                    ivIcon.setImageResource(imageResource)
                }

                spinnerData = itemSelected

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        val imageResource = when (product.category) {
            0-> R.drawable.cat0
            1 -> R.drawable.cat1
            2 -> R.drawable.cat2
            3 -> R.drawable.cat3
            4 -> R.drawable.cat4
            5 -> R.drawable.cat5
            6 -> R.drawable.cat6
            7 -> R.drawable.cat7
            else -> R.drawable.cat0
        }

        binding.apply {

            tietName.setText(product.name)
            tietDesc.setText(product.description)
            tietCost.setText(product.cost.toString())
            tietPrice.setText(product.price.toString())
            spCat.setSelection(product.category)
            tietStock.setText(product.stock.toString())


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onStart() {
        super.onStart()

        binding.tietName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietDesc.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietCost.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietPrice.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietStock.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
    }
    private fun InsertProduct(): Boolean{
        product.name = binding.tietName.text.toString()
        product.description = binding.tietDesc.text.toString()
        product.cost = binding.tietCost.text.toString().toInt()
        product.price = binding.tietPrice.text.toString().toInt()
        product.category = spinnerData
        product.stock = binding.tietStock.text.toString().toInt()
        return try{
            lifecycleScope.launch{
                repository.insertProduct(product)

            }
            true
        }catch(e: IOException){
            e.printStackTrace()
            false
        }
    }
    private fun validateFields(): Boolean{
        var Ok = true
        Ok = Ok && binding.tietName.text.toString().isNotEmpty()
        Ok = Ok && binding.tietDesc.text.toString().isNotEmpty()
        Ok = Ok && binding.tietCost.text.toString().isNotEmpty()
        Ok = Ok && binding.tietPrice.text.toString().isNotEmpty()
        Ok = Ok && binding.tietStock.text.toString().isNotEmpty()
        if(Ok) {
            var priceVsCost = binding.tietCost.text.toString().toInt() < binding.tietPrice.text.toString().toInt()
            if (!priceVsCost) {
                messageDialog(getString(R.string.priceVsCost))
            }
            Ok = Ok && priceVsCost
        }
        return(Ok)
    }
    private fun messageDialog(text: String){
        Snackbar.make(binding.dial, text, Snackbar.LENGTH_SHORT)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .show()
    }
}