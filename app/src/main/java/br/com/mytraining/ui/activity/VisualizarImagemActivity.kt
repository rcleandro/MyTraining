package br.com.mytraining.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.mytraining.R
import br.com.mytraining.utils.Constanst
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_visualizar_imagem.*

class VisualizarImagemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_visualizar_imagem)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        tbVisualizarImagem.title =
            if (intent.getStringExtra("TITLE") != null)
                intent.getStringExtra("TITLE")
            else "Visualizar imagem"

        tbVisualizarImagem.setNavigationOnClickListener {
            finish()
        }

        val url =
            if (intent.getStringExtra("URL") != null)
                intent.getStringExtra("URL")
            else
                Constanst.URL_DEFAULT_PROFILE_PHOTO

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.profile_default)
            .error(R.drawable.profile_default)
            .into(imgVisualizarFoto)
    }
}