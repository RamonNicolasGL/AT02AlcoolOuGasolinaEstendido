package com.example.alcoolougasolinaestendido

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaPostosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_postos)

        title = getString(R.string.lista_de_postos_salvos)

        recyclerView = findViewById(R.id.recyclerViewPostos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        loadPostos()
    }

    private fun setupAdapter() {

        adapter = PostoAdapter(emptyList()) { posto ->

            val intent = Intent(this, DetalhesPostoActivity::class.java)
            intent.putExtra("POSTO_ID", posto.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun loadPostos() {
        val postos = PostoRepository.getPostos(this)
        adapter.updateData(postos)
    }
}