package ru.elleriumsoft.xml.exchange.importxml;

import org.apache.log4j.Logger;
import ru.elleriumsoft.department.entity.EntityDept;
import ru.elleriumsoft.department.entity.EntityDeptHome;
import ru.elleriumsoft.department.object.Employee;
import ru.elleriumsoft.occupation.Occupation;
import ru.elleriumsoft.occupation.entity.EntityOccupation;
import ru.elleriumsoft.occupation.entity.EntityOccupationHome;
import ru.elleriumsoft.structure.Structure;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDb;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDbHome;
import ru.elleriumsoft.xml.exchange.DeptInfo;
import ru.elleriumsoft.xml.exchange.Exchange;

import javax.ejb.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Dmitriy on 17.05.2017.
 */
public class ImportXmlBean implements SessionBean
{
    private Exchange exchange;
    private boolean errorOnImport;
    private String typeErrorImport;
    private String resultOfImport;
    public final static String PATH_TO_FILE = "xml\\import.xml";

    private static final Logger logger = Logger.getLogger(ImportXmlBean.class.getName());

    public void importFromObjectToDatabase(boolean withOverwrite)
    {
        setResultOfImport("");
        setErrorOnImport(false);
        try
        {
            importOccupations(withOverwrite);
            importStructureAndEmployee(withOverwrite);
        }
        catch (RemoteException e)
        {
            logger.info("Remote exception: " + e.getMessage());
            setTypeErrorImport("Remote exception!");
            setErrorOnImport(true);
        }
    }

    private void importOccupations(boolean withOverwrite) throws RemoteException
    {
        int countEntry = 0;
        if (exchange.isWithOccupations() && exchange.getOccupations() != null && exchange.getOccupations().size() > 0)
        {

            EntityOccupationHome entityOccupationHome = new Occupation().getEntityOccupationHome();
            for (Occupation occ : exchange.getOccupations())
            {
                try
                {
                    entityOccupationHome.create(occ.getId(), occ.getName());
                    countEntry++;
                } catch (CreateException e)
                {
                    logger.info("id " + occ.getId() + " уже существует");
                    if (withOverwrite)
                    {
                        try
                        {
                            EntityOccupation entity = entityOccupationHome.findByPrimaryKey(occ.getId());
                            if (!entity.getNameOccupation().equals(occ.getName()))
                            {
                                entity.setNameOccupation(occ.getName());
                                entity.setNeedUpdate(true);
                            }
                            countEntry++;
                        } catch (FinderException e1)
                        {
                           logger.info("id " + occ.getId() + " не удалось перезаписать");
                            setTypeErrorImport("id " + occ.getId() + " не удалось перезаписать!");
                            setErrorOnImport(true);
                           return;
                        }

                    }
                }
            }
        }
        setResultOfImport(", профессий: " + String.valueOf(countEntry));
    }

