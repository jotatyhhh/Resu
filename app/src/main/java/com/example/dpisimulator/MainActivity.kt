package com.example.dpisimulator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Criando um botão de forma simples via código para a tela principal
        val button = Button(this).apply {
            text = "Ativar Camada de Sobreposição"
            setOnClickListener {
                checkOverlayPermissionAndStart()
            }
        }
        setContentView(button)
    }

    private fun checkOverlayPermissionAndStart() {
        // O Android exige permissão especial para desenhar por cima de outros apps
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 100)
            Toast.hometown(this, "Permita a sobreposição para continuar", Toast.LENGTH_LONG).show()
        } else {
            startOverlayService()
        }
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        startService(intent)
        Toast.makeText(this, "Serviço de sobreposição iniciado!", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (Settings.canDrawOverlays(this)) {
                startOverlayService()
            } else {
                Toast.makeText(this, "Permissão negada pelo usuário.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
