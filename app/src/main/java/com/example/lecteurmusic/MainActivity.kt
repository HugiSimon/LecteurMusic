package com.example.lecteurmusic

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.lecteurmusic.adapter.MusicAdapter
import com.example.lecteurmusic.model.MusicModel

class MainActivity : AppCompatActivity() {
    var mediaPlayer = MediaPlayer()

    var pos = 0
    var repeat = 1
    var shuffle = 0
    var sort = 0

    var musics = arrayOf<MusicModel>()
    var musicsBase = arrayOf<MusicModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createListMusic()
        chooseMusic()
        playPause()
        //stop()
        sort()
        btnNext()
        previous()
        repeat()
        shuffle()
    }
    private fun createListMusic() {
        musics += MusicModel(R.drawable.album_lyfe, "Out thë way", "Yeat", R.raw.out_the_way_yeat)
        musics += MusicModel(R.drawable.album_arizonatears, "Watch This", "Lil Uzi Vert", R.raw.watch_this_lil_uzi_vert)
        musics += MusicModel(R.drawable.album_nudy_land, "Hell Shell", "Young Nudy", R.raw.hell_shell_young_nudy)
        musics += MusicModel(R.drawable.album_rodeo, "90210", "Travis Scott", R.raw.nine_zero_two_one_travis_scott)
        musics += MusicModel(R.drawable.album_dying_to_live, "ZEZE", "Kodak Black", R.raw.zeze_kodak_black)
        musics += MusicModel(R.drawable.album_tell_em, "Tell Em", "Cochise", R.raw.tell_em_cochise)
        musics += MusicModel(R.drawable.album_swimming, "Small Worlds", "Mac Miller", R.raw.small_worlds_mac_miller)
        musics += MusicModel(R.drawable.album_sweet, "I WANNA SEE SOME ASS", "Jack Harlow", R.raw.i_wanna_see_some_ass_jack_harlow)
        musics += MusicModel(R.drawable.album_whole_lotta_red, "Sky", "Playboi Carti", R.raw.sky_playboi_carti)
        musics += MusicModel(R.drawable.album_good_kid, "Money Trees", "Kendrick Lamar", R.raw.money_trees_kendrik_lamar)
        musics += MusicModel(R.drawable.album_trip_at_knight, "Miss the Rage", "Trippie Redd", R.raw.miss_the_rage_trippie_redd)

        musicsBase = musics

        mediaPlayer = MediaPlayer.create(this, R.raw.out_the_way_yeat)

        val mListView = findViewById<ListView>(R.id.lvListSong)
        mListView.adapter = MusicAdapter(this, musics)
    }

    private fun chooseMusic() {
        val mListView = findViewById<ListView>(R.id.lvListSong)
        mListView.setOnItemClickListener { _, _, position, _ ->
            pos = position
            playMusic()
        }
    }

    private fun playPause() {
        val ibtnPlay = findViewById<ImageButton>(R.id.ibtnPlay)
        ibtnPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                ibtnPlay.setImageResource(R.drawable.baseline_play_arrow_24)
            } else {
                mediaPlayer.start()
                ibtnPlay.setImageResource(R.drawable.baseline_pause_24)
            }
        }
    }

    /*private fun stop() {
        val ibtnStop = findViewById<ImageButton>(R.id.ibtnStop)
        ibtnStop.setOnClickListener {
            mediaPlayer.stop()
            val ibtnPlay = findViewById<ImageButton>(R.id.ibtnPlay)
            ibtnPlay.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }*/

    private fun sort() {
        val ibtnSort = findViewById<ImageButton>(R.id.ibtnSort)
        ibtnSort.setOnClickListener {
            sort++
            if (sort > 2) {
                sort = 0
            }
            when (sort) {
                0 -> {
                    musics = musicsBase
                    ibtnSort.background = getDrawable(R.drawable.circle)
                    val Toast = Toast.makeText(applicationContext, "Sort by default", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    musics = musics.sortedBy { it.title }.toTypedArray()
                    ibtnSort.background = getDrawable(R.drawable.circle_active)
                    val Toast = Toast.makeText(applicationContext, "Sort by name", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    musics = musics.sortedBy { it.artist }.toTypedArray()
                    ibtnSort.background = getDrawable(R.drawable.circle_double_active)
                    val Toast = Toast.makeText(applicationContext, "Sort by artist", Toast.LENGTH_SHORT).show()
                }
            }
            val mListView = findViewById<ListView>(R.id.lvListSong)
            mListView.adapter = MusicAdapter(this, musics)
        }

    }

    private fun btnNext() {
        val ibtnNext = findViewById<ImageButton>(R.id.ibtnSkipNext)
        ibtnNext.setOnClickListener{next()}
    }

    private fun next() {
        if (pos == musics.size - 1 && repeat == 0) {
            val ibtnPlay = findViewById<ImageButton>(R.id.ibtnPlay)
            ibtnPlay.setImageResource(R.drawable.baseline_play_arrow_24)
            mediaPlayer.stop()
            return
        } else {
            if (pos < musics.size - 1) {
                pos++
            } else {
                pos = 0
            }
            playMusic()
        }
    }

    private fun previous() {
        val ibtnPrevious = findViewById<ImageButton>(R.id.ibtnSkipPrevious)
        ibtnPrevious.setOnClickListener {
            if (pos == 0) {
                return@setOnClickListener
            } else {
                pos--
            }
            playMusic()
        }
    }

    private fun repeat() {
        val ibtnRepeat = findViewById<ImageButton>(R.id.ibtnRepeat)
        ibtnRepeat.setOnClickListener {
            repeat++
            if (repeat > 2) {
                repeat = 0
            }
            when (repeat) {
                0 -> ibtnRepeat.background = getDrawable(R.drawable.circle)
                1 -> ibtnRepeat.background = getDrawable(R.drawable.circle_active)
                2 -> ibtnRepeat.background = getDrawable(R.drawable.circle_double_active)
            }
        }
    }

    private fun shuffle() {
        val ibtnShuffle = findViewById<ImageButton>(R.id.ibtnShuffle)
        ibtnShuffle.setOnClickListener {
            if (shuffle == 0) {
                shuffle = 1
                ibtnShuffle.background = getDrawable(R.drawable.circle_active)
                var musicsTemp = musicsBase
                musics = arrayOf()
                musics += musicsTemp[pos]
                musicsTemp = removeElement(musicsTemp, pos)
                pos = 0
                while (musicsTemp.isNotEmpty()) {
                    val random = (0 until musicsTemp.size).random()
                    musics += musicsTemp[random]
                    musicsTemp = removeElement(musicsTemp, random)
                }
            } else {
                shuffle = 0
                ibtnShuffle.background = getDrawable(R.drawable.circle)
                musics = musicsBase
            }
            val mListView = findViewById<ListView>(R.id.lvListSong)
            mListView.adapter = MusicAdapter(this, musics)
        }
    }

    private fun playMusic() {
        mediaPlayer.stop()
        val music = musics[pos]
        mediaPlayer = MediaPlayer.create(this, music.song)

        mediaPlayer.setOnCompletionListener {
            if (repeat == 2) {
                playMusic()
            } else {
                next()
            }

        }

        mediaPlayer.start()

        val ivAlbum = findViewById<ImageView>(R.id.imageView)
        ivAlbum.setImageResource(music.album)

        val tvTitre = findViewById<TextView>(R.id.textView)
        tvTitre.text = music.title + " - " + music.artist

        val ibtnPlay = findViewById<ImageButton>(R.id.ibtnPlay)
        ibtnPlay.setImageResource(R.drawable.baseline_pause_24)
    }

    private fun removeElement(arr: Array<MusicModel>, index: Int): Array<MusicModel> {
        val list = arr.toMutableList()
        list.removeAt(index)
        return list.toTypedArray()
    }

}