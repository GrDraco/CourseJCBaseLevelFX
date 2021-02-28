package chat.core.loader;

import java.sql.*;

public class Loader {
    private static Connection connection;
    private static Statement statement;

    public static Connection instance() throws ClassNotFoundException, SQLException {
        if(statement == null) {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:ChatDb.db");
            statement = connection.createStatement();
            createTables();
        }
        return connection;
    }

    private static void createTables() throws ClassNotFoundException, SQLException
    {
        if(statement == null)
            return;
        statement.execute("CREATE TABLE if not exists Users " +
                              "('Id' INTEGER PRIMARY KEY AUTOINCREMENT, 'Login' text KEY, 'NickName' text KEY, 'Password' text);");
    }

    public static void close() throws ClassNotFoundException, SQLException {
        if (connection != null)
            connection.close();
        if (statement != null)
            statement.close();
        connection = null;
        statement = null;
    }

    public static String auth(String login, String password) throws SQLException, ClassNotFoundException {
        PreparedStatement query = instance().prepareStatement("SELECT * " +
                                                         "FROM Users " +
                                                        "WHERE Login = ? " +
                                                          "AND Password = ?");
        query.setString(1, login);
        query.setString(2, password);
        ResultSet result = query.executeQuery();
        if(result == null || !result.next())
            return null;
        return result.getString("NickName");
    }
    public static boolean changeNickName(String lastNickName, String newNickName) throws SQLException, ClassNotFoundException {
        PreparedStatement query = instance().prepareStatement("UPDATE Users " +
                                                                     "SET NickName = ? " +
                                                                   "WHERE NickName = ?; ");
        query.setString(1, newNickName);
        query.setString(2, lastNickName);
        return query.execute();
    }
}
