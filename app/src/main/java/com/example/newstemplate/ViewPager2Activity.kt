package com.example.newstemplate

import android.gesture.GestureOverlayView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.newstemplate.databinding.ActivityViewPager2Binding
import com.example.newstemplate.databinding.ViewPagerContainBinding
import com.example.newstemplate.fragment.Vg2Fragment
import com.example.newstemplate.libraries.Generic
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.Timer
import java.util.TimerTask
import kotlin.math.log


interface  IViewPager2ActivityListener{
    fun click(title:String)
}


class ViewPager2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPager2Binding
    private lateinit var adapter: vgFragmentStateAdapter
    private lateinit var listener: IViewPager2ActivityListener
    private var viewpager2: ViewPager2? = null
    private lateinit var weak:WeakReference<ViewPager2Activity>
    private var count = 0

   // https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/main/java/androidx/viewpager2/integration/testapp/CardViewActivity.kt
  //  https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/main/java/androidx/viewpager2/integration/testapp/cards/CardViewAdapter.kt
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPager2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        weak = WeakReference(this)

        viewpager2 = binding.viewpager2

        listener = object :  IViewPager2ActivityListener{
            override fun click(title: String) {
                binding.viewpager2Btn.text = title
            }

        }

        //adapter = viewpagerAdapter()//NewsContentLayoutViewPagerAdapter(this)
        adapter = vgFragmentStateAdapter(this,this)

        viewpager2?.apply {
            offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            //orientation = ViewPager2.ORIENTATION_VERTICAL
            isUserInputEnabled = false
        }

        //binding.viewpager2.adapter = vgAdapter()//adapter
        viewpager2?.adapter = adapter//vgFragmentStateAdapter(this)//adapter



        //viewpager2?.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
        //    override fun onPageSelected(position: Int) {
        //        super.onPageSelected(position)
        //        Log.d("TAG", "onPageSelected: $position")
        //    }
       // })





        binding.viewpager2Btn.setOnClickListener {
            //adapter.getItem(binding.viewpager2.currentItem)?.let {
              //  it.setText("dddd")
            //}

            //Generic.requestCallback(resultCallback)

          //  Handler(Looper.getMainLooper()).postDelayed({
              //  val r = WeakReference(this).get()
             //   r?.let {
             //       r.binding.viewpager2Btn.text = ""+count
            //    }
                //testPost()

           // }, 10000)

              Handler(Looper.getMainLooper()).postDelayed({
                  binding.viewpager2Btn.text = ""+count

             }, 10000)
            count++

        }

        viewpager2?.registerOnPageChangeCallback(vListener)

        viewpager2?.setCurrentItem(3,false)

        binding.touchOverlay.addOnGestureListener(object :
            GestureOverlayView.OnGestureListener {
            override fun onGestureStarted(overlay: GestureOverlayView?, event: MotionEvent?) {
                handleOnTouchEvent(event)
            }

            override fun onGesture(overlay: GestureOverlayView?, event: MotionEvent?) {
                handleOnTouchEvent(event)
            }

            override fun onGestureEnded(overlay: GestureOverlayView?, event: MotionEvent?) {
                handleOnTouchEvent(event)
            }

            override fun onGestureCancelled(overlay: GestureOverlayView?, event: MotionEvent?) {
                handleOnTouchEvent(event)
            }

        })
    }

    val callbackReference1 = WeakReference(this@ViewPager2Activity)

    private fun testPost() {
        Log.d("TAG", "testPost: ")
        binding.viewpager2Btn.post {
            Log.d("TAG", "testPost: s")
            val callbackReference = WeakReference(this@ViewPager2Activity).get()
            Log.d("TAG", "testPost: $callbackReference")
            Log.d("TAG", "testPost: 1 ${callbackReference1.get()}")
            callbackReference?.let {
                it.binding.viewpager2Btn.text = "333333333 post"
            }
        }
    }

    enum class SwipeDirection {
        ALL, LEFT, RIGHT, NONE
    }

    var direction = SwipeDirection.ALL
    var eventX = 0f
    var eventY = 0f

    private fun handleOnTouchEvent(event: MotionEvent?): Boolean {
        if (direction == SwipeDirection.NONE) //disable any swipe
            return false
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("TAG", "handleOnTouchEvent: ACTION_DOWN ${event.x}")

                eventX = event.x
                eventY = event.y

                if (!binding.viewpager2.isFakeDragging) {
                    binding.viewpager2.beginFakeDrag()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y

                val deltaX = x - eventX
                val deltaY = y - eventY

                if((deltaX < -30f || deltaX > 30f) && (deltaY < 30f && deltaY > -30f )){
                    binding.viewpager2.fakeDragBy(deltaX)
                    eventX = x
                    eventY = y
                    Log.d("TAG", "handleOnTouchEvent: $x $y")
                    return true
                }

                //val delta = value - lastValue
                //Log.d("TAG", "handleOnTouchEvent: ACTION_MOVE ${valueX} ${valueY}")
                return false


            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                Log.d("TAG", "handleOnTouchEvent: ACTION_CANCEL ${event.x}")
                binding.viewpager2.endFakeDrag()
                eventX = 0f
                eventY = 0f
            }
        }
        return true
    }

    private val vListener = object:ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            Log.d("TAG", "onPageSelected: $position")
            //super.onPageSelected(position)
           // GlobalScope.launch {
            /*
            lifecycleScope.launch {
                delay(10000)
                weak.get()?.let {
                    Log.d("TAG", "onPageSelected: $position")
               }
            }*/

        }
    }

    override fun onDestroy() {
        viewpager2?.unregisterOnPageChangeCallback(vListener)
        viewpager2?.adapter = null
        super.onDestroy()
        resultCallback = null
        Log.d("TAG", "onDestroy: runBlocking")
        binding.viewpager2Btn.setOnClickListener(null)
    }

    private var resultCallback: ((isSuccess: Boolean, errMessage: String?) -> Unit)? = {b:Boolean,e:String?->

        update(b,e)
    }

    private fun update(b: Boolean, e: String?) {



        val callbackReference = WeakReference(resultCallback).get()

        Log.d("TAG", "update1: $callbackReference")

        callbackReference?.let {
            Log.d("TAG", "update1: WeakReference have")
            lifecycleScope.launch {

                Log.d("TAG", "update1: lifecycleScope")

                binding.viewpager2Btn.text = e
                val a = viewpager2
                Log.d("TAG", "update1: $a")
            }
            //Log.d("TAG", "update1: $callback")
        }
    }

    fun setTitle(s:String){
        binding.viewpager2Btn.text = s
    }

}

class vgAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = ViewPagerContainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Viewpager2ViewHolder(binding)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        Log.d("TAG", "onViewDetachedFromWindow: ")
        super.onViewDetachedFromWindow(holder)

    }

    override fun getItemCount(): Int {
        return 3000
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: $position")
        (holder as Viewpager2ViewHolder).setTxt(""+position)
    }

    private inner class Viewpager2ViewHolder(itemView: ViewPagerContainBinding):RecyclerView.ViewHolder(itemView.root){
        private var tv: TextView = itemView.viewPagerCotainTv1

        open fun setTxt(t:String){
            tv.text = t
        }

    }

}

class vgFragmentStateAdapter(context: AppCompatActivity,val context1: ViewPager2Activity)
    : FragmentStateAdapter(context){

    private val fragmentMap = mutableMapOf<Int, WeakReference<Vg2Fragment>>()

    fun getItem(position: Int): Vg2Fragment? = fragmentMap[position]?.get()

    override fun getItemCount(): Int {
        return 300
    }



    override fun createFragment(position: Int): Fragment {

        val fragment =  Vg2Fragment.newInstance(""+position, context1)

        fragmentMap[position] = WeakReference(fragment)

        return fragment

    }

}

