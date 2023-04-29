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
            ps = CON.conectar().prepareStatement("SELECT title,description,status,id from TASKS");
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
}
