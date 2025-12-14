package com.example.studycompanion.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studycompanion.R;

public class GoalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        EditText etGoal = view.findViewById(R.id.et_goal);
        Button btnAdd = view.findViewById(R.id.btn_add_goal);
        TextView tvList = view.findViewById(R.id.tv_goal_list);

        btnAdd.setOnClickListener(v -> {
            String newGoal = etGoal.getText().toString();
            if (!newGoal.isEmpty()) {
                String currentText = tvList.getText().toString();
                tvList.setText(currentText + "\n- " + newGoal);
                etGoal.setText("");
            }
        });

        return view;
    }
}
