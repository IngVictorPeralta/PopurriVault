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
import com.vhpg.popurrivault.data.ContactRepository
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.db.model.ContactEntity
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.AddContactDialogBinding
import com.vhpg.popurrivault.databinding.ProductDialogBinding
//import com.vhpg.popurrivault.util.CameraHelper
import kotlinx.coroutines.launch
import java.io.IOException

class AddContactDialog(
    private val newContact: Boolean = true,
    private val typeContact: String,
    private var supplierKey: Long? = null,
    private var contact: ContactEntity = ContactEntity(
        name = "",
        email = "",
        phoneNumber = "",
        address = "",
        type = "",
        creditLimit = 0.0
    ),
    private val linkContact: (Long?,ContactEntity) -> Unit

): DialogFragment() {
    //private lateinit var cameraHelper: CameraHelper
    private var _binding: AddContactDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: ContactRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = AddContactDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as PopurriVaultBDApp).contactRepository

        builder = AlertDialog.Builder(requireContext())//,R.style.CustomDialog)


        binding.apply {

            tietName.setText(contact.name)
            tietEmail.setText(contact.email)
            tietPhoneNumber.setText(contact.phoneNumber)
            tietAddress.setText(contact.address)

        }

        /*binding.ivCameraLaunch.setOnClickListener {
            val cameraHelper = CameraHelper(this)
            cameraHelper.requestCameraPermission()
        }*/
        dialog = if(newContact){
            buildDialog(getString(R.string.save),getString(R.string.cancel),{
                //Create
                contact.name = "${binding.tietName.text}"
                contact.email = "${binding.tietEmail.text}"
                contact.phoneNumber = "${binding.tietPhoneNumber.text}"
                contact.address = "${binding.tietAddress.text}"
                contact.type = typeContact


                try{
                    lifecycleScope.launch{
                        supplierKey =  repository.insertContact(contact)
                    }

                    //message(R.string.product_saved)
                    linkContact(supplierKey,contact)
                }catch(e: IOException){
                    e.printStackTrace()

                }
            },{
                //Cancelar
            })
        }else{
            buildDialog(getString(R.string.update),getString(R.string.delete),{
                contact.name = "${binding.tietName.text}"
                contact.email = "${binding.tietEmail.text}"
                contact.phoneNumber = "${binding.tietPhoneNumber.text}"
                contact.address = "${binding.tietAddress.text}"
                contact.type = typeContact

                try{
                    lifecycleScope.launch{
                        repository.updateContact(contact)
                    }
                    //message(R.string.product_saved)
                    //Toast.makeText(requireContext(), getString(R.string.product_saved), Toast.LENGTH_SHORT).show()
                    linkContact(supplierKey,contact)
                }catch(e: IOException){
                    e.printStackTrace()
                    //message(R.string.Error_not_saved_product)
                    //Toast.makeText(requireContext(), getString(R.string.Error_not_saved_product), Toast.LENGTH_SHORT).show()
                }
            },{
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirmation))
                    .setMessage(getString(R.string.questionDeleteProduct)+ contact.name)
                    .setPositiveButton(getString(R.string.acept)){_,_ ->

                        try{
                            lifecycleScope.launch{
                                repository.deleteContact(contact)
                            }
                            //message(R.string.del_product)
                            //Toast.makeText(requireContext(), getString(R.string.product_deleted), Toast.LENGTH_SHORT).show()
                            //updateUI()

                        }catch(e: IOException){
                            e.printStackTrace()
                            //message(R.string.Error_not_deleted_product)
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
        binding.tietEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietPhoneNumber.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietAddress.addTextChangedListener(object: TextWatcher {
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
        Ok = Ok && binding.tietEmail.text.toString().isNotEmpty()
        Ok = Ok && binding.tietPhoneNumber.text.toString().isNotEmpty()
        Ok = Ok && binding.tietAddress.text.toString().isNotEmpty()

        /*if(Ok) {
            var priceVsCost = binding.tietCost.text.toString().toInt() < binding.tietPrice.text.toString().toInt()
            if (!priceVsCost) {
                messageDialog(getString(R.string.priceVsCost))
            }
            Ok = Ok && priceVsCost
        }*/
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
            .setTitle(getString(R.string.contact))
            .setPositiveButton(btn1Text,DialogInterface.OnClickListener { dialog, which ->
                positiveButton()
            })
            .setNegativeButton(btn2Text){_, _ ->
                negativeButton()
            }.create()
}