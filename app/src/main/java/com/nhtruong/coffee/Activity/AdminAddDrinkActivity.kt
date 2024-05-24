package com.nhtruong.coffee.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.nhtruong.coffee.R
import com.nhtruong.coffee.ViewModel.AdminAddDrinkViewModel
import com.nhtruong.coffee.databinding.ActivityAdminAddDrinkBinding
import java.util.*


class AdminAddDrinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddDrinkBinding
    private lateinit var viewModel: AdminAddDrinkViewModel

    private var imageUri: Uri? = null
    private val storageReference = FirebaseStorage.getInstance().getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { startActivity(Intent(this@AdminAddDrinkActivity, AdminMainActivity::class.java)) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.terra_red)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        }
        viewModel = ViewModelProvider(this).get(AdminAddDrinkViewModel::class.java)

        // Thiết lập ArrayAdapter cho Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.drink_category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding.spinnerCategory.adapter = adapter
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


        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val description = binding.etDescription.text.toString()

            if (imageUri != null) {
                uploadImageToFirebaseStorage(imageUri!!) { picUrl ->
                    viewModel.addDrink(title, category, price, description, picUrl) {
                        Toast.makeText(this, "Add Drink Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AdminMainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                val picUrl = "https://firebasestorage.googleapis.com/v0/b/coffee-16fc2.appspot.com/o/coffeeshop.png?alt=media"
                viewModel.addDrink(title, category, price, description, picUrl) {
                    Toast.makeText(this, "Add Drink Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
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