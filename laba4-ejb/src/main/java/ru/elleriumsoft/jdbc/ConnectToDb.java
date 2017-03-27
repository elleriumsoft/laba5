package ru.elleriumsoft.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public class ConnectToDb
{
    public Connection getConnection()
    {
        try
        {
            //DataSource dataSource = (DataSource) ic.lookup("java:jboss/laba3");
            try
            {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            return DriverManager.getConnection("jdbc:sqlite:c:/laba4/lababase.s3db");
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(Connection connection)
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}

