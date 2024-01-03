package com.example.newstemplate.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.example.newstemplate.DeleteCacheActivity
import com.example.newstemplate.DragAndDropActivity
import com.example.newstemplate.ImageSliderActivity
import com.example.newstemplate.ImageSliderObj
import com.example.newstemplate.MainActivity
import com.example.newstemplate.NavigatorActivity
import com.example.newstemplate.R
import com.example.newstemplate.Tasks
import com.example.newstemplate.TxtSizeActivity
import com.example.newstemplate.databinding.FragmentHomeBinding
import com.example.newstemplate.databinding.FragmentHomeListBinding
import com.example.newstemplate.databinding.NewsLayoutBinding
import com.example.newstemplate.libraries.Generic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeListFragment : Fragment() {
    //layout binding
    private val TAG = "HomeListFragment"
    private var _binding: FragmentHomeListBinding? = null
    private lateinit var imageSlider: SliderLayout
    private lateinit var imageSliderIndicatorLinearLayout: LinearLayoutCompat
    //螢幕寬
    private val w by lazy {
        Generic.widthPx(requireActivity())
    }
    private var param1: String? = null
    //image slider 預設首圖位置
    private var currentIndicatorPosition = 0
    //image slider 少於幾張圖,要處理的動作
    private var imageSlidrLessNum = 2

    private var data: ArrayList<Tasks> = ArrayList()
    private lateinit var mainAdapter2: HomeAdapter
    var isLoading = false
    var show = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeListBinding.inflate(inflater, container, false)
        //SliderLayout高度
        imageSlider = _binding?.imageSlider!!.apply {
            layoutParams.height = w / 16 * 9
        }
        imageSliderIndicatorLinearLayout = _binding?.imageSliderIndicatorLinearLayout!!
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //val animated = AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.play)
        //animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
        //     override fun onAnimationEnd(drawable: Drawable?) {
        //         animated.start()
        //     }

        // })
        //  _binding?.homeImage!!.setImageDrawable(animated)
        // animated?.start()

        data = getTaskData()

        var adapter1  = HomeAdapter(getTaskData(), callback)
        mainAdapter2  = HomeAdapter(data, callback)

        _binding?.homeRecyclerView1?.layoutManager = LinearLayoutManager(requireContext())
        _binding?.homeRecyclerView2?.layoutManager = LinearLayoutManager(requireContext())

        _binding?.homeRecyclerView1?.adapter = adapter1
        _binding?.homeRecyclerView2?.adapter = mainAdapter2


        _binding?.homeNestedScrollView!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->

            //底部
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    //code to fetch more data for endless scrolling
                    Log.d("TAG", "onViewCreated: trigger")
                    if (!isLoading) {
                        loadMore()
                    }
                }
            }
        })

        //_binding?.homeNestedScrollView!!.viewTreeObserver.addOnScrollChangedListener(ddd)

        //  binding.homeNestedScrollView.viewTreeObserver.addOnScrollChangedListener(ddd)
    }

    private val ddd = ViewTreeObserver.OnScrollChangedListener {
        val scrollBonuds = Rect()

        _binding?.homeNestedScrollView!!.getHitRect(scrollBonuds)
        if (imageSlider.getLocalVisibleRect(scrollBonuds)) {
            Log.d(TAG, "homeNestedScrollView: viewTreeObserver => v")
            if (!show) {
                imageSlider.startAutoCycle()
                show = true
            }
        } else {
            Log.d(TAG, "homeNestedScrollView: viewTreeObserver => not v")
            if (show) {
                imageSlider.stopAutoCycle()
                show = false
            }
        }
    }

    private fun loadMore() {
        isLoading = true
        data.add(Tasks("",null))

        mainAdapter2.notifyItemInserted(data.lastIndex)

        Handler().postDelayed({
            data.removeAt(data.lastIndex)

            val scrollPosition = data.size
            mainAdapter2.notifyItemRemoved(scrollPosition)


            //(0 until  10).forEach {
            data.add(Tasks("ww", MainActivity::class.java))
            data.add(Tasks("444", MainActivity::class.java))
            data.add(Tasks("55", MainActivity::class.java))
            data.add(Tasks("66", MainActivity::class.java))
            data.add(Tasks("llllllll",null))
            data.add(Tasks("333333", MainActivity::class.java))
            data.add(Tasks("444", MainActivity::class.java))
            data.add(Tasks("55", MainActivity::class.java))
            data.add(Tasks("66", MainActivity::class.java))
            data.add(Tasks("33337733", MainActivity::class.java))
            //  }

            mainAdapter2.notifyDataSetChanged()

            isLoading = false
        }, 1500)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getSliderData()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val callback = object : IHomeAdapterListener {

        override fun click(activity: Class<*>) {
            Toast.makeText(requireContext(),activity::class.simpleName.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun getSliderData() {
        //取得資料
        GlobalScope.launch{
            println("World!1 + ${Thread.currentThread().name}")

            var data = getData()
            println("World!2 + ${Thread.currentThread().name}")

            withContext(Dispatchers.Main) {
                println("World!3 + ${Thread.currentThread().name}")
                addIndicator(imageSliderIndicatorLinearLayout,data.count())
                data.forEach { imageSliderObj ->
                    //slider object
                    TextSliderView(requireContext()).apply {
                        //文字
                        description(imageSliderObj.title)
                        //圖片的比例類型。
                        scaleType = BaseSliderView.ScaleType.Fit
                        //點選圖片事件
                        setOnSliderClickListener {
                            Toast.makeText(
                                requireContext(), imageSliderObj.title,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        //圖片及預設圖片
                        image(imageSliderObj.imageUrl).empty(R.mipmap.image_default)
                    }.also {
                        //加入到 SliderLayout
                        imageSlider.addSlider(it)
                    }
                }

                //SliderLayout增加 indicator layout
                //imageSlider.setCustomIndicator(imageSliderIndicator)

                imageSlider.apply {
                    //開始的圖片位置
                    setCurrentPosition(currentIndicatorPosition, true)
                    //delay => 第一次執行滑動時間
                    //duration => 每次滑動的時間
                    //autoRecover => false 使用者手指停留在該圖片時，則會停留在該圖片，不會繼續在滑動
                    startAutoCycle(3000L, 3000L, true)
                }

                //只有一張圖則不輪播
                if(data.count() < imageSlidrLessNum)imageSlider.stopAutoCycle()

                imageSlider.addOnPageChangeListener(object: ViewPagerEx.OnPageChangeListener{
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        //TODO("Not yet implemented")
                    }

                    override fun onPageSelected(position: Int) {
                        Log.d("TAG", "onPageSelected: $position")
                        changeIndicator(position,currentIndicatorPosition)
                        currentIndicatorPosition = position
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        //TODO("Not yet implemented")
                    }

                })

            }
        }
    }

    /**
     *
     * 改變image slider indicator 圖示
     * */
    private fun changeIndicator(currentIndicate:Int,beforeIndicate:Int = -1){
        imageSliderIndicatorLinearLayout.getChildAt(currentIndicate).setBackgroundResource(R.drawable.selected)
        if(beforeIndicate != -1)imageSliderIndicatorLinearLayout.getChildAt(beforeIndicate).setBackgroundResource(R.drawable.unselected)
    }

    private fun addIndicator(imageSliderIndicatorLinearLayout: LinearLayoutCompat, count: Int) {

        if(count < imageSlidrLessNum)imageSliderIndicatorLinearLayout.visibility = View.GONE

        val indicateWidth = (w - 350)/count

        (0 until count).forEach { i ->
            var image = ImageView(requireContext())
            image.setBackgroundResource(R.drawable.unselected)
            //如果要加下面那行，比重一樣所以寬有設跟沒設一樣，imageSliderIndicatorLinearLayout 會滿版
            //layoutParams.weight = 1f
            // Set layout parameters
            val layoutParams = LinearLayoutCompat.LayoutParams(
                indicateWidth,
                8
            )

            //第一個左邊不設left margin
            if(i != 0)layoutParams.leftMargin = 10

            //a.方法
            //image.layoutParams = layoutParams
            //imageSliderIndicatorLinearLayout.addView(image)
            //b.方法
            imageSliderIndicatorLinearLayout.addView(image,layoutParams)

        }

        //設定 image slider indicator
        changeIndicator(currentIndicatorPosition)

    }

    private suspend fun getData(): ArrayList<ImageSliderObj> {

        return  withContext(Dispatchers.IO) {

            delay(2000)

            ArrayList<ImageSliderObj>().apply {

                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698748147_66456.jpg",
                        "高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個高山峰親曝給兒子吃這個 成長突飛猛進1"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747935_57304.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進2"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747945_53957.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進3"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747955_14884.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進4"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698748147_66456.jpg",
                        "高山峰親曝給兒子吃這個高山峰親曝給兒5"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747935_57304.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進6"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747945_53957.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進7"
                    )
                )
                add(
                    ImageSliderObj(
                        "https://img.news.ebc.net.tw/EbcNews/news/2023/10/31/1698747955_14884.jpg",
                        "高山峰親曝給兒子吃這個 成長突飛猛進8"
                    )
                )

            }
        }
    }

    private fun getTaskData(): ArrayList<Tasks> {

        return  ArrayList<Tasks>().apply {
            add(Tasks("hhh", TxtSizeActivity::class.java))
            add(Tasks("圖片 slider", ImageSliderActivity::class.java))
            add(Tasks("清除暫存資料", DeleteCacheActivity::class.java))
            add(Tasks("drag & drop Recycer View", DragAndDropActivity::class.java))
            add(Tasks("navigator", NavigatorActivity::class.java))
            add(Tasks("1", TxtSizeActivity::class.java))
            add(Tasks("圖片 slider", ImageSliderActivity::class.java))
            add(Tasks("清除暫存資料", DeleteCacheActivity::class.java))
            add(Tasks("drag & drop Recycer View", DragAndDropActivity::class.java))
            add(Tasks("navigator", NavigatorActivity::class.java))
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            HomeListFragment().apply {
                arguments = Bundle().apply {
                    //  putString(ARG_PARAM1, param1)
                }
            }
    }

}

class HomeAdapter(val tasks: List<Tasks>, private val listener:IHomeAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1
    val TAG = "MainAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val binding = NewsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return HomeItemViewHolder(binding)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_dialog, parent, false)
            return LoadingViewHolder(view)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = tasks[position]

        if(holder is HomeItemViewHolder){
            holder.setText(task.title)
            holder.changeColor()
            holder.itemView.setOnClickListener {
                listener.click(task.activity!!)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (tasks[position].title == "") {
            return VIEW_TYPE_LOADING

        }
        return VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return tasks.count()
    }

    inner class HomeItemViewHolder(viewBinding : NewsLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val TAG = "HomeItemViewHolder"
        private var textView: TextView = viewBinding.itemNewsTitleTv
        private var imageView: ImageView = viewBinding.itemNewsIv

        fun setText(text:String){
            textView.text = text
        }

        fun changeColor(){
            imageView.setColorFilter(Color.parseColor("#99000000"))
        }
    }
    private inner class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}