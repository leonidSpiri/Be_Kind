package ru.spiridonov.be.kind.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.spiridonov.be.kind.domain.entity.WorkItem

object WorkItemDiffCallback : DiffUtil.ItemCallback<WorkItem>() {
    override fun areItemsTheSame(oldItem: WorkItem, newItem: WorkItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: WorkItem, newItem: WorkItem) = oldItem == newItem
}