package com.dualbit.horizontalstickyheader

import androidx.recyclerview.widget.RecyclerView

// Estrai la logica del controllo viewType in una funzione di estensione
fun RecyclerView.isSectionViewType(position: Int, adapter: RecyclerView.Adapter<*>?): Boolean {
    return adapter?.getItemViewType(position) == AdapterElementType.Section.viewType
}