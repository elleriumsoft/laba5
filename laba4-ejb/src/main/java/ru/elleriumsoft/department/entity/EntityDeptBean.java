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

    private EntityContext entityContext;
    private static final Logger logger = Logger.getLogger(EntityDeptBean.class.getName());

    public EntityDeptBean()
    {
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
        setId((Integer)entityContext.getPrimaryKey());
        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = connection.prepareStatement("select employee.name, employee.id_occ, employee.date, occupations.occupation from employee, occupations where employee.id = ? and employee.id_occ=occupations.id");
            statement.setInt(1, getId());
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                throw new NoSuchEntityException("Load wasn't execute");
            }
            setNameEmployee(result.getString(1));
            setIdProfession(result.getInt(2));
            setEmploymentDate(result.getString(3));
            setNameProfession(result.getString(4));
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
