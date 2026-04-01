package com.lihan.qrcraft.scan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import com.lihan.qrcraft.scan.domain.repository.ScanRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ScanViewModel(
    private val repository: ScanRepository
): ViewModel() {

    private val _uiEvent = Channel<ScanUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(ScanState())
    val state = _state.asStateFlow()

    fun onAction(action: ScanAction){
        when(action){
            ScanAction.CloseAppClick -> Unit
            ScanAction.GrantAccessClick -> Unit
            ScanAction.DismissCameraPermissionDialog -> {
                _state.update { it.copy(
                    isShowCameraPermissionDialog = false
                ) }
            }
            ScanAction.ShowCameraPermissionDialog ->{
                _state.update { it.copy(
                    isShowCameraPermissionDialog = true
                ) }
            }

            is ScanAction.ScanFailed -> scanFailed(action.exception)
            is ScanAction.ScanSuccess -> scanSuccess(action.barcodes)
        }
    }

    private fun scanFailed(exception: Exception){
        exception.printStackTrace()
    }

    private fun scanSuccess(barcodes: List<Barcode>){
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }

            val barcode = barcodes.first()
            val type = barcode.valueType

            val content = when(type){
                Barcode.TYPE_URL -> {
                    barcode.url?.url?:""
                }
                Barcode.TYPE_PHONE,->{
                    "${barcode.phone?.type} ${barcode.phone?.number}"
                }
                Barcode.TYPE_GEO -> {
                    "${barcode.geoPoint?.lat},${barcode.geoPoint?.lng}"
                }
                Barcode.TYPE_WIFI -> {
                    """
                SSID: ${barcode.wifi?.ssid}
                Password:${barcode.wifi?.password}
                Encryption type:${barcode.wifi?.encryptionType}
                """.trimIndent()

                }
                Barcode.TYPE_CONTACT_INFO ->{
                    """
                ${barcode.contactInfo?.name}
                ${barcode.contactInfo?.emails}
                ${barcode.contactInfo?.phones?.joinToString(" ")}"
                """.trimIndent()
                }
                else -> {
                    barcode.rawValue?:""
                }
            }

            delay(2000L)

            repository.upsertQRCodeData(
                QRCodeHistory(
                    type = type,
                    content = content,
                    createdAt = Instant.now().toEpochMilli(),
                    isGenerated = false,
                    isFavorite = false
                )
            )

            _uiEvent.send(
                ScanUiEvent.ScanSuccessToResult(
                    type = type,
                    content = content
                )
            )
            delay(300L)
            _state.update { it.copy(
                isLoading = false
            ) }
        }


    }

}