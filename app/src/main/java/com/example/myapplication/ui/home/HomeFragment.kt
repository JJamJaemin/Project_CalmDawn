package com.example.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.HomeActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.MemoAdapter
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.db.DBLoader
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var adapter: MemoAdapter
    private lateinit var text_msg:TextView

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAdd = view.findViewById<FloatingActionButton>(R.id.btn_add)
        text_msg = view.findViewById(R.id.text_msg)
        btnAdd.setOnClickListener{
            startActivity(Intent(requireContext(),HomeActivity::class.java))
        }
        adapter = MemoAdapter(requireContext())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.setList(DBLoader(requireContext()).memoList(null))
        if(adapter.itemCount > 0){
            text_msg.visibility = View.INVISIBLE
        }else{
            text_msg.visibility = View.VISIBLE
        }
    }
}