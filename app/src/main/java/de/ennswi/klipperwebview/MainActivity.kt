package de.ennswi.klipperwebview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var klipperWebViewClient: KlipperWebViewClient
    private lateinit var klipperWebChromeClient: KlipperWebChromeClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        klipperWebViewClient = KlipperWebViewClient(this)
        klipperWebChromeClient = KlipperWebChromeClient(this)


        var sharedPref = getSharedPreferences(
            getString(R.string.preference_value_name_shared_data),
            MODE_PRIVATE
        )

        val url = sharedPref.getString(
            getString(R.string.preference_value_url),
            getString(R.string.preference_value_url_default)
        )

        sharedPref =
            getSharedPreferences(getString(R.string.preference_value_lock), MODE_PRIVATE)
        val lcApplicationLock = sharedPref.getBoolean(
            getString(R.string.preference_value_lock),
            false)
        klipperWebViewClient.setLockEnabled(lcApplicationLock)

        webView = findViewById(R.id.webview)
        webView.loadUrl(url.toString())
        webView.webViewClient = klipperWebViewClient
        webView.webChromeClient = klipperWebChromeClient

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true;

        WebView.setWebContentsDebuggingEnabled(true);



    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.fragment_menu, menu)

        val switch =
            menu.findItem(R.id.id_menu_item_lock).actionView.findViewById(R.id.id_switch) as Switch

        val sharedPref =
            getSharedPreferences(getString(R.string.preference_value_lock), MODE_PRIVATE)
        val lcApplicationLock = sharedPref.getBoolean(
            getString(R.string.preference_value_lock),
            false
        )

        switch.isChecked = lcApplicationLock;

        if (switch.isChecked) {
            switch.buttonDrawable?.setTint(ContextCompat.getColor(this, R.color.teal_700))
        } else {
            switch.buttonDrawable?.setTint(ContextCompat.getColor(this, R.color.white))
        }

        switch.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                button.buttonDrawable?.setTint(ContextCompat.getColor(this, R.color.teal_700))
                klipperWebViewClient.enableCSSInjection()

                val sharedPref =
                    getSharedPreferences(getString(R.string.preference_value_lock), MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.preference_value_lock), true);
                    apply()
                }
            } else {
                button.buttonDrawable?.setTint(ContextCompat.getColor(this, R.color.white))
                klipperWebViewClient.removeCSSInjection()

                val sharedPref =
                    getSharedPreferences(getString(R.string.preference_value_lock), MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.preference_value_lock), false);
                    apply()
                }
            }
        }

        reloadView()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.id_menu_item_settings -> {
                showSettings()
                true
            }
            R.id.id_menu_reload -> {
                reloadView()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun reloadView() {
        webView.reload()
    }


    private fun showSettings() {
        val myIntent = Intent(this, SettingsActivity::class.java)

        this.startActivity(myIntent)
    }
}