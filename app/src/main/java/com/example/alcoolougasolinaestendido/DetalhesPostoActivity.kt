package com.example.alcoolougasolinaestendido

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetalhesPostoActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etAlcool: EditText
    private lateinit var etGasolina: EditText
    private lateinit var tvData: TextView
    private lateinit var btnSalvar: Button
    private lateinit var btnMapa: Button
    private lateinit var btnExcluir: Button

    private var postoAtual: Posto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_posto)

        title = getString(R.string.detalhes_do_posto)

        etNome = findViewById(R.id.etNomePostoDetalhe)
        etAlcool = findViewById(R.id.etPrecoAlcoolDetalhe)
        etGasolina = findViewById(R.id.etPrecoGasolinaDetalhe)
        tvData = findViewById(R.id.tvDataCadastro)
        btnSalvar = findViewById(R.id.btnSalvarAlteracoes)
        btnMapa = findViewById(R.id.btnVerMapa)
        btnExcluir = findViewById(R.id.btnExcluir)

        val postoId = intent.getStringExtra("POSTO_ID")
        if (postoId == null) {
            finish()
            return
        }

        postoAtual = PostoRepository.getPostoById(this, postoId)
        postoAtual?.let { popularDados(it) }

        btnSalvar.setOnClickListener { salvarAlteracoes() }
        btnExcluir.setOnClickListener { excluirPosto() }
        btnMapa.setOnClickListener { verNoMapa() }
    }

    private fun popularDados(posto: Posto) {
        etNome.setText(posto.nome)
        etAlcool.setText(posto.precoAlcool.toString())
        etGasolina.setText(posto.precoGasolina.toString())
        tvData.text = "${getString(R.string.data_do_cadastro)} ${posto.dataRegistro}"
    }

    private fun salvarAlteracoes() {
        val nome = etNome.text.toString()
        val alcoolStr = etAlcool.text.toString()
        val gasolinaStr = etGasolina.text.toString()

        if (nome.isBlank() || alcoolStr.isBlank() || gasolinaStr.isBlank()) {
            Toast.makeText(this, getString(R.string.preencha_todos_campos), Toast.LENGTH_SHORT).show()
            return
        }

        postoAtual?.let {
            it.nome = nome
            it.precoAlcool = alcoolStr.toDouble()
            it.precoGasolina = gasolinaStr.toDouble()

            PostoRepository.updatePosto(this, it)
            Toast.makeText(this, getString(R.string.posto_atualizado_sucesso), Toast.LENGTH_SHORT).show()
            finish() // Volta para a lista
        }
    }

    private fun excluirPosto() {
        postoAtual?.let {
            PostoRepository.deletePosto(this, it.id)
            Toast.makeText(this, getString(R.string.posto_excluido_sucesso), Toast.LENGTH_SHORT).show()
            finish() // Volta para a lista
        }
    }

    private fun verNoMapa() {
        postoAtual?.let { posto ->
            if (posto.latitude != null && posto.longitude != null) {
                val gmmIntentUri = Uri.parse("geo:${posto.latitude},${posto.longitude}?q=${posto.latitude},${posto.longitude}(${Uri.encode(posto.nome)})")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                } else {

                    val genericMapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    startActivity(genericMapIntent)
                }
            } else {
                Toast.makeText(this, getString(R.string.erro_localizacao), Toast.LENGTH_SHORT).show()
            }
        }
    }
}