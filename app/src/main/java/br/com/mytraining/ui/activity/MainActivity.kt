package br.com.mytraining.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.mytraining.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            if (!user.isEmailVerified) {
                abreVerifiqueEmailActivity()
            } else {
                abreInicioActivity()
            }
        }

        btnCadastro.setOnClickListener {
            abreCadastroUsuarioActivity()
        }

        btnEntrar.setOnClickListener {
            if (validacaoLogin()) {
                loginCarregando(true)
                auth.signInWithEmailAndPassword(
                    edtEmailLogin.text.toString(),
                    edtSenhaLogin.text.toString()
                ).addOnCompleteListener { login ->
                    if (login.isSuccessful) {
                        abreInicioActivity()
                    } else {
                        exibeToast("Autenticação falhou. Verifique seu e-mai e senha.")
                        loginCarregando(false)
                    }
                }
            } else {
                exibeToast("Verifique os campos incorretos")
            }
        }
    }

    private fun loginCarregando(flag: Boolean) {
        edtEmailLogin.isEnabled = !flag
        edtSenhaLogin.isEnabled = !flag
        btnCadastro.isEnabled = !flag
        btnEntrar.isEnabled = !flag

        btnEntrar.text = if (flag) "Carregando..." else "Entrar"
        pgLogin.visibility = if (flag) View.VISIBLE else View.INVISIBLE
    }

    private fun validacaoLogin(): Boolean {
        if (edtEmailLogin.text.isNullOrBlank()
            || edtEmailLogin.text.isNullOrEmpty()
            || !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmailLogin.text).matches()
        ) {
            edtEmailLogin.error = "Endereço de e-mail invalido."
            edtEmailLogin.requestFocus()
            return false
        }

        if (edtSenhaLogin.text.isNullOrBlank() || edtSenhaLogin.text.isNullOrEmpty()) {
            edtSenhaLogin.error = "Senha de acesso invalida."
            edtSenhaLogin.requestFocus()
            return false
        }

        if (edtSenhaLogin.text!!.length < 6) {
            edtSenhaLogin.error = "Senha de acesso deve ter no mínimo 6 caracteres."
            edtSenhaLogin.requestFocus()
            return false
        }
        return true
    }

    private fun exibeToast(mensagem: String) {
        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun abreInicioActivity() {
        startActivity(Intent(this, InicioActivity::class.java))
        finish()
    }

    private fun abreVerifiqueEmailActivity() {
        startActivity(Intent(this, VerifiqueEmailActivity::class.java))
        finish()
    }

    private fun abreCadastroUsuarioActivity() {
        startActivity(Intent(this, CadastroUsuarioActivity::class.java))
        finish()

    }
}