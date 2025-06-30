package com.luis.caiza.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    lateinit var manejadorArchivoShared: FileHandler
    lateinit var manejadorArchivoEncriptado: FileHandler
    lateinit var manejadorArchivoInterno: FileHandler
    lateinit var manejadorArchivoExterno: FileHandler
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword:EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser:Button
    lateinit var checkBoxRecordarme: CheckBox
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        //Inicialización de variables
        manejadorArchivoShared = SharedPreferencesManager(this)
        manejadorArchivoEncriptado = EncryptedSharedPreferencesManager(this)
        manejadorArchivoInterno = FileInternalManager(this)
        //manejadorArchivoExterno = ExternalFileManager(this)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)

        LeerDatosDePreferencias()
        //Eventos clic
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val clave = editTextPassword.text.toString()
            //Validaciones de datos requeridos y formatos
            if(!validateRequiredData())
                return@setOnClickListener
            //Guardar datos en preferencias.
            GuardarDatosEnPreferencias()
            //Si pasa validación de datos requeridos, ir a pantalla principal
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_LOGIN, email)
            startActivity(intent)
            finish()
        }
        buttonNewUser.setOnClickListener{

        }
        mediaPlayer=MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.start()
    }
    private fun validateRequiredData():Boolean{
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.error_email_required))
            editTextEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.error_password_required))
            editTextPassword.requestFocus()
            return false
        }
        if (password.length < 3) {
            editTextPassword.setError(getString(R.string.error_password_min_length))
            editTextPassword.requestFocus()
            return false
        }
        return true
    }

    private fun LeerDatosDePreferencias() {
        val datosShared = manejadorArchivoShared.ReadInformation()
        val datosEncriptados = manejadorArchivoEncriptado.ReadInformation()
        val datosArchivoInterno = manejadorArchivoInterno.ReadInformation()
        val datosArchivoExterno = manejadorArchivoExterno.ReadInformation()

        val email = when {
            datosEncriptados.first.isNotEmpty() -> datosEncriptados.first
            datosShared.first.isNotEmpty() -> datosShared.first
            datosArchivoInterno.first.isNotEmpty() -> datosArchivoInterno.first
            else -> datosArchivoExterno.first
        }

        val clave = when {
            datosEncriptados.second.isNotEmpty() -> datosEncriptados.second
            datosShared.second.isNotEmpty() -> datosShared.second
            datosArchivoInterno.second.isNotEmpty() -> datosArchivoInterno.second
            else -> datosArchivoExterno.second
        }

        editTextEmail.setText(email)
        editTextPassword.setText(clave)
        checkBoxRecordarme.isChecked = email.isNotEmpty() && clave.isNotEmpty()
    }

    private fun GuardarDatosEnPreferencias() {
        val email = editTextEmail.text.toString()
        val clave = editTextPassword.text.toString()
        val datos = if (checkBoxRecordarme.isChecked) {
            email to clave
        } else {
            "" to ""
        }

        manejadorArchivoShared.SaveInformation(datos)
        manejadorArchivoEncriptado.SaveInformation(datos)
        manejadorArchivoInterno.SaveInformation(datos)
        manejadorArchivoExterno.SaveInformation(datos)
    }


    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }
}

