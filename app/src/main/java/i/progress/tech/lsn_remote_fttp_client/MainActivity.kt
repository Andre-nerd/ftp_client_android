package i.progress.tech.lsn_remote_fttp_client

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import i.progress.tech.lsn_remote_fttp_client.StateHolderFtpClient.PARENT_DIR
import i.progress.tech.lsn_remote_fttp_client.ui.theme.Fttp_clientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Fttp_clientTheme {
                val ftpViewModel: FTPViewModel by viewModels()
                ftpViewModel.initFTPConnect()
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DownLoadLogFileScreen(ftpViewModel)
                }
            }
        }
    }
}

@Composable
fun DownLoadLogFileScreen(ftpViewModel: FTPViewModel) {
    val isConnect = StateHolderFtpClient.isConnect.collectAsState()
    val listFiles = StateHolderFtpClient.dirList.collectAsState()
    val selectedFiles = ftpViewModel.selectedFiles.collectAsState()
    var catalog by rememberSaveable { mutableStateOf(StateHolderFtpClient.root) }
    val color = if (isConnect.value) Color.Green else Color.Red
    Column {
        Text(catalog)

        Box {
            ListingFiles(selectedFiles = selectedFiles.value, list = listFiles.value) { it ->
                Log.e(FTP_LOG, "ListingFiles | listFiles.value${listFiles.value}")
                Log.e(FTP_LOG, "ListingFiles | fileForLoad?.isDirectory ${it?.isDirectory == true}  ${it?.name}")
                if (it?.isDirectory == true) {
                    ftpViewModel.clearSelectedFiles()
                    catalog = "$catalog/${it.name}"
                    ftpViewModel.getAllFilesInDir(catalog)
                    Log.i(FTP_LOG, " catalog = it?.isDirectory == true $catalog")
                    return@ListingFiles
                }
                if (it.name.equals(PARENT_DIR)) {
                    val list = catalog.split("/").toMutableList()
                    list.removeLast()
                    val builder = StringBuilder()
                    list.forEach { path ->
                        builder.append(path)
                    }
                    ftpViewModel.clearSelectedFiles()
                    catalog = builder.toString()
                    Log.i(FTP_LOG, " catalog = builder.toString() $catalog")
                    ftpViewModel.getAllFilesInDir("$catalog")
                    return@ListingFiles
                }
                ftpViewModel.handlerSelectedFile(it)
            }
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, start = 4.dp, end = 4.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { ftpViewModel.downLoadLogFile(catalog = catalog) }) {
                        Text("Загрузить файлы")
                    }
                    Button(onClick = {
                        ftpViewModel.initFTPConnect()
                    }) {
                        Row {
                            Text("FTP Сервер")
                            Spacer(modifier = Modifier.size(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(color, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}
