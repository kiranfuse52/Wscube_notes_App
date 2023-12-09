package com.kiran.wscube_notes_app;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kiran.wscube_notes_app.Database.DatabaseHelper;
import com.kiran.wscube_notes_app.Database.Note;
import com.kiran.wscube_notes_app.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ActivityMainBinding binding;
    private ArrayList<Note> arrAllNotes;
    private RecyclerNotesAdapter recyclerNotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.recyclerviewnotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        setContentView(binding.getRoot());
        databaseHelper = DatabaseHelper.getInstance(this);
        showNotes();

        binding.fabAdd.setOnClickListener(view -> {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.add_note_lay);
            EditText Title = dialog.findViewById(R.id.title);
            EditText Desc = dialog.findViewById(R.id.description);
            Button btnAdd = dialog.findViewById(R.id.save);

            btnAdd.setOnClickListener(view1 -> {
                String title = Title.getText().toString();
                String desc = Desc.getText().toString();
                if (!desc.equals("")) {
                    Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                    databaseHelper.noteDao().addNote(new Note(title, desc));
                    showNotes();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Please enter the Description", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }


        });

    }

    private void filter(String newText) {
        if (newText.isEmpty()) {
            // If the search query is empty, show the original list of notes
            recyclerNotesAdapter.filterList(arrAllNotes);
        } else {
            List<Note> filteredList = new ArrayList<>();
            for (Note singleNote : arrAllNotes) {
                if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                        || singleNote.getDesc().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(singleNote);

                }
            }

            recyclerNotesAdapter.filterList(filteredList);
        }
    }


    public void showNotes() {
        arrAllNotes = (ArrayList<Note>) databaseHelper.noteDao().getNotes();
        if (arrAllNotes.size() > 0) {
            binding.linearLayout.setVisibility(View.GONE);
            binding.recyclerviewnotes.setVisibility(View.VISIBLE);
            recyclerNotesAdapter = new RecyclerNotesAdapter(this, arrAllNotes, databaseHelper);
            binding.recyclerviewnotes.setAdapter(recyclerNotesAdapter);
        } else {
            binding.linearLayout.setVisibility(View.VISIBLE);
            binding.recyclerviewnotes.setVisibility(View.GONE);
        }
    }
}