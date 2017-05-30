package ru.elleriumsoft.structure.object;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.StateOfElements;
import ru.elleriumsoft.structure.Structure;
import ru.elleriumsoft.structure.StructureElement;
import ru.elleriumsoft.structure.VariantsOfState;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDb;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDbHome;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Dmitriy on 02.04.2017.
 */
public class ObjectOfStructureBean implements SessionBean
{
    private Structure objectStructure;
    private ArrayList<StateOfElements> statesOfElements;
    private boolean needUpdatePageAfterChangeState;

    private static final Logger logger = Logger.getLogger(ObjectOfStructure.class.getName());

    public int getMaxId()
    {
        int maxId = 0;
        try
        {
            maxId = objectStructure.getStructureHome().findByMaxId().getId();
        } catch (Exception e)
        {
            logger.info(e.getStackTrace());
        }
        logger.info("MAX_ID=" + maxId);
        return maxId;
    }

    public void initStructureFromDb()
    {
        Vector<StructureProcessingFromDb> structureFromBean = getDataFromDb();
        objectStructure.setStructureForPrint(new ArrayList<StructureElement>());

        if (structureFromBean == null)
        {
            return;
        }

        ArrayList<StructureElement> structureFromDb = new ArrayList<>();
        for (StructureProcessingFromDb element : structureFromBean)
        {
            try
            {
                structureFromDb.add(newStructureElement(element.getId(), element.getNameDepartment(), element.getParent_id()));
            } catch (RemoteException e)
            {
                logger.info("remote error: " + e.getMessage());
            }
        }

        int level = 0;
        int parentId = 0;
        initElement(structureFromDb, level, parentId);
    }

    /**
     * Рекурсивное добавление элементов дерева в лист с указанием уровня
     * @param level    - уровень погружения
     * @param parentId - предок
     */
    private void initElement(ArrayList<StructureElement> structureFromDb, int level, int parentId)
    {
        StructureElement el;
        for (int i = 0; i < structureFromDb.size(); i++)
        {
            el = structureFromDb.get(i);

            if (el.getParentId() == parentId)
            {
                StructureElement elementForAdd = newStructureElement(el.getId(), el.getNameDepartment(), el.getParentId());
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
            StructureProcessingFromDbHome structureProcessingFromDbHome = objectStructure.getStructureHome();
            logger.info("len=" + statesOfElements.size());
            for (StateOfElements state : statesOfElements)
            {
                logger.info("id=" + state.getId() + ", " + "state=" + state.getState());
                dataFromDb.add(structureProcessingFromDbHome.findByPrimaryKey(state.getId()));
            }
        } catch (Exception e)
        {
            logger.info("error: " + e.getMessage());
        }
        return dataFromDb;
    }

    public String getNameDeptForSelectedId(int selectedId)
    {
        try
        {
            return objectStructure.getStructureHome().findByPrimaryKey(selectedId).getNameDepartment();

        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        } catch (SQLException e)
        {
            logger.info("sql error: " + e.getMessage());
        } catch (FinderException e)
        {
            logger.info(selectedId + " не найден!");
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
                    logger.info("remote error: " + e.getMessage());
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
            if (element.getParentId() == idElement)
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
        if (children != null && !children.isEmpty())
        {
            logger.info("size=" + children.size());
            for (StructureProcessingFromDb structureProcessingFromDb : children)
            {

                addStateElement(structureProcessingFromDb.getId(), VariantsOfState.CLOSE);

            }
            setStateOfElement(idElement, VariantsOfState.OPEN);
        } else
        {
            logger.info(idElement + " not found in list");
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
        try
        {
            return (Vector) objectStructure.getStructureHome().findParentKeys(id);
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

    public Integer getIdForChangeByCommand()
    {
        return objectStructure.getElementIdForChange();
    }

    public void modificationStructure(String newNameOfDepartment)
    {
        try
        {
            switch (objectStructure.getCommandForChangeStructure())
            {
                case "add":
                {
                    actionAddElement(newNameOfDepartment, getMaxId());
                    break;
                }
                case "edit":
                {
                    actionEditElement(newNameOfDepartment);
                    break;
                }
                case "delete":
                {
                    actionDeleteElement();
                    break;
                }
            }
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        }
        objectStructure.setCommandForChangeStructure("no");
    }

    private void actionAddElement(String param, int maxId) throws RemoteException
    {
        try
        {
            objectStructure.getStructureHome().create(maxId + 1, param, objectStructure.getElementIdForChange());
            openList(objectStructure.getElementIdForChange());
            needUpdatePageAfterChangeState = true;
            initStructureFromDb();
        } catch (CreateException e)
        {
            logger.info("create error: " + e.getMessage());
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        }
    }

    private void actionEditElement(String param) throws RemoteException
    {
        try
        {
            StructureProcessingFromDb structureProcessingFromDb = objectStructure.getStructureHome().findByPrimaryKey(objectStructure.getElementIdForChange());
            if (structureProcessingFromDb != null)
            {
                structureProcessingFromDb.setNeedUpdate();
                structureProcessingFromDb.setNameDepartment(param);
            }
            initStructureFromDb();
        } catch (SQLException e)
        {
            logger.info("sql error: " + e.getMessage());
        } catch (FinderException e)
        {
            logger.info("Элемент для редактирования " + objectStructure.getElementIdForChange() + " не найден");
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
        element.setParentId(parent_id);
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

            for (StructureElement structureElement : structureFromDb)
            {
                if (structureElement.getParentId() == id)
                {
                    deleteElement(structureElement.getId(), structureFromDb);
                }
            }
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        } catch (RemoveException e)
        {
            logger.info("remove error: " + e.getMessage());
        }
    }

    public void ejbCreate() throws CreateException
    {
        objectStructure = new Structure();
        statesOfElements = new ArrayList<>();
        statesOfElements.add(new StateOfElements(1, VariantsOfState.CLOSE));
        needUpdatePageAfterChangeState = false;
        setErrorOnImport("no");
    }

    private Vector<StructureProcessingFromDb> getAllElements()
    {
        Vector<StructureProcessingFromDb> vector = new Vector<>();
        try
        {
            vector = (Vector) objectStructure.getStructureHome().findAll();
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
            structureProcessingFromDb = objectStructure.getStructureHome().findByPrimaryKey(id);
        } catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        return structureProcessingFromDb;
    }

    public int getSizeStructure()
    {
        return objectStructure.getStructureForPrint().size();
    }

    public StructureElement getStructureElement(int id)
    {
        return objectStructure.getStructureForPrint().get(id);
    }


    public void setErrorOnImport(String errorMessage)
    {
        objectStructure.setErrorOnImport(errorMessage);
    }

    public void setResultOfImport(String resultMessage)
    {
        objectStructure.setResultOfImport(resultMessage);
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
