package org.example.cronoplanv2.model.ItemsDAO;

import org.example.cronoplanv2.model.SQLConnection;
import org.example.cronoplanv2.model.Settings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsDAO {
    private final SQLConnection CON;
    private PreparedStatement ps;
    private ResultSet rs;

    public SettingsDAO() {CON = SQLConnection.getInstancia();}

    public Settings getSettings() {

        try {
            ps = CON.conectar().prepareStatement("select Time,id_user,char_ammount,char_time_measure  from SETTINGS where id_User like 'Default_user'");
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Settings(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4));
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
            ps = CON.conectar().prepareStatement("Update SETTINGS set Time=?,isUpdated=1 where id_user like 'Default_user'");
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
            ps = CON.conectar().prepareStatement("select isUpdated from SETTINGS where id_user like 'Default_user'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if(rs.getBoolean(1)){
                    ps = CON.conectar().prepareStatement("update SETTINGS set isUpdated=0 where id_user like 'Default_user'");
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

    public void save(int measure, int ammount) {
        try {
            ps = CON.conectar().prepareStatement("Update SETTINGS set char_time_measure="+measure+",char_ammount="+ammount+" where id_user like 'Default_user'");
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
    }
}
