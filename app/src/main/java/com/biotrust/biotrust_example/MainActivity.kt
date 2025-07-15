package com.biotrust.biotrust_example

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.biotrust.biometricfacevalidator.BiometricValidationLauncher
import br.com.biotrust.biometricfacevalidator.FaceBiometricConfig
import br.com.biotrust.biometricfacevalidator.model.ValidationMode
import br.com.biotrust.biometricfacevalidator.model.ValidationResult
import com.biotrust.biotrust_example.ui.theme.BioTrustExampleTheme


@ExperimentalGetImage
class MainActivity : AppCompatActivity() {

    private lateinit var validationLauncher: BiometricValidationLauncher
    private val uuid = "ADICIONE SEU UUID FORNECIDO PELO BIOTRUST AQUI"
    private val apiUrl = "https://api.biotrust.com.br"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Criar configuração básica
        val baseConfig = FaceBiometricConfig.Builder()
            .setUuid(uuid)
            .setApiUrl(apiUrl)
            .setLocale("pt-BR")
            .build()


        // Inicializar o launcher de validação com a configuração
        // IMPORTANTE: Isso deve ser feito apenas UMA VEZ no onCreate
        validationLauncher = BiometricValidationLauncher(
            this,
            createValidationCallback(),
            baseConfig
        )



        enableEdgeToEdge()
        setContent {
            BioTrustExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        validationLauncher.setValidationMode(ValidationMode.LIVENESS_ONLY)
        validationLauncher.setDocumentInfo(null) // Limpar informações de documento
        validationLauncher.launch(this)
    }
}


@ExperimentalGetImage
private fun createValidationCallback(): BiometricValidationLauncher.ValidationResultCallback {
    return object : BiometricValidationLauncher.ValidationResultCallback {
        override fun onValidationComplete(result: ValidationResult?) {

            if(result != null && result.isSuccess){

                if (result.documentResult != null) {

                    val docResult = result.documentResult
                    Log.d("Validation", "Nome: " + docResult.nome)
                    Log.d("Validation", "CPF: " + docResult.documento)
                    Log.d("Validation", "Liveness confidence: " + result.vivacityConfidence)
                    Log.d(
                        "Validation",
                        "Similaridade: " + docResult.biometriaFacial.similaridade + "%"
                    )
                    Log.d(
                        "Validation",
                        "Disponível: " + docResult.biometriaFacial.isDisponivel
                    )

                } else {

                    Log.d("Validation", "Liveness confidence: " + result.vivacityConfidence)

                }

            }else{
                Log.d("Validation", "result: " + result?.message)
            }

        }

    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "BioTrust Usage Example",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BioTrustExampleTheme {
        Greeting()
    }
}