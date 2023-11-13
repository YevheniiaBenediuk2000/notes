package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Note> notesList;
    private ArrayAdapter<Note> adapter;
    private ListView listView;
    private Note selectedNote;


    private static final int ADD_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.notes_list);

        notesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listView.setAdapter(adapter);

        // Handle click on Add button
        Button addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the AddNoteActivity
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        // Handle long-press to delete a note
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected note
                final Note selectedNote = notesList.get(position);

                // Show a confirmation dialog
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the selected note
                                deleteNote(selectedNote);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true; // Return true to consume the long-press event
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected note
                selectedNote = notesList.get(position);

                // Create an Intent to start the AddNoteActivity for editing
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("editNote", selectedNote);
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

    }

    // Method to add a new note to the list
    public void addNoteToList(Note note) {
        notesList.add(note);
        adapter.notifyDataSetChanged();
    }

    // Method to delete a note
    private void deleteNote(Note note) {
        notesList.remove(note);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST) {
            // This is the result from the AddNoteActivity for adding a new note
            if (resultCode == RESULT_OK && data != null) {
                // Retrieve the new note from the data
                Note newNote = (Note) data.getSerializableExtra("newNote");
                if (newNote != null) {
                    // Add the new note to your notesList
                    addNoteToList(newNote);
                }
            }
        } else if (requestCode == EDIT_NOTE_REQUEST) {
            // This is the result from the AddNoteActivity for editing an existing note
            if (resultCode == RESULT_OK && data != null) {
                // Retrieve the edited note from the data
                Note editedNote = (Note) data.getSerializableExtra("editedNote");
                if (editedNote != null) {
                    // Find the position of the edited note in the notesList
                    int position = notesList.indexOf(selectedNote);
                    if (position != -1) {
                        // Update the note in your notesList with the editedNote
                        notesList.set(position, editedNote);
                        // Notify the adapter that data has changed
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

}


