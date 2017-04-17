package ru.elleriumsoft.occupation.entity;

import ru.elleriumsoft.jdbc.ConnectToDb;

import javax.ejb.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitriy on 17.04.2017.
 */
public class EntityOccupationBean implements EntityBean
{
    private Integer id;
    private String nameOccupation;
    private EntityContext entityContext;

    public EntityOccupationBean()
    {
    }

    public Integer ejbFindByPrimaryKey(Integer key) throws FinderException
    {
        Connection connection = new ConnectToDb().getConnection();
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = connection.prepareStatement("select id from occupations WHERE id=?");
            preparedStatement.setInt(1, key);
            ResultSet resultSet  = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new FinderException("Объект не найден");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            new ConnectToDb().closeConnection(connection);
        }
        return key;
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException
    {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException
    {
    }

    public void ejbRemove() throws RemoveException, EJBException
    {
    }

    public void ejbActivate() throws EJBException
    {
    }

    public void ejbPassivate() throws EJBException
    {
    }

    public void ejbLoad() throws EJBException
    {
        setId((Integer)entityContext.getPrimaryKey());
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select occupation from occupations where id = ?");
            statement.setInt(1, getId());
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                throw new NoSuchEntityException("Load wasn't execute");
            }
            setNameOccupation(result.getString(1));
        } catch (SQLException ex) {
            throw new EJBException("Cannot load current record with id: " + getId());
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

    public Collection ejbFindAll() throws FinderException, EJBException
    {
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select id from occupations");
            ResultSet result = statement.executeQuery();
            List<Integer> array = new ArrayList<>();
            while (result.next()) {
                Integer id = result.getInt(1);
                array.add(id);
            }
            return array;
        } catch (SQLException ex) {
            throw new EJBException("Cannot find all record");
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

    public void ejbStore() throws EJBException
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNameOccupation()
    {
        return nameOccupation;
    }

    public void setNameOccupation(String nameOccupation)
    {
        this.nameOccupation = nameOccupation;
    }
}
