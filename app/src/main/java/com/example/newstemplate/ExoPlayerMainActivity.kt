package com.example.newstemplate

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import com.example.newstemplate.databinding.ActivityExoPlayerMainBinding
import java.util.Timer
import java.util.TimerTask


//https://medium.com/@rawatsumit115/play-videos-by-using-jetpack-media3-exoplayer-in-android-kotlin-c736136de580
//還未完成.................................................................................................
//.......................
class ExoPlayerMainActivity : AppCompatActivity() {

    private val TAG = "ExoPlayerMainActivity_LOG"
    private lateinit var binding: ActivityExoPlayerMainBinding

    private var mediaItems: ArrayList<MediaItem> = arrayListOf()
    private var player: ExoPlayer?= null

    private lateinit var audios: ArrayList<String>
    private lateinit var playerControlView: PlayerControlView
    private var playIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExoPlayerMainBinding.inflate(layoutInflater)
        playerControlView = binding.playerControlView
        setContentView(binding.root)

        audios = arrayListOf(
            "https://img.news.ebc.net.tw/EbcNews/audio/430801.mp3",
            "https://img.news.ebc.net.tw/EbcNews/audio/430800.mp3")

        audios.forEach { url->
            mediaItems.add(MediaItem.fromUri(url))
        }

        initExoPlayer()


        playerControlView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_next).apply {
            playState(PlayStateEnum.NEXT)
        }

        playerControlView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_prev).apply {
            playState(PlayStateEnum.PREVIOUSE)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        player = null
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
                addMediaItems(mediaItems)
                prepare()
                addListener(playerListener)
            }
        playerControlView.player = player
    }

    private fun playState(state:PlayStateEnum){
        when(state){
            PlayStateEnum.PLAY -> {
                player?.playWhenReady = true

                Log.d(TAG,"PLAY")
            }
            PlayStateEnum.PAUSE -> {
                Log.d(TAG,"PAUSE")
            }
            PlayStateEnum.STOP -> {
                Log.d(TAG,"STOP")
            }
            PlayStateEnum.RESTART_PLAYER -> {
                Log.d(TAG,"RESTART_PLAYER")

            }
            PlayStateEnum.RELEASE_PLAYER -> {
                Log.d(TAG,"RELEASE_PLAYER")
            }
            PlayStateEnum.NEXT -> {
                playIndex++
                checkSeekPosition()
                //player?.seekToDefaultPosition(playIndex)
            }
            PlayStateEnum.PREVIOUSE -> {
                playIndex--
                checkSeekPosition()
                //player?.seekToDefaultPosition(playIndex)
            }
        }
    }

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
                    Log.d(TAG,"STATE_ENDED")
                }
                STATE_READY -> {
                    Log.d(TAG,"STATE_READY")
                    playState(PlayStateEnum.PLAY)
                }
            }
        }

        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            //換下一首
            Log.d(TAG,"onTracksChanged")
            playIndex++
        }
    }
}

enum class PlayStateEnum{
    PLAY,PAUSE,STOP,RESTART_PLAYER,RELEASE_PLAYER,NEXT,PREVIOUSE
}