package com.example.beta.data.database.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.data.model.ChipModel
import com.example.beta.databinding.ItemFilterChipBinding

class FilterChipAdapter(
    private var chips: List<ChipModel>,
    private val onChipSelected: (Set<String>) -> Unit
) : RecyclerView.Adapter<FilterChipAdapter.ChipViewHolder>() {

    private val selectedChips = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemFilterChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val chip = chips[position]
        holder.bind(chip, selectedChips.contains(chip.text))
        Log.d("FilterChipAdapter", "Chip clicked: ${chip.text}")
        holder.itemView.setOnClickListener {

            // Use the chip's text for selection instead of its id
            if (selectedChips.contains(chip.text)) {
                selectedChips.remove(chip.text)
            } else {
                selectedChips.add(chip.text)
            }
            notifyItemChanged(position)

            // Invoke the callback with the current set of selected chip texts
            onChipSelected(selectedChips)
        }
    }

    override fun getItemCount(): Int = chips.size

    fun updateChips(newChips: List<ChipModel>) {
        chips = newChips
        notifyDataSetChanged()
    }



    inner class ChipViewHolder(
        private val binding: ItemFilterChipBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chipModel: ChipModel, isSelected: Boolean) {
            with(binding.filterChip) {
                text = chipModel.text
                isChecked = isSelected
                chipModel.iconResId?.let { iconResId ->
                    chipIcon = ContextCompat.getDrawable(context, iconResId)
                }
            }
        }
    }
}
