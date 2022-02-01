package com.goodwy.audiobook.misc

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import com.goodwy.audiobook.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//private const val PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

class PermissionHelper(private val activity: Activity, private val permissions: Permissions) {

  private val PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_EXTERNAL_STORAGE else Manifest.permission.WRITE_EXTERNAL_STORAGE

  fun storagePermission(gotPermission: () -> Unit = {}) {
    val root = activity.findViewById<View>(android.R.id.content)
    @Suppress("CheckResult")
    GlobalScope.launch(Dispatchers.Main) {
      when (permissions.request(PERMISSION)) {
        Permissions.PermissionResult.GRANTED -> gotPermission()
        Permissions.PermissionResult.DENIED_FOREVER -> handleDeniedForever(root)
        Permissions.PermissionResult.DENIED_ASK_AGAIN -> showRationale(root) {
          storagePermission(gotPermission)
        }
      }
    }
  }

  private fun showRationale(root: View, listener: () -> Unit) {
    val context = root.context
    Snackbar.make(root, context.getString(R.string.permission_external_new_explanation), BaseTransientBottomBar.LENGTH_LONG)
      .setAction(context.getString(R.string.permission_retry)) {
        listener()
      }
      .show()
  }

  private fun handleDeniedForever(root: View) {
    val context = root.context
    Snackbar.make(root, context.getString(R.string.permission_external_new_explanation), BaseTransientBottomBar.LENGTH_LONG)
      .setAction(root.context.getString(R.string.permission_goto_settings)) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
      }
      .show()
  }
}
