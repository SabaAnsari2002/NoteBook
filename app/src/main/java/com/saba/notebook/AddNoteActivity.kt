
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

class AddNoteActivity : ComponentActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var attachButton: Button
    private lateinit var dbHelper: DatabaseHelper

    private val bitmaps = mutableListOf<Bitmap>()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
        uris?.let { selectedImages ->
            for (imageUri in selectedImages) {
                insertImageIntoMessage(imageUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        titleEditText = findViewById(R.id.note_title)
        dateEditText = findViewById(R.id.note_date)
        messageEditText = findViewById(R.id.note_message)
        saveButton = findViewById(R.id.save_button)
        attachButton = findViewById(R.id.atach_button)

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

        attachButton.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun insertImageIntoMessage(imageUri: Uri) {
        val bitmap = getBitmapFromUri(imageUri)
        bitmap?.let {
            val newWidth = it.width / 5
            val newHeight = it.height / 5

            val resizedBitmap = Bitmap.createScaledBitmap(it, newWidth, newHeight, true)
            bitmaps.add(resizedBitmap)

            val drawable = BitmapDrawable(resources, resizedBitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

            val selectionStart = messageEditText.selectionStart
            val spannableString = SpannableString(" ")
            val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
            spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            messageEditText.text.insert(selectionStart, spannableString)
        }
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
            val userId = intent.getIntExtra("USER_ID", -1)
            val noteId = dbHelper.addNote(userId, title, date, message)
            if (noteId > -1) {
                // ذخیره تصاویر
                for (bitmap in bitmaps) {
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    dbHelper.addImage(noteId, byteArray)
                }

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
        val builder = AlertDialog.Builder(this)
        builder.setMessage("آیا می‌خواهید نوت را ذخیره کرده و خارج شوید؟")
            .setCancelable(false)
            .setPositiveButton("بله") { dialog, id ->
                saveNote()  // ذخیره نوت
                finish()  // بستن اکتیویتی و بازگشت به هوم
            }
            .setNegativeButton("خیر") { dialog, id ->
                dialog.dismiss()  // نوت را ذخیره نکن
                super.onBackPressed()  // فراخوانی متد پیش‌فرض onBackPressed برای بستن اکتیویتی
            }
        val alert = builder.create()
        alert.show()
    }



    override fun onDestroy() {
        super.onDestroy()
        // بازیافت تمام Bitmap‌ها در زمان نابودی اکتیویتی
        bitmaps.forEach { it.recycle() }
        bitmaps.clear()
    }
}