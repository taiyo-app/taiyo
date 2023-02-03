package com.taiyoapp.taiyo.anime.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.presentation.fragment.AnimeDetailFragment

class AnimeDetailActivity : AppCompatActivity() {
    private var animeId: Int = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)
        parseExtra()
        if (savedInstanceState == null) {
            launchAnimeDetailFragment(animeId)
        }
    }

    private fun launchAnimeDetailFragment(id: Int) {
        val fragment = AnimeDetailFragment.newInstance(id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    private fun parseExtra() {
        if (!intent.hasExtra(ANIME_ID)) {
            finish()
            return
        }
        animeId = intent.getIntExtra(ANIME_ID, UNDEFINED_ID)
    }

    companion object {
        private const val ANIME_ID = "id"
        private const val UNDEFINED_ID = -1

        fun newIntent(context: Context, animeId: Int): Intent {
            val intent = Intent(context, AnimeDetailActivity::class.java)
            intent.putExtra(ANIME_ID, animeId)
            return intent
        }
    }
}