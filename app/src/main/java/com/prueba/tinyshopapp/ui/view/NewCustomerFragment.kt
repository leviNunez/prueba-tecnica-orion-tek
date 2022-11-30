package com.prueba.tinyshopapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.prueba.tinyshopapp.R
import com.prueba.tinyshopapp.data.database.AddressList
import com.prueba.tinyshopapp.data.database.CustomerDatabase
import com.prueba.tinyshopapp.data.database.CustomerEntity
import com.prueba.tinyshopapp.data.repository.CustomerRepositoryImpl
import com.prueba.tinyshopapp.databinding.FragmentNewCustomerBinding
import com.prueba.tinyshopapp.domain.Address
import com.prueba.tinyshopapp.ui.viewmodel.newcustomer.NewCustomerViewModel
import com.prueba.tinyshopapp.ui.viewmodel.newcustomer.NewCustomerViewModelFactory
import com.prueba.tinyshopapp.utils.collectLifecycleFlow

class NewCustomerFragment : Fragment() {

    lateinit var binding: FragmentNewCustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_customer, container, false)

        val application = requireNotNull(this.activity)

        val customerDao = CustomerDatabase.getInstance(application).customerDatabaseDao

        val repository = CustomerRepositoryImpl(customerDao)

        val viewModelFactory = NewCustomerViewModelFactory(repository)

        val newCustomerViewModel =
            ViewModelProvider(this, viewModelFactory)[NewCustomerViewModel::class.java]

        collectLifecycleFlow(newCustomerViewModel.navigateToHome) {
            this.findNavController()
                .navigate(
                    NewCustomerFragmentDirections.actionNewCustomerFragmentToHomeFragment()
                )
        }

        binding.saveButton.setOnClickListener {
            if (validateForm()) {
                saveCustomer(newCustomerViewModel)
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = newCustomerViewModel

        return binding.root
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val name = binding.nameEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        val city = binding.cityEditText.text.toString()
        val state = binding.stateEditText.text.toString()
        val zipCode = binding.zipcodeEditText.text.toString()

        if (name.isBlank() || name.length < 2) {
            binding.nameTextLayout.error = "Invalid name"
            isValid = false
        } else {
            binding.nameTextLayout.error = null
        }
        if (address.isBlank() || address.length < 5) {
            binding.addressTextLayout.error = "Invalid address"
            isValid = false
        }

        if (city.isBlank() || city.length < 2) {
            binding.cityTextLayout.error = "Invalid city"
            isValid = false
        }

        if (state.isBlank() || state.length < 2) {
            binding.stateTextLayout.error = "Invalid state"
            isValid = false
        }

        if (zipCode.isBlank() || zipCode.length < 5) {
            binding.zipcodeTextLayout.error = "Invalid zip code"
            isValid = false
        }
        return isValid
    }

    private fun saveCustomer(newCustomerViewModel: NewCustomerViewModel) {
        val name = binding.nameEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        val city = binding.cityEditText.text.toString()
        val state = binding.stateEditText.text.toString()
        val zipCode = binding.zipcodeEditText.text.toString()
        val country = binding.countryEditText.text.toString()

        val addresses = listOf(
            Address(
                street = address,
                city = city,
                state = state,
                zipCode = zipCode,
                country = country
            )
        )
        val newCustomer = CustomerEntity(
            name = name,
            addressList = AddressList(
                addresses = addresses
            )
        )
        newCustomerViewModel.saveCustomer(newCustomer)
    }

}