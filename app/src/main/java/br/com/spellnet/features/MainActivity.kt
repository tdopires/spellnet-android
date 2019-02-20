package br.com.spellnet.features

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.spellnet.R
import br.com.spellnet.features.decklist.view.DeckListFragment
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
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


}
