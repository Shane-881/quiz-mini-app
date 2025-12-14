package com.example.studycompanion.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studycompanion.R;
import com.example.studycompanion.data.QuizResult;

import java.util.Locale;

public class QuizHistoryAdapter extends ListAdapter<QuizResult, QuizHistoryAdapter.HistoryViewHolder> {

    public QuizHistoryAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<QuizResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<QuizResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull QuizResult oldItem, @NonNull QuizResult newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull QuizResult oldItem, @NonNull QuizResult newItem) {
            return oldItem.score == newItem.score && oldItem.timestamp == newItem.timestamp;
        }
    };

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView subjectView;
        private final TextView scoreView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectView = itemView.findViewById(R.id.text_subject);
            scoreView = itemView.findViewById(R.id.text_score);
        }

        public void bind(QuizResult result) {
            subjectView.setText(result.subject);
            scoreView.setText(String.format(Locale.getDefault(), "%d/%d", result.score, result.totalQuestions));
        }
    }
}
