package i.progress.tech.lsn_remote_fttp_client

import android.content.Context
import android.util.Log
import android.widget.Toast
import i.progress.tech.lsn_remote_fttp_client.StateHolderFtpClient.PAGE_SIZE
import i.progress.tech.lsn_remote_fttp_client.StateHolderFtpClient.PARENT_DIR
import i.progress.tech.lsn_remote_fttp_client.StateHolderFtpClient.password
import i.progress.tech.lsn_remote_fttp_client.StateHolderFtpClient.port
import i.progress.tech.lsn_remote_fttp_client.StateHolderFtpClient.root
import i.progress.tech.lsn_remote_fttp_client.StateHolderFtpClient.server
import i.progress.tech.lsn_remote_fttp_client.StateHolderFtpClient.user
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.PrintCommandListener
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import org.apache.commons.net.ftp.FTPListParseEngine
import org.apache.commons.net.ftp.FTPReply
import java.io.IOException
import java.io.OutputStream
import java.io.PrintWriter
import java.util.Arrays
import java.util.stream.Collectors


const val FTP_LOG = "FTP_LOG"

class FtpClient {
    private var ftp: FTPClient? = null


    fun listFiles(path: String?): List<String>? {
        Log.i(FTP_LOG, "FtpClient | fun listFiles ...")
        return try {
            val files = ftp?.listFiles(path)
            val res = files?.let {
                Arrays.stream(it)
                    .map { obj: FTPFile -> obj.name }
                    .collect(Collectors.toList())
            }
            Log.i(FTP_LOG, "FtpClient | fun listFiles() | ftp $ftp | list $res")
            res
        } catch (e: Throwable) {
            Log.e(FTP_LOG, "Error FtpClient | fun listFiles() | ftp $ftp")
            StateHolderFtpClient.setIsConnect(false)
            emptyList()
        }
    }

    suspend fun downloadAndSaveFile(
        catalog: String,
        filename: String,
        applicationContext: Context
    ) {
        withContext(Dispatchers.IO) {
            try {
                ftp?.setFileType(FTP.BINARY_FILE_TYPE)
                Log.d(LOG_SAVE_FILE, "Downloading")
                ftp?.enterLocalPassiveMode()
                var outputStream: OutputStream? = null
                var success = false
                try {
                    outputStream = getFileOutputSteam(applicationContext, filename)
                    success = ftp?.retrieveFile("$catalog$filename", outputStream) ?: false
                    Log.i(LOG_SAVE_FILE, "Downloading success")
                } catch (e: Throwable) {
                    Log.i(LOG_SAVE_FILE, "Downloading ERROR | $e")
                } finally {
                    outputStream?.close()
                }
                success
            } catch (e: Throwable) {
                Log.i(LOG_SAVE_FILE, "Download file $filename ERROR! $e")
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Download file $$catalog$filename ERROR!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun open(context: Context) {
        withContext(Dispatchers.Main) {
            Log.i(FTP_LOG, "FtpClient | fun open() ...")
        }
        withContext(Dispatchers.IO) {

            ftp = FTPClient()
            ftp?.addProtocolCommandListener(PrintCommandListener(PrintWriter(System.out)))
            try {
                ftp?.connect(server, port)
                withContext(Dispatchers.Main) {
                    Log.e(FTP_LOG, "Success FtpClient | ftp?.connect Success")
                    StateHolderFtpClient.setIsConnect(true)
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    Log.e(FTP_LOG, "Error FtpClient | ftp?.connect $t")
                    Toast.makeText(context, "Error FtpClient | ftp?.connect $t", Toast.LENGTH_LONG).show()
                    StateHolderFtpClient.setIsConnect(false)
                }
            }
            val reply: Int = ftp?.replyCode ?: -1
            withContext(Dispatchers.Main) {
                Log.i(FTP_LOG, "FtpClient | fun open() | reply $reply")
            }
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp?.disconnect()
                StateHolderFtpClient.setIsConnect(false)
                Log.e(FTP_LOG, "FtpClient | Exception in connecting to FTP Server")
            }
            try {
                val isLogin = ftp?.login(user, password)
                withContext(Dispatchers.Main) {
                    Log.e(FTP_LOG, "isLogin $isLogin")
                }
                parsingEngine(root)
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    Log.e(FTP_LOG, "Error FtpClient | LOGIN ERROR $t")
                    Toast.makeText(context, "Error FtpClient | LOGIN ERROR $t", Toast.LENGTH_LONG).show()
                    StateHolderFtpClient.setIsConnect(false)
                }
            }
        }
    }

    suspend fun parsingEngine(path: String) {
        Log.e(FTP_LOG, "FTPListParseEngine | suspend fun parsingEngine(path: String) $path ")
        withContext(Dispatchers.IO) {
            val engine: FTPListParseEngine? = ftp?.initiateListParsing(path)
            val list = mutableListOf<FTPFile>(FTPFile().apply {
                name = PARENT_DIR
            })

            while (engine?.hasNext() == true) {
                val files = engine.getNext(PAGE_SIZE) // "page size" you want
                // do whatever you want with these files, display them, etc.
                // expensive FTPFile objects not created until needed.
                files.forEach {
                    Log.e(FTP_LOG, "FTPListParseEngine |  isDirectory ${it.isDirectory} | name ${it.name}")
                }
                if (files != null) list.addAll(files)
            }
            StateHolderFtpClient.setDirList(list)
        }
    }

    @Throws(IOException::class)
    fun close() {
        ftp?.disconnect()
        StateHolderFtpClient.setIsConnect(false)
    }
}