package com.vhpg.popurrivault.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope

import com.google.android.material.snackbar.Snackbar
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.ProductDialogBinding
//import com.vhpg.popurrivault.util.CameraHelper
import kotlinx.coroutines.launch
import java.io.IOException

/*class ProductDialog(
    private val newProduct: Boolean = true,
    private var product: ProductEntity = ProductEntity(
        name = "",
        description = "",
        image="",
        cost = 0.0,
        price = 0.0,
        stock = 0,
        supplier = null
    ),
    private val updateUI: () -> Unit,
    private val message: (Int) -> Unit
): DialogFragment() {
    //private lateinit var cameraHelper: CameraHelper
    private var _binding: ProductDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: ProductRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ProductDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as PopurriVaultBDApp).productRepository

        builder = AlertDialog.Builder(requireContext())//,R.style.CustomDialog)

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

        var spinnerData = 0
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

        /*binding.ivCameraLaunch.setOnClickListener {
            val cameraHelper = CameraHelper(this)
            cameraHelper.requestCameraPermission()
        }*/
        dialog = if(newProduct){
            buildDialog(getString(R.string.save),getString(R.string.cancel),{
                //Create
                product.name = binding.tietName.text.toString()
                product.description = binding.tietDesc.text.toString()
                product.cost = binding.tietCost.text.toString().toDouble()
                product.price = binding.tietPrice.text.toString().toDouble()
                product.category = spinnerData
                product.stock = binding.tietStock.text.toString().toInt()
                try{
                    lifecycleScope.launch{
                        repository.insertProduct(product)
                    }
                    message(R.string.product_saved)

                    updateUI()
                }catch(e: IOException){
                    e.printStackTrace()
                    message(R.string.Error_not_saved_product)

                }
            },{
                //Cancelar
            })
        }else{
            buildDialog(getString(R.string.update),getString(R.string.delete),{
                product.name = binding.tietName.text.toString()
                product.description = binding.tietDesc.text.toString()
                product.cost = binding.tietCost.text.toString().toDouble()
                product.price = binding.tietPrice.text.toString().toDouble()
                product.category = spinnerData
                product.stock = binding.tietStock.text.toString().toInt()
                try{
                    lifecycleScope.launch{
                        repository.updateProduct(product)
                    }
                    message(R.string.product_saved)
                    //Toast.makeText(requireContext(), getString(R.string.product_saved), Toast.LENGTH_SHORT).show()
                    updateUI()
                }catch(e: IOException){
                    e.printStackTrace()
                    message(R.string.Error_not_saved_product)
                    //Toast.makeText(requireContext(), getString(R.string.Error_not_saved_product), Toast.LENGTH_SHORT).show()
                }
            },{
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirmation))
                    .setMessage(getString(R.string.questionDeleteProduct)+ product.name)
                    .setPositiveButton(getString(R.string.acept)){_,_ ->

                        try{
                            lifecycleScope.launch{
                                repository.deleteProduct(product)
                            }
                            message(R.string.del_product)
                            //Toast.makeText(requireContext(), getString(R.string.product_deleted), Toast.LENGTH_SHORT).show()
                            updateUI()
                        }catch(e: IOException){
                            e.printStackTrace()
                            message(R.string.Error_not_deleted_product)
                            //Toast.makeText(requireContext(), getString(R.string.Error_not_deleted_product), Toast.LENGTH_SHORT).show()
                        }

                    }
                    .setNegativeButton(getString(R.string.cancel)){dialog, _ ->
                        dialog.dismiss()

                    }.create()
                    .show()

            })
        }

        /*dialog= builder.setView(binding.root)
            .setTitle(getString(R.string.newProduct))
            .setPositiveButton(getString(R.string.save),DialogInterface.OnClickListener { dialog, which ->
                //Save
                product.name = binding.tietName.text.toString()
                product.description = binding.tietDesc.text.toString()
                product.cost = binding.tietCost.text.toString().toDouble()
                product.price = binding.tietPrice.text.toString().toDouble()
                product.category = spinnerData
                product.stock = binding.tietStock.text.toString().toInt()
                try{
                    lifecycleScope.launch{
                        repository.insertProduct(product)
                    }
                    Toast.makeText(requireContext(), getString(R.string.product_saved), Toast.LENGTH_SHORT).show()
                    updateUI()
                }catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(requireContext(), getString(R.string.Error_not_saved_product), Toast.LENGTH_SHORT).show()
                }
            }
                )
            .setNegativeButton(getString(R.string.cancel),DialogInterface.OnClickListener { dialog, which ->  })
            .create()

         */
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        val alertDialog = dialog as AlertDialog

        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

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
    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.newProduct))
            .setPositiveButton(btn1Text,DialogInterface.OnClickListener { dialog, which ->
                positiveButton()
            })
            .setNegativeButton(btn2Text){_, _ ->
                negativeButton()
            }.create()
}*/