package ru.elleriumsoft.actionforstucture;

import org.apache.log4j.Logger;
import ru.elleriumsoft.printstructure.StructureElement;
import ru.elleriumsoft.printstructure.VariantsOfState;
import ru.elleriumsoft.printstructure.objectstructure.ObjectOfStructure;
import ru.elleriumsoft.structure.StructureProcessingFromDb;
import ru.elleriumsoft.structure.StructureProcessingFromDbHome;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public class ActionForStructureBean implements SessionBean
{
    private Integer idForAction;
    private String action;

    //private String answer;
    ArrayList<StructureElement> structureFromDb;

    private static final Logger logger = Logger.getLogger(ActionForStructureBean.class.getName());

    public void action(String param, int maxId, ObjectOfStructure objectOfStructure)
    {

        //if (param == null){ return;}
    try
    {
        switch (action)
        {
            case "add":
            {
                actionAddElement(param, maxId, objectOfStructure);
                break;
            }
            case "edit":
            {
                actionEditElement(param, objectOfStructure);
                break;
            }
            case "delete":
            {
                logger.info("delete");
                actionDeleteElement(objectOfStructure);
                break;
            }
            default:
            {
                setIdForAction(0);
                setAction("");
            }
        }
    }
    catch (RemoteException e)
    {
        logger.info(e.getMessage());
    }
//        if (action.equals("add"))
//        {
//            actionAddElement(param, maxId, objectOfStructure);
//        }
//        if (action.equals("edit"))
//        {
//            actionEditElement(param, objectOfStructure);
//        }

//        setIdForAction(0);
//        setAction("");
    }

    private void actionAddElement(String param, int id, ObjectOfStructure objectOfStructure) throws RemoteException
    {
        try
        {
            getHome().create(id+1, param, idForAction);
            objectOfStructure.addStateElement(id+1, VariantsOfState.CLOSE);
            objectOfStructure.initStructureFromDb();
        } catch (CreateException e)
        {
            e.printStackTrace();
        }
    }

    private void actionEditElement(String param, ObjectOfStructure objectOfStructure) throws RemoteException
    {
        try
        {
            StructureProcessingFromDb structureProcessingFromDb = getHome().findByPrimaryKey(idForAction);
            if (structureProcessingFromDb != null)
            {
                structureProcessingFromDb.setNeedUpdate();
                structureProcessingFromDb.setNameDepartment(param);
            }
            objectOfStructure.initStructureFromDb();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } catch (FinderException e)
        {
            e.printStackTrace();
        }
    }

    private void actionDeleteElement(ObjectOfStructure objectOfStructure) throws RemoteException
    {
       //answer = "";
        structureFromDb = new ArrayList<>();
        for (StructureProcessingFromDb structureElement : getAllElements())
        {
            structureFromDb.add(new StructureElement(structureElement.getId(), structureElement.getNameDepartment(), structureElement.getParent_id(), 0));
        }

        deleteElement(idForAction, objectOfStructure);

        objectOfStructure.removeDeleted();
        objectOfStructure.initStructureFromDb();
    }

    private void deleteElement(Integer id, ObjectOfStructure objectOfStructure)
    {
        try
        {
            StructureProcessingFromDb structureProcessingFromDb = getElement(id);
            //answer = answer + "Элемент " + structureProcessingFromDb.getNameDepartment() + " удален из структуры!" + "<br>";
            structureProcessingFromDb.remove();
            objectOfStructure.setStateOfElement(id, VariantsOfState.DELETED);
        } catch (RemoteException e)
        {
            e.printStackTrace();
            //answer = answer + "Не удалось удалить элемент!" + "<br>";
        } catch (RemoveException e)
        {
            e.printStackTrace();
        }
        for (StructureElement structureElement : structureFromDb)
        {
            if (structureElement.getParent_id() == id)
            {
                deleteElement(structureElement.getId(), objectOfStructure);
            }
        }
    }


    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public Integer getIdForAction()
    {
        return idForAction;
    }

    public void setIdForAction(Integer idForAction)
    {
        this.idForAction = idForAction;
    }

    public ActionForStructureBean()
    {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
    {
    }

    public void ejbCreate() throws CreateException
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

    private StructureProcessingFromDbHome getHome()
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return structureProcessingFromDbHome;
    }

    Vector<StructureProcessingFromDb> getAllElements()
    {
        Vector<StructureProcessingFromDb> vector = new Vector<>();
        try {
//            InitialContext ic = new InitialContext();
//            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
//            StructureProcessingFromDbHome structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
//            vector = (Vector) structureProcessingFromDbHome.findAll();
            vector = (Vector) getHome().findAll();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return vector;
    }

    StructureProcessingFromDb getElement(Integer id)
    {
        StructureProcessingFromDb structureProcessingFromDb = null;
        try {
            structureProcessingFromDb = getHome().findByPrimaryKey(id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return structureProcessingFromDb;
    }
}
