package ru.elleriumsoft.jdbc;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public class ConnectToDb
{
    public final static String JNDI_ROOT = "java:global/laba4-ear-1.0/laba4-ejb-1.0/";

    private static final Logger logger = Logger.getLogger(ConnectToDb.class.getName());

    public Connection getConnection()
    {
        try
        {
            try
            {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e)
            {
                logger.info("Error: no found sqlite JDBC");
            }

            return DriverManager.getConnection("jdbc:sqlite:lababase.s3db");
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
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            logger.info("SQL error during closing connection");
        }
    }
}

