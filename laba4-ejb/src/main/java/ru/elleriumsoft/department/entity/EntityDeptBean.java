package ru.elleriumsoft.department.entity;

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
 * Created by Dmitriy on 12.04.2017.
 */
public class EntityDeptBean implements EntityBean
{
    private Integer id;
    private String nameEmployee;
    private Integer idDepartment;
    private Integer idProfession;
    private String nameProfession;
    private String employmentDate;

    private boolean needUpdate;

    private EntityContext entityContext;
    private static final Logger logger = Logger.getLogger(EntityDeptBean.class.getName());

    public EntityDeptBean()
    {
    }

    public void setNeedUpdate()
    {
        needUpdate = true;
    }

    public Integer ejbFindByPrimaryKey(Integer key) throws FinderException
    {
        logger.info("Loading id=" + key);
        Connection connection = new ConnectToDb().getConnection();
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = connection.prepareStatement("select id from employee WHERE id=?");
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
            PreparedStatement statement = connection.prepareStatement("select employee.name, employee.id_occ, employee.date, employee.id_dept, occupations.occupation from employee, occupations where employee.id = ? and employee.id_occ=occupations.id");
            statement.setInt(1, getId());
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                throw new NoSuchEntityException("Load wasn't execute");
            }
            setNameEmployee(result.getString(1));
            setIdProfession(result.getInt(2));
            setEmploymentDate(result.getString(3));
            setIdDepartment(result.getInt(4));
            setNameProfession(result.getString(5));
        } catch (SQLException ex) {
            throw new EJBException("Cannot load current record with id: " + getId());
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

    public Collection ejbFindAll(Integer idDeptartment) throws FinderException, EJBException
    {
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select id from employee WHERE id_dept = ?");
            statement.setInt(1, idDeptartment);
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

    public Integer ejbCreate(Integer id, Integer id_dept, String name, String date, Integer occ_id) throws CreateException
    {
        try {
            ejbFindByPrimaryKey(id);
            throw new DuplicateKeyException("Такой ключ уже есть");
        }
        catch (FinderException e) {}
        logger.info("create new entity dept");
        logger.info("id=" + id);
        logger.info("id_dept=" + id_dept);
        logger.info("name=" + name);
        logger.info("date=" + date);
        logger.info("id_prof=" + occ_id);
        this.id = id;
        this.idDepartment = id_dept;
        this.nameEmployee = name;
        this.employmentDate = date;
        this.idProfession = occ_id;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = new ConnectToDb().getConnection();
            statement = connection.prepareStatement("INSERT INTO employee"
                    + "(id, id_dept, name, date, id_occ) VALUES(?, ?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setInt(2, idDepartment);
            statement.setString(3, nameEmployee);
            statement.setString(4, employmentDate);
            statement.setInt(5, idProfession);
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

    public void ejbPostCreate(Integer id, Integer id_dept, String name, String date, Integer occ_id) throws CreateException {}

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
                    "UPDATE employee SET id_dept = ?, id_occ = ?, name = ?, date = ? WHERE id = ?");
            statement.setInt(1, getIdDepartment());
            statement.setInt(2, getIdProfession());
            statement.setString(3, getNameEmployee().toLowerCase());
            statement.setString(4, getEmploymentDate());
            statement.setInt(5, id);

            if (statement.executeUpdate() < 1) {
                throw new NoSuchEntityException("...");
            }
        } catch (SQLException e) {
            throw new EJBException("Ошибка UPDATE");
        } finally {
            new ConnectToDb().closeConnection(connection);
        }
    }

    public void ejbRemove() throws RemoveException, EJBException
    {
        Connection connection = null;
        try
        {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement
                    ("DELETE FROM employee WHERE id = ?");
            statement.setInt(1, id);
            if (statement.executeUpdate() != 1)
            {
                throw new RemoveException("Could not remove: " + id);
            }
            statement.close();
            connection.close();
        } catch (SQLException e)
        {
            throw new EJBException("Could not remove", e);
        }
    }

    public Integer ejbFindByMaxId() throws FinderException, EJBException
    {
        int maxId = 0;
        try(Connection connection = new ConnectToDb().getConnection();)
        {
            PreparedStatement statement = connection.prepareStatement("select id from employee where id = (select max(id) from employee)");
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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNameEmployee()
    {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee)
    {
        this.nameEmployee = nameEmployee;
    }

    public Integer getIdDepartment()
    {
        return idDepartment;
    }

    public void setIdDepartment(Integer idDepartment)
    {
        this.idDepartment = idDepartment;
    }

    public Integer getIdProfession()
    {
        return idProfession;
    }

    public void setIdProfession(Integer idProfession)
    {
        this.idProfession = idProfession;
    }

    public String getEmploymentDate()
    {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate)
    {
        this.employmentDate = employmentDate;
    }

    public String getNameProfession()
    {
        return nameProfession;
    }

    public void setNameProfession(String nameProfession)
    {
        this.nameProfession = nameProfession;
    }
}
