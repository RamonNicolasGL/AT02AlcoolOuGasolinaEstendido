package com.example.alcoolougasolinaestendido

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostoAdapter(
    private var postos: List<Posto>,
    private val onItemClick: (Posto) -> Unit
) : RecyclerView.Adapter<PostoAdapter.PostoViewHolder>() {

    class PostoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomePosto: TextView = view.findViewById(R.id.tvNomePostoItem)
        val tvPrecos: TextView = view.findViewById(R.id.tvPrecosItem)
        val tvMelhorOpcao: TextView = view.findViewById(R.id.tvMelhorOpcaoItem) // <-- ADICIONE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_posto, parent, false)
        return PostoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostoViewHolder, position: Int) {
        val posto = postos[position]
        holder.tvNomePosto.text = posto.nome
        holder.tvPrecos.text = "Álcool: R$ ${posto.precoAlcool} | Gasolina: R$ ${posto.precoGasolina}"

        val textoMelhorOpcao = holder.itemView.context.getString(R.string.melhor_opcao_era)
        holder.tvMelhorOpcao.text = "$textoMelhorOpcao ${posto.melhorOpcao}" // <-- ADICIONE

        holder.itemView.setOnClickListener {
            onItemClick(posto)
        }
    }

    override fun getItemCount() = postos.size

    fun updateData(newPostos: List<Posto>) {
        postos = newPostos
        notifyDataSetChanged()
    }
}