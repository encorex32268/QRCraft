package com.lihan.qrcraft.core.domain.repository

class FakeClipboard : DefaultClipboard {
    var copiedText: String? = null
    override fun copyText(text: String) {
        copiedText = text
    }
}
