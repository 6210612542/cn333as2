package com.example.mynotes.ui.main

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mynotes.models.TaskList
import java.lang.ClassCastException

class MainViewModel(val sharedPreferences: SharedPreferences) : ViewModel() {
    lateinit var note: TaskList
    lateinit var onListAdded: (() -> Unit)
    lateinit var removeNote: () -> Unit
    var decreaseOne: Int = -1

    val lists: MutableList<TaskList> by lazy {
        retrieveLists()
    }

    private fun retrieveLists(): MutableList<TaskList> {
        val sharedPreferencesContents = sharedPreferences.all
        val note = ArrayList<TaskList>()

        for (noteList in sharedPreferencesContents) {
            try {
                val keyNote = TaskList(noteList.key,noteList.value as String)
                note.add(keyNote)
            } catch (e: ClassCastException){
            }
        }
        return note
    }

    fun saveList(note: TaskList) {
        val editor = sharedPreferences.edit()
        val text: String = note.tasks
        editor.putString(note.name, text)
        editor.apply()
    }

    fun removeList(note: TaskList) {
        val deleteindex = lists.indexOf(note)
        decreaseOne = deleteindex
        lists.remove(note)
        removeNote.invoke()

        val editor = sharedPreferences.edit()
        editor.remove(note.name)
        editor.apply()
    }

    fun updateList(note: TaskList) {
        val editor = sharedPreferences.edit()
        val text: String = note.tasks
        editor.putString(note.name, text)
        editor.apply()
        refreshLists()
    }

    fun createList(note: TaskList) {
        val create = sharedPreferences.edit()
        val text: String = note.tasks
        create.putString(note.name, text)
        create.apply()
        lists.add(note)
        onListAdded.invoke()
    }

    fun find(key: String):Boolean {
        return sharedPreferences.contains(key)
    }

    fun refreshLists() {
        lists.clear()
        lists.addAll(retrieveLists())
    }




}