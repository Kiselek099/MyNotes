package com.example.mynotes

import Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val notes: MutableList<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteIdTV: TextView = itemView.findViewById(R.id.noteIdTV)
        val noteTV: TextView = itemView.findViewById(R.id.noteTV)
        val dataTV: TextView = itemView.findViewById(R.id.dataTV)
        val completeCB: CheckBox = itemView.findViewById(R.id.completeCB)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteIdTV.text=(position+1).toString()
        holder.noteTV.text = note.text
        holder.dataTV.text = note.createdAt
        holder.completeCB.isChecked = note.isCompleted
        holder.completeCB.setOnCheckedChangeListener { _, isChecked ->
            note.isCompleted = isChecked
        }
    }
    override fun getItemCount(): Int = notes.size
    fun updateNotes(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
}