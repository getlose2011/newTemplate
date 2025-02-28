package com.example.newstemplate


import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newstemplate.databinding.ActivityDragAndDropBinding
import com.example.newstemplate.databinding.RowDragItemBinding
import com.example.newstemplate.libraries.Generic


//https://stackoverflow.com/questions/51201482/android-percent-screen-width-in-recyclerview-item
//http://www.devexchanges.info/2016/06/android-tip-auto-column-grid-layout-by.html?fbclid=IwAR1y9uPoGdOkilWZ9Rko9_hkVIFq_rMtTUHG_q546bMaZLoIKiFcX4eGC5M
class DragAndDropActivity : AppCompatActivity() {

    private val TAG = "DragAndDropActivity"
    private lateinit var binding: ActivityDragAndDropBinding
    //是否按了編輯按鈕,isEdit = true正在編輯
    private var isEdit = false
    private lateinit var data: ArrayList<Category>
    private lateinit var dAdapter: DragAndDropAdapter
    private lateinit var dragEditBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDragAndDropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dragEditBtn = binding.dragEditBtn.apply {
            setOnClickListener {
                checkDragFun()
            }
        }

        data = getData()

       dAdapter = DragAndDropAdapter(data)

        binding.dragRecyclerView.apply {
            adapter = dAdapter
            dAdapter.setColumNum(getColumNum())
        }

        Generic.setViewConnectAdapterOrNotify(binding.dragRecyclerView,dAdapter)

    }

    //編輯則套用attachToRecyclerView，完成則取消attachToRecyclerView
    private fun checkDragFun() {
        isEdit = !isEdit

        dragEditBtn.text = if(isEdit) "完成" else "編輯"

        if(isEdit) {
            itemTouchHelper.attachToRecyclerView(binding.dragRecyclerView)
        }else{
            itemTouchHelper.attachToRecyclerView(null)
        }

        dAdapter.setDrag(isEdit)

        dAdapter.getData().forEach {
            Log.d(TAG, "checkDragFun: ${it.cn}")
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
        ,0){

        val CHECK_FROM_POS = -1

        var s = CHECK_FROM_POS
        var e = 0
        var isDrag = false

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {

            if(s == CHECK_FROM_POS) s = viewHolder.adapterPosition
            e = target.adapterPosition

            val f = viewHolder.adapterPosition
            val t = target.adapterPosition

            //有些分類固定不能動
            if(!data[e].move)return false

            dAdapter.itemMove(f,t)

            return true
        }

        //onSelectedChanged
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            when (actionState) {
                ItemTouchHelper.ACTION_STATE_DRAG -> {
                    //正在drag
                    isDrag = true
                }
                ItemTouchHelper.ACTION_STATE_SWIPE ->{

                }
                ItemTouchHelper.ACTION_STATE_IDLE -> {
                    //drag 結束
                    if(s != CHECK_FROM_POS){
                        dAdapter.changeSort(s,e)
                    }
                    isDrag = false
                    s = CHECK_FROM_POS
                }
            }
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {

            val p = viewHolder.adapterPosition
            //有些分類固定不能動
            if(!data[p].move)return makeMovementFlags(0,0)

            return super.getMovementFlags(recyclerView, viewHolder)
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }
    })

    private fun getData(): ArrayList<Category> {
        return ArrayList<Category>().apply {
            add(Category( 0,"即時",false))
            add(Category(1,"熱門",false))
            for(i in 2..20){
                add(Category(i,"政治$i"))
            }
        }
    }
}

open class DragAndDropAdapter(private var data: ArrayList<Category>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "DragAndDropAdapter"
    private var checkEdit = false
    private var sCount = 0

    fun getData(): ArrayList<Category>{
        return data
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //取得recycler view width
        val recyclerViewWidth = (parent as RecyclerView).width

        val binding = RowDragItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        //方法1
        //設定文字textview寬度
        //binding.rowDrogCategoryNameTxt.width = recyclerViewWidth / sCount

        //方法2
        // get RelativeLayout
        val relativeLayout: RelativeLayout = binding.rowDrogRelativeLayout
        // get LayoutParams,設置寬
        val layoutParams = relativeLayout.layoutParams.apply {
            width = (recyclerViewWidth - (4*30)) / sCount
        }
        //重新設定RelativeLayout width
        relativeLayout.layoutParams = layoutParams

        return DragAndDropItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = data[position]
        (holder as DragAndDropItemViewHolder).setText(category.cn,category.move)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    fun itemMove(f: Int, t: Int) {

        // 使用 Collections.swap 方法来交换列表中的元素位置
        //Collections.swap(data, f, t)

        // 使用 notifyItemMoved 方法来通知适配器中的数据项已经在RecyclerView中移动
        this.notifyItemMoved(f, t)

    }

    /**
     * 通知recycler view,判斷要不要出現圖片
     * */
    @SuppressLint("NotifyDataSetChanged")
    fun setDrag(edit: Boolean) {
        checkEdit = edit
        this.notifyDataSetChanged()
    }

    //判斷data順序要不要改變
    fun changeSort(f:Int,t:Int) {

        //Log.d(TAG, "changeSort: $f $t")
        
        //移到目的地是不能移動的項目或則相同位置,則不動作
        if(!data[t].move || f==t){
            return
        }

        //查詢所拖曳的sort值並且給新的sort值
        data[f].sort = t

        //f,t之間的data要重新排sort
        //9->11
        if (f < t){

            for (i in f+1 .. t){
                data[i].sort--
            }
        }else if(f > t){

            //11->9
            for (i in t until  f){
                data[i].sort++
            }
        }

        //重新排序
        data.sortBy { it.sort }
    }

    //設定一列有幾個
    fun setColumNum(columNum: Int) {
        sCount = columNum
    }

    inner class DragAndDropItemViewHolder(viewBinding : RowDragItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val TAG = "DragAndDropItemViewHolder"
        private var txt: TextView = viewBinding.rowDrogCategoryNameTxt
        private var iv: ImageView = viewBinding.rowDrogIv

        @SuppressLint("UseCompatLoadingForDrawables")
        fun setText(text:String,move: Boolean){
            txt.text = text
            iv.visibility = View.GONE
            if(move && checkEdit){
                iv.visibility = View.VISIBLE
            }

        }

    }

}

data class Category(var sort:Int, val cn:String, val move: Boolean = true)