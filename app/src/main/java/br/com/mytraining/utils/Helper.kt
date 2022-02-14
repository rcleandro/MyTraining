package br.com.mytraining.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object {
        val REQUISICAO_FOTO_CAMERA = 105

        fun formataData(data: Timestamp): String {
            val dataFormato = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val horaFormato = SimpleDateFormat("HH:mm", Locale.US)
            return "${dataFormato.format(data.time)} às ${horaFormato.format(data.time)}"
        }

        fun checarPermissoes(activity: Activity): Boolean {
            val camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)

            val read = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val listPermissionsNeeded: MutableList<String> = ArrayList()

            if (camera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA)
            }
            if (read != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (listPermissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    activity,
                    listPermissionsNeeded.toTypedArray(),
                    REQUISICAO_FOTO_CAMERA
                )
                return false
            }
            return true
        }
    }
}