# Project Documentation: Study Companion App

## 1. Feature List / Functional Requirements
1.  **Study Notes Management (CRUD)**: Users can create, read, edit, and delete personal study notes.
2.  **Subject-Based Quizzes**: Users can take quizzes filtrable by specific subjects (e.g., Android, Software Engineering).
3.  **Quiz History**: The app saves and displays a history of past quiz scores.
4.  **Quick Reference Guide**: A dedicated section provides static "cheat sheets" for key topics (Java, Git, etc.).
5.  **Daily Study Goals**: A checklist feature allowing users to add and track daily learning objectives.
6.  **User Profile & Stats**: Displays the user's name and aggregated study statistics (e.g., quizzes taken).
7.  **Local Data Persistence**: All user data (notes, history) is persisted locally using a database, ensuring availability offline.
8.  **Responsive Navigation**: A persistent bottom navigation bar allows quick switching between major app sections.
9.  **Material Design UI**: The interface uses modern Material Design 3 components (Cards, FABs) for a robust user experience.
10. **Subject Expansion**: The system supports easily adding new subjects dynamically or via code updates.

## 2. Feasibility Study
| Requirement | Resource / API / Library | Description |
| :--- | :--- | :--- |
| **Local Database** | `androidx.room:room-runtime` | robust SQLite object-mapping library for persisting Notes and Quiz History. |
| **UI Components** | `com.google.android.material` | Provides Material 3 components like `FloatingActionButton`, `CardView`, and `BottomNavigationView`. |
| **Navigation** | `androidx.fragment.app.FragmentTransaction` | Manages UI screens (Fragments) and the back stack without heavy Activity transitions. |
| **Reactive UI** | `androidx.lifecycle:lifecycle-livedata` | Allows the UI to automatically update when database data changes (e.g., new note added). |
| **View Binding** | `viewBinding` (Gradle Feature) | Replaces `findViewById` with type-safe generated binding classes for traversing XML layouts. |
| **Asynchronous Ops** | `java.util.concurrent.Executor` | Handles database operations in the background to prevent freezing the UI thread. |
| **Scrolling Lists** | `androidx.recyclerview.widget.RecyclerView` | Efficiently displays long lists of items (Notes, Quiz History, References). |
| **Theme Support** | `res/values/themes.xml` | Centralized system for defining app-wide colors (`colorPrimary`) and styles. |
| **Image Loading** | `android.widget.ImageView` | Standard Android SDK component for displaying profile icons and other assets. |
| **Input Handling** | `android.widget.EditText` | Standard component for capturing user input for Notes and Goals. |

## 3. Technical Developer Guide

### Feature A: Implementing Study Notes with Room

**Objective**: Create a persistent storage system for study notes.

1.  **Define the Entity**: Create a class `Note.java` annotated with `@Entity`.
    ```java
    @Entity(tableName = "note_table")
    public class Note {
        @PrimaryKey(autoGenerate = true)
        public int id;
        public String title;
        public String content;
    }
    ```
2.  **Create the DAO**: Define an interface `NoteDao.java` with SQL methods.
    ```java
    @Dao
    public interface NoteDao {
        @Insert
        void insert(Note note);
        @Query("SELECT * FROM note_table ORDER BY id DESC")
        LiveData<List<Note>> getAllNotes();
    }
    ```
3.  **Initialize Database**: Build the Room database singleton in `AppDatabase.java`.
4.  **ViewModel**: Create `NoteViewModel.java` to expose LiveData to the UI, decoupling logic from the Fragment.
5.  **UI Integration**: In `NoteListFragment`, observe the LiveData and update a `RecyclerView` adapter when data changes.

### Feature B: Implementing Daily Goals Checklist

**Objective**: A simple, transient or persistent list for tracking daily tasks.

1.  **Layout**: Create `fragment_goals.xml` using a `LinearLayout` (vertical). Include an `EditText` for input, a `Button` to add, and a `TextView` (or RecyclerView) to display the list.
2.  **Fragment Logic**: In `GoalFragment.java`, inflate the layout.
3.  **Event Handling**: Set an `OnClickListener` on the "Add" button.
    ```java
    btnAdd.setOnClickListener(v -> {
        String goal = etGoal.getText().toString();
        if(!goal.isEmpty()){
             currentText += "\n- " + goal;
             tvList.setText(currentText); // Append to the display
             etGoal.setText(""); // Clear input
        }
    });
    ```
4.  **State Management**: For robust implementations, save this string to `SharedPreferences` or a database so it survives app restarts.

### Feature C: Bottom Navigation Architecture

**Objective**: seamless switching between main app features.

1.  **Menu Resource**: Define `res/menu/bottom_nav_menu.xml` with items (Notes, Reference, Goals, Profile).
2.  **Layout Integration**: Add `BottomNavigationView` to `activity_main.xml`.
3.  **Activity Logic**: In `MainActivity.java`, set a listener on the navigation view.
    ```java
    bottomNav.setOnItemSelectedListener(item -> {
        Fragment selectedFragment = null;
        if (item.getItemId() == R.id.nav_notes) selectedFragment = new NoteListFragment();
        else if (item.getItemId() == R.id.nav_reference) selectedFragment = new ReferenceFragment();
        // ... handled other cases
        getSupportFragmentManager().beginTransaction()
           .replace(R.id.fragment_container, selectedFragment)
           .commit();
        return true;
    });
    ```
