package br.com.spellnet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.spellnet.decklist.view.DeckListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DeckListFragment.newInstance())
                .commitNow()
        }
    }

}
