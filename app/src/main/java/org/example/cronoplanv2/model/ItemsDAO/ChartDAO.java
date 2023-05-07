package org.example.cronoplanv2.model.ItemsDAO;

import org.example.cronoplanv2.model.SQLConnection;
import org.example.cronoplanv2.model.Task;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChartDAO {
    private final SQLConnection CON;
    private PreparedStatement ps;
    private ResultSet rs;

    public ChartDAO() {CON = SQLConnection.getInstancia();}

    public ArrayList[] getData() {
        ArrayList<Float> x = null;
        ArrayList<Float> y = null;
            try {
                ps = CON.conectar().prepareStatement("SELECT title,description,status,id_task from TASKS");
                x = new ArrayList<Float>();
                y = new ArrayList<Float>();
                rs = ps.executeQuery();
                while (rs.next()) {
                    x.add(rs.getFloat("x"));
                    y.add(rs.getFloat("y"));
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
            ArrayList[] xy = new ArrayList[2];
            xy[0] = x;
            xy[1] = y;

            return xy;
    }
}
