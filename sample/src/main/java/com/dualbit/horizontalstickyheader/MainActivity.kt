package com.dualbit.horizontalstickyheader

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dualbit.horizontalstickyheader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding
    lateinit var adapter: MainAdapter

    private val headerDecoration by lazy {
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
                AdapterItemCell.Section("A"),
                AdapterItemCell.Cell("1A"),
                AdapterItemCell.Cell("2A"),
                AdapterItemCell.Cell("3A"),
                AdapterItemCell.Cell("4A"),
                AdapterItemCell.Cell("5A"),
                AdapterItemCell.Cell("6A"),
                AdapterItemCell.Cell("7A"),
                AdapterItemCell.Cell("8A"),
                AdapterItemCell.Section("B"),
                AdapterItemCell.Cell("1B"),
                AdapterItemCell.Cell("2B"),
                AdapterItemCell.Cell("3B"),
                AdapterItemCell.Cell("4B"),
                AdapterItemCell.Section("C"),
                AdapterItemCell.Cell("1C"),
                AdapterItemCell.Cell("2C"),
                AdapterItemCell.Cell("3C"),
                AdapterItemCell.Cell("4C"),
                AdapterItemCell.Cell("5C"),
                AdapterItemCell.Cell("6C"),
                AdapterItemCell.Cell("7C"),
            )
        )

        viewBinding.rclMain.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewBinding.rclMain.post {
            viewBinding.rclMain.addItemDecoration(headerDecoration)
        }

        viewBinding.rclMain.adapter = adapter

    }
}