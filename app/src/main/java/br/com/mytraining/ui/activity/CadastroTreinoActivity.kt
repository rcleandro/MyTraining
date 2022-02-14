package br.com.mytraining.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mytraining.R
import br.com.mytraining.data.Exercicio
import br.com.mytraining.data.Treino
import br.com.mytraining.ui.recyclerview.adapter.ExerciciosAdapter
import br.com.mytraining.utils.Helper
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cadastro_treino.*
import java.sql.Timestamp
import java.util.*

class CadastroTreinoActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var adapter: ExerciciosAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var exercicioArrayList: ArrayList<Exercicio>
    private lateinit var exercicioMarcadosArrayList: ArrayList<Exercicio>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cadastro_treino)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        tbCadastroTreino.setNavigationOnClickListener {
            finish()
        }

        btnCadastroTreino.setOnClickListener {
            cadastraTreino()
        }

        exercicioArrayList = arrayListOf()
        exercicioMarcadosArrayList = arrayListOf()
        definirAdapter()
    }

    private fun definirAdapter() {
        listaCarregando(true)
        dbref = FirebaseDatabase.getInstance().getReference("exercicios")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (exercicoSnapshot in snapshot.children) {
                        val exercicio = exercicoSnapshot.getValue(Exercicio::class.java)
                        exercicioArrayList.add(exercicio!!)
                    }
                    adapter = ExerciciosAdapter(exercicioArrayList)
                    adapter.onItemClickListener = { exercicio ->
                        exercicio.selecionado = !exercicio.selecionado!!

                        if (exercicio.selecionado == true)
                            exercicioMarcadosArrayList.add(exercicio)
                        else
                            exercicioMarcadosArrayList.remove(exercicio)
                    }
                    adapter.onImageClickedListener = { }
                    layoutManager =
                        LinearLayoutManager(Activity(), LinearLayoutManager.VERTICAL, false)
                    recyclerViewCadastroTreino.layoutManager = layoutManager
                    recyclerViewCadastroTreino.adapter = adapter
                    exercicioArrayList = arrayListOf()
                    listaCarregando(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listaCarregando(false)
                exibeToast("Falha ao realizar a operação. Motivo: ${error.message}")
            }
        })
    }

    private fun cadastraTreino() {
        cadastroCarregando(true)

        val nome = edtNomeCadastroTreino.text.toString().toLong()
        val descricao = edtDescicaoCadastroTreino.text.toString()
        val lista = if (exercicioMarcadosArrayList.isNullOrEmpty()) listOf()
        else exercicioMarcadosArrayList

        dbref = FirebaseDatabase.getInstance().getReference("treinos")
        val treino = Treino(
            nome,
            descricao,
            Helper.formataData(Timestamp(Date().time)),
            lista
        )
        dbref.child(nome.toString()).setValue(treino).addOnSuccessListener {
            exibeToast("Treino salvo com sucesso")
            finish()
        }.addOnFailureListener {
            exibeToast(it.message!!)
            cadastroCarregando(false)
        }
    }

    private fun listaCarregando(flag: Boolean) {
        pgCadastroTreino.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }

    private fun exibeToast(mensagem: String) {
        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun cadastroCarregando(flag: Boolean) {
        edtNomeCadastroTreino.isEnabled = !flag
        edtDescicaoCadastroTreino.isEnabled = !flag
        btnCadastroTreino.isEnabled = !flag
        btnCadastroTreino.text = if (flag) "Carregando..." else "Cadastrar"
        pgCadastroTreino.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }
}