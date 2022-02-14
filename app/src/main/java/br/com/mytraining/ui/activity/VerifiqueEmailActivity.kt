package br.com.mytraining.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.mytraining.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_verifique_email.*

class VerifiqueEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_verifique_email)
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        val auth = FirebaseAuth.getInstance()

        tvEmail.text = auth.currentUser!!.email

        btnVerifiquei.setOnClickListener {
            carregando(true)
            btnVerifiquei.text = "Carregando..."
            auth.currentUser!!.reload().addOnCompleteListener {
                if (auth.currentUser!!.isEmailVerified) {
                    abreMainActivity()
                } else {
                    exibeToast("Parece que seu e-mail ainda não foi verificado.")
                    btnVerifiquei.text = "Verifiquei"
                    carregando(false)
                }
            }
        }

        btnReenviar.setOnClickListener {
            carregando(true)
            btnReenviar.text = "Carregando..."
            auth.currentUser!!.sendEmailVerification().addOnCompleteListener { envioEmail ->
                if (envioEmail.isSuccessful) {
                    exibeToast("E-mail de verificação re-enviado com sucesso.")
                } else {
                    exibeToast(envioEmail.exception?.message!!)
                }

                carregando(false)
                btnReenviar.text = "Não recebi o e-mail"
            }
        }
        btnSair.setOnClickListener {
            auth.signOut()
            abreMainActivity()
        }
    }

    private fun carregando(flag: Boolean) {
        btnReenviar.isEnabled = !flag
        btnSair.isEnabled = !flag
        btnVerifiquei.isEnabled = !flag
    }

    private fun exibeToast(mensagem: String) {
        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun abreMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}