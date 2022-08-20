
package com.example.lunchtray.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lunchtray.R
import com.example.lunchtray.databinding.FragmentAccompanimentMenuBinding
import com.example.lunchtray.model.OrderViewModel


class AccompanimentMenuFragment : Fragment() {


    private var _binding: FragmentAccompanimentMenuBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccompanimentMenuBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            accompanimentMenuFragment = this@AccompanimentMenuFragment
        }
    }

    /**
     * Navigate to the checkout fragment.
     */
    fun goToNextScreen() {
        findNavController().navigate(R.id.action_accompanimentMenuFragment_to_checkoutFragment)
    }

    /**
     * Cancel the order and start over.
     */
    fun cancelOrder() {
        sharedViewModel.resetOrder()
        findNavController().navigate(R.id.action_accompanimentMenuFragment_to_startOrderFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
