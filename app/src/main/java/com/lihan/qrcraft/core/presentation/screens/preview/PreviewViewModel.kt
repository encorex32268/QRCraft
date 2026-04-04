package com.lihan.qrcraft.core.presentation.screens.preview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.core.domain.repository.DefaultClipboard
import com.lihan.qrcraft.core.domain.repository.FileManager
import com.lihan.qrcraft.core.domain.repository.HistoryRepository
import com.lihan.qrcraft.core.presentation.mapper.toDomain
import com.lihan.qrcraft.core.presentation.mapper.toUi
import com.lihan.qrcraft.history.presentation.ScanHistoryUiEvent
import io.github.alexzhirkevich.qrose.ImageFormat
import io.github.alexzhirkevich.qrose.QrCodePainter
import io.github.alexzhirkevich.qrose.options.QrBackground
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrCodeShape
import io.github.alexzhirkevich.qrose.options.QrColors
import io.github.alexzhirkevich.qrose.options.QrLogo
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrOptions
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.toByteArray
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreviewViewModel(
    private val clipboard: DefaultClipboard,
    private val savedStateHandle: SavedStateHandle,
    private val repository: HistoryRepository,
    private val fileManager: FileManager
): ViewModel(){

    private val route = savedStateHandle.toRoute<Route.Preview>()

    private var originTitle = ""

    private var hasLoadedInitialData = false

    private val _uiEvent = Channel<PreviewUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(PreviewState(screenTitle = route.screenTitle))
    val state = _state
        .onStart {
            if (!hasLoadedInitialData){
                setupData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            PreviewState(screenTitle = route.screenTitle)
        )


    fun onAction(action: PreviewAction){
        when(action){
            PreviewAction.CopyClick -> copyToClipboard()
            PreviewAction.BackClick -> backClick()
            PreviewAction.SaveClick -> saveQRCode()
            PreviewAction.FavoriteClick -> favoriteClick()
            PreviewAction.ShareClick -> shareQRCode()
            else -> Unit
        }
    }

    private fun shareQRCode() {
        val currentState = state.value
        val qrCodeHistoryUi = currentState.qrCodeHistoryUi ?: return

        viewModelScope.launch {

            val title = qrCodeHistoryUi.title?: QRCodeType.getQRCodeType(qrCodeHistoryUi.type).name
            _uiEvent.send(
                PreviewUiEvent.ShareQRCode(
                    title = title,
                    content = qrCodeHistoryUi.content
                )
            )
        }
    }


    private fun setupData(){
        val id = route.id

        viewModelScope.launch {
            repository
                .getHistoryById(id)
                .filterNotNull()
                .onEach { qRCodeHistory ->
                    _state.update { it.copy(
                        qrCodeHistoryUi = qRCodeHistory.toUi()
                    ) }
                }
                .launchIn(viewModelScope)

        }
    }

    private fun favoriteClick(){
        viewModelScope.launch {
            val currentQRCodeHistoryUi = state.value.qrCodeHistoryUi
            if (currentQRCodeHistoryUi == null){
                _uiEvent.send(PreviewUiEvent.DataError)
            }else{
               repository.updateFavoriteStatus(
                   id = currentQRCodeHistoryUi.id,
                   isFavorite = !currentQRCodeHistoryUi.isFavorite
               )
            }
        }
    }

    private fun copyToClipboard() {
        val currentQRCodeHistoryUi = state.value.qrCodeHistoryUi
        if (currentQRCodeHistoryUi == null){
            viewModelScope.launch {
                _uiEvent.send(PreviewUiEvent.DataError)
            }
        }else{
            if (currentQRCodeHistoryUi.type == QRCodeType.WiFi.type) {
                val content = currentQRCodeHistoryUi.content
                val password = content
                    .split("\n").getOrNull(1)
                    ?.split(":")?.getOrNull(1)?:content
                clipboard.copyText(password)
            }else{
                clipboard.copyText(currentQRCodeHistoryUi.content)
            }
        }
    }

    private fun backClick(){
        viewModelScope.launch {
            val currentState = state.value
            val currentItem = currentState.qrCodeHistoryUi
            val currentTitle = currentState.title.text.toString()

            if (currentItem == null){
                _uiEvent.send(PreviewUiEvent.DataError)
                return@launch
            }
            if (currentTitle != originTitle){
                repository.upsert(
                    qrCodeHistory = currentItem.copy(
                        title = currentTitle
                    ).toDomain()
                )
            }
            _uiEvent.send(PreviewUiEvent.Back)
        }
    }

    private fun saveQRCode(){

        val currentState = state.value

        if (currentState.qrCodeHistoryUi == null){
            return
        }

        if (currentState.qrCodeHistoryUi.content.isEmpty()){
            return
        }

        val content = currentState.qrCodeHistoryUi.content

        val painter = QrCodePainter(
            data = content,
            options = QrOptions(
                background = QrBackground(
                    fill = SolidColor(Color.White)
                ),
                colors = QrColors(
                    dark = QrBrush.solid(Color.Black),
                    light = QrBrush.solid(Color.White)
                )
            )
        )
        viewModelScope.launch{
            val srcByteArray = painter.toByteArray(512,512)
            val hasPaddingBitmapByteArray = createQRCodeImageWithPadding(srcByteArray)?: return@launch

            fileManager.saveFile(hasPaddingBitmapByteArray)

            _uiEvent.send(PreviewUiEvent.SaveSucceed)
        }
    }
}













