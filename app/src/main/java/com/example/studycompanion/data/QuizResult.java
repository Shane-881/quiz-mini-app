package com.example.studycompanion.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_results")
public class QuizResult {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String subject;
    public int score;
    public int totalQuestions;
    public long timestamp;

    public QuizResult(String subject, int score, int totalQuestions, long timestamp) {
        this.subject = subject;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timestamp = timestamp;
    }
}
