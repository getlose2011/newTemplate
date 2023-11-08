package com.example.newstemplate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
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

        clickActivity(DragAndDropActivity::class.java)

        //deleteCache(this)
    }

    private fun getTaskData(): ArrayList<Tasks> {

        return  ArrayList<Tasks>().apply {
            add(Tasks("字級大小", TxtSizeActivity::class.java))
            add(Tasks("圖片 slider", ImageSliderActivity::class.java))
            add(Tasks("清除暫存資料", DeleteCacheActivity::class.java))
            add(Tasks("drag & drop Recycer View", DragAndDropActivity::class.java))
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

class MainAdapter(private val tasks: List<Tasks>,private val listener:IMainAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "MainAdapter"
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RowMainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MainItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = tasks[position]
        (holder as MainItemViewHolder).setText(task.title)
        (holder as MainItemViewHolder).itemView.setOnClickListener {
            listener.click(task.activity)
        }
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
    
}

data class Tasks(val title:String,val activity:Class<*>)


