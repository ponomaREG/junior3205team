package com.team3205.junior.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.team3205.junior.R
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class DownloadForegroundWorkManager constructor(
    val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val notifyManager = context.getSystemService(NotificationManager::class.java)

    companion object{
        const val KEY_URL = "URL"
        const val KEY_NAME = "NAME"
        const val KEY_ID = "ID"
        const val KEY_PROGRESS = "PROGRESS"
        const val CHANNEL_ID = "Download repos in foreground"
        const val SUCCESS_NOTIFICATION_ID = 1
        const val TAG = "ForegroundDownloader"
    }

    override fun doWork(): Result {
        val url = inputData.getString(KEY_URL) ?: return Result.failure()
        val name = inputData.getString(KEY_NAME) ?: return Result.failure()
        val notificationId = inputData.getInt(KEY_ID , 1)
        createNotificationChannelIfNeed()
        val foregroundNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Попытка скачивания $name...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()
        val foregroundInfo = ForegroundInfo(notificationId, foregroundNotification)
        setForegroundAsync(foregroundInfo)
        try {
            downloadFile(url, name, notificationId)
        }catch (exc: IOException){
            return Result.failure()
        }
        showSuccessNotification(name)

        return Result.success()
    }

    private fun createNotificationChannelIfNeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notifyManager?.getNotificationChannel(CHANNEL_ID)
            if (notificationChannel == null) {
                notificationChannel = NotificationChannel(
                        CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_LOW
                )
                notifyManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun downloadFile(url: String, name: String, notificationId: Int){
        val urlObj = URL(url)
        val connection: HttpURLConnection = urlObj.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val fileLength: Int = connection.contentLength
        val path: String = getPathForSave(name)
        val input: InputStream = BufferedInputStream(connection.inputStream)
        val output: OutputStream = FileOutputStream(path)
        val data = ByteArray(1024)
        var total: Long = 0
        var count: Int

        while (input.read(data).also { count = it } != -1) {
            total += count.toLong()
            val progress = ((total * 100) / fileLength).toInt()
            showProgress(
                    progress,
                    name,
                    notificationID = notificationId,
                    indeterminate = fileLength < 0 //Периодически приходит размер получаемого файла равный -1, поэтому ломается прогресс
            )
            output.write(data, 0, count)
            TimeUnit.MILLISECONDS.sleep(100)
        }
        output.flush()
        output.close()
        input.close()
    }

    private fun getPathForSave(name: String): String{
        return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "$name.zip"
            ).toString()
        }else{
            val fileCollection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val contentDetails = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "$name.zip")
                put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/repositories")
            }
            val resolver = context.contentResolver
            val contentURI = resolver.insert(fileCollection, contentDetails)
            (contentURI?.path ?: context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath).toString()
        }
    }

    private fun showProgress(progress: Int,
                             name: String,
                             indeterminate: Boolean,
                             notificationID: Int
    ) {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Скачивание $name ...")
                .setProgress(100, progress, indeterminate)
                .build()
        if(indeterminate) setProgressAsync(Data.Builder().putInt(KEY_PROGRESS, -1).build())
        else setProgressAsync(Data.Builder().putInt(KEY_PROGRESS,progress).build())
        notifyManager?.notify(notificationID, notification)
    }

    private fun showSuccessNotification(name: String){
        val finNotify = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Успешно скачен $name!")
                .build()
        notifyManager.notify(SUCCESS_NOTIFICATION_ID,finNotify)
    }
}