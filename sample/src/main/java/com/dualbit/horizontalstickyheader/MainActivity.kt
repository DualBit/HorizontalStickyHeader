package com.dualbit.horizontalstickyheader

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dualbit.horizontalstickyheader.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding
    lateinit var adapter: MainAdapter

    val headerDecoration by lazy {
        HHeaderItemDecoration(viewBinding.rclMain, shouldFadeOutHeader = false) {
            viewBinding.rclMain.isSectionViewType(it, adapter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = MainAdapter(
            listOf(
                AdapterItemCell.Section("Sec A"),
                AdapterItemCell.Cell("Val 1A"),
                AdapterItemCell.Cell("Val 2A"),
                AdapterItemCell.Cell("Val 3A"),
                AdapterItemCell.Cell("Val 4A"),
                AdapterItemCell.Section("Sec B"),
                AdapterItemCell.Cell("Val 1B"),
                AdapterItemCell.Cell("Val 2B"),
                AdapterItemCell.Cell("Val 3B"),
                AdapterItemCell.Cell("Val 4B"),
                AdapterItemCell.Section("Sec C"),
                AdapterItemCell.Cell("Val 1C"),
                AdapterItemCell.Cell("Val 2C"),
                AdapterItemCell.Cell("Val 3C"),
                AdapterItemCell.Cell("Val 4C"),
            )
        )

        viewBinding.rclMain.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        /*viewBinding.rclMain.run {
            viewBinding.rclMain.addItemDecoration(headerDecoration)
        }*/

//        viewBinding.rclMain.run {
//            viewBinding.rclMain.addItemDecoration(
//                HHeaderItemDecoration(
//                    this,
//                    shouldFadeOutHeader = false
//                ) {
//                    adapter?.getItemViewType(it) == AdapterElementType.Section.viewType
//                })
//        }

        viewBinding.rclMain.adapter = adapter

    }
}