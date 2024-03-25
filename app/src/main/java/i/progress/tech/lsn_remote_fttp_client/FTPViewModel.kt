package i.progress.tech.lsn_remote_fttp_client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.net.ftp.FTPFile

class FTPViewModel : ViewModel() {
    private var ftpClient: FtpClient? = null
    private val _selectedFiles = MutableStateFlow<List<FTPFile>>(emptyList())
    val selectedFiles: StateFlow<List<FTPFile>>
        get() = _selectedFiles

    fun initFTPConnect() {
        viewModelScope.launch {
            ftpClient?.close()
            ftpClient = FtpClient()
            ftpClient!!.open(App.appContext)
        }
    }

    fun getAllFilesInDir(path: String) {
        viewModelScope.launch {
            ftpClient?.parsingEngine(path)
        }
    }

    fun downLoadLogFile(catalog: String) {
        viewModelScope.launch {
            selectedFiles.value.forEach {
                ftpClient?.downloadAndSaveFile(catalog = catalog, filename = it.name, App.appContext)
            }
        }
    }

    fun closeFtpConnect() {
        ftpClient?.close()
        ftpClient = null
    }

    fun handlerSelectedFile(file: FTPFile) {
        val list = selectedFiles.value.toMutableList()
        if (list.contains(file)) {
            list.remove(file)
        } else {
            list.add(file)
        }
        _selectedFiles.value = list
        Log.e(FTP_LOG, "fun handlerSelectedFile  | ${selectedFiles.value.map { it.name }}")
    }

    fun clearSelectedFiles() {
        _selectedFiles.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        ftpClient?.close()
        ftpClient = null
    }
}