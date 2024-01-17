package com.example.myapplication.ui.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.CustomDialog.PhoneNumberManager.getPhoneNumber
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.example.myapplication.db.DBHelper
import com.example.myapplication.db.DBHelper.UserData

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var phonenum = getPhoneNumber()
        if (phonenum != null) {
            setPhoneNumber(root, phonenum)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    fun setPhoneNumber(root : View, phoneNumber: String) {
        val dbHelper = context?.let { DBHelper(it) }
        var user : UserData? = null
        if (dbHelper != null) {
            user = dbHelper.getUserDataByPhoneNumber(phoneNumber)
        }

        if (user != null) {
            root.findViewById<TextView>(R.id.et_phonenum).text =user.phoneNumber
            root.findViewById<TextView>(R.id.et_name).text= user.name
            root.findViewById<TextView>(R.id.et_age).text = user.age.toString()
            root.findViewById<EditText>(R.id.et_emphone).hint =user.emergencyPhoneNumber

            if(user.gender=="Man"){
                root.findViewById<TextView>(R.id.et_gender).text="남성"
            }
            else{
                root.findViewById<TextView>(R.id.et_gender).text="여성"
            }

        }
    }

}