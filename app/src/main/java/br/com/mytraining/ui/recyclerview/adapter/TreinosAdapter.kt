package br.com.mytraining.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mytraining.data.Treino
import br.com.mytraining.databinding.ItemTreinoBinding

class TreinosAdapter(
    private var treinos: ArrayList<Treino>
) : RecyclerView.Adapter<TreinosAdapter.Holder>() {

    lateinit var onItemClickListener: (item: Treino) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemTreinoBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(treinos[position])
    }

    override fun getItemCount(): Int {
        return treinos.count()
    }

    class Holder(
        private val binding: ItemTreinoBinding,
        private val onItemClickListener: ((item: Treino) -> Unit)?
        ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var treino: Treino

        @SuppressLint("SetTextI18n")
        fun bind(treino: Treino) {
            this.treino = treino

            binding.root.setOnClickListener { onItemClickListener?.invoke(treino) }

            binding.treinoItemNome.text = "Treino ${treino.nome}"
            binding.treinoItemDescricao.text = treino.descricao
            binding.treinoItemData.text = "Cadastrado em ${treino.data}"
        }
    }
}