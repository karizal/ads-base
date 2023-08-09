package com.karizal.ads_base.pref

import android.content.Context
import com.google.gson.Gson
import com.karizal.ads_base.AdsBaseConst

class AdsBasePref(context: Context) {
    private val pref = context.getSharedPreferences(AdsBaseConst.ADS_PREF, Context.MODE_PRIVATE)
    fun clear() {
        pref.edit().clear().apply()
    }

    fun clearContains(key: String) {
        val keys = pref.all.keys.filter { it.contains(key) }
        keys.forEach {
            pref.edit().remove(it).apply()
        }
    }

    fun saveString(key: String, value: String) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String = pref.getString(key, "") ?: ""

    fun saveInt(key: String, value: Int) {
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun saveLong(key: String, value: Long) {
        val editor = pref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String): Long = pref.getLong(key, 0)

    fun getInt(key: String): Int = pref.getInt(key, 0)
    fun getInt(key: String, default: Int = 0): Int = pref.getInt(key, default)

    fun getBoolean(key: String): Boolean = pref.getBoolean(key, false)

    fun <T> getObject(key: String, clazz: Class<T>): T? {
        val json = pref.getString(key, "{}")
        return try {
            Gson().fromJson(json, clazz)
        } catch (e: Exception) {
            null
        }
    }

    fun saveBoolean(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun saveObject(key: String, value: Any) {
        val editor = pref.edit()
        editor.putString(key, Gson().toJson(value))
        editor.apply()
    }
}