package com.saba.notebook

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.saba.notebook.DatabaseHelper
import com.saba.notebook.R
import java.util.*

class AddNoteActivity : ComponentActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        titleEditText = findViewById(R.id.note_title)
        dateEditText = findViewById(R.id.note_date)
        messageEditText = findViewById(R.id.note_message)
        saveButton = findViewById(R.id.save_button)

        dbHelper = DatabaseHelper(this)

        // Set default date to current date
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        dateEditText.setText("$currentYear-${currentMonth + 1}-$currentDay")

        // Date picker for date input
        dateEditText.setOnClickListener {
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                dateEditText.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
            }, currentYear, currentMonth, currentDay).show()
        }

        saveButton.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = titleEditText.text.toString()
        val date = dateEditText.text.toString()
        val message = messageEditText.text.toString()

        if (title.isNotEmpty() && date.isNotEmpty() && message.isNotEmpty()) {
            val userId = intent.getIntExtra("USER_ID", -1)
            val noteId = dbHelper.addNote(userId, title, date, message)
            if (noteId > -1) {
                val resultIntent = Intent()
                resultIntent.putExtra("NEW_NOTE_TITLE", title)
                resultIntent.putExtra("NEW_NOTE_DATE", date)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        saveNote()  // سیو کردن نوت قبل از خروج
        super.onBackPressed()
    }
}
