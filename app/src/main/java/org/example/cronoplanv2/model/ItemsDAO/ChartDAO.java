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

    public ArrayList[] getData(int measure,int ammount) {
        ArrayList<Float> x = new ArrayList<Float>();
        ArrayList<Float> y = new ArrayList<Float>();
        ArrayList<String> ws = new ArrayList<String>();


            try {
                if(measure == 0){
                    ps = CON.conectar().prepareStatement("SELECT TOP ? [date], [hours_focused]" +
                                                            "FROM [dbo].[V_TASK_TIME_DAYS]" +
                                                            "ORDER BY [date] DESC");
                }else if(measure==1){
                    ps = CON.conectar().prepareStatement("SELECT TOP ?"+
                            "CONCAT(YEAR([date]) ,"+
                            "RIGHT('00' + CONVERT(VARCHAR(2), DATEPART(WEEK, DATEADD(day,-2,[date]))), 2)) AS [date],"+
                            "FORMAT(DATEADD(WEEK, DATEDIFF(WEEK, 0, DATEADD(day,-1,[date])), 0), 'dd MMM')"+
                    "AS [week_start],"+
                            "SUM([hours_focused]) AS [total_hours_focused]"+
                    "FROM"+
                            "[dbo].[V_TASK_TIME_DAYS]"+
                    "GROUP BY"+
                    "CONCAT(YEAR([date]) ,"+
                            "RIGHT('00' + CONVERT(VARCHAR(2), DATEPART(WEEK, DATEADD(day,-2,[date]))), 2)),"+
                    "FORMAT(DATEADD(WEEK, DATEDIFF(WEEK, 0, DATEADD(day,-1,[date])), 0), 'dd MMM')"+
                    "ORDER BY"+
                    "CONCAT(YEAR([date]) ,"+
                            "RIGHT('00' + CONVERT(VARCHAR(2), DATEPART(WEEK, DATEADD(day,-2,[date]))), 2)) DESC"
  );
                }else if(measure==2){
                    ps = CON.conectar().prepareStatement("SELECT top ? "+
                            "CONCAT(YEAR([date]), FORMAT(MONTH([date]),  '00')) AS [month],SUM([hours_focused]) AS [total_hours_focused] " +
                            "FROM[dbo].[V_TASK_TIME_DAYS]"+
                            "GROUP BY"+
                            "YEAR([date]), MONTH([date])"+
                            "ORDER BY"+
                            "YEAR([date]), MONTH([date])");
                }
                ps.setInt(ammount,1);

                rs = ps.executeQuery();
                if(measure!=2){
                while (rs.next()) {
                    x.add(rs.getFloat("date"));
                    y.add(rs.getFloat("hours_focused"));
                }}else{
                    while (rs.next()) {
                        x.add(rs.getFloat("date"));
                        y.add(rs.getFloat("hours_focused"));
                        ws.add(rs.getString("week_start"));
                    }
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
