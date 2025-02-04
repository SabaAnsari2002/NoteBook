package com.saba.notebook

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import java.io.ByteArrayOutputStream
import java.util.*

class AddNoteActivityTheme5 : ComponentActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var attachButton: Button
    private lateinit var dbHelper: DatabaseHelper

    private val bitmaps = mutableListOf<Pair<Bitmap, Int>>()
    private var isEditing = false
    private var originalTitle: String? = null
    private var userId: Int = -1
    private var noteId: Long = -1

    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
        uris?.let { selectedImages ->
            for (imageUri in selectedImages) {
                insertImageIntoMessage(imageUri)
            }
        }
    }
    // وارد کردن عکس با تنظیم سایز در متدی که عکس از دوربین دریافت می‌شود
    private val takePhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val photo = result.data?.extras?.get("data") as? Bitmap
            photo?.let {
                // تنظیم اندازه تصویر به یک‌پنجم اندازه اصلی
                val newWidth = it.width * 10
                val newHeight = it.height * 10
                val resizedPhoto = Bitmap.createScaledBitmap(it, newWidth, newHeight, true)
                val position = messageEditText.selectionStart
                insertBitmapIntoMessage(resizedPhoto, position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note5)

        titleEditText = findViewById(R.id.note_title)
        dateEditText = findViewById(R.id.note_date)
        messageEditText = findViewById(R.id.note_message)
        saveButton = findViewById(R.id.save_button)
        attachButton = findViewById(R.id.atach_button)

        dbHelper = DatabaseHelper(this)

        userId = intent.getIntExtra("USER_ID", -1)

        // Set default date to current date
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        dateEditText.setText("$currentYear-${currentMonth + 1}-$currentDay")

        // Check if editing an existing note
        val noteTitle = intent.getStringExtra("NOTE_TITLE")
        val noteDate = intent.getStringExtra("NOTE_DATE")
        if (!noteTitle.isNullOrEmpty() && !noteDate.isNullOrEmpty()) {
            titleEditText.setText(noteTitle)
            dateEditText.setText(noteDate)

            // Retrieve and set the note message
            val noteText = dbHelper.getNoteText(userId, noteTitle) ?: ""

            // Retrieve images and their positions
            val images = dbHelper.getImagesForNoteTitle(userId, noteTitle)

            // Sort images by position
            val sortedImages = images.sortedBy { it.second }

            // Set the text first
            messageEditText.setText(noteText)

            // Insert images at correct positions
            var offset = 0
            for ((imageData, position) in sortedImages) {
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                insertBitmapIntoMessage(bitmap, position + offset, resize = false)  // Resize = false here
                offset++ // Increase offset as one character is added for each image
            }

            isEditing = true
            originalTitle = noteTitle
            noteId = dbHelper.getNoteId(userId, noteTitle)
        }

        // Date picker for date input
        dateEditText.setOnClickListener {
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                dateEditText.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
            }, currentYear, currentMonth, currentDay).show()
        }

        saveButton.setOnClickListener {
            saveNote()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }

        attachButton.setOnClickListener {
            showAttachmentOptions()
        }
    }

    private fun showAttachmentOptions() {
        val options = arrayOf("انتخاب از گالری", "گرفتن عکس با دوربین")
        AlertDialog.Builder(this)
            .setTitle("انتخاب تصویر")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> getContent.launch("image/*")
                    1 -> {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        takePhoto.launch(cameraIntent)
                    }
                }
            }
            .show()
    }

    private fun insertImageIntoMessage(imageUri: Uri) {
        val bitmap = getBitmapFromUri(imageUri)
        bitmap?.let {
            val position = messageEditText.selectionStart
            insertBitmapIntoMessage(it, position)
        }
    }

    private fun insertBitmapIntoMessage(bitmap: Bitmap, position: Int, resize: Boolean = true) {
        val finalBitmap = if (resize) {
            // Resize the image if necessary
            val newWidth = bitmap.width / 5
            val newHeight = bitmap.height / 5
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } else {
            bitmap
        }

        bitmaps.add(Pair(finalBitmap, position))

        val drawable = BitmapDrawable(resources, finalBitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        val spannableString = SpannableString(" ")
        val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        messageEditText.text.insert(position, spannableString)
    }


    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveNote() {
        val title = titleEditText.text.toString()
        val date = dateEditText.text.toString()
        val message = messageEditText.text.toString()

        if (title.isNotEmpty() && date.isNotEmpty() && message.isNotEmpty()) {
            if (isEditing) {
                // Update existing note
                val updatedRows = dbHelper.updateNote(userId, originalTitle, title, date, message)
                if (updatedRows > 0) {
                    // Note updated successfully
                    updateImages()
                    Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show()
                    return
                }
            } else {
                // Add new note
                noteId = dbHelper.addNote(userId, title, date, message)
                if (noteId > -1) {
                    // Save images
                    saveImages()
                } else {
                    Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            val resultIntent = Intent()
            resultIntent.putExtra("NEW_NOTE_TITLE", title)
            resultIntent.putExtra("NEW_NOTE_DATE", date)
            resultIntent.putExtra("ORIGINAL_NOTE_TITLE", originalTitle)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveImages() {
        for ((bitmap, position) in bitmaps) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            dbHelper.addImage(noteId, byteArray, position)
        }
    }

    private fun updateImages() {
        val images = bitmaps.map { (bitmap, position) ->
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            Pair(stream.toByteArray(), position)
        }
        dbHelper.updateNoteImages(noteId, images)
    }
    override fun onBackPressed() {
        val title = titleEditText.text.toString().trim()
        val message = messageEditText.text.toString().trim()

        if (title.isEmpty() && message.isEmpty()) {
            // اگر عنوان و پیام خالی باشند، بدون نمایش پیغام به صفحه اصلی برگرد
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        } else {
            // اگر عنوان یا پیام پر باشند، پیغام ذخیره را نمایش بده
            AlertDialog.Builder(this)
                .setMessage("آیا می‌خواهید نوت را ذخیره کرده و خارج شوید؟")
                .setCancelable(false)
                .setPositiveButton("بله") { dialog, _ ->
                    if (title.isEmpty() || message.isEmpty()) {
                        Toast.makeText(this, "لطفاً عنوان و پیام را وارد کنید", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        saveNote()
                        finish()
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

                    }
                }
                .setNegativeButton("خیر") { dialog, _ ->
                    dialog.dismiss()
                    super.onBackPressed()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

                }
                .create()
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // بازیافت تمام Bitmap‌ها در زمان نابودی اکتیویتی
        bitmaps.forEach { it.first.recycle() }
        bitmaps.clear()
    }
}
