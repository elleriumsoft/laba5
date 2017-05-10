package ru.elleriumsoft.structure.object;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.StateOfElements;
import ru.elleriumsoft.structure.Structure;
import ru.elleriumsoft.structure.StructureElement;
import ru.elleriumsoft.structure.VariantsOfState;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDb;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDbHome;

import javax.ejb.*;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
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
    //private int selectedId;
    private boolean needUpdatePageAfterChangeState;

    private static final Logger logger = Logger.getLogger(ObjectOfStructure.class.getName());

    public int getMaxId()
    {
        int maxId = 0;
        //StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try
        {
//            InitialContext ic = new InitialContext();
//            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
//            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
//            StructureProcessingFromDb structureProcessingFromDb = structureProcessingFromDbHome.findByMaxId();
            maxId = getHome().findByMaxId().getId();//structureProcessingFromDb.getId();
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
                structureFromDb.add(newStructureElement(element.getId(), element.getNameDepartment(), element.getParent_id()));
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
     *
     * @param level    - уровень погружения
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
                StructureElement elementForAdd = newStructureElement(el.getId(), el.getNameDepartment(), el.getParent_id());
                elementForAdd.setLevel(level);
                elementForAdd.setStateOfElement(getStateById(el.getId()));
                objectStructure.getStructureForPrint().add(elementForAdd);
                initElement(structureFromDb, level + 1, el.getId());
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
        Vector<StructureProcessingFromDb> dataFromDb = new Vector<>();
        try
        {
//            InitialContext ic = new InitialContext();
//            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
            StructureProcessingFromDbHome structureProcessingFromDbHome = getHome();//(StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
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
        } finally
        {
            return dataFromDb;
        }
    }

//    public int getSelectedId()
//    {
//        return selectedId;
//    }
//
//    public void setSelectedId(int selectedId)
//    {
//        this.selectedId = selectedId;
//    }

    public String getNameDeptForSelectedId(int selectedId)
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


    public boolean checkNeedUpdatePage()
    {
        if (needUpdatePageAfterChangeState)
        {
            needUpdatePageAfterChangeState = false;
            return true;
        } else
        {
            return false;
        }
    }

    public void changeStateOfElementStructure(String id)
    {
        needUpdatePageAfterChangeState = false;

        if (id == null)
        {
            return;
        }
        int idElement = Integer.valueOf(id);
        StateOfElements state = getStateOfElement(idElement);

        switch (state.getState())
        {
            case VariantsOfState.OPEN:
            {
                closeList(idElement, idElement);
                removeDeleted();
                needUpdatePageAfterChangeState = true;
                break;
            }
            case VariantsOfState.CLOSE:
            {
                try
                {
                    openList(idElement);
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                needUpdatePageAfterChangeState = true;
                break;
            }
        }
    }

    private void closeList(int idElement, int firstElement)
    {
        for (int i = 0; i < getSizeStructure(); i++)
        {
            StructureElement element = getStructureElement(i);
            if (element.getParent_id() == idElement)
            {
                closeList(element.getId(), firstElement);
            }
        }

        if (idElement != firstElement)
        {
            setStateOfElement(idElement, VariantsOfState.DELETED);
        } else
        {
            setStateOfElement(idElement, VariantsOfState.CLOSE);
        }
    }

    private void openList(int idElement) throws RemoteException
    {
        Vector<StructureProcessingFromDb> children = getChildForElementFromDb(idElement);
        if (children != null && children.size() > 0)
        {
            logger.info("size=" + children.size());
            for (StructureProcessingFromDb structureProcessingFromDb : children)
            {

                addStateElement(structureProcessingFromDb.getId(), VariantsOfState.CLOSE);

            }
            setStateOfElement(idElement, VariantsOfState.OPEN);
        } else
        {
            logger.info("not found");
            setStateOfElement(idElement, VariantsOfState.NO_CHILD);
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

        for (int i = statesOfElements.size() - 1; i > 0; i--)
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

    private Vector getChildForElementFromDb(int id)
    {
//        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try
        {
//            InitialContext ic = new InitialContext();
//            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
//            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            return (Vector) getHome().findParentKeys(id);//structureProcessingFromDbHome.findParentKeys(id);
        } catch (Exception e)
        {

            logger.info(e.getStackTrace());
        }
        return null;
    }

    public void setCommandForChangeStructure(String command)
    {
        objectStructure.setCommandForChangeStructure(command);
    }

    public void setIdForChangeByCommand(Integer id)
    {
        objectStructure.setElementIdForChange(id);
    }

    public void modificationStructure(String param)
    {
        try
        {
            switch (objectStructure.getCommandForChangeStructure())
            {
                case "add":
                {
                    actionAddElement(param, getMaxId());
                    break;
                }
                case "edit":
                {
                    actionEditElement(param);
                    break;
                }
                case "delete":
                {
                    actionDeleteElement();
                }
            }
        } catch (RemoteException e)
        {
            logger.info(e.getMessage());
        }
        objectStructure.setCommandForChangeStructure("no");
    }

    private void actionAddElement(String param, int maxId) throws RemoteException
    {
        try
        {
            getHome().create(maxId + 1, param, objectStructure.getElementIdForChange());
            //addStateElement(maxId + 1, VariantsOfState.CLOSE);
            try
            {
                openList(objectStructure.getElementIdForChange());
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
            needUpdatePageAfterChangeState = true;
            initStructureFromDb();
        } catch (CreateException e)
        {
            e.printStackTrace();
        }
    }

    private void actionEditElement(String param) throws RemoteException
    {
        try
        {
            StructureProcessingFromDb structureProcessingFromDb = getHome().findByPrimaryKey(objectStructure.getElementIdForChange());
            if (structureProcessingFromDb != null)
            {
                structureProcessingFromDb.setNeedUpdate();
                structureProcessingFromDb.setNameDepartment(param);
            }
            initStructureFromDb();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } catch (FinderException e)
        {
            e.printStackTrace();
        }
    }

    private void actionDeleteElement() throws RemoteException
    {
        ArrayList<StructureElement> structureFromDb = new ArrayList<>();
        for (StructureProcessingFromDb structureElement : getAllElements())
        {
            structureFromDb.add(newStructureElement(structureElement.getId(), structureElement.getNameDepartment(), structureElement.getParent_id()));
        }

        deleteElement(objectStructure.getElementIdForChange(), structureFromDb);

        removeDeleted();
        initStructureFromDb();
    }

    private StructureElement newStructureElement(int id, String name, int parent_id)
    {
        StructureElement element = new StructureElement();
        element.setId(id);
        element.setNameDepartment(name);
        element.setParent_id(parent_id);
        element.setStateOfElement(VariantsOfState.DELETED);
        element.setLevel(0);
        return element;
    }

    private void deleteElement(Integer id, ArrayList<StructureElement> structureFromDb)
    {
        try
        {
            StructureProcessingFromDb structureProcessingFromDb = getElement(id);
            structureProcessingFromDb.remove();
            setStateOfElement(id, VariantsOfState.DELETED);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (RemoveException e)
        {
            e.printStackTrace();
        }
        for (StructureElement structureElement : structureFromDb)
        {
            if (structureElement.getParent_id() == id)
            {
                deleteElement(structureElement.getId(), structureFromDb);
            }
        }
    }

    public void ejbCreate() throws CreateException
    {
        objectStructure = new Structure();
        statesOfElements = new ArrayList<>();
        statesOfElements.add(new StateOfElements(1, VariantsOfState.CLOSE));
        needUpdatePageAfterChangeState = false;
    }

    private StructureProcessingFromDbHome getHome()
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
        } catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        return structureProcessingFromDbHome;
    }

    private Vector<StructureProcessingFromDb> getAllElements()
    {
        Vector<StructureProcessingFromDb> vector = new Vector<>();
        try
        {
            vector = (Vector) getHome().findAll();
        } catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        return vector;
    }

    private StructureProcessingFromDb getElement(Integer id)
    {
        StructureProcessingFromDb structureProcessingFromDb = null;
        try
        {
            structureProcessingFromDb = getHome().findByPrimaryKey(id);
        } catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        return structureProcessingFromDb;
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
