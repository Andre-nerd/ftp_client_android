package i.progress.tech.lsn_remote_fttp_client

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.apache.commons.net.ftp.FTPFile

object StateHolderFtpClient {
    var server: String? = "192.168.157.93"
        //Notebook
        private set
    var user: String? = "anomius"
        private set
    var root = "/log"
    private set
    var password: String? = null
        private set

    var port = 21
        private set
    const val PAGE_SIZE = 25
    const val PARENT_DIR = "../"

//    var server: String? = "192.168.0.1" //ATB
//        private set
//    var user: String? = null
//        private set
//    var root = "/var/log/"
//        private set

    private val _isConnect = MutableStateFlow(false)
    val isConnect: StateFlow<Boolean>
        get() = _isConnect

    fun setIsConnect(v: Boolean) {
        _isConnect.value = v
    }

    private val _dirList = MutableStateFlow<List<FTPFile>>(emptyList())
    val dirList: StateFlow<List<FTPFile>>
        get() = _dirList

    fun setDirList(l: List<FTPFile>) {
        _dirList.value = l
    }
}