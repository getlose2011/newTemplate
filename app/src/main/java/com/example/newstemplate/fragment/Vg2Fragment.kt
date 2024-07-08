package com.example.newstemplate.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newstemplate.ViewPager2Activity
import com.example.newstemplate.databinding.FragmentVg2Binding

private const val ARG_PARAM1 = "param1"
class Vg2Fragment : Fragment() {
    private var param1: String? = null
    // news_content_layout
    private var _binding: FragmentVg2Binding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //private lateinit var viewPager2ActivityWeakReference: WeakReference<ViewPager2Activity>
    private lateinit var viewPager2Activity: ViewPager2Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        Log.d("TAG", "onDestroyView: onCreate $param1")
    }

    fun setText(s:String){
        binding.vg2FragmentTv.text = s
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentVg2Binding.inflate(inflater, container, false)
        binding.vg2FragmentBtn.setOnClickListener {


            Handler(Looper.getMainLooper()).postDelayed({
                //viewPager2ActivityWeakReference.get()?.let {
                //    Log.d("TAG", "onCreateView: runBlocking1")
                //    it.setTitle("ccccccccccccccccccccccc")
                //}


                activity?.let {
                    (it as ViewPager2Activity).setTitle("cccccccddddddddddddddddddddddd")
                    Log.d("TAG", "onCreateView: runBlocking1")
                }

               parentFragment?.let {

               }

                //viewPager2Activity.setTitle("ccccccccccccccccccccccc")
            }, 10000)




        }
        return binding.root
    }

    fun setWeak(context: Context){
        //viewPager2ActivityWeakReference = WeakReference(context as ViewPager2Activity)
        viewPager2Activity = context as ViewPager2Activity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vg2FragmentTv.text = param1
    }

    override fun onDestroyView() {
        Log.d("TAG", "onDestroyView: $param1")
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("TAG", "onDestroyView: onDetach $param1")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, context: ViewPager2Activity) =
            Vg2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
                setWeak(context)
            }
    }
}