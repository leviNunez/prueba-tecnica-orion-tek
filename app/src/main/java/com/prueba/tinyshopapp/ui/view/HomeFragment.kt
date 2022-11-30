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
import com.prueba.tinyshopapp.databinding.FragmentHomeBinding
import com.prueba.tinyshopapp.ui.viewmodel.home.HomeViewModel
import com.prueba.tinyshopapp.ui.viewmodel.home.HomeViewModelFactory
import com.prueba.tinyshopapp.utils.collectLifecycleFlow

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity)

        val customerDao = CustomerDatabase.getInstance(application).customerDatabaseDao

        val repository = CustomerRepositoryImpl(customerDao)

        val viewModelFactory = HomeViewModelFactory(repository)

        val homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        val adapter = CustomerListAdapter(CustomerItemListener { customer ->
            homeViewModel.displayCustomerDetail(customer.id)
        })

        binding.customersList.adapter = adapter

        homeViewModel.customers.observe(viewLifecycleOwner) { customers ->
            customers?.let {
                adapter.submitList(it)
            }
        }
        collectLifecycleFlow(homeViewModel.navigateToNewCustomer) { shouldNavigate ->
            if (shouldNavigate) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToNewCustomerFragment())
            }
        }

        collectLifecycleFlow(homeViewModel.navigateToCustomerDetail) { customerId ->
            this.findNavController()
                .navigate(
                    HomeFragmentDirections.actionHomeFragmentToCustomerDetailFragment(
                        customerId
                    )
                )
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = homeViewModel

        return binding.root
    }

}

