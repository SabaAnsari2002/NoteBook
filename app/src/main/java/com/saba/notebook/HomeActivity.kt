package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity

class HomeActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: ArrayAdapter<String>
    private lateinit var notesList: MutableList<String>
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dbHelper = DatabaseHelper(this)

        // Assuming userId is passed from Login/Signup activity
        userId = intent.getIntExtra("USER_ID", -1)

        notesListView = findViewById(R.id.notes_list)
        notesList = dbHelper.getNotesByUserId(userId).toMutableList()
        notesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList)
        notesListView.adapter = notesAdapter

        notesListView.setOnItemClickListener { _, _, position, _ ->
            val noteDetails = notesList[position].split("\n")
            val noteTitle = noteDetails[0]
            val noteDate = noteDetails[1]

            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("NOTE_TITLE", noteTitle)
            intent.putExtra("NOTE_DATE", noteDate)
            startActivityForResult(intent, 1)
        }

        val addButton: Button = findViewById(R.id.add_note_button)
        addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivityForResult(intent, 1)
        }

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val btnLogout = findViewById<Button>(R.id.logout_button)

        btnLogout.setOnClickListener {
            // Clear login state
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val newNoteTitle = data?.getStringExtra("NEW_NOTE_TITLE")
            val newNoteDate = data?.getStringExtra("NEW_NOTE_DATE")
            val originalNoteTitle = data?.getStringExtra("ORIGINAL_NOTE_TITLE")

            if (!newNoteTitle.isNullOrEmpty() && !newNoteDate.isNullOrEmpty()) {
                val displayText = "$newNoteTitle\n$newNoteDate"

                if (!originalNoteTitle.isNullOrEmpty()) {
                    // Find the index of the original note and replace it
                    val index = notesList.indexOfFirst { it.startsWith(originalNoteTitle) }
                    if (index != -1) {
                        notesList[index] = displayText
                    }
                } else {
                    // Add new note
                    notesList.add(displayText)
                }

                notesAdapter.notifyDataSetChanged()
            }
        }
    }

}