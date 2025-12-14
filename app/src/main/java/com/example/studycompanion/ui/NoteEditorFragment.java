package com.example.studycompanion.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studycompanion.R;
import com.example.studycompanion.data.Note;
import com.example.studycompanion.viewmodel.NoteViewModel;

public class NoteEditorFragment extends Fragment {
    private EditText editTitle;
    private EditText editContent;
    private NoteViewModel noteViewModel;
    private Button buttonSave;

    // Manual rudimentary argument passing
    private Note noteToEdit;

    public void setNoteToEdit(Note note) {
        this.noteToEdit = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);

        editTitle = view.findViewById(R.id.edit_title);
        editContent = view.findViewById(R.id.edit_content);
        buttonSave = view.findViewById(R.id.btn_save);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        if (noteToEdit != null) {
            editTitle.setText(noteToEdit.title);
            editContent.setText(noteToEdit.content);
        }

        buttonSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteToEdit != null) {
                Note updatedNote = new Note(title, content, System.currentTimeMillis());
                updatedNote.id = noteToEdit.id;
                noteViewModel.update(updatedNote);
            } else {
                Note newNote = new Note(title, content, System.currentTimeMillis());
                noteViewModel.insert(newNote);
            }
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
