package org.example.cronoplanv2.controler;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.Task;
import org.example.cronoplanv2.model.ItemsDAO.TaskDAO;

import java.util.ArrayList;
import java.util.List;

public class KanbanFragment extends Fragment {
    private List<Task> toDoTasks = new ArrayList<>();
    private List<Task> doingTasks = new ArrayList<>();
    private List<Task> doneTasks = new ArrayList<>();

    private KanbanAdapter toDoAdapter;
    private KanbanAdapter doingAdapter;
    private KanbanAdapter doneAdapter;

    private RecyclerView toDoRecyclerView;
    private RecyclerView doingRecyclerView;
    private RecyclerView doneRecyclerView;
    private ImageView bnAddTask;


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

        initViews(view);
        initListeners();

        // Inicializacion de adapters
        toDoAdapter = new KanbanAdapter(this.getActivity(),toDoTasks, requireContext());
        doingAdapter = new KanbanAdapter(this.getActivity(),doingTasks, requireContext());
        doneAdapter = new KanbanAdapter(this.getActivity(),doneTasks, requireContext());

        // Asicnacion de adapters a los reciclerViews
        toDoRecyclerView.setAdapter(toDoAdapter);
        doingRecyclerView.setAdapter(doingAdapter);
        doneRecyclerView.setAdapter(doneAdapter);

        // asignacion de layout manager al reciclerview
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        doingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        doneRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        ItemTouchHelper.Callback callbackToDo = new KanbanItemTouchHelper(toDoAdapter);
        ItemTouchHelper.Callback callbackInProgress = new KanbanItemTouchHelper(doingAdapter);
        ItemTouchHelper.Callback callbackDone = new KanbanItemTouchHelper(doneAdapter);

        ItemTouchHelper itemTouchHelperToDo = new ItemTouchHelper(callbackToDo);
        ItemTouchHelper itemTouchHelperInProgress = new ItemTouchHelper(callbackInProgress);
        ItemTouchHelper itemTouchHelperDone = new ItemTouchHelper(callbackDone);

        toDoAdapter.setItemTouchHelper(itemTouchHelperToDo);
        doingAdapter.setItemTouchHelper(itemTouchHelperInProgress);
        doneAdapter.setItemTouchHelper(itemTouchHelperDone);

        itemTouchHelperToDo.attachToRecyclerView(toDoRecyclerView);
        itemTouchHelperInProgress.attachToRecyclerView(doingRecyclerView);
        itemTouchHelperDone.attachToRecyclerView(doneRecyclerView);
        update();
        return view;
    }

    //update() refreshes the kanban data
    public void update(){
        toDoTasks.clear();
        doingTasks.clear();
        doneTasks.clear();
        ArrayList<Task> task_list = TASKDATA.list();
        for (Task task : task_list) {
            int status = task.getStatus();
            if (status == 0) {
                toDoTasks.add(task);
                toDoAdapter.notifyDataSetChanged();
            } else if (status == 1) {
                doingTasks.add(task);
                doingAdapter.notifyDataSetChanged();
            } else {
                doneTasks.add(task);
                doneAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initViews(View view) {
        // Inicializacion reciclerviews
        toDoRecyclerView = view.findViewById(R.id.toDoRecyclerView);
        doingRecyclerView = view.findViewById(R.id.inProgressRecyclerView);
        doneRecyclerView = view.findViewById(R.id.doneRecyclerView);
        bnAddTask = view.findViewById(R.id.bnAssTask);
    }

    private void initListeners() {
        bnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddTaskDialog();
            }
        });
    }

    /*
    * Dialog for creating tasks
    */
    private void openAddTaskDialog() {
        // Create a new dialog
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_task);

        // Find the relevant input fields
        EditText titleEditText = dialog.findViewById(R.id.etTitle);
        EditText descriptionEditText = dialog.findViewById(R.id.etDescrip);
        // Find the Spinner view
        Spinner cbStatus = dialog.findViewById(R.id.cbStatus);

        // Set up an ArrayAdapter to populate the Spinner with the values from the status_array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.status_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbStatus.setAdapter(adapter);
        // Other input fields...

        // Set up a "Save" button that will add the new task when clicked
        Button saveButton = dialog.findViewById(R.id.bnSaveTask);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the values from the input fields
                String title = titleEditText.getText().toString();
                if(title.trim().equals("")) {
                    titleEditText.setError("Este campo es obligatorio");
                }else{
                    String description = descriptionEditText.getText().toString();
                    int status = cbStatus.getSelectedItemPosition();
                    // Other values...

                    // Create a new task object
                    Task newTask = new Task(title, description, status);
                    TASKDATA.insertTask(newTask);
                    update();
                    // Dismiss the dialog
                    dialog.dismiss();
                }
            }
        });

        // Set up a "Cancel" button that will close the dialog when clicked
        ImageView cancelButton = dialog.findViewById(R.id.bnClose);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }
}