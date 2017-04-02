package ru.elleriumsoft.structure;

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
 * Created by Dmitriy on 18.03.2017.
 */
public class StructureProcessingFromDbBean implements EntityBean
{
    private Integer id;
    private String nameDepartment;
    private Integer parent_id;
    private int nestingLevel;
    private boolean needUpdate;

    private final static Logger logger = Logger.getLogger(StructureProcessingFromDbBean.class);

    private EntityContext entityContext;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public void setNeedUpdate()
    {
        needUpdate = true;
    }

    public Integer getParent_id()
    {
        return parent_id;
    }

    public void setParent_id(Integer parent_id)
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

    public Integer ejbFindByPrimaryKey(Integer key) throws FinderException
    {
        Connection connection = new ConnectToDb().getConnection();
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = connection.prepareStatement("select id from structure WHERE id=?");
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
        needUpdate = false;
    }

    public void unsetEntityContext() throws EJBException
    {
    }

    @Override
    public void ejbRemove() throws RemoveException, EJBException
    {
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement
                    ("DELETE FROM structure WHERE id = ?");
            statement.setInt(1, id);
            if(statement.executeUpdate() != 1) {
                throw new RemoveException("Could not remove: " + id);
            }
            statement.close();
            connection.close();
        } catch(SQLException e) {
            throw new EJBException("Could not remove", e);
        }
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
        setId((Integer)entityContext.getPrimaryKey());
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select dept, parent_id from structure where id = ?");
            statement.setInt(1, getId());
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                throw new NoSuchEntityException("Load wasn't execute");
            }
            setNameDepartment(result.getString(1));
            setParent_id(result.getInt(2));
        } catch (SQLException ex) {
            throw new EJBException("Cannot load current record with id: " + getId());
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

    public void ejbStore() throws EJBException
    {
        if (!needUpdate) {return;}
        needUpdate = false;

        logger.info("Store");
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = new ConnectToDb().getConnection();
            statement = connection.prepareStatement(
                    "UPDATE structure SET dept = ?, parent_id = ? WHERE id = ?");
            statement.setString(1, getNameDepartment());
            statement.setInt(2, getParent_id());
            statement.setInt(3, id);
            if (statement.executeUpdate() < 1) {
                throw new NoSuchEntityException("...");
            }
        } catch (SQLException e) {
            throw new EJBException("Ошибка UPDATE");
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

    public Collection ejbFindAll() throws FinderException, EJBException
    {
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select id from structure");
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

    public Collection ejbFindParentKeys(Integer key) throws FinderException, EJBException
    {
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select id from structure where parent_id = ?");
            statement.setInt(1, key);
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

    public Integer ejbCreate(Integer id, String name, Integer parent_id) throws CreateException
    {
        try {
            ejbFindByPrimaryKey(id);
            throw new DuplicateKeyException("Такой ключ уже есть");
        }
        catch (FinderException e) {}
        this.id = id;
        this.nameDepartment = name;
        this.parent_id = parent_id;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = new ConnectToDb().getConnection();
            statement = connection.prepareStatement("INSERT INTO structure"
                    + "(id, dept, parent_id) VALUES(?, ?, ?)");
            statement.setInt(1, id);
            statement.setString(2, nameDepartment);
            statement.setInt(3, parent_id);
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

    public void ejbPostCreate(Integer id, String name, Integer parent_id) throws CreateException
    {

    }

    public Integer ejbFindByMaxId() throws FinderException, EJBException
    {
        int maxId = 0;
        try(Connection connection = new ConnectToDb().getConnection();)
        {
            PreparedStatement statement = connection.prepareStatement("select id from structure where id = (select max(id) from structure)");
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new FinderException("Объект не найден");
            }
            maxId =  resultSet.getInt(1);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return maxId;
    }
}
