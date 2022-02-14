package br.com.mytraining.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.mytraining.R
import br.com.mytraining.data.Exercicio
import br.com.mytraining.utils.Constanst
import br.com.mytraining.utils.Helper
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cadastro_exercicio.*
import kotlinx.android.synthetic.main.bottom_sheet_altera_foto.view.*
import java.io.ByteArrayOutputStream

class CadastroExercicioActivity : AppCompatActivity() {

    val REQUISICAO_FOTO_GALERIA = 101
    val REQUISICAO_FOTO_CAMERA = 102

    private lateinit var dbref: DatabaseReference
    private var imageUri = Constanst.URL_DEFAULT_IMAGE_PHOTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cadastro_exercicio)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        tbCadastroExercicio.setNavigationOnClickListener {
            finish()
        }

        fabAlterarFotoExercicio.setOnClickListener {
            if (Helper.checarPermissoes(this)) selecionaImagem()
        }

        btnCadastroExercico.setOnClickListener {
            cadastraExercicio()
        }
    }

    private fun cadastraExercicio() {
        cadastroCarregando(true)

        val nome = edtNomeCadastroExercicio.text.toString().toLong()
        val imagem = imageUri
        val observacao = edtObservacoesCadastroExercico.text.toString()

        dbref = FirebaseDatabase.getInstance().getReference("exercicios")
        val exercicio = Exercicio(imagem, nome, observacao)
        dbref.child(nome.toString()).setValue(exercicio).addOnSuccessListener {
            exibeToast("Exercício salvo com sucesso")
            finish()
        }.addOnFailureListener {
            exibeToast(it.message!!)
            cadastroCarregando(false)
        }
    }

    private fun selecionaImagem() {
        val btmSheetLayout = layoutInflater.inflate(R.layout.bottom_sheet_altera_foto, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetStyle)

        dialog.setContentView(btmSheetLayout)

        btmSheetLayout.imgBtnExcluirFoto.visibility = View.GONE

        btmSheetLayout.imgBtnGaleria.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Selecionar foto"),
                REQUISICAO_FOTO_GALERIA
            )
            dialog.dismiss()
        }

        btmSheetLayout.imgBtnCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(this.packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUISICAO_FOTO_CAMERA)
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUISICAO_FOTO_GALERIA || requestCode == REQUISICAO_FOTO_CAMERA) {
            val referenciaStorage = FirebaseStorage.getInstance().reference
            val referenciaArquivo = referenciaStorage.child(
                "imagens_exercicio/" + System.currentTimeMillis().toString()
            )

            when (requestCode) {
                REQUISICAO_FOTO_CAMERA -> {
                    if (resultCode == RESULT_OK) {
                        val imagemBitmap = data?.extras?.get("data") as Bitmap
                        val baos = ByteArrayOutputStream()
                        imagemBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val arquivo = baos.toByteArray()
                        val tarefaUploadArquivo = referenciaArquivo.putBytes(arquivo)
                        tarefaUploadArquivo.addOnCompleteListener { upload ->
                            trataUploadFoto(upload, referenciaArquivo)
                        }
                    }
                }
                REQUISICAO_FOTO_GALERIA -> {
                    if (data?.data != null) {
                        val arquivo = data.data
                        val tarefaUploadArquivo = referenciaArquivo.putFile(arquivo!!)
                        tarefaUploadArquivo.addOnCompleteListener { upload ->
                            trataUploadFoto(upload, referenciaArquivo)
                        }
                    }
                }
            }
        }
    }

    private fun trataUploadFoto(
        upload: Task<UploadTask.TaskSnapshot>,
        referenciaArquivo: StorageReference
    ) {
        if (upload.isSuccessful) {
            referenciaArquivo.downloadUrl.addOnSuccessListener { uri ->
                imageUri = uri.toString()
                carregaImagem(uri)
            }.addOnFailureListener { excecao ->
                exibeToast("Fala ao realizar a operação. Motivo: " + excecao.message)
            }
        } else {
            exibeToast("Falha ao realizar a operação. Motivo: " + upload.exception?.message)
        }
    }

    private fun carregaImagem(uri: Uri) {
        Picasso.get()
            .load(uri)
            .placeholder(R.drawable.imagem_padrao)
            .error(R.drawable.imagem_padrao)
            .into(imgExercicio)
    }

    private fun exibeToast(mensagem: String) {
        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun cadastroCarregando(flag: Boolean) {
        edtNomeCadastroExercicio.isEnabled = !flag
        edtObservacoesCadastroExercico.isEnabled = !flag
        fabAlterarFotoExercicio.isEnabled = !flag
        btnCadastroExercico.isEnabled = !flag
        btnCadastroExercico.text = if (flag) "Carregando..." else "Cadastrar"
        pgCadastroExercico.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }
}