    private void importStructureAndEmployee(boolean withOverwrite) throws RemoteException
    {
        int countDepts = 0;
        int countEmps = 0;
        StructureProcessingFromDbHome structureHome = new Structure().getStructureHome();
        EntityDeptHome deptHome = new Employee().getDeptHome();

        for (DeptInfo depts : exchange.getDepartments())
        {
            try
            {
                structureHome.create(depts.getIdDept(), depts.getNameDept(), depts.getParentIdDept());
            } catch (CreateException e)
            {
                logger.info("dept with id " + depts.getIdDept() + " уже существует");
                if (withOverwrite)
                {
                    try
                    {
                        StructureProcessingFromDb entity = structureHome.findByPrimaryKey(depts.getIdDept());
                        if (!(entity.getNameDepartment().equals(depts.getNameDept()) && entity.getParent_id() == depts.getParentIdDept()))
                        {
                            entity.setNameDepartment(depts.getNameDept());
                            entity.setParent_id(depts.getParentIdDept());
                            entity.setNeedUpdate();
                        }
                        countDepts++;
                    } catch (FinderException |  SQLException e1)
                    {
                        logger.info("dept with id " + depts.getIdDept() + " не удалось перезаписать");
                        setTypeErrorImport("Department with id " + depts.getIdDept() + " не удалось перезаписать!");
                        setErrorOnImport(true);
                        return;
                    }
                }
            }
            if (exchange.isWithEmployees() && depts.getEmployees() != null && depts.getEmployees().size() > 0)
            {
                for (Employee emp : depts.getEmployees())
                {
                    try
                    {
                        deptHome.create(emp.getId(), depts.getIdDept(), emp.getNameEmployee(), emp.getEmploymentDate(), emp.getIdProfession());
                    } catch (CreateException e)
                    {
                        logger.info("emp with id " + emp.getId() + " уже существует");
                        if (withOverwrite)
                        {
                            try
                            {
                                EntityDept entity = deptHome.findByPrimaryKey(emp.getId());
                                entity.setNameEmployee(emp.getNameEmployee());
                                entity.setEmploymentDate(emp.getEmploymentDate());
                                entity.setIdProfession(emp.getIdProfession());
                                entity.setIdDepartment(depts.getIdDept());
                                entity.setNeedUpdate();
                                countEmps++;
                            } catch (FinderException e1)
                            {
                                logger.info("emp with id " + depts.getIdDept() + " не удалось перезаписать");
                                setTypeErrorImport("Emp with id " + depts.getIdDept() + " не удалось перезаписать!");
                                setErrorOnImport(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
        String txt;
        if (withOverwrite)
        {
            txt = "обновлено";
        }
        else
        {
            txt = "добавлено";
        }
        setResultOfImport(txt + " отделов: " + String.valueOf(countDepts) + ", сотрудников: " + String.valueOf(countEmps) + getResultOfImport());
    }

    // восстанавливаем объект из XML файла
    public void importFromXmlToObject()
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Exchange.class);
            Unmarshaller un = jaxbContext.createUnmarshaller();

            exchange = (Exchange) un.unmarshal(new File(PATH_TO_FILE));

            if (verifyParentIds())
            {
                setErrorOnImport(false);
                logger.info("size deps=" + exchange.getDepartments().size());
            }
            else
            {
                setErrorOnImport(true);
                setTypeErrorImport("Links to non-existing parent elements");
            }
        } catch (JAXBException e)
        {
            setErrorOnImport(true);
            setTypeErrorImport("Error converting data to a database object");
        }
    }

    private boolean verifyParentIds()
    {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(0);
        for (DeptInfo deptInfo : exchange.getDepartments())
        {
            ids.add(deptInfo.getIdDept());
        }

        for (StructureProcessingFromDb structureElement : getAllDepts())
        {
            try
            {
                ids.add(structureElement.getId());
            } catch (RemoteException e)
            {
                logger.info("remote error: " + e.getMessage());
            }
        }

        for (DeptInfo deptInfo: exchange.getDepartments())
        {
            Integer parentId = deptInfo.getParentIdDept();
//            logger.info("PARENT_ID=" + parentId);
            if (!ids.contains(parentId))
            {
                return false;
            }
        }
        return true;
    }

    private Vector<StructureProcessingFromDb> getAllDepts()
    {
        StructureProcessingFromDbHome structureProcessingFromDb = new Structure().getStructureHome();
        Vector allDeptsFromDb = new Vector();
        try
        {
            allDeptsFromDb = (Vector) structureProcessingFromDb.findAll();
        } catch (FinderException e)
        {
            logger.info("finder error: " + e.getMessage());
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        }

        return allDeptsFromDb;
    }

    public void ejbCreate() throws CreateException
    {
        exchange = new Exchange();
        exchange.setDepartments(new ArrayList<DeptInfo>());
        exchange.setOccupations(new ArrayList<Occupation>());
        setErrorOnImport(false);
    }

    public Exchange getExchange()
    {
        return exchange;
    }

    public boolean isErrorOnImport()
    {
        return errorOnImport;
    }

    private void setErrorOnImport(boolean errorOnImport)
    {
        this.errorOnImport = errorOnImport;
    }

    public String getTypeErrorImport()
    {
        return typeErrorImport;
    }

    private void setTypeErrorImport(String typeErrorImport)
    {
        this.typeErrorImport = typeErrorImport;
    }

    public String getResultOfImport()
    {
        return resultOfImport;
    }

    private void setResultOfImport(String resultOfImport)
    {
        this.resultOfImport = resultOfImport;
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
    {
    }

    public void ejbRemove() throws EJBException
    {
    }

    public void ejbActivate() throws EJBException
    {
    }

    public void ejbPassivate() throws EJBException
    {
    }
}
