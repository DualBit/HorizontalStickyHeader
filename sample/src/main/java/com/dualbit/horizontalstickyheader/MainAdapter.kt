package com.dualbit.horizontalstickyheader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dualbit.horizontalstickyheader.databinding.AdapterCellItemBinding
import com.dualbit.horizontalstickyheader.databinding.AdapterSectionItemBinding

class MainAdapter(
    private var elements: List<AdapterItemCell>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            AdapterElementType.Section.viewType -> {
                SectionHolder(
                    AdapterSectionItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                CellHolder(
                    AdapterCellItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                )
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(elements[position]) {
            is AdapterItemCell.Section -> AdapterElementType.Section.viewType
            else -> AdapterElementType.Cell.viewType
        }
    }


    override fun getItemCount() = elements.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            AdapterElementType.Section.viewType -> {
                (elements[position] as? AdapterItemCell.Section)?.let {
                    (holder as? MainAdapter.SectionHolder)?.bind(it)
                }
            }

            AdapterElementType.Cell.viewType -> {
                (elements[position] as? AdapterItemCell.Cell)?.let {
                    (holder as? MainAdapter.CellHolder)?.bind(it)
                }
            }

        }
    }

    inner class SectionHolder(private val viewBinding: AdapterSectionItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: AdapterItemCell.Section) {
            viewBinding.name.text = item.title
        }
    }

    inner class CellHolder(private val viewBinding: AdapterCellItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: AdapterItemCell.Cell) {
            viewBinding.value.text = item.value
        }
    }

}

sealed class AdapterItemCell {
    data class Section(val title: String) : AdapterItemCell()
    data class Cell(val value: String) : AdapterItemCell()
}
