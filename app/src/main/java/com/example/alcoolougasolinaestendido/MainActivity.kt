package com.example.alcoolougasolinaestendido

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var etPrecoAlcool: EditText
    private lateinit var etPrecoGasolina: EditText
    private lateinit var etNomePosto: EditText
    private lateinit var switchLembrar: SwitchMaterial
    private lateinit var tvResultado: TextView
    private lateinit var btnSalvar: Button
    private lateinit var btnVerLista: Button

    private lateinit var btnChangeLanguage: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, getString(R.string.permissao_localizacao_negada), Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPrecoAlcool = findViewById(R.id.etPrecoAlcool)
        etPrecoGasolina = findViewById(R.id.etPrecoGasolina)
        etNomePosto = findViewById(R.id.etNomePosto)
        switchLembrar = findViewById(R.id.switchLembrar)
        tvResultado = findViewById(R.id.tvResultado)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnVerLista = findViewById(R.id.btnVerLista)
        btnChangeLanguage = findViewById(R.id.btnChangeLanguage)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // REQUISITO 1: Salvar e restaurar o estado do Switch
        setupSwitch()

        // REQUISITO 4: Pedir permissão e obter localização
        requestLocationPermission()

        btnSalvar.setOnClickListener {
            salvarDadosPosto()
        }

        btnVerLista.setOnClickListener {

            val intent = Intent(this, ListaPostosActivity::class.java)
            startActivity(intent)
        }

        btnChangeLanguage.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun setupSwitch() {
        val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        val switchState = sharedPrefs.getBoolean("switch_state", false)
        switchLembrar.isChecked = switchState

        updateSwitchText(switchState)

        switchLembrar.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("switch_state", isChecked).apply()

            updateSwitchText(isChecked)

            calcularMelhorOpcao()
        }
    }

    private fun calcularMelhorOpcao() {
        val precoAlcoolStr = etPrecoAlcool.text.toString()
        val precoGasolinaStr = etPrecoGasolina.text.toString()

        if (precoAlcoolStr.isNotEmpty() && precoGasolinaStr.isNotEmpty()) {
            val precoAlcool = precoAlcoolStr.toDouble()
            val precoGasolina = precoGasolinaStr.toDouble()

            val resultado = if ((precoAlcool / precoGasolina) >= 0.7) {
                getString(R.string.gasolina)
            } else {
                getString(R.string.alcool)
            }
            tvResultado.text = getString(R.string.melhor_opcao_e, resultado)
        }
    }


    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permissão já concedida
                getCurrentLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Explicar ao usuário por que a permissão é necessária (opcional)
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                // Pedir a permissão
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLocation = location
                    } else {
                        Toast.makeText(this, getString(R.string.erro_localizacao), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun salvarDadosPosto() {
        val nomePosto = etNomePosto.text.toString()
        val precoAlcoolStr = etPrecoAlcool.text.toString()
        val precoGasolinaStr = etPrecoGasolina.text.toString()

        if (nomePosto.isBlank() || precoAlcoolStr.isBlank() || precoGasolinaStr.isBlank()) {
            Toast.makeText(this, getString(R.string.erro_salvar_dados), Toast.LENGTH_LONG).show()
            return
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dataAtual = dateFormat.format(Date())

        val precoAlcool = precoAlcoolStr.toDouble()
        val precoGasolina = precoGasolinaStr.toDouble()

        val melhorOpcao = if ((precoAlcool / precoGasolina) >= 0.7) {
            getString(R.string.gasolina)
        } else {
            getString(R.string.alcool)
        }

        val novoPosto = Posto(
            nome = nomePosto,
            precoAlcool = precoAlcool,
            precoGasolina = precoGasolina,
            dataRegistro = dataAtual,
            latitude = currentLocation?.latitude,
            longitude = currentLocation?.longitude,
            melhorOpcao = melhorOpcao // <-- ADICIONE ESTA LINHA
        )

        PostoRepository.addPosto(this, novoPosto)
        Toast.makeText(this, getString(R.string.dados_salvos_sucesso), Toast.LENGTH_SHORT).show()

        // Limpar campos após salvar
        etNomePosto.text.clear()
        etPrecoAlcool.text.clear()
        etPrecoGasolina.text.clear()
        tvResultado.text = ""
    }

    private fun updateSwitchText(isChecked: Boolean) {
        if (isChecked) {
            switchLembrar.text = getString(R.string.gasolina)
        } else {
            switchLembrar.text = getString(R.string.alcool)
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("Português", "English")
        val languageCodes = arrayOf("pt-BR", "en")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Escolha um idioma")
        builder.setItems(languages) { dialog, which ->
            val selectedLocale = languageCodes[which]
            // Define o idioma para o aplicativo
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(selectedLocale)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
        builder.show()
    }
}