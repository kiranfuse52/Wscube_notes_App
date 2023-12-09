package com.kiran.wscube_notes_app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.kiran.wscube_notes_app.Database.DatabaseHelper;
import com.kiran.wscube_notes_app.Database.Note;

import java.util.ArrayList;
import java.util.List;

public class RecyclerNotesAdapter extends RecyclerView.Adapter<RecyclerNotesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Note> arrNotes,filterList;

    private ArrayList<Note> arrOriginalNotes; // Original unfiltered data
    private DatabaseHelper databaseHelper;

    public RecyclerNotesAdapter(Context context, ArrayList<Note> arrNotes, DatabaseHelper databaseHelper) {
        this.context = context;
        this.arrNotes = arrNotes;
        this.arrOriginalNotes = new ArrayList<>(arrNotes); // Copy original data
        this.databaseHelper = databaseHelper;
    }

    public void filterList(List<Note> filteredList) {
        arrNotes.clear();
        arrNotes.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void updateList(List<Note> newList) {
        arrNotes.clear();
        arrNotes.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerNotesAdapter.ViewHolder holder, int position) {
        holder.title.setText(arrNotes.get(position).getTitle());
        holder.Desc.setText(arrNotes.get(position).getDesc());
        holder.linear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteItem(position);
                return true;
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, Desc;
        LinearLayout linear;
        ImageView update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_title);
            Desc = itemView.findViewById(R.id.textView_notes);
            linear = itemView.findViewById(R.id.linear);
            update = itemView.findViewById(R.id.update);
        }
    }

    public void deleteItem(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this Note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelper.noteDao().deleteNote(new Note(arrNotes.get(pos).getId(), arrNotes.get(pos).getTitle(), arrNotes.get(pos).getDesc()));
                ((MainActivity) context).showNotes();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public void updateItem(int possi) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_note_lay);
        EditText Title = dialog.findViewById(R.id.title);
        EditText Desc = dialog.findViewById(R.id.description);
        Button btnAdd = dialog.findViewById(R.id.save);

        Title.setText(arrNotes.get(possi).getTitle());
        Desc.setText(arrNotes.get(possi).getDesc());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                String title = Title.getText().toString();
                String desc = Desc.getText().toString();
                if (!desc.equals("")) {
                    dialog.dismiss();
                    databaseHelper.noteDao().update(arrNotes.get(possi).getId(), title, desc);
                    Toast.makeText(context, "Notes Updated", Toast.LENGTH_SHORT).show();
                    ((MainActivity) context).showNotes();
                } else {
                    Toast.makeText(context, "Please enter the Description", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}
