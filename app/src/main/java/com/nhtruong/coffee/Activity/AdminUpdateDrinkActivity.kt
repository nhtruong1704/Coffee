package com.nhtruong.coffee.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nhtruong.coffee.R
import com.nhtruong.coffee.ViewModel.AdminUpdateDrinkViewModel
import com.nhtruong.coffee.databinding.ActivityAdminUpdateDrinkBinding
import com.nhtruong.coffee.model.ItemsModel
import java.util.*

class AdminUpdateDrinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUpdateDrinkBinding
    private lateinit var viewModel: AdminUpdateDrinkViewModel
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var drinkId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUpdateDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { startActivity(Intent(this@AdminUpdateDrinkActivity, AdminMainActivity::class.java)) }
        viewModel = ViewModelProvider(this).get(AdminUpdateDrinkViewModel::class.java)
        storageReference = FirebaseStorage.getInstance().reference

        drinkId = intent.getStringExtra("drinkId") ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        }
        // Setup Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.drink_category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
        }

        viewModel.getDrinkData(drinkId) { item ->
            binding.etTitle.setText(item.title)
            binding.etPrice.setText(item.price.toString())
            binding.etDescription.setText(item.description)
            Glide.with(this).load(item.picUrl).into(binding.ivPicture)
            // Set spinner selection
            val categories = resources.getStringArray(R.array.drink_category_array)
            val categoryIndex = categories.indexOf(item.category)
            if (categoryIndex >= 0) {
                binding.spinnerCategory.setSelection(categoryIndex)
            }
        }

        val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                binding.ivPicture.setImageURI(imageUri)
            }
        }

        binding.tvChoosePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getImage.launch(intent)
        }

        binding.btnUpdate.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val description = binding.etDescription.text.toString()

            if (imageUri != null) {
                uploadImageToFirebaseStorage(imageUri!!) { imageUrl ->
                    viewModel.updateDrink(drinkId, title, category, price, description, imageUrl)
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminMainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                viewModel.updateDrink(drinkId, title, category, price, description)
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminMainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteDrink(drinkId) {
                Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri, onComplete: (String) -> Unit) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storageReference.child("images/$fileName")

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    onComplete(uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }
}
