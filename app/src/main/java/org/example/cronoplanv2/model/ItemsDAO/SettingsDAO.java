package org.example.cronoplanv2.model.ItemsDAO;

import org.example.cronoplanv2.controler.SettingsActivity;
import org.example.cronoplanv2.model.SQLConnection;
import org.example.cronoplanv2.model.Settings;
import org.example.cronoplanv2.model.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SettingsDAO {
    private final SQLConnection CON;
    private PreparedStatement ps;
    private ResultSet rs;

    public SettingsDAO() {CON = SQLConnection.getInstancia();}

    public Settings getSettings() {

        try {
            ps = CON.conectar().prepareStatement("select Time,id_user  from SETTINGS where id_User like 'Default_user'");
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Settings(rs.getInt(1),rs.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return null;
    }

    public void save(int mins) {
        try {
            ps = CON.conectar().prepareStatement("Update SETTINGS set Time=?,isUpdated=1 where User_id like 'Default_user'");
            ps.setInt(1,mins);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
    }

    public boolean isUpdated() {

        try {
            ps = CON.conectar().prepareStatement("select isUpdated from SETTINGS where User_id like 'Default_user'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if(rs.getBoolean(1)){
                    ps = CON.conectar().prepareStatement("update SETTINGS set isUpdated=0 where User_id like 'Default_user'");
                    ps.executeUpdate();
                    return true;
                }else{
                    return false;
                }
            }else{
                throw new SQLException("Unexpected null return from a select");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return false;
    }
}
