package i.progress.tech.lsn_remote_fttp_client

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import i.progress.tech.lsn_remote_fttp_client.ui.theme.CianUse
import i.progress.tech.lsn_remote_fttp_client.ui.theme.Neutral10
import i.progress.tech.lsn_remote_fttp_client.ui.theme.YellowDir
import i.progress.tech.lsn_remote_fttp_client.ui.theme.styleLargeText
import org.apache.commons.net.ftp.FTPFile

@Composable
fun ListingFiles(
    list: List<FTPFile>,
    selectedFiles: List<FTPFile>,
    callback: (FTPFile) -> Unit
) {
    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            LazyColumn(modifier = Modifier.background(Neutral10)) {
                items(items = list) {
                    Text(modifier = Modifier
                        .clickable {
                            Log.e(FTP_LOG, ".clickable  | ${it.isDirectory}  ${it.name}")
                            callback.invoke(it)
                        }
                        .background(
                            if (it.isDirectory) YellowDir else {
                                if (selectedFiles.contains(it)) CianUse else
                                    Neutral10
                            }
                        )
                        .padding(4.dp),
                        text = it.name,
                        color = if (it.isDirectory) Neutral10 else {
                            if (selectedFiles.contains(it)) Color.DarkGray else
                                Color.White
                        },
                        style = styleLargeText
                    )
                }
            }
        }
    }
}