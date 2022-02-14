package br.com.mytraining.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import br.com.mytraining.R
import br.com.mytraining.ui.fragment.ExerciciosFragment
import br.com.mytraining.ui.fragment.PerfilFragment
import br.com.mytraining.ui.fragment.TreinosFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_inicio.*

class InicioActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    private val treinos = TreinosFragment.newInstance()
    private val exercicios = ExerciciosFragment.newInstance()
    private val perfil = PerfilFragment.newInstance()
    lateinit var usuarioLogado: DocumentSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_inicio)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.treinos -> {
                    openFragment(treinos)
                }
                R.id.exercicios -> {
                    openFragment(exercicios)
                }
                R.id.perfil -> {
                    openFragment(perfil)
                }
            }
            true
        }

        loadUsuario()
        openFragment(treinos)
    }

    private fun loadUsuario() {
        this.db.collection("usuarios")
            .document(this.auth.currentUser!!.uid)
            .addSnapshotListener { usuario, _ ->
                this.usuarioLogado = usuario!!
            }

        this.db.collection("usuarios")
            .document(this.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { resultado ->
                this.usuarioLogado = resultado
                iniciarInterface()
            }
            .addOnFailureListener { excecao ->
                Toast.makeText(
                    this,
                    "Falha ao realziar a operação. Motivo: " + excecao.message,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun iniciarInterface() {
        pbInicio.visibility = View.INVISIBLE
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}