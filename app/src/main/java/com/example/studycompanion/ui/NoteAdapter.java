package com.example.studycompanion.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studycompanion.R;
import com.example.studycompanion.data.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {
    private final OnItemClickListener listener;
    private final OnDeleteClickListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Note note);
    }

    public NoteAdapter(OnItemClickListener listener, OnDeleteClickListener deleteListener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.title.equals(newItem.title) &&
                    oldItem.content.equals(newItem.content);
        }
    };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.bind(currentNote, listener, deleteListener);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView contentView;
        private final ImageView deleteBtn;

        private NoteViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.text_title);
            contentView = itemView.findViewById(R.id.text_content);
            deleteBtn = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(Note note, OnItemClickListener listener, OnDeleteClickListener deleteListener) {
            titleView.setText(note.title);
            contentView.setText(note.content);
            itemView.setOnClickListener(v -> listener.onItemClick(note));
            deleteBtn.setOnClickListener(v -> deleteListener.onDeleteClick(note));
        }
    }
}
