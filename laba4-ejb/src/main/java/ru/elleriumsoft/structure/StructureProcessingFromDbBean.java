package ru.elleriumsoft.structure;

import ru.elleriumsoft.jdbc.ConnectToDb;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public class StructureProcessingFromDbBean implements EntityBean
{
    private String id;
    private String nameDepartment;
    private String parent_id;
    private int nestingLevel;

    private EntityContext entityContext;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNameDepartment()
    {
        return nameDepartment;
    }

    public void setNameDepartment(String nameDepartment)
    {
        this.nameDepartment = nameDepartment;
    }

    public String getParent_id()
    {
        return parent_id;
    }

    public void setParent_id(String parent_id)
    {
        this.parent_id = parent_id;
    }

    public int getNestingLevel()
    {
        return nestingLevel;
    }

    public void setNestingLevel(int nestingLevel)
    {
        this.nestingLevel = nestingLevel;
    }

    public StructureProcessingFromDbBean()
    {
    }

    public String ejbFindByPrimaryKey(String key) throws SQLException, FinderException
    {
        Connection connection = new ConnectToDb().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select id from structure WHERE id=?");
        preparedStatement.setInt(1, Integer.valueOf(key));
        ResultSet resultSet  = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new FinderException("Объект не найден");
        }
        new ConnectToDb().closeConnection(connection);
        return key;
    }

    public String ejbCreate(String id, String nameDepartment, String parent_id) throws CreateException, EJBException, RemoteException
    {
        this.id = id;
        this.nameDepartment = nameDepartment;
        this.parent_id = parent_id;
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into structure values (?, ?, ?)");
            statement.setInt(1, Integer.valueOf(id));
            statement.setString(2, nameDepartment);
            statement.setInt(3, Integer.valueOf(parent_id));
            if (statement.executeUpdate() < 1) {
                throw new CreateException("Insert wasn't execute");
            }
            return this.id;
        } catch (SQLException ex) {
            throw new EJBException("Cannot insert current a_author record with id: " + getId());
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
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

    @Override
    public void ejbLoad() throws EJBException
    {
        setId((String)entityContext.getPrimaryKey());
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select dept, parent_id from structure where id = ?");
            statement.setInt(1, Integer.valueOf(getId()));
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                throw new NoSuchEntityException("Load wasn't execute");
            }
            setNameDepartment(result.getString(1));
            setParent_id(String.valueOf(result.getInt(2)));
        } catch (SQLException ex) {
            throw new EJBException("Cannot load current record with id: " + getId());
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

    public void ejbStore() throws EJBException
    {
    }

    public Collection ejbFindAll() throws FinderException, EJBException
    {
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select id from structure");
            ResultSet result = statement.executeQuery();
            List<String> array = new ArrayList<>();
            while (result.next()) {
                String id = String.valueOf(result.getInt(1));
                array.add(id);
            }
            return array;
        } catch (SQLException ex) {
            throw new EJBException("Cannot find all record");
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

}
