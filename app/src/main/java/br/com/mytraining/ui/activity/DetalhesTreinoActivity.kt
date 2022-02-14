package br.com.mytraining.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mytraining.R
import br.com.mytraining.data.Exercicio
import br.com.mytraining.data.Treino
import br.com.mytraining.ui.recyclerview.adapter.ExerciciosAdapter
import kotlinx.android.synthetic.main.activity_detalhes_treino.*

class DetalhesTreinoActivity : AppCompatActivity() {

    private lateinit var treino: Treino
    private lateinit var adapter: ExerciciosAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var exercicioArrayList: ArrayList<Exercicio>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detalhes_treino)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        treino = intent.extras!!["treino"] as Treino

        tbDetalheTreino.setNavigationOnClickListener {
            finish()
        }

        exercicioArrayList = arrayListOf()
        definirAdapter(treino)

        tbDetalheTreino.title = "Treino ${treino.nome}"
        tvDetalheTreinoDescricao.text = treino.descricao
        tvDetalheTreinoData.text = "Cadastrado em ${treino.data}"
    }

    private fun definirAdapter(treino: Treino) {
        detalhesCarregando(true)
        for (item in treino.exercicio) {
            exercicioArrayList.add(item)
        }
        adapter = ExerciciosAdapter(exercicioArrayList)
        adapter.onItemClickListener = { }
        adapter.onImageClickedListener = { exercicio ->
            abreDetalhesExercicioActivity(exercicio)
        }
        layoutManager =
            LinearLayoutManager(Activity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewDetalheTreino.layoutManager = layoutManager
        recyclerViewDetalheTreino.adapter = adapter
        exercicioArrayList = arrayListOf()
        detalhesCarregando(false)
    }

    private fun abreDetalhesExercicioActivity(exercicio: Exercicio) {
        val intent = Intent(this, DetalhesExercicioActivity::class.java)
        intent.putExtra("exercicio", exercicio)
        this.startActivity(intent)
    }

    private fun detalhesCarregando(flag: Boolean) {
        pgDetalheTreino.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }
}