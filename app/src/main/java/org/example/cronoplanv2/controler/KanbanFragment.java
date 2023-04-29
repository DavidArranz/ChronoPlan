package org.example.cronoplanv2.controler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.Task;
import org.example.cronoplanv2.model.ItemsDAO.TaskDAO;

import java.util.ArrayList;
import java.util.List;

public class KanbanFragment extends Fragment {
    private List<Task> toDoTasks = new ArrayList<>();
    private List<Task> inProgressTasks = new ArrayList<>();
    private List<Task> doneTasks = new ArrayList<>();

    private KanbanAdapter toDoAdapter;
    private KanbanAdapter inProgressAdapter;
    private KanbanAdapter doneAdapter;

    private RecyclerView toDoRecyclerView;
    private RecyclerView inProgressRecyclerView;
    private RecyclerView doneRecyclerView;

    public static boolean updated = true;

    private final TaskDAO TASKDATA = new TaskDAO();


    public KanbanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kanban, container, false);
        // Inicializacion reciclerviews
        toDoRecyclerView = view.findViewById(R.id.toDoRecyclerView);
        inProgressRecyclerView = view.findViewById(R.id.inProgressRecyclerView);
        doneRecyclerView = view.findViewById(R.id.doneRecyclerView);

        // Inicializacion de adapters
        toDoAdapter = new KanbanAdapter(toDoTasks, requireContext());
        inProgressAdapter = new KanbanAdapter(inProgressTasks, requireContext());
        doneAdapter = new KanbanAdapter(doneTasks, requireContext());

        // Asicnacion de adapters a los reciclerViews
        toDoRecyclerView.setAdapter(toDoAdapter);
        inProgressRecyclerView.setAdapter(inProgressAdapter);
        doneRecyclerView.setAdapter(doneAdapter);

        // asignacion de layout manager al reciclerview
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        inProgressRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        doneRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        ItemTouchHelper.Callback callbackToDo = new KanbanItemTouchHelper(toDoAdapter);
        ItemTouchHelper.Callback callbackInProgress = new KanbanItemTouchHelper(inProgressAdapter);
        ItemTouchHelper.Callback callbackDone = new KanbanItemTouchHelper(doneAdapter);

        ItemTouchHelper itemTouchHelperToDo = new ItemTouchHelper(callbackToDo);
        ItemTouchHelper itemTouchHelperInProgress = new ItemTouchHelper(callbackInProgress);
        ItemTouchHelper itemTouchHelperDone = new ItemTouchHelper(callbackDone);

        toDoAdapter.setItemTouchHelper(itemTouchHelperToDo);
        inProgressAdapter.setItemTouchHelper(itemTouchHelperInProgress);
        doneAdapter.setItemTouchHelper(itemTouchHelperDone);

        itemTouchHelperToDo.attachToRecyclerView(toDoRecyclerView);
        itemTouchHelperInProgress.attachToRecyclerView(inProgressRecyclerView);
        itemTouchHelperDone.attachToRecyclerView(doneRecyclerView);
        //comprueba si ya esta actualizado
        if(updated) {
            toDoTasks.clear();
            inProgressTasks.clear();
            doneTasks.clear();
            ArrayList<Task> task_list = TASKDATA.list();
            for (Task task : task_list) {
                int status = task.getStatus();
                if (status == 1) {
                    toDoTasks.add(task);
                } else if (status == 2) {
                    inProgressTasks.add(task);
                } else {
                    doneTasks.add(task);
                }
            }
        }


        // notificacion de los cambios
        toDoAdapter.notifyDataSetChanged();
        inProgressAdapter.notifyDataSetChanged();
        doneAdapter.notifyDataSetChanged();

        return view;
    }
}