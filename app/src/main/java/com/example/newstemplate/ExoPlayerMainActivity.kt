package com.example.newstemplate


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import com.example.newstemplate.Receiver.ExoPlayerReceiver
import com.example.newstemplate.databinding.ActivityExoPlayerMainBinding
import com.example.newstemplate.service.ExoPlayerApi
import com.example.newstemplate.service.ExoPlayerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//參考
//https://medium.com/@rawatsumit115/play-videos-by-using-jetpack-media3-exoplayer-in-android-kotlin-c736136de580
class ExoPlayerMainActivity : AppCompatActivity() {

    private val TAG = "ExoPlayerMainActivity_LOG"
    private lateinit var binding: ActivityExoPlayerMainBinding
    private lateinit var currentTv : TextView

    private var mediaItems: ArrayList<MediaItem> = arrayListOf()
    private var player: ExoPlayer?= null
    private lateinit var playerControlView: PlayerControlView
    //目前播放的位置(例如有十首音源檔，如果是2則是播放第3首音源檔)
    private var playIndex = 0
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExoPlayerMainBinding.inflate(layoutInflater)
        playerControlView = binding.playerControlView
        setContentView(binding.root)

        getData()

        initExoPlayer()

        currentTv = playerControlView.findViewById(R.id.exo_current)

        playerControlView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_play).setOnClickListener {
            playState(PlayStateEnum.PLAY)
        }

        playerControlView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_pause).setOnClickListener {
            playState(PlayStateEnum.PAUSE)
        }

        playerControlView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_next).setOnClickListener {
            playState(PlayStateEnum.NEXT)
        }

        playerControlView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_prev).setOnClickListener {
            playState(PlayStateEnum.PREVIOUSE)
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ExoPlayerReceiver.ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        stopHandler()
        playState(PlayStateEnum.RELEASE_PLAYER)
        super.onDestroy()
        player = null
    }

    private fun getData(){
        //取得資料
        lifecycleScope.launch(Dispatchers.Default){
            var result = ExoPlayerApi().getData()

            if(result.success){
                result.data?.forEach { e->
                    mediaItems.add(MediaItem.fromUri(e.audio))
                }
            }

            withContext(Dispatchers.Main) {
                if(mediaItems.isNotEmpty()){
                    // 開始更新當前播放時間
                    //startUpdatingCurrentTime()
                    //add player list
                    player?.addMediaItems(mediaItems)

                    Intent(this@ExoPlayerMainActivity, ExoPlayerService::class.java).apply {
                        ContextCompat.startForegroundService(this@ExoPlayerMainActivity,this)
                    }

                }
            }
        }
    }

    /**
     * 取得目前播放秒數
     * */
    private fun startUpdatingCurrentTime() {
        handler.post(object : Runnable {
            override fun run() {
                player?.let{
                    val c = formatTime(it.currentPosition)
                    currentTv.text = c

                    Intent(this@ExoPlayerMainActivity, ExoPlayerService::class.java).apply {
                        action = "CURRENT_POSITION"
                        putExtra("data", c)
                    }.also {intent->
                        startService(intent)
                    }

                }
                handler.postDelayed(this, 1000) // 每秒更新一次
            }
        })
    }

    private fun stopHandler() {
        handler.removeCallbacksAndMessages(null) // 停止更新
    }

    /**
     * 分:秒
     * */
    private fun formatTime(milliseconds: Long): String {
        val totalSeconds = (milliseconds / 1000).toInt()
        val minutes = ((totalSeconds % 3600) / 60).toString().padStart(2, '0')
        val seconds = (totalSeconds % 60).toString().padStart(2, '0')
        return "$minutes:$seconds"
    }

    @OptIn(UnstableApi::class) private fun initExoPlayer() {

        player = ExoPlayer.Builder(this)
            .build()
            .apply {

                // val source = if (mediaUrl.contains("m3u8"))
                //     getHlsMediaSource()
                // else
                //      getProgressiveMediaSource()
                //   setMediaSource(source)
                //addMediaItems(mediaItems)
                prepare()
                addListener(playerListener)
            }.also {
                playerControlView.player = it
            }
    }

    /**
     * 播放狀態
     * */
    private fun playState(state:PlayStateEnum){
        when(state){
            PlayStateEnum.PLAY -> {
                startUpdatingCurrentTime()
                player?.playWhenReady = true
                Log.d(TAG,"PLAY")
            }
            PlayStateEnum.PAUSE -> {
                player?.playWhenReady = false
                stopHandler()
                Log.d(TAG,"PAUSE")
            }
            PlayStateEnum.RESTART_PLAYER -> {
                player?.seekTo(0)
                player?.playWhenReady = true
                Log.d(TAG,"RESTART_PLAYER")

            }
            PlayStateEnum.RELEASE_PLAYER -> {
                player?.apply {
                    playWhenReady = false
                    release()
                }
                player = null
                Log.d(TAG,"RELEASE_PLAYER")
            }
            PlayStateEnum.NEXT -> {
                checkSeekPosition()
                playIndex++
                //player?.seekToDefaultPosition(playIndex)
            }
            PlayStateEnum.PREVIOUSE -> {
                playIndex--
                checkSeekPosition()
                //player?.seekToDefaultPosition(playIndex)
            }
        }
    }

    /**
     * 選擇播放第幾首
     * */
    private fun checkSeekPosition(){
        val c = mediaItems.count()
        if(playIndex < 0)playIndex = 0
        if(playIndex >= c)playIndex = c
        player?.seekToDefaultPosition(playIndex)
    }

    private val playerListener = object: Player.Listener{
        override fun onPlaybackStateChanged(@Player.State playbackState: Int) {
            when(playbackState){
                STATE_ENDED -> {
                    //全部播完
                    stopHandler()
                    Log.d(TAG,"STATE_ENDED")
                }
                STATE_READY -> {
                    //player 準備完成
                    Log.d(TAG,"STATE_READY")
                    playState(PlayStateEnum.PLAY)
                }
            }
        }

        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            //換下一首
            Log.d(TAG,"onTracksChanged")
            //playState(PlayStateEnum.NEXT)
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ExoPlayerReceiver.ACTION == intent.action) {
                playState(PlayStateEnum.NEXT)
            }
        }
    }
}

enum class PlayStateEnum{
    PLAY,PAUSE,RESTART_PLAYER,RELEASE_PLAYER,NEXT,PREVIOUSE
}