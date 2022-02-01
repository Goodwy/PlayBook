package com.goodwy.audiobook.misc

import android.annotation.SuppressLint
import android.os.Build

@SuppressLint("ObsoleteSdkInt")
val DARK_THEME_SETTABLE = Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN

/**
 * val DARK_THEME_SETTABLE = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
 * val DARK_THEME_SETTABLE = Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN*/
