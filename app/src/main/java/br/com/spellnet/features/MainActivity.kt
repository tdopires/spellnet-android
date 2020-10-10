package br.com.spellnet.features

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.spellnet.R
import br.com.spellnet.features.decklist.view.DeckListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            handleDeckUrlIntentAndStartDeckListFragment(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeckUrlIntentAndStartDeckListFragment(intent)
    }

    private fun handleDeckUrlIntentAndStartDeckListFragment(intent: Intent?) {
        val deckUrlFromIntent = if (intent?.action == Intent.ACTION_SEND && "text/plain" == intent.type) {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else null

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DeckListFragment.newInstance(deckUrlFromIntent))
            .commitNow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}
