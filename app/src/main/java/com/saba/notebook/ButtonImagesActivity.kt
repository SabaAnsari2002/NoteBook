package com.saba.notebook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ButtonImagesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button_images)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        loadingText = findViewById(R.id.loadingText)

        recyclerView.layoutManager = GridLayoutManager(this, 4)

        val buttonType = intent.getStringExtra("BUTTON_TYPE") ?: "ADD_NOTE"

        lifecycleScope.launch {
            showLoading()

            val imageList = withContext(Dispatchers.IO) {
                getImageListForButton(buttonType)
            }

            hideLoading()

            recyclerView.adapter = ImageAdapter(imageList.toMutableList()) { selectedImage ->
                saveSelectedImageToPreferences(selectedImage, buttonType)
                finish()
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        loadingText.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        loadingText.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun getImageListForButton(buttonType: String): List<ByteArray> {
        val drawableResList = when (buttonType) {
            "ADD_NOTE" -> listOf(
                R.drawable.addpost1, R.drawable.addpost2,
                R.drawable.addpost3, R.drawable.addpost4,
                R.drawable.addpost5, R.drawable.addpost6,
                R.drawable.addpost7, R.drawable.addpost8,
                R.drawable.addpost9, R.drawable.addpost10,
                R.drawable.addpost11, R.drawable.addpost12,
                R.drawable.addpost13, R.drawable.addpost14,
                R.drawable.addpost15, R.drawable.addpost16,
                R.drawable.addpost17, R.drawable.addpost18,
                R.drawable.addpost19, R.drawable.addpost20,
                R.drawable.addpost21, R.drawable.addpost22,
                R.drawable.addpost23, R.drawable.addpost24,
                R.drawable.addpost25, R.drawable.addpost26,
                R.drawable.addpost27, R.drawable.addpost28,
                R.drawable.addpost29, R.drawable.addpost30,
                R.drawable.addpost31, R.drawable.addpost32,
                R.drawable.addpost33, R.drawable.addpost34,
                R.drawable.addpost35
            )
            "LOGOUT" -> listOf(
                R.drawable.logout1, R.drawable.logout2,
                R.drawable.logout3, R.drawable.logout4,
                R.drawable.logout5, R.drawable.logout6,
                R.drawable.logout7, R.drawable.logout8,
                R.drawable.logout9, R.drawable.logout10,
                R.drawable.logout11, R.drawable.logout12,
                R.drawable.logout13, R.drawable.logout14,
                R.drawable.logout15, R.drawable.logout16,
                R.drawable.logout17, R.drawable.logout18,
                R.drawable.logout19, R.drawable.logout20,
                R.drawable.logout21, R.drawable.logout22,
                R.drawable.logout23, R.drawable.logout24,
                R.drawable.logout25, R.drawable.logout26,
                R.drawable.logout27, R.drawable.logout28,
                R.drawable.logout29, R.drawable.logout30,
                R.drawable.logout31, R.drawable.logout32,
                R.drawable.logout33, R.drawable.logout34,
                R.drawable.logout35
            )
            "ATTACH" -> listOf(
                R.drawable.attach1, R.drawable.attach2,
                R.drawable.attach3, R.drawable.attach4,
                R.drawable.attach5, R.drawable.attach6,
                R.drawable.attach7, R.drawable.attach8,
                R.drawable.attach9, R.drawable.attach10,
                R.drawable.attach11, R.drawable.attach12,
                R.drawable.attach13, R.drawable.attach14,
                R.drawable.attach15, R.drawable.attach16,
                R.drawable.attach17, R.drawable.attach18,
                R.drawable.attach19, R.drawable.attach20,
                R.drawable.attach21, R.drawable.attach22,
                R.drawable.attach23, R.drawable.attach24,
                R.drawable.attach25, R.drawable.attach26,
                R.drawable.attach27, R.drawable.attach28,
                R.drawable.attach29, R.drawable.attach30,
                R.drawable.attach31, R.drawable.attach32,
                R.drawable.attach33, R.drawable.attach34,
                R.drawable.attach35
            )
            "DELETE" -> listOf(
                R.drawable.bin1, R.drawable.bin2,
                R.drawable.bin3, R.drawable.bin4,
                R.drawable.bin5, R.drawable.bin6,
                R.drawable.bin7, R.drawable.bin8,
                R.drawable.bin9, R.drawable.bin10,
                R.drawable.bin11, R.drawable.bin12,
                R.drawable.bin13, R.drawable.bin14,
                R.drawable.bin15, R.drawable.bin16,
                R.drawable.bin17, R.drawable.bin18,
                R.drawable.bin19, R.drawable.bin20,
                R.drawable.bin21, R.drawable.bin22,
                R.drawable.bin23, R.drawable.bin24,
                R.drawable.bin25, R.drawable.bin26,
                R.drawable.bin27, R.drawable.bin28,
                R.drawable.bin29, R.drawable.bin30,
                R.drawable.bin31, R.drawable.bin32,
                R.drawable.bin33, R.drawable.bin34,
                R.drawable.bin35
            )
            else -> emptyList()
        }

        return drawableResList.map { resId ->
            val bitmap = BitmapFactory.decodeResource(resources, resId)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        }
    }

    private fun saveSelectedImageToPreferences(selectedImage: ByteArray, buttonType: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val encodedImage = android.util.Base64.encodeToString(selectedImage, android.util.Base64.DEFAULT)

        when (buttonType) {
            "LOGOUT" -> editor.putString("LOGOUT_BUTTON_IMAGE", encodedImage)
            "ADD_NOTE" -> editor.putString("ADD_NOTE_BUTTON_IMAGE", encodedImage)
            "ATTACH" -> editor.putString("ATTACH_BUTTON_IMAGE", encodedImage)
            "DELETE" -> editor.putString("DELETE_BUTTON_IMAGE", encodedImage)
        }

        editor.apply()
    }
}