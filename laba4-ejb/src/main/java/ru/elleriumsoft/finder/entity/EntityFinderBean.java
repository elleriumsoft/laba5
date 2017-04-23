package ru.elleriumsoft.finder.entity;

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
 * Created by Dmitriy on 23.04.2017.
 */
public class EntityFinderBean implements EntityBean
{
    private Integer id;
    private String nameEmployee;
    private Integer idDepartment;
    private String nameDepartment;
    private Integer idProfession;
    private String nameProfession;
    private String employmentDate;

    private String nameForFind;
    private Integer occForFind;
    private String startDateForFind;
    private String endDateForFind;

    private EntityContext entityContext;
    private static final Logger logger = Logger.getLogger(EntityFinderBean.class.getName());

    public EntityFinderBean()
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

    public Collection ejbFinder(String nameForFind, String occForFind, String startDateForFind, String endDateForFind) throws FinderException, EJBException
    {
        //if (nameForFind.equals("")) { nameForFind = null; }
        if (nameForFind == null) { nameForFind = ""; }
//        if () { startDateForFind = null; }
//        if () { endDateForFind = null; }
        setNameForFind(nameForFind);
        if (occForFind == null)
        {
            setOccForFind(0);
        }
        else
        {
            setOccForFind(Integer.valueOf(occForFind));
        }
        if (startDateForFind == null || startDateForFind.equals("") ) { startDateForFind = "1900-01-01"; }
        if (endDateForFind == null ||endDateForFind.equals("")) { endDateForFind = "2100-01-01"; }
        setStartDateForFind(startDateForFind);
        setEndDateForFind(endDateForFind);
        logger.info("name=" + getNameForFind());
        logger.info("occ_id=" + getOccForFind());
        logger.info("dates = " + getStartDateForFind() + "-" + getEndDateForFind());

        Connection connection = null;
        try {
            connection = new ConnectToDb().getConnection();
            PreparedStatement statement = getStatementDependingOnParameters(connection);
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

    public PreparedStatement getStatementDependingOnParameters(Connection connection) throws SQLException
    {
        PreparedStatement statement = null;
//        if (getNameForFind() != null && getOccForFind() == 0 && getStartDateForFind() == null)
//        {
//            logger.info("поиск по имени");
//            statement = connection.prepareStatement("select id from employee WHERE name LIKE ?");
//            statement.setString(1, "%" + getNameForFind() + "%");
//        }
//        if (getNameForFind() == null && getOccForFind() != 0 && getStartDateForFind() == null)
//        {
//            logger.info("поиск по профе");
//            statement = connection.prepareStatement("select id from employee WHERE id_occ = ?");
//            statement.setInt(1, getOccForFind());
//        }
//        if (getNameForFind() == null && getOccForFind() == 0 && getStartDateForFind() != null)
//        {
//            logger.info("поиск по дате");
//            statement = connection.prepareStatement("select id from employee WHERE date BETWEEN ? and ?");
//            statement.setString(1, getStartDateForFind());
//            statement.setString(2, getEndDateForFind());
//        }
//        if (getNameForFind() != null && getOccForFind() != 0 && getStartDateForFind() == null)
//        {
//            logger.info("поиск по имени и профе");
//            statement = connection.prepareStatement("select id from employee WHERE name LIKE ? and id_occ = ?");
//            statement.setString(1, "%" + getNameForFind() + "%");
//            statement.setInt(2, getOccForFind());
//        }
        if (getOccForFind() == 0)
        {
            logger.info("поиск без должности");
            statement = connection.prepareStatement("select id from employee WHERE name LIKE ? and date BETWEEN ? and ? ORDER BY id_dept");
            statement.setString(1, "%" + getNameForFind() + "%");
            statement.setString(2, getStartDateForFind());
            statement.setString(3, getEndDateForFind());
        }
        else
        {
            logger.info("поиск по всем параметрам");
            statement = connection.prepareStatement("select id from employee WHERE name LIKE ? and id_occ = ? and date BETWEEN ? and ? ORDER BY id_dept");
            statement.setString(1, "%" + getNameForFind() + "%");
            statement.setInt(2, getOccForFind());
            statement.setString(3, getStartDateForFind());
            statement.setString(4, getEndDateForFind());

        }
//        if (getNameForFind() == null && getOccForFind() != 0 && getStartDateForFind() != null)
//        {
//            logger.info("поиск по профе и дате");
//            statement = connection.prepareStatement("select id from employee WHERE id_occ = ? and date BETWEEN ? and ?");
//            statement.setInt(1, getOccForFind());
//            statement.setString(2, getStartDateForFind());
//            statement.setString(3, getEndDateForFind());
//        }
        return statement;
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
            PreparedStatement statement = connection.prepareStatement("select employee.name, employee.id_occ, employee.date, employee.id_dept, occupations.occupation, structure.id, structure.dept from employee, occupations, structure where employee.id = ? and employee.id_dept = structure.id and employee.id_occ=occupations.id");
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
            setIdDepartment(result.getInt(6));
            setNameDepartment(result.getString(7));
        } catch (SQLException ex) {
            throw new EJBException("Cannot load current record with id: " + getId());
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

    public String getNameDepartment()
    {
        return nameDepartment;
    }

    public void setNameDepartment(String nameDepartment)
    {
        this.nameDepartment = nameDepartment;
    }

    public Integer getIdProfession()
    {
        return idProfession;
    }

    public void setIdProfession(Integer idProfession)
    {
        this.idProfession = idProfession;
    }

    public String getNameProfession()
    {
        return nameProfession;
    }

    public void setNameProfession(String nameProfession)
    {
        this.nameProfession = nameProfession;
    }

    public String getEmploymentDate()
    {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate)
    {
        this.employmentDate = employmentDate;
    }

    public String getNameForFind()
    {
        return nameForFind;
    }

    public void setNameForFind(String nameForFind)
    {
        this.nameForFind = nameForFind;
    }

    public Integer getOccForFind()
    {
        return occForFind;
    }

    public void setOccForFind(Integer occForFind)
    {
        this.occForFind = occForFind;
    }

    public String getStartDateForFind()
    {
        return startDateForFind;
    }

    public void setStartDateForFind(String startDateForFind)
    {
        this.startDateForFind = startDateForFind;
    }

    public String getEndDateForFind()
    {
        return endDateForFind;
    }

    public void setEndDateForFind(String endDateForFind)
    {
        this.endDateForFind = endDateForFind;
    }
}
