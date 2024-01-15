package com.example.newstemplate.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.newstemplate.BaseFragment
import com.example.newstemplate.R
import com.example.newstemplate.databinding.FragmentViewPagerSimpleListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPagerSimpleListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewPagerSimpleListFragment : BaseFragment() {

    private val TAG = "ViewPagerSimpleFragment"
    // TODO: Rename and change types of parameters
    private var job: Job? = null
    private var _binding: FragmentViewPagerSimpleListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var txt : String? = null
    private lateinit var categoryTxt: TextView
    private lateinit var categoryName: String
    private lateinit var processBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = "${it.getString(ARG_PARAM1, "")}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPagerSimpleListBinding.inflate(inflater, container, false)
        categoryTxt = binding.viewPagerListTxt
        processBar = binding.inBaseProgressbarOverlay.baseLoadingProgressBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryTxt.text = categoryName

        if(txt != null){
            setData()
        }else{
            getData()
        }
    }

    override fun onStop() {

        job?.cancel()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        processBar.visibility = View.VISIBLE
        //取得資料
        job = GlobalScope.launch(Dispatchers.Main) {
            //main 1
            processBar.visibility = View.VISIBLE
            withContext(Dispatchers.IO){
                // not ui thread 2
                var data = getDataFromApi()
                if(data.isNotEmpty()){
                    txt = data
                }
            }
            //main 3
            processBar.visibility = View.GONE
            //slider image
            setData()

        }
    }

    private fun setData() {
        categoryTxt.text = txt
    }

    private suspend fun getDataFromApi(): String {
        return withContext(Dispatchers.IO) {
            delay(4000)
            "123456"
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment ViewPagerSimpleListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            ViewPagerSimpleListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}