package com.example.studycompanion.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.studycompanion.data.AppDatabase;
import com.example.studycompanion.data.QuizDao;
import com.example.studycompanion.data.QuizResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizViewModel extends AndroidViewModel {
    private final QuizDao quizDao;
    private final SavedStateHandle stateHandle;

    private static final String KEY_INDEX = "current_index";
    private static final String KEY_SCORE = "current_score";
    private static final String KEY_SUBJECT = "current_subject";
    private static final String KEY_FINISHED = "is_finished";

    // Hardcoded questions for demo
    public static class Question {
        public String text;
        public String[] options;
        public int correctIndex;

        public Question(String t, String[] o, int c) {
            text = t;
            options = o;
            correctIndex = c;
        }
    }

    private List<Question> questions = new ArrayList<>();

    public QuizViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        this.stateHandle = savedStateHandle;
        AppDatabase db = AppDatabase.getDatabase(application);
        quizDao = db.quizDao();

        // Initialize if new
        if (!stateHandle.contains(KEY_INDEX)) {
            stateHandle.set(KEY_INDEX, 0);
            stateHandle.set(KEY_SCORE, 0);
            stateHandle.set(KEY_FINISHED, false);
        }
    }

    public void startQuiz(String subject) {
        stateHandle.set(KEY_SUBJECT, subject);
        stateHandle.set(KEY_INDEX, 0);
        stateHandle.set(KEY_SCORE, 0);
        stateHandle.set(KEY_FINISHED, false);
        loadQuestions(subject);
    }

    // Ensure questions are loaded on recreation
    public void ensureQuestionsLoaded() {
        String subject = stateHandle.get(KEY_SUBJECT);
        if (subject != null)
            loadQuestions(subject);
    }

    private void loadQuestions(String subject) {
        questions.clear();
        if ("Android Basics".equals(subject)) {
            questions.add(new Question("Base class for all Android components?",
                    new String[] { "Activity", "Context", "View", "Application" }, 1));
            questions.add(new Question("Layout for linear arrangement?",
                    new String[] { "ConstraintLayout", "LinearLayout", "FrameLayout", "RelativeLayout" }, 1));
            questions.add(new Question("Method called when Activity is visible?",
                    new String[] { "onCreate", "onStart", "onResume", "onPause" }, 1));
        } else {
            questions.add(new Question("FIFO Data Structure?", new String[] { "Stack", "Queue", "Tree", "Graph" }, 1));
            questions.add(new Question("Faster search algorithm?",
                    new String[] { "Linear", "Binary", "Bubble", "Merge" }, 1));
        }
    }

    public LiveData<Integer> getCurrentIndex() {
        return stateHandle.getLiveData(KEY_INDEX);
    }

    public LiveData<Integer> getCurrentScore() {
        return stateHandle.getLiveData(KEY_SCORE);
    }

    public LiveData<Boolean> isFinished() {
        return stateHandle.getLiveData(KEY_FINISHED);
    }

    public Question getCurrentQuestion() {
        int idx = getCurrentIndex().getValue() != null ? getCurrentIndex().getValue() : 0;
        if (idx < questions.size())
            return questions.get(idx);
        return null;
    }

    public void submitAnswer(int optionIndex) {
        if (Boolean.TRUE.equals(isFinished().getValue()))
            return;

        int idx = getCurrentIndex().getValue();
        if (idx < questions.size()) {
            if (questions.get(idx).correctIndex == optionIndex) {
                int score = getCurrentScore().getValue();
                stateHandle.set(KEY_SCORE, score + 1);
            }
            if (idx + 1 < questions.size()) {
                stateHandle.set(KEY_INDEX, idx + 1);
            } else {
                stateHandle.set(KEY_FINISHED, true);
                saveResult();
            }
        }
    }

    private void saveResult() {
        String subject = stateHandle.get(KEY_SUBJECT);
        int score = getCurrentScore().getValue();
        QuizResult result = new QuizResult(subject, score, questions.size(), System.currentTimeMillis());
        AppDatabase.databaseWriteExecutor.execute(() -> quizDao.insert(result));
    }

    public LiveData<List<QuizResult>> getHistory() {
        return quizDao.getAllResults();
    }
}
