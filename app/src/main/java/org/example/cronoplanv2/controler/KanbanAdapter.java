package org.example.cronoplanv2.controler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.Task;

import java.util.List;

public class KanbanAdapter extends RecyclerView.Adapter<KanbanAdapter.ViewHolder> {
    private List<Task> tasks;
    private Context context;

    public KanbanAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    public void moveItem(int fromPosition, int toPosition) {
        Task task = tasks.get(fromPosition);
        tasks.remove(fromPosition);
        tasks.add(toPosition, task);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.tvTitle);
            descriptionTextView = itemView.findViewById(R.id.tvDescription);
            itemView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        itemTouchHelper.startDrag(ViewHolder.this);
                    }
                    return false;
                }
            });
        }
    }
    private ItemTouchHelper itemTouchHelper;
    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kanban_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Task task = tasks.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle task click
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
