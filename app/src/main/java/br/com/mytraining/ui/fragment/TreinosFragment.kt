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
import br.com.mytraining.data.Treino
import br.com.mytraining.databinding.FragmentTreinosBinding
import br.com.mytraining.ui.activity.CadastroTreinoActivity
import br.com.mytraining.ui.activity.DetalhesTreinoActivity
import br.com.mytraining.ui.recyclerview.adapter.TreinosAdapter
import com.google.firebase.database.*
import java.util.*

class TreinosFragment : Fragment() {

    lateinit var binding: FragmentTreinosBinding
    private lateinit var adapter: TreinosAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var dbref: DatabaseReference
    private lateinit var treinoArrayList: ArrayList<Treino>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTreinosBinding.inflate(inflater, container, false)

        binding.fabAddTreino.setOnClickListener {
            abreCadastroTreinoActivity()
        }

        treinoArrayList = arrayListOf()
        definirAdapter()

        return binding.root
    }

    private fun definirAdapter() {
        cadastroCarregando(true)
        dbref = FirebaseDatabase.getInstance().getReference("treinos")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (treinoSnapshot in snapshot.children) {
                        val treino = treinoSnapshot.getValue(Treino::class.java)
                        treinoArrayList.add(treino!!)
                    }
                    adapter = TreinosAdapter(treinoArrayList)
                    adapter.onItemClickListener = { treino ->
                        abreDetalhesTreinoActivity(treino)
                    }
                    layoutManager =
                        LinearLayoutManager(Activity(), LinearLayoutManager.VERTICAL, false)
                    binding.recyclerViewTreinos.layoutManager = layoutManager
                    binding.recyclerViewTreinos.adapter = adapter
                    treinoArrayList = arrayListOf()
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

    private fun abreCadastroTreinoActivity() {
        requireActivity().startActivity(Intent(activity, CadastroTreinoActivity::class.java))
    }

    private fun abreDetalhesTreinoActivity(treino: Treino) {
        val intent = Intent(activity, DetalhesTreinoActivity::class.java)
        intent.putExtra("treino", treino)
        requireActivity().startActivity(intent)
    }

    private fun cadastroCarregando(flag: Boolean) {
        binding.pgTreino.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance() = TreinosFragment()
    }
}