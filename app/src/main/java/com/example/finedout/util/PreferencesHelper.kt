package com.example.finedout.util

import android.content.Context
import com.example.finedout.Person
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferencesHelper {
    private const val PREFS_NAME = "FinedOutPrefs"
    private const val KEY_PEOPLE = "people"

    fun savePeople(context: Context, people: List<Person>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(people)
        prefs.edit().putString(KEY_PEOPLE, json).apply()
    }

    fun loadPeople(context: Context): List<Person> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_PEOPLE, null)
        return if (json != null) {
            val type = object : TypeToken<List<Person>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }
}