package com.example.newstemplate.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.newstemplate.databinding.FragmentNavigatorBinding
import kotlin.random.Random

private const val ARG_PARAM1 = "param1"

class NavigatorFragment : Fragment() {
    //layout binding
    private var _binding: FragmentNavigatorBinding? = null

    private var param1: String? = null
    private lateinit var navigatorTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = "${it.getString(ARG_PARAM1)}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorBinding.inflate(inflater, container, false)
        navigatorTxt = _binding?.navigatorTxt!!
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val randomInt = Random.nextInt(1, 101111)

           // param1 += ", Random Integer: ${randomInt}}"

        Log.d("NavigatorFragment", "onViewCreated: ")

        navigatorTxt.text = param1//randomInt.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            NavigatorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

}