package com.example.mynotes

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.mynotes.databinding.ListDetailActivityBinding
import com.example.mynotes.models.TaskList
import com.example.mynotes.ui.detail.ListDetailFragment
import com.example.mynotes.ui.main.MainViewModel
import com.example.mynotes.ui.main.MainViewModelFactory

class ListDetailActivity() : AppCompatActivity() {
    private lateinit var binding: ListDetailActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        ).get(MainViewModel::class.java)
        binding = ListDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.note = intent.getParcelableExtra(MainActivity.INTENT_NOTE_KEY)!!
        title = viewModel.note.name

        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val editNoteText: EditText = findViewById(R.id.editNote)
        val detail = sharedPreferences.getString(title as String, "not found")
        if (detail != null) {
            Log.d(TAG, detail)
            editNoteText.setText(detail)
        } else {
            Log.d(ContentValues.TAG, "broke")
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, ListDetailFragment.newInstance()).commitNow()
        }
    }


    override fun onBackPressed() {
        val note: EditText = findViewById(R.id.editNote)
        viewModel.saveList(TaskList(viewModel.note.name, note.text.toString()))
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_NOTE_KEY, viewModel.note)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}