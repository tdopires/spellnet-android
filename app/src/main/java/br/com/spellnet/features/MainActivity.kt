package br.com.spellnet.features

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.spellnet.R
import br.com.spellnet.features.decklist.view.DeckListFragment
import br.com.spellnet.features.menu.MenuViewModel
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val menuViewModel: MenuViewModel by viewModel()
    private lateinit var functions: FirebaseFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.main_activity)
        functions = Firebase.functions

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
        return when (item.itemId) {
            R.id.clear_card_price_cache_button -> {
                menuViewModel.clearAllCardPriceCache()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cache_menu, menu)
        return true
    }

}
