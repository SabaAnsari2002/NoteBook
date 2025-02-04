package com.saba.notebook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HomeActivityTheme7 : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: NoteAdapter
    private lateinit var notesList: MutableList<String>
    private lateinit var deleteButton: Button
    private var userId: Int = -1
    private val selectedNotes = mutableSetOf<Int>()
    private var isSelectionMode = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home7)

        dbHelper = DatabaseHelper(this)
        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            // If USER_ID is not provided, redirect to login
            startActivity(Intent(this, LoginActivityTheme7::class.java))
            finish()
            return
        }
        notesListView = findViewById(R.id.notes_list)
        notesList = dbHelper.getNotesByUserId(userId).toMutableList()
        notesAdapter = NoteAdapter(this, R.layout.note_list_item, notesList)
        notesListView.adapter = notesAdapter

        deleteButton = findViewById(R.id.delete_button)
        deleteButton.visibility = View.GONE  // مخفی کردن دکمه حذف در ابتدا

        setupListeners()
        setupButtons()
        setupTouchListener()
    }

    private fun setupListeners() {
        notesListView.setOnItemLongClickListener { _, view, position, _ ->
            if (!isSelectionMode) {
                isSelectionMode = true
                selectedNotes.clear()
            }
            toggleNoteSelection(view, position)
            true
        }

        notesListView.setOnItemClickListener { _, view, position, _ ->
            if (isSelectionMode) {
                toggleNoteSelection(view, position)
            } else {
                openNoteDetails(position)
            }
        }
    }

    private fun toggleNoteSelection(view: View, position: Int) {
        val checkBox = view.findViewById<CheckBox>(R.id.note_checkbox)
        checkBox.visibility = View.VISIBLE
        if (selectedNotes.contains(position)) {
            selectedNotes.remove(position)
            checkBox.isChecked = false
        } else {
            selectedNotes.add(position)
            checkBox.isChecked = true
        }
        updateUIForSelectionMode()
    }

    private fun openNoteDetails(position: Int) {
        val noteDetails = notesList[position].split("\n")
        val noteTitle = noteDetails[0]
        val noteDate = noteDetails[1]

        val intent = Intent(this, AddNoteActivityTheme7::class.java)
        intent.putExtra("USER_ID", userId)
        intent.putExtra("NOTE_TITLE", noteTitle)
        intent.putExtra("NOTE_DATE", noteDate)
        startActivityForResult(intent, 1)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    private fun setupButtons() {
        deleteButton.setOnClickListener {
            if (selectedNotes.isNotEmpty()) {
                showDeleteConfirmationDialog()
            }
        }

        val addButton: Button = findViewById(R.id.add_note_button)
        addButton.setOnClickListener {
            exitSelectionMode()
            val intent = Intent(this, AddNoteActivityTheme7::class.java)
            intent.putExtra("USER_ID", userId)
            startActivityForResult(intent, 1)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val btnLogout = findViewById<Button>(R.id.logout_button)
        btnLogout.setOnClickListener {
            exitSelectionMode()

            // نمایش دیالوگ تأیید لاگ‌اوت
            AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    // انجام عملیات لاگ‌اوت
                    sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                    startActivity(Intent(this, MainActivityTheme7::class.java))
                    finish()
                }
                .setNegativeButton("No", null)  // بستن دیالوگ بدون انجام لاگ‌اوت
                .show()
        }

        findViewById<Button>(R.id.theme_button).setOnClickListener {
            val intent = Intent(this, ThemeSelectionActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupTouchListener() {
        val rootLayout: View = findViewById(android.R.id.content)
        rootLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (isSelectionMode) {
                    exitSelectionMode()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun exitSelectionMode() {
        if (isSelectionMode) {
            isSelectionMode = false
            selectedNotes.clear()
            notesAdapter.notifyDataSetChanged()
            updateUIForSelectionMode()
        }
    }

    private fun updateUIForSelectionMode() {
        deleteButton.visibility = if (isSelectionMode && selectedNotes.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Notes")
            .setMessage("Are you sure you want to delete the selected notes?")
            .setPositiveButton("Yes") { _, _ ->
                deleteSelectedNotes()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteSelectedNotes() {
        val notesToDelete = selectedNotes.map { notesList[it].split("\n")[0] }
        val deletedCount = dbHelper.deleteMultipleNotes(userId, notesToDelete)
        if (deletedCount > 0) {
            selectedNotes.sortedDescending().forEach { position ->
                notesList.removeAt(position)
            }
            selectedNotes.clear()
            notesAdapter.notifyDataSetChanged()
            isSelectionMode = false
            updateUIForSelectionMode()
            Toast.makeText(this, "$deletedCount notes deleted", Toast.LENGTH_SHORT).show()
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
                    val index = notesList.indexOfFirst { it.startsWith(originalNoteTitle) }
                    if (index != -1) {
                        notesList[index] = displayText
                    }
                } else {
                    notesList.add(displayText)
                }

                notesAdapter.notifyDataSetChanged()
            }
        }
    }


    override fun onBackPressed() {
        if (isSelectionMode) {
            exitSelectionMode()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to exit the application?")
                .setPositiveButton("Yes") { _, _ ->
                    finishAffinity() // بستن کامل برنامه
                    super.onBackPressed() // فراخوانی onBackPressed از کلاس پایه برای جلوگیری از خطا
                }
                .setNegativeButton("No", null)
                .show()
        }
    }


    inner class NoteAdapter(context: android.content.Context, resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.note_list_item, parent, false)
            val noteTitle = view.findViewById<TextView>(R.id.note_title)
            val checkBox = view.findViewById<CheckBox>(R.id.note_checkbox)

            noteTitle.text = getItem(position)
            checkBox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
            checkBox.isChecked = selectedNotes.contains(position)

            return view
        }
    }
}