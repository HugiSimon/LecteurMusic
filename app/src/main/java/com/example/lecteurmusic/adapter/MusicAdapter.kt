package com.example.lecteurmusic.adapter

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.lecteurmusic.R
import com.example.lecteurmusic.model.MusicModel

class MusicAdapter(var contex: Context, var musics: Array<MusicModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return musics.size
    }

    override fun getItem(position: Int): Any {
        return musics[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater = contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.list_music, parent, false)

        val ivAlbum = rowView.findViewById<ImageView>(R.id.ivAlbum)
        val tvTitle = rowView.findViewById<TextView>(R.id.tvTitre)
        val tvArtist = rowView.findViewById<TextView>(R.id.tvArtiste)
        val tvDuration = rowView.findViewById<TextView>(R.id.tvDuree)

        val music = getItem(position) as MusicModel

        ivAlbum.setImageResource(music.album)
        tvTitle.text = music.title
        tvArtist.text = music.artist
        MediaPlayer.create(contex, music.song).apply {
            val minutes = duration / 1000 / 60
            val seconds = duration / 1000 % 60
            if (seconds < 10)
                tvDuration.text = "$minutes:0$seconds"
            else
                tvDuration.text = "$minutes:$seconds"
            release()
        }

        return rowView
    }
}
