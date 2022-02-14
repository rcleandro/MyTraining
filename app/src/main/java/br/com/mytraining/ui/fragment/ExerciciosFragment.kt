package br.com.mytraining.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mytraining.data.Exercicio
import br.com.mytraining.databinding.FragmentExerciciosBinding
import br.com.mytraining.ui.activity.CadastroExercicioActivity
import br.com.mytraining.ui.activity.DetalhesExercicioActivity
import br.com.mytraining.ui.recyclerview.adapter.ExerciciosAdapter
import com.google.firebase.database.*

class ExerciciosFragment : Fragment() {

    lateinit var binding: FragmentExerciciosBinding
    private lateinit var adapter: ExerciciosAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var dbref: DatabaseReference
    private lateinit var exercicioArrayList: ArrayList<Exercicio>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExerciciosBinding.inflate(inflater, container, false)

        binding.favAddExercicio.setOnClickListener {
            abreCadastroExercicioActivity()
        }

        exercicioArrayList = arrayListOf()
        definirAdapter()

        return binding.root
    }

    private fun definirAdapter() {
        cadastroCarregando(true)
        dbref = FirebaseDatabase.getInstance().getReference("exercicios")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (exercicoSnapshot in snapshot.children) {
                        val exercicio = exercicoSnapshot.getValue(Exercicio::class.java)
                        exercicioArrayList.add(exercicio!!)
                    }
                    adapter = ExerciciosAdapter(exercicioArrayList)
                    adapter.onItemClickListener = { }
                    adapter.onImageClickedListener = { exercicio ->
                        abreDetalhesExercicioActivity(exercicio)
                    }
                    layoutManager =
                        LinearLayoutManager(Activity(), LinearLayoutManager.VERTICAL, false)
                    binding.recyclerViewExercicio.layoutManager = layoutManager
                    binding.recyclerViewExercicio.adapter = adapter
                    exercicioArrayList = arrayListOf()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    activity,
                    "Falha ao realizar a operação. Motivo: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        cadastroCarregando(false)
    }

    private fun abreDetalhesExercicioActivity(exercicio: Exercicio) {
        val intent = Intent(context, DetalhesExercicioActivity::class.java)
        intent.putExtra("exercicio", exercicio)
        this.startActivity(intent)
    }

    private fun abreCadastroExercicioActivity() {
        requireActivity().startActivity(Intent(activity, CadastroExercicioActivity::class.java))
    }

    private fun cadastroCarregando(flag: Boolean) {
        binding.pgExercico.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciciosFragment()
    }
}