package net.kkolyan.elements.engine.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author nplekhanov
 */
public class Database {
    public static void execute(Connection conn, String sql) {
        try {
            Statement statement = conn.createStatement();
            try {
                statement.execute(sql);
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static int update(Connection conn, String sql, Object...args) {
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            try {
                for (int i = 0; i < args.length; i ++) {
                    statement.setObject(i + 1, args[i]);
                }
                return statement.executeUpdate();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
