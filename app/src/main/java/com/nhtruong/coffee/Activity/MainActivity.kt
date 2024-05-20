package com.nhtruong.coffee.Activity


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.nhtruong.coffee.Adapter.BestSellerAdapter
import com.nhtruong.coffee.Adapter.CategoryAdapter
import com.nhtruong.coffee.Adapter.SliderAdapter
import com.nhtruong.coffee.ViewModel.MainViewModel
import com.nhtruong.coffee.databinding.ActivityMainBinding
import com.nhtruong.coffee.model.ItemsModel
import com.nhtruong.coffee.model.SliderModel

class MainActivity : BaseActivity() {

    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBanners()
        initCategories()
        initBestSeller()
        observeUserName()
        setupNavigation()


        //chuc nang tim kiem 20/05/2024
        initSearch()

    }

    private fun initBestSeller() {
        binding.progressBarBestSeller.visibility = View.VISIBLE
        viewModel.bestSeller.observe(this, Observer {
            //chuc nang tim kiem 20/05/2024
            val adapter = BestSellerAdapter(it)

            binding.viewBestSeller.layoutManager = GridLayoutManager(this, 2)
            binding.viewBestSeller.adapter = BestSellerAdapter(it)

            //chuc nang tim kiem 20/05/2024
            adapter.notifyDataSetChanged()

            binding.progressBarBestSeller.visibility=View.GONE
        })
        viewModel.loadBestSeller()
    }

    private fun initCategories() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        })
        viewModel.loadCategory()
    }

    private fun initBanners() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer {
            banners(it)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewPagerSlider.adapter = SliderAdapter(images, binding.viewPagerSlider)
        binding.viewPagerSlider.clipToPadding = false
        binding.viewPagerSlider.clipChildren = false
        binding.viewPagerSlider.offscreenPageLimit = 3
        binding.viewPagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPagerSlider)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Di chuyển task hiện tại về background thay vì kết thúc nó
        moveTaskToBack(true)
    }

    private fun observeUserName() {
        viewModel.userName.observe(this, Observer { userName ->
            binding.tvUserName.text = userName
        })
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { viewModel.loadUserName(it) }
    }


    private fun setupNavigation() {
        binding.ivHome.setOnClickListener {
            // Chuyển đến MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.ivCart.setOnClickListener {
            // Chuyển đến CartActivity
            val intent = Intent(this, UserCartActivity::class.java)
            startActivity(intent)
        }

        binding.ivFavorite.setOnClickListener {
            // Chuyển đến FavoritesActivity
            val intent = Intent(this, UserFavoritesActivity::class.java)
            startActivity(intent)
        }

        binding.ivOrder.setOnClickListener {
            // Chuyển đến OrderActivity
            val intent = Intent(this, UserOrderActivity::class.java)
            startActivity(intent)
        }

        binding.ivProfile.setOnClickListener {
            // Chuyển đến ProfileActivity
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }





    //chuc nang tim kiem 20/05/2024

    private fun initSearch(){
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    viewModel.searchItems(s.toString())
                } else {
                    // If the search text is empty, load all items
                    viewModel.loadBestSeller()
                }
            }
        })
    }





}