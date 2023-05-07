package org.example.cronoplanv2.controler;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.Task;

import java.sql.SQLOutput;
import java.util.List;

public class KanbanAdapter extends RecyclerView.Adapter<KanbanAdapter.ViewHolder> {
    private List<Task> tasks;
    private Context context;

    private Activity mActivity;

    public KanbanAdapter(Activity activity, List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
        mActivity = activity;
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
        public TextView tvId;
        public TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            //on long touch
            titleTextView = itemView.findViewById(R.id.tvTitle);
            descriptionTextView = itemView.findViewById(R.id.tvDescription);
            tvId=itemView.findViewById(R.id.tvId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            final GestureDetector gestureDetector = new GestureDetector(itemView.getContext(),
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public void onLongPress(MotionEvent e) {
                            itemTouchHelper.startDrag(ViewHolder.this);
                        }
                    });
            itemView.setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ResourceType")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(!gestureDetector.onTouchEvent(event)){
                        String title,descrip;
                        int id,status;
                        title = ((TextView) v.findViewById(R.id.tvTitle)).getText().toString();
                        descrip = ((TextView) v.findViewById(R.id.tvDescription)).getText().toString();
                        id = Integer.valueOf(((TextView) v.findViewById(R.id.tvId)).getText().toString());
                        status = Integer.valueOf(((TextView) v.findViewById(R.id.tvStatus)).getText().toString());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("task", new Task(title,descrip,status,id));
                        TimerFragment timerFragment = new TimerFragment();
                        timerFragment.setArguments(bundle);
                        FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, timerFragment)
                                .commit();
                        (((MainActivity) mActivity)).setNavigationBar(3);

                    }
                    return true;
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
        holder.tvId.setText(String.valueOf(task.getId()));
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.tvStatus.setText(String.valueOf(task.getStatus()));

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
