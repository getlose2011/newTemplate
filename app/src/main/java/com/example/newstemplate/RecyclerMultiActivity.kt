package com.example.newstemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstemplate.databinding.ActivityRecyclerMultiBinding
import com.example.newstemplate.databinding.ActivityViewPager2Binding

class RecyclerMultiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerMultiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerMultiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var data = getTaskData()

        var adapter  = MainAdapter(data, callback)
        binding.recyclerMultiView.layoutManager = LinearLayoutManager(this)
        binding.recyclerMultiView.adapter = adapter

        binding.recyclerMultiBtn.setOnClickListener {
            val c = binding.recyclerMultiView.childCount
            Log.d("TAG", "onSelectedChanged: $c")
            for (i in 0 until c ){
                adapter.update()
               // val child = binding.recyclerMultiView.getChildAt(i)
              //  val a = binding.recyclerMultiView.getChildViewHolder(child)
              //  a?.let {
             //       (it as MainAdapter.MainItemViewHolder).setText("3333333333")
             //   }
            }
        }
    }

    //all courses activity
    private val callback = object : IMainAdapterListener {

        override fun click(activity: Class<*>) {

        }
    }

    private fun getTaskData(): ArrayList<Tasks> {

        return  ArrayList<Tasks>().apply {
            add(Tasks("字級大小", TxtSizeActivity::class.java))
            add(Tasks("圖片 slider", ImageSliderActivity::class.java))
            add(Tasks("清除暫存資料", DeleteCacheActivity::class.java))
            add(Tasks("drag & drop Recycer View", DragAndDropActivity::class.java))
            add(Tasks("navigator",NavigatorActivity::class.java))
            add(Tasks("home",HomeActivity::class.java))
            add(Tasks("icon font",IconFontActivity::class.java))
            add(Tasks("mp3",MediaActivity::class.java))
            add(Tasks("animation", AnimationActivity::class.java))
            add(Tasks("flash", FlashActivity::class.java))
            add(Tasks("coroutine", CoroutineActivity::class.java))
            add(Tasks("viewpager simple", ViewPagerSimpleActivity::class.java))
            add(Tasks("viewpager 2", ViewPager2Activity::class.java))
            add(Tasks("字級大小", TxtSizeActivity::class.java))
            add(Tasks("圖片 slider", ImageSliderActivity::class.java))
            add(Tasks("清除暫存資料", DeleteCacheActivity::class.java))
            add(Tasks("drag & drop Recycer View", DragAndDropActivity::class.java))
            add(Tasks("navigator",NavigatorActivity::class.java))
            add(Tasks("home",HomeActivity::class.java))
            add(Tasks("icon font",IconFontActivity::class.java))
            add(Tasks("mp3",MediaActivity::class.java))
            add(Tasks("animation", AnimationActivity::class.java))
            add(Tasks("flash", FlashActivity::class.java))
            add(Tasks("coroutine", CoroutineActivity::class.java))
            add(Tasks("viewpager simple", ViewPagerSimpleActivity::class.java))
            add(Tasks("viewpager 2", ViewPager2Activity::class.java))
        }

    }
}