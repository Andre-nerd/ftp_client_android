package i.progress.tech.lsn_remote_fttp_client

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.OutputStream

const val LOG_SAVE_FILE = "LOG_SAVE_FILE"
fun getFileOutputSteam(applicationContext: Context, filename: String): OutputStream? {
    if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
        Log.d(LOG_SAVE_FILE, "Downloading Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED")
        return null
    }
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
    }
    val dstUri = applicationContext.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
    return if (dstUri != null) {
        applicationContext.contentResolver.openOutputStream(dstUri)
    } else {
        Log.d(LOG_SAVE_FILE, "Downloading dstUri == null")
        null
    }
}

