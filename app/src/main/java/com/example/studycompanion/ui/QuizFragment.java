package com.example.studycompanion.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studycompanion.R;
import com.example.studycompanion.viewmodel.QuizViewModel;

import java.util.Locale;

public class QuizFragment extends Fragment {
    private static final String ARG_SUBJECT = "arg_subject";

    private QuizViewModel quizViewModel;
    private TextView textProgress;
    private TextView textQuestion;
    private RadioGroup radioGroup;
    private Button btnSubmit;

    public static QuizFragment newInstance(String subject) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT, subject);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textProgress = view.findViewById(R.id.text_progress);
        textQuestion = view.findViewById(R.id.text_question);
        radioGroup = view.findViewById(R.id.radio_group_options);
        btnSubmit = view.findViewById(R.id.btn_submit);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        // Initialize quiz only if starting fresh (logic handled in VM via
        // SavedStateHandle check)
        if (getArguments() != null) {
            String subject = getArguments().getString(ARG_SUBJECT);
            // We only call startQuiz if the VM doesn't have state yet?
            // Actually VM constructor checks SavedStateHandle.
            // But we need to pass the subject if it's new.
            // Let's rely on VM.ensureQuestionsLoaded() or similar.
            // A clearer pattern:
            if (savedInstanceState == null && quizViewModel.getCurrentQuestion() == null) {
                quizViewModel.startQuiz(subject);
            } else {
                quizViewModel.ensureQuestionsLoaded();
            }
        }

        // Observe State
        quizViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), index -> {
            updateUI();
        });

        quizViewModel.isFinished().observe(getViewLifecycleOwner(), finished -> {
            if (finished) {
                showResult();
            }
        });

        btnSubmit.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(getContext(), "Select an answer", Toast.LENGTH_SHORT).show();
                return;
            }
            int index = radioGroup.indexOfChild(view.findViewById(selectedId));
            quizViewModel.submitAnswer(index);
        });
    }

    private void updateUI() {
        QuizViewModel.Question question = quizViewModel.getCurrentQuestion();
        if (question == null)
            return;

        textQuestion.setText(question.text);
        textProgress.setText(String.format(Locale.getDefault(), "Question %d",
                quizViewModel.getCurrentIndex().getValue() + 1));

        // Remove old radio buttons
        radioGroup.removeAllViews();
        radioGroup.clearCheck(); // Clear check state

        // Add new radio buttons
        for (String option : question.options) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(option);
            rb.setId(View.generateViewId());
            radioGroup.addView(rb);
        }
    }

    private void showResult() {
        // Show result dialog or navigate back
        int score = quizViewModel.getCurrentScore().getValue();
        // Simple toast and pop back
        Toast.makeText(getContext(), "Quiz Finished! Score: " + score, Toast.LENGTH_LONG).show();
        getParentFragmentManager().popBackStack();
    }
}
