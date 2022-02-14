package br.com.mytraining.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.mytraining.R
import br.com.mytraining.data.Exercicio
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detalhes_exercicio.*

class DetalhesExercicioActivity : AppCompatActivity() {

    private lateinit var exercicio: Exercicio
    private lateinit var exercicioArrayList: ArrayList<Exercicio>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detalhes_exercicio)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        exercicio = intent.extras!!["exercicio"] as Exercicio

        tbDetalheExercicio.setNavigationOnClickListener {
            finish()
        }

        exercicioArrayList = arrayListOf()

        tbDetalheExercicio.title = "Exercicio ${exercicio.nome}"
        tvDetalheExercicioObservacao.text = exercicio.observacoes

        Picasso.get()
            .load(exercicio.imagem)
            .placeholder(R.drawable.imagem_padrao)
            .error(R.drawable.imagem_padrao)
            .into(imgDetalheExercicio)
    }
}