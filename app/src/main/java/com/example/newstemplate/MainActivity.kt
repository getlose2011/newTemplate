package com.example.newstemplate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newstemplate.databinding.ActivityMainBinding
import com.example.newstemplate.databinding.RowMainItemBinding

interface IMainAdapterListener{
    fun click(activity: Class<*>)
}

fun deleteCache(context: Context){
    context.cacheDir.deleteRecursively()
}

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        var data = getTaskData()
        
        var adapter  = MainAdapter(data, callback)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.adapter = adapter

        //clickActivity(CustomSelectorActivity::class.java)
        clickActivity(FlashActivity::class.java)

        //deleteCache(this)

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
        }

    }

    //all courses activity
    private val callback = object : IMainAdapterListener {

        override fun click(activity: Class<*>) {
            clickActivity(activity)
        }
    }

    private fun clickActivity(activity: Class<*>){
        Intent(this@MainActivity, activity).also {
            startActivity(it)
        }
    }
}

class MainAdapter(val tasks: List<Tasks>,private val listener:IMainAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1
    val TAG = "MainAdapter"
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            val binding = RowMainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return MainItemViewHolder(binding)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_dialog, parent, false)
            return LoadingViewHolder(view)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = tasks[position]

        if (tasks[position].title.isNotEmpty()) {
            (holder as MainItemViewHolder).setText(task.title)
            (holder as MainItemViewHolder).itemView.setOnClickListener {
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

    inner class MainItemViewHolder(viewBinding : RowMainItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val TAG = "MainItemViewHolder"
        private var textView: TextView = viewBinding.rowMainTxt

        fun setText(text:String){
            textView.text = text
        }
    }
    private inner class LoadingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
}

data class Tasks(val title:String,val activity:Class<*>? = null)


