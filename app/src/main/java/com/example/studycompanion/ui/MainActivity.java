package com.example.studycompanion.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.studycompanion.R;
import com.example.studycompanion.data.Note;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_notes) {
                replaceFragment(new NoteListFragment());
                return true;
            } else if (itemId == R.id.nav_quiz) {
                // Navigate to Quiz Selection
                replaceFragment(new QuizSelectionFragment());
                return true;
            } else if (itemId == R.id.nav_reference) {
                replaceFragment(new ReferenceFragment());
                return true;
            } else if (itemId == R.id.nav_goals) {
                replaceFragment(new GoalFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                replaceFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        // Set default selection
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_notes);
        }
    }

    public void navigateToEditNote(Note note) {
        NoteEditorFragment fragment = new NoteEditorFragment();
        fragment.setNoteToEdit(note);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void navigateToQuiz(String subject) {
        // We will implement this next
        QuizFragment fragment = QuizFragment.newInstance(subject);
        replaceFragment(fragment, true);
    }

    private void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }
}
