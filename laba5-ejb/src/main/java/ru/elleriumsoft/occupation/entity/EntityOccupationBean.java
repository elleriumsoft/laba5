package ru.elleriumsoft.occupation.entity;

import org.apache.log4j.Logger;
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
    private boolean needUpdate;
    private static final Logger logger = Logger.getLogger(EntityOccupationBean.class.getName());

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
            logger.info("sql error: " + e.getMessage());
        }
        finally
        {
            new ConnectToDb().closeConnection(connection);
        }
        return key;
    }

    public Integer ejbCreate(Integer id, String nameOccupation) throws CreateException
    {
        try {
            ejbFindByPrimaryKey(id);
            throw new DuplicateKeyException("Такой ключ уже есть");
        }
        catch (FinderException e) {}
        this.id = id;
        this.nameOccupation = nameOccupation;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = new ConnectToDb().getConnection();
            statement = connection.prepareStatement("INSERT INTO occupations"
                    + "(id, occupation) VALUES(?, ?)");
            statement.setInt(1, id);
            statement.setString(2, nameOccupation);
            if (statement.executeUpdate() != 1) {
                throw new CreateException("Ошибка вставки");
            }
            return this.id;
        } catch (SQLException e) {
            throw new EJBException("Ошибка INSERT");
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

    public void ejbPostCreate(Integer id, String nameOccupation) throws CreateException {}

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
        if (!isNeedUpdate()) {return;}
        setNeedUpdate(false);

        logger.info("Store Occupation");
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = new ConnectToDb().getConnection();
            statement = connection.prepareStatement(
                    "UPDATE occupations SET occupation = ? WHERE id = ?");
            statement.setString(1, getNameOccupation());
            statement.setInt(2, id);

            if (statement.executeUpdate() < 1) {
                throw new NoSuchEntityException("...");
            }
        } catch (SQLException e) {
            throw new EJBException("Ошибка UPDATE");
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
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

    private boolean isNeedUpdate()
    {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate)
    {
        this.needUpdate = needUpdate;
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException
    {
        this.entityContext = entityContext;
        setNeedUpdate(false);
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
}
