package ru.elleriumsoft.structure.objectstructure;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.Structure;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDb;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDbHome;
import ru.elleriumsoft.structure.StateOfElements;
import ru.elleriumsoft.structure.StructureElement;
import ru.elleriumsoft.structure.VariantsOfState;
import ru.elleriumsoft.structure.print.printonscreen.PrintStructureBean;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 02.04.2017.
 */

public class ObjectOfStructureBean implements SessionBean
{
    private Structure objectStructure;
    private ArrayList<StateOfElements> statesOfElements;
    private int selectedId;

    public int getMaxId()
    {
        int maxId = 0;
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            StructureProcessingFromDb structureProcessingFromDb = structureProcessingFromDbHome.findByMaxId();
            maxId = structureProcessingFromDb.getId();
        } catch (Exception e)
        {

            logger.info(e.getStackTrace());
        }
        logger.info("MAX_ID=" + maxId);
        return maxId;
    }

    public int getSizeStructure()
    {
        return objectStructure.getStructureForPrint().size();
    }

    public StructureElement getStructureElement(int id)
    {
        return objectStructure.getStructureForPrint().get(id);
    }

    public void initStructureFromDb()
    {
        Vector<StructureProcessingFromDb> structureFromBean = getDataFromDb();
        if (structureFromBean == null)
        {
            throw new NullPointerException("Нету инфы из базы");
        }
        ArrayList<StructureElement> structureFromDb = new ArrayList<>();

        for (StructureProcessingFromDb element : structureFromBean)
        {
            try
            {
                structureFromDb.add(new StructureElement(element.getId(), element.getNameDepartment(), element.getParent_id()));
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        objectStructure.setStructureForPrint(new ArrayList<StructureElement>());
        int level = 0;
        int parentId = 0;
        initElement(structureFromDb, level, parentId);
    }

    /**
     * Рекурсивное добавление элементов дерева в лист с указанием уровня
     * @param level - уровень погружения
     * @param parentId - предок
     */
    private void initElement(ArrayList<StructureElement> structureFromDb, int level, int parentId)
    {
        StructureElement el;
        for (int i = 0; i < structureFromDb.size(); i++)
        {
            el = structureFromDb.get(i);

            if (el.getParent_id() == parentId)
            {
                StructureElement elementForAdd = new StructureElement(el.getId(), el.getNameDepartment(), el.getParent_id());
                elementForAdd.setLevel(level);
                elementForAdd.setStateOfElement(getStateById(el.getId()));
                objectStructure.getStructureForPrint().add(elementForAdd);
                initElement(structureFromDb, level+1, el.getId());
            }
        }
    }

    private int getStateById(int id)
    {
        for (StateOfElements state : statesOfElements)
        {
            if (state.getId() == id)
            {
                return state.getState();
            }
        }
        return VariantsOfState.DELETED;
    }

    public Vector getDataFromDb()
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        Vector<StructureProcessingFromDb> dataFromDb = new Vector<>();
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            logger.info("len=" + statesOfElements.size());
            for (StateOfElements state : statesOfElements)
            {
                logger.info("id=" + state.getId() + ", " + "state=" + state.getState());
                dataFromDb.add(structureProcessingFromDbHome.findByPrimaryKey(state.getId()));
            }
        } catch (Exception e)
        {
            logger.info("error");
            logger.info(e.getMessage());
        }
        finally
        {
            return  dataFromDb;
        }
    }

    public StateOfElements getStateOfElement(int id)
    {
        for (StateOfElements stateOfElement : statesOfElements)
        {
            if (stateOfElement.getId() == id)
            {
                return stateOfElement;
            }
        }
        return null;
    }

    public void setStateOfElement(int id, int newState)
    {
        for (StateOfElements stateOfElement : statesOfElements)
        {
            if (stateOfElement.getId() == id)
            {
                stateOfElement.setState(newState);
            }
        }
    }

    public void removeDeleted()
    {

        for (int i =  statesOfElements.size()-1; i > 0; i--)
        {
            if (statesOfElements.get(i).getState() == VariantsOfState.DELETED)
            {
                statesOfElements.remove(i);
            }
        }
    }

    public void addStateElement(int id, int state)
    {
        statesOfElements.add(new StateOfElements(id, state));
    }

    private static final Logger logger = Logger.getLogger(PrintStructureBean.class.getName());


    public ObjectOfStructureBean()
    {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
    {
    }

    public void ejbCreate() throws CreateException
    {
        objectStructure = new Structure();
        statesOfElements = new ArrayList<>();
        statesOfElements.add(new StateOfElements(1, VariantsOfState.CLOSE));
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

    public int getSelectedId()
    {
        return selectedId;
    }

    public void setSelectedId(int selectedId)
    {
        this.selectedId = selectedId;
    }

    public String getNameDeptForSelectedId()
    {
        for (StructureElement element : objectStructure.getStructureForPrint())
        {
            if (element.getId() == selectedId)
            {
                return element.getNameDepartment();
            }
        }
        return "";
    }

    public Structure getObjectStructure()
    {
        return objectStructure;
    }
}
