package org.example.cronoplanv2.model.ItemsDAO;

import org.example.cronoplanv2.model.SQLConnection;
import org.example.cronoplanv2.model.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private final SQLConnection CON;
    private PreparedStatement ps;
    private ResultSet rs;
    public TaskDAO() {
        CON = SQLConnection.getInstancia();
    }

    public ArrayList<Task> list() {

        ArrayList<Task> tasks = null;
        try {
            ps = CON.conectar().prepareStatement("SELECT title,description,status,id_task from TASKS");
            tasks = new ArrayList<Task>();
            rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(new Task(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return tasks;
    }

    public void insertTask(Task newTask) {
        try {
            ps = CON.conectar().prepareStatement("INSERT into TASKS (title,description,status) values(?,?,?) ");
            ps.setString(1,newTask.getTitle());
            ps.setString(2, newTask.getDescription());
            ps.setInt(3,newTask.getStatus());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps = null;
            CON.desconectar();
        }
    }

    public void insertTime(int id) {
        try {
            ps = CON.conectar().prepareStatement("exec pinsert_time ?");
            ps.setInt(1,id);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps = null;
            CON.desconectar();
        }
    }
}
