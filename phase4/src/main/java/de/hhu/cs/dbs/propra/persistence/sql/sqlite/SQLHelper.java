package de.hhu.cs.dbs.propra.persistence.sql.sqlite;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQLHelper {

    public static ResultSet executeQuery(
            Connection connection, HashMap<Integer, Object> input, String sql) throws SQLException {
        PreparedStatement preparedStatement = prepareStatement(connection, input, sql);
        return preparedStatement.executeQuery();
    }

    public static void executeUpdate(
            Connection connection, HashMap<Integer, Object> input, String sql) throws SQLException {
        PreparedStatement preparedStatement = prepareStatement(connection, input, sql);
        preparedStatement.executeUpdate();
    }

    private static PreparedStatement prepareStatement(
            Connection connection, HashMap<Integer, Object> input, String sql) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        for (Map.Entry<Integer, Object> entry : input.entrySet()) {
            Integer key = entry.getKey();
            Object value = entry.getValue();
            preparedStatement.setObject(key, value);
        }
        preparedStatement.closeOnCompletion();
        return preparedStatement;
    }
}
