package net.mustelinae.drift

import kotlinx.browser.window

class LocalStorage {
    companion object {
        var dropzone: Dropzone = window.localStorage.getItem("dropzone")?.let { lsDropzone ->
            dropzones.find { it.name == lsDropzone }
        } ?: dropzones[0]
            set(value) {
                field = value
                window.localStorage.setItem("dropzone", value.name)
            }
    }
}