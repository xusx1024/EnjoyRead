package com.xilin.enjoyread.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build


/** SharedPreferences 工具类
 * Created by Administrator on 2017/9/17.
 */
class SharedPreferencesUtil private constructor() {
    var context: Context? = null
    var prefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    private object Holder {
        val INSTANCE = SharedPreferencesUtil()
    }

    companion object {
        val instance: SharedPreferencesUtil by lazy { Holder.INSTANCE }

        fun init(context: Context, prefersname: String, mode: Int) {
            instance.context = context
            instance.prefs = instance.context!!.getSharedPreferences(prefersname, mode)
            instance.editor = instance.prefs!!.edit()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return this.prefs!!.getBoolean(key, defaultValue)
    }

    fun getBoolean(key: String): Boolean {
        return this.prefs!!.getBoolean(key, false)
    }

    fun getString(key: String, defaultStr: String): String {
        return this.prefs!!.getString(key, defaultStr)
    }

    fun getString(key: String): String {
        return this.prefs!!.getString(key, null)
    }

    fun getInt(key: String, defaultInt: Int): Int {
        return this.prefs!!.getInt(key, defaultInt)
    }

    fun getInt(key: String): Int {
        return this.prefs!!.getInt(key, 0)
    }

    fun getFloat(key: String, defaultFloat: Float): Float {
        return this.prefs!!.getFloat(key, defaultFloat)
    }

    fun getFloat(key: String): Float {
        return this.prefs!!.getFloat(key, 0F)
    }

    fun getLong(key: String, defaultLong: Long): Long {
        return this.prefs!!.getLong(key, defaultLong)
    }

    fun getLong(key: String): Long {
        return this.prefs!!.getLong(key, 0L)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String, defaultValue: Set<String>): Set<String> {
        return this.prefs!!.getStringSet(key, defaultValue)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String): Set<String> {
        return this.prefs!!.getStringSet(key, null)
    }

    /**
     * 此处的* 和Any? 都可以
     */
    fun getAll(): Map<String, *> {
        return this.prefs!!.all
    }

    fun exists(key: String): Boolean {
        return this.prefs!!.contains(key)
    }

    fun putString(key: String, value: String): SharedPreferencesUtil {
        editor!!.putString(key, value)
        return this
    }

    fun putInt(key: String, value: Int): SharedPreferencesUtil {
        editor!!.putInt(key, value)
        return this
    }

    fun putBoolean(key: String, value: Boolean): SharedPreferencesUtil {
        editor!!.putBoolean(key, value)
        return this
    }

    fun putFloat(key: String, value: Float): SharedPreferencesUtil {
        editor!!.putFloat(key, value)
        return this
    }

    fun putLong(key: String, value: Long): SharedPreferencesUtil {
        editor!!.putLong(key, value)
        return this
    }

    fun putStringSet(key: String, value: Set<String>): SharedPreferencesUtil {
        editor!!.putStringSet(key, value)
        return this
    }

    fun putObject(key: String, value: Any): SharedPreferencesUtil {
        return this
    }


    fun commit() {
        editor!!.commit()
    }


}
