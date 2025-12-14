package com.example.studycompanion.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizDao {
    @Insert
    void insert(QuizResult result);

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    LiveData<List<QuizResult>> getAllResults();

    @Query("SELECT * FROM quiz_results WHERE subject = :subject ORDER BY timestamp DESC")
    LiveData<List<QuizResult>> getResultsBySubject(String subject);
}
