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
        ArrayList<Float> y = new ArrayList<Float>();
        ArrayList<String> x = new ArrayList<String>();
        ArrayList<Integer> date = new ArrayList<Integer>();


            try {
                if(measure == 0){
                    ps = CON.conectar().prepareStatement("SELECT TOP "+ammount+" CONVERT(varchar(8),[date],112) as [date],hours_focused" +
                                                            " FROM V_TASK_TIME_DAYS " +
                                                            "ORDER BY [date] ASC");
                }else if(measure==1){
                    ps = CON.conectar().prepareStatement("SELECT TOP "+ammount+" "+
                            "CONCAT(YEAR([date]) ,"+
                            "RIGHT('00' + CONVERT(VARCHAR(2), DATEPART(WEEK, DATEADD(day,0,[date]))), 2)) AS [date],"+
                            "FORMAT(DATEADD(WEEK, DATEDIFF(WEEK, 0, DATEADD(day,-1,[date])), 0), 'dd MMM') "+
                    "AS [week_start],"+
                            "SUM([hours_focused]) AS [total_hours_focused] "+
                    "FROM"+
                            "[dbo].[V_TASK_TIME_DAYS]"+
                    "GROUP BY "+
                    "CONCAT(YEAR([date]) ,"+
                            "RIGHT('00' + CONVERT(VARCHAR(2), DATEPART(WEEK, DATEADD(day,0,[date]))), 2)),"+
                    "FORMAT(DATEADD(WEEK, DATEDIFF(WEEK, 0, DATEADD(day,-1,[date])), 0), 'dd MMM') "+
                    "ORDER BY "+
                    "CONCAT(YEAR([date]) ,"+
                            "RIGHT('00' + CONVERT(VARCHAR(2), DATEPART(WEEK, DATEADD(day,0,[date]))), 2)) ASC"
  );
                }else if(measure==2){
                    ps = CON.conectar().prepareStatement("SELECT top "+ammount+" "+
                            "CONCAT(YEAR([date]), FORMAT(MONTH([date]),  '00')) AS [date],SUM([hours_focused]) AS [hours_focused] " +
                            "FROM[dbo].[V_TASK_TIME_DAYS] "+
                            "GROUP BY "+
                            "YEAR([date]), MONTH([date]) "+
                            "ORDER BY "+
                            "YEAR([date]), MONTH([date]) ASC");
                }
                System.out.println(ps.toString());
                rs = ps.executeQuery();
                if(measure!=1){
                while (rs.next()) {
                    y.add(rs.getFloat("hours_focused"));
                    date.add(rs.getInt("date"));
                }}else{
                    while (rs.next()) {
                        y.add(rs.getFloat("total_hours_focused"));
                        x.add(rs.getString("week_start"));
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
            ArrayList[] xy = new ArrayList[3];
            xy[1] = y;
            xy[0] = x;
            xy[2] = date;

            return xy;
    }
}
