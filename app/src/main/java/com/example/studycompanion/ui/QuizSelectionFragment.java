package com.example.studycompanion.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studycompanion.R;
import com.example.studycompanion.viewmodel.QuizViewModel;

public class QuizSelectionFragment extends Fragment {

    private QuizViewModel quizViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_selection, container, false);

        view.findViewById(R.id.btn_subject_1).setOnClickListener(v -> startQuiz("Android Basics"));
        view.findViewById(R.id.btn_subject_2).setOnClickListener(v -> startQuiz("Software Engineering"));
        view.findViewById(R.id.btn_subject_3).setOnClickListener(v -> startQuiz("Artificial Intelligence"));
        view.findViewById(R.id.btn_subject_4).setOnClickListener(v -> startQuiz("Cloud Computing"));
        view.findViewById(R.id.btn_subject_5).setOnClickListener(v -> startQuiz("Networking"));

        RecyclerView recyclerHistory = view.findViewById(R.id.recycler_history);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        QuizHistoryAdapter adapter = new QuizHistoryAdapter();
        recyclerHistory.setAdapter(adapter);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        quizViewModel.getHistory().observe(getViewLifecycleOwner(), adapter::submitList);

        return view;
    }

    private void startQuiz(String subject) {
        ((MainActivity) requireActivity()).navigateToQuiz(subject);
    }
}
