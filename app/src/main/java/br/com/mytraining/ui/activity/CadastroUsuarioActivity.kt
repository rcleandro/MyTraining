package br.com.mytraining.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.mytraining.R
import br.com.mytraining.utils.Constanst
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*

class CadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cadastro_usuario)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        this.auth = FirebaseAuth.getInstance()

        tbCadastro.setNavigationOnClickListener {
            abreMainActivity()
        }

        btnCadastro.setOnClickListener {
            cadastro()
        }
    }

    private fun cadastro() {
        if (validacaoCadastro()) {
            cadastroCarregando(true)

            auth.createUserWithEmailAndPassword(
                edtEmailCadastro.text.toString(),
                edtSenhaCadastro.text.toString()
            ).addOnCompleteListener { cadastro ->
                if (cadastro.isSuccessful) {
                    auth.currentUser!!.sendEmailVerification().addOnCompleteListener { envioEmail ->
                        if (envioEmail.isSuccessful) {
                            val db = FirebaseFirestore.getInstance()
                            val usuario = hashMapOf(
                                "foto" to Constanst.URL_DEFAULT_PROFILE_PHOTO,
                                "nome" to edtNomeCadastro.text.toString(),
                                "email" to auth.currentUser!!.email
                            )
                            db.collection("usuarios")
                                .document(auth.currentUser!!.uid)
                                .set(usuario)
                                .addOnSuccessListener {
                                    sucessoCadastro()
                                }.addOnFailureListener {
                                    auth.currentUser!!.delete().addOnCompleteListener {
                                        erroCadastro(envioEmail.exception?.message!!)
                                        cadastroCarregando(false)
                                    }
                                }
                        } else {
                            auth.currentUser!!.delete().addOnCompleteListener {
                                erroCadastro(envioEmail.exception?.message!!)
                                cadastroCarregando(false)
                            }
                        }
                    }
                } else {
                    erroCadastro("Falha ao realizar opera????o. Motivo: ${cadastro.exception?.message}")
                    cadastroCarregando(false)
                }
            }
        } else {
            erroCadastro("Verifique os campos incorretos")
        }
    }

    private fun sucessoCadastro() {
        abreMainActivity()
    }

    private fun cadastroCarregando(flag: Boolean) {
        edtNomeCadastro.isEnabled = !flag
        edtEmailCadastro.isEnabled = !flag
        edtSenhaCadastro.isEnabled = !flag
        btnCadastro.isEnabled = !flag
        btnCadastro.text = if (flag) "Carregando..." else "Cadastrar"
        pgCadastro.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }

    private fun erroCadastro(s: String) {
        Toast.makeText(
            this,
            s,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun validacaoCadastro(): Boolean {
        if (edtNomeCadastro.text.isNullOrBlank() || edtNomeCadastro.text.isNullOrEmpty()) {
            edtNomeCadastro.error = "Nome de usu??rio incorreto."
            edtNomeCadastro.requestFocus()
            return false
        }

        if (edtEmailCadastro.text.isNullOrBlank()
            || edtEmailCadastro.text.isNullOrEmpty()
            || !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmailCadastro.text).matches()
        ) {
            edtEmailCadastro.error = "Endere??o de e-mail incorreto."
            edtEmailCadastro.requestFocus()
            return false
        }

        if (edtSenhaCadastro.text.isNullOrBlank() || edtSenhaCadastro.text.isNullOrEmpty()) {
            edtSenhaCadastro.error = "Senha de acesso invalida."
            edtSenhaCadastro.requestFocus()
            return false
        }

        if (edtSenhaCadastro.text!!.length < 6) {
            edtSenhaCadastro.error = "Senha de acesso deve ter no m??nimo 6 caracteres."
            edtSenhaCadastro.requestFocus()
            return false
        }
        return true
    }

    private fun abreMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}