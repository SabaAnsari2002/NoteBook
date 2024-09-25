package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.widget.RelativeLayout

class HomeActivityTheme9 : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: NoteAdapter
    private lateinit var notesList: MutableList<String>
    private lateinit var deleteButton: Button
    private lateinit var backgroundImageView: ImageView
    private var userId: Int = -1
    private val selectedNotes = mutableSetOf<Int>()
    private var isSelectionMode = false
    private lateinit var relativeLayout: RelativeLayout

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home9)

        // مقداردهی sharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        relativeLayout = findViewById(R.id.relativeLayout)

        // بارگذاری و اعمال رنگ پس‌زمینه از SharedPreferences (در صورت وجود)
        val savedColor = sharedPreferences.getString("BACKGROUND_COLOR", null)
        if (savedColor != null) {
            relativeLayout.setBackgroundColor(Color.parseColor(savedColor))
        }

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            // If USER_ID is not provided, redirect to login
            startActivity(Intent(this, LoginActivityTheme9::class.java))
            finish()
            return
        }

        // Initialize views
        notesListView = findViewById(R.id.notes_list)
        deleteButton = findViewById(R.id.delete_button)
        backgroundImageView = findViewById(R.id.background_image)

        // Set up the notes list
        notesList = dbHelper.getNotesByUserId(userId).toMutableList()
        notesAdapter = NoteAdapter(this, R.layout.note_list_item, notesList)
        notesListView.adapter = notesAdapter

        // Hide delete button initially
        deleteButton.visibility = View.GONE

        // Load and set the background image
        loadBackgroundImage()
        loadLogoutButtonImage()
        loadDeleteButtonImage()
        loadAddNoteButtonImage()
        setupListeners()
        setupButtons()
        setupTouchListener()
    }

    private fun loadLogoutButtonImage() {
        // بازیابی رشته Base64 از SharedPreferences
        val encodedImage = sharedPreferences.getString("LOGOUT_BUTTON_IMAGE", null)
        if (encodedImage != null) {
            // تبدیل رشته Base64 به بایت
            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            // تبدیل بایت به Bitmap
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            // تنظیم Bitmap به دکمه لاگ اوت
            val logoutButton = findViewById<Button>(R.id.logout_button)
            logoutButton.background = BitmapDrawable(resources, bitmap)
        }
    }
    private fun loadDeleteButtonImage() {
        // بازیابی رشته Base64 از SharedPreferences
        val encodedImage = sharedPreferences.getString("DELETE_BUTTON_IMAGE", null)
        if (encodedImage != null) {
            // تبدیل رشته Base64 به بایت
            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            // تبدیل بایت به Bitmap
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            // تنظیم Bitmap به دکمه لاگ اوت
            val deleteButton = findViewById<Button>(R.id.delete_button)
            deleteButton.background = BitmapDrawable(resources, bitmap)
        }
    }
    private fun loadAddNoteButtonImage() {
        // بازیابی رشته Base64 از SharedPreferences
        val encodedImage = sharedPreferences.getString("ADD_NOTE_BUTTON_IMAGE", null)
        if (encodedImage != null) {
            // تبدیل رشته Base64 به بایت
            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            // تبدیل بایت به Bitmap
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            // تنظیم Bitmap به دکمه لاگ اوت
            val addNoteButton = findViewById<Button>(R.id.add_note_button)
            addNoteButton.background = BitmapDrawable(resources, bitmap)
        }
    }

    private fun loadBackgroundImage() {
        // Retrieve the Base64 encoded image string from SharedPreferences
        val encodedImage = sharedPreferences.getString("SELECTED_HOME_IMAGE", null)
        if (encodedImage != null) {
            // Decode the Base64 string into a byte array
            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            // Convert the byte array into a Bitmap
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            // Set the Bitmap to the ImageView
            backgroundImageView.setImageBitmap(bitmap)
        }
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

        val intent = Intent(this, AddNoteActivityTheme9::class.java)
        intent.putExtra("USER_ID", userId)
        intent.putExtra("NOTE_TITLE", noteTitle)
        intent.putExtra("NOTE_DATE", noteDate)
        startActivityForResult(intent, 1)
    }

    private fun setupButtons() {
        deleteButton.setOnClickListener {
            if (selectedNotes.isNotEmpty()) {
                showDeleteConfirmationDialog()
            }
        }

        val addButton: Button = findViewById(R.id.add_note_button)
        addButton.setOnClickListener {
            exitSelectionMode() // خروج از حالت انتخاب (اگر چنین عملکردی در برنامه وجود دارد)

            // بررسی اینکه آیا صفحه افزودن نوت شخصی‌سازی شده است یا نه
            val isCustomized = sharedPreferences.getBoolean("isAddNoteCustomized", false)

            // اجرای فعالیت مناسب بر اساس شخصی‌سازی
            val intent = if (isCustomized) {
                Intent(this, AddNoteActivityTheme9::class.java) // اگر شخصی‌سازی شده، فعالیت مربوط به Theme9 اجرا می‌شود
            } else {
                Intent(this, AddNoteActivityTheme10::class.java) // اگر شخصی‌سازی نشده، فعالیت مربوط به Theme10 اجرا می‌شود
            }

            // انتقال userId به فعالیت افزودن نوت
            intent.putExtra("USER_ID", userId)
            startActivityForResult(intent, 1) // اجرای فعالیت با امکان بازگشت نتیجه
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
                    startActivity(Intent(this, MainActivityTheme10::class.java))
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
            .setPositiveButton("Yes") { _, _ -> deleteSelectedNotes() }
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
                    finishAffinity() // Close the app completely
                    super.onBackPressed()
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