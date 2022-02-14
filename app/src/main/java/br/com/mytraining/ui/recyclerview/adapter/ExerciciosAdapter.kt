package br.com.mytraining.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import br.com.mytraining.R
import br.com.mytraining.data.Exercicio
import br.com.mytraining.databinding.ItemExercicioBinding
import com.squareup.picasso.Picasso

class ExerciciosAdapter(
    private var exercicios: ArrayList<Exercicio>
) : RecyclerView.Adapter<ExerciciosAdapter.Holder>() {

    lateinit var onItemClickListener: (item: Exercicio) -> Unit
    lateinit var onImageClickedListener: (item: Exercicio) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemExercicioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), onItemClickListener, onImageClickedListener
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(exercicios[position])
    }

    override fun getItemCount(): Int {
        return exercicios.count()
    }

    class Holder(
        private val binding: ItemExercicioBinding,
        private val onItemClickListener: ((item: Exercicio) -> Unit)?,
        private val onImageClickedListener: ((item: Exercicio) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var exercicio: Exercicio

        @SuppressLint("SetTextI18n")
        fun bind(exercicio: Exercicio) {
            this.exercicio = exercicio

            exercicio.selecionado = false

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(exercicio)
                onImageClickedListener?.invoke(exercicio)
                alteraLayout()
            }

            binding.exercicioItemNome.text = "Exercício ${exercicio.nome}"
            binding.exercicioItemObservacoes.text = exercicio.observacoes

            Picasso.get()
                .load(exercicio.imagem)
                .placeholder(R.drawable.imagem_padrao)
                .error(R.drawable.imagem_padrao)
                .into(binding.imageView)
        }

        private fun alteraLayout() {
            if (exercicio.selecionado!!) {
                binding.exercicioItemLayout.backgroundTintList =
                    ColorStateList.valueOf(
                        getColor(
                            binding.exercicioItemLayout.context,
                            R.color.selected
                        )
                    )

                binding.exercicioItemNome
                    .setTextColor(
                        getColor(
                            binding.exercicioItemLayout.context,
                            R.color.white
                        )
                    )

                binding.exercicioItemObservacoes
                    .setTextColor(
                        getColor(
                            binding.exercicioItemLayout.context,
                            R.color.white
                        )
                    )

                binding.checkImagem.visibility = View.VISIBLE
            } else {
                binding.exercicioItemLayout.backgroundTintList =
                    ColorStateList.valueOf(
                        getColor(
                            binding.exercicioItemLayout.context,
                            R.color.white
                        )
                    )

                binding.exercicioItemNome
                    .setTextColor(
                        getColor(
                            binding.exercicioItemLayout.context,
                            R.color.font
                        )
                    )

                binding.exercicioItemObservacoes
                    .setTextColor(
                        getColor(
                            binding.exercicioItemLayout.context,
                            R.color.font
                        )
                    )

                binding.checkImagem.visibility = View.INVISIBLE
            }
        }
    }
}