package com.example.alcoolougasolinaestendido

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PostoRepository {
    private const val PREFS_NAME = "postos_prefs"
    private const val POSTOS_KEY = "postos_list"
    private val gson = Gson()

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getPostos(context: Context): MutableList<Posto> {
        val prefs = getSharedPreferences(context)
        val json = prefs.getString(POSTOS_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Posto>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun savePostos(context: Context, postos: List<Posto>) {
        val prefs = getSharedPreferences(context)
        val json = gson.toJson(postos)
        prefs.edit().putString(POSTOS_KEY, json).apply()
    }

    fun addPosto(context: Context, posto: Posto) {
        val postos = getPostos(context)
        postos.add(posto)
        savePostos(context, postos)
    }

    fun getPostoById(context: Context, id: String): Posto? {
        return getPostos(context).find { it.id == id }
    }

    fun updatePosto(context: Context, updatedPosto: Posto) {
        val postos = getPostos(context)
        val index = postos.indexOfFirst { it.id == updatedPosto.id }
        if (index != -1) {
            postos[index] = updatedPosto
            savePostos(context, postos)
        }
    }

    fun deletePosto(context: Context, id: String) {
        val postos = getPostos(context)
        postos.removeAll { it.id == id }
        savePostos(context, postos)
    }
}