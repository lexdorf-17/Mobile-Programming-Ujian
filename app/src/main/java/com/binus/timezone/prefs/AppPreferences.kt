package com.binus.timezone.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class AppPreferences(context: Context) {

    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var pref : SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "timezone",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val editor : SharedPreferences.Editor = pref.edit()

    fun clearPreferences() {
        editor.apply{
            clear()
            apply()
        }
    }

    fun setDatetime(value: String) = editor.putString(Constants.PREF_DATETIME, value).apply()
    fun getDatetime(): String? = pref.getString(Constants.PREF_DATETIME, "")
    fun removeDatetime() = editor.remove(Constants.PREF_DATETIME).commit()

    fun setImage(value: String) = editor.putString(Constants.PREF_IMAGE, value).apply()
    fun getImage(): String? = pref.getString(Constants.PREF_IMAGE, "")
    fun removeImage() = editor.remove(Constants.PREF_IMAGE).commit()

    fun setLatlang(value: String) = editor.putString(Constants.PREF_LATLANG, value).apply()
    fun getLatlang(): String? = pref.getString(Constants.PREF_LATLANG, "")
    fun removeLatlang() = editor.remove(Constants.PREF_LATLANG).commit()

    fun setTimezone(value: String) = editor.putString(Constants.PREF_TIMEZONE, value).apply()
    fun getTimezone(): String? = pref.getString(Constants.PREF_TIMEZONE, "")
    fun removeTimezone() = editor.remove(Constants.PREF_TIMEZONE).commit()

    fun setUtcDatetime(value: String) = editor.putString(Constants.PREF_UTC_DATETIME, value).apply()
    fun getUtcDatetime(): String? = pref.getString(Constants.PREF_UTC_DATETIME, "")
    fun removeUtcDatetime() = editor.remove(Constants.PREF_UTC_DATETIME).commit()

    fun setUtcOffset(value: String) = editor.putString(Constants.PREF_UTC_OFFSET, value).apply()
    fun getUtcOffset(): String? = pref.getString(Constants.PREF_UTC_OFFSET, "")
    fun removeUtcOffset() = editor.remove(Constants.PREF_UTC_OFFSET).commit()
}