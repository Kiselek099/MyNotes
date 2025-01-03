package com.example.mynotes

import Note
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditNoteFragment : Fragment() {

    private lateinit var note: Note
    private lateinit var changeET: EditText
    private lateinit var backBTN: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_note, container, false)
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
        note = arguments?.getParcelable("note") ?: throw IllegalArgumentException("Note not found")
        changeET = view.findViewById(R.id.changeET)
        backBTN = view.findViewById(R.id.backBTN)

        changeET.setText(note.text)

        backBTN.setOnClickListener {
            val updatedText = changeET.text.toString()
            if (updatedText.isBlank()) {
                Toast.makeText(requireContext(), "Текст не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updatedNote = note.copy(text = updatedText)
            parentFragmentManager.setFragmentResult(
                "edit_note_result",
                Bundle().apply { putParcelable("updated_note", updatedNote) }
            )
            parentFragmentManager.popBackStack()
        }
        return view
    }
}