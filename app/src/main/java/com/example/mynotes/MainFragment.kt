package com.example.mynotes

import Note
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private lateinit var notesAdapter: NotesAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "Мои заметки"
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_exit) {
                activity?.finish()
                true
            } else {
                false
            }
        }

        val noteET: EditText = view.findViewById(R.id.noteET)
        val saveBTN: Button = view.findViewById(R.id.saveBTN)
        val notesRV: RecyclerView = view.findViewById(R.id.notesRV)

        dbHelper = DBHelper(requireContext(), null)
        notesAdapter = NotesAdapter(mutableListOf())
        notesRV.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        saveBTN.setOnClickListener {
            val noteText = noteET.text.toString()
            if (noteText.isNotBlank()) {
                val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                dbHelper.addNote(noteText, false, timestamp)
                loadNotes()
                noteET.text.clear()
            } else {
                Toast.makeText(context, "Введите текст заметки", Toast.LENGTH_SHORT).show()
            }
        }

        loadNotes()

        return view
    }

    private fun loadNotes() {
        val cursor = dbHelper.getNotes()
        val notes = mutableListOf<Note>()
        var idCounter = 1
        cursor?.use {
            while (it.moveToNext()) {
                val text = it.getString(it.getColumnIndexOrThrow(DBHelper.KEY_TEXT))
                val isCompleted = it.getInt(it.getColumnIndexOrThrow(DBHelper.KEY_COMPLETED)) > 0
                val createdAt = it.getString(it.getColumnIndexOrThrow(DBHelper.KEY_CREATED_AT))
                notes.add(Note(idCounter++, text, isCompleted, createdAt))
            }
        }
        notesAdapter.updateNotes(notes)
    }
}