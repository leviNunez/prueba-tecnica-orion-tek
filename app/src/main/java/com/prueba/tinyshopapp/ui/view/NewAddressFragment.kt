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
import com.prueba.tinyshopapp.data.database.CustomerDatabase
import com.prueba.tinyshopapp.data.repository.CustomerRepositoryImpl
import com.prueba.tinyshopapp.databinding.FragmentNewAddressBinding
import com.prueba.tinyshopapp.domain.Address
import com.prueba.tinyshopapp.ui.viewmodel.newaddress.NewAddressViewModel
import com.prueba.tinyshopapp.ui.viewmodel.newaddress.NewAddressViewModelFactory
import com.prueba.tinyshopapp.utils.collectLifecycleFlow

class NewAddressFragment : Fragment() {

    lateinit var binding: FragmentNewAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_address, container, false)

        val application = requireNotNull(this.activity)

        val customerDao = CustomerDatabase.getInstance(application).customerDatabaseDao

        val repository = CustomerRepositoryImpl(customerDao)

        val viewModelFactory = NewAddressViewModelFactory(repository)

        val newAddressViewModel =
            ViewModelProvider(this, viewModelFactory)[NewAddressViewModel::class.java]

        collectLifecycleFlow(newAddressViewModel.navigateToCustomerDetail) { customerId ->
            this.findNavController()
                .navigate(
                    NewAddressFragmentDirections.actionNewAddressFragmentToCustomerDetailFragment(
                        customerId
                    )
                )
        }

        binding.saveButton.setOnClickListener {
            if (validateForm()) {
                saveAddress(newAddressViewModel)
            }
        }
        newAddressViewModel.getCustomer().observe(viewLifecycleOwner) {

        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = newAddressViewModel

        return binding.root
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val address = binding.addressEditText.text.toString()
        val city = binding.cityEditText.text.toString()
        val state = binding.stateEditText.text.toString()
        val zipCode = binding.zipcodeEditText.text.toString()

        if (address.isBlank() || address.length < 5) {
            binding.addressTextLayout.error = "Invalid address"
            isValid = false
        } else {
            binding.addressTextLayout.error = null
        }

        if (city.isBlank() || city.length < 2) {
            binding.cityTextLayout.error = "Invalid city"
            isValid = false
        } else {
            binding.cityTextLayout.error = null
        }

        if (state.isBlank() || state.length < 2) {
            binding.stateTextLayout.error = "Invalid state"
            isValid = false
        } else {
            binding.stateTextLayout.error = null
        }

        if (zipCode.isBlank() || zipCode.length < 5) {
            binding.zipcodeTextLayout.error = "Invalid zip code"
            isValid = false
        } else {
            binding.zipcodeTextLayout.error = null
        }
        return isValid
    }

    private fun saveAddress(newAddressViewModel: NewAddressViewModel) {
        val address = binding.addressEditText.text.toString().uppercase()
        val city = binding.cityEditText.text.toString().uppercase()
        val state = binding.stateEditText.text.toString().uppercase()
        val zipCode = binding.zipcodeEditText.text.toString()
        val country = binding.countryEditText.text.toString()

        val newAddress =
            Address(
                street = address,
                city = city,
                state = state,
                zipCode = zipCode,
                country = country
            )

        newAddressViewModel.updateCustomer(newAddress)
    }

}