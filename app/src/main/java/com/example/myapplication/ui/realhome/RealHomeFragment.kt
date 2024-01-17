package com.example.myapplication.ui.realhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRealhomeBinding

class RealHomeFragment: Fragment() {

    private var date: TextView?=null
    private var _binding: FragmentRealhomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRealhomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val hour = arguments?.getString(hour)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        date = root.findViewById(R.id.date)


        if(hour!=null){
            date?.text = hour
        }
        else{
            date?.text = "0"
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val hour = "hour"

        fun newInstance(hour: Int): RealHomeFragment {
            val fragment = RealHomeFragment()
            val args = Bundle()
            args.putString(hour.toString(), hour.toString())
            fragment.arguments = args
            return fragment
        }
    }
}
