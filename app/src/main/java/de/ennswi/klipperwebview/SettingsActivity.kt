package de.ennswi.klipperwebview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
//    private lateinit var activity:Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val b = intent.extras
//        activity= b?.get("activity") as Activity
        val sharedPref = getSharedPreferences(getString(R.string.preference_value_name_shared_data),MODE_PRIVATE)
        val url = sharedPref.getString(
            getString(R.string.preference_value_url),
            getString(R.string.preference_value_url_default)
        )
        findViewById<EditText>(R.id.id_url).setText(url)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.id_setting_save -> {
                val sharedPref = getSharedPreferences(getString(R.string.preference_value_name_shared_data),MODE_PRIVATE)
                with (sharedPref.edit()) {
                    var url: EditText = findViewById<EditText>(R.id.id_url)
                    putString(getString(R.string.preference_value_url),url.text.toString());
                    apply()
                }

                finish()
                true
            }
            R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}