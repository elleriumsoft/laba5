package ru.elleriumsoft.printstructure;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.StructureProcessingFromDb;
import ru.elleriumsoft.structure.StructureProcessingFromDbHome;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Dmitriy on 23.03.2017.
 */
public class PrintStructureBean implements SessionBean
{
    private ArrayList<StructureElement> structureFromDb;
    private ArrayList<StructureElement> structureForPrint;
    private ArrayList<StateOfElements> statesOfElements;
    private int maxId = 0;

    private static final Logger logger = Logger.getLogger(PrintStructureBean.class.getName());

    public int getMaxId()
    {
        return maxId;
    }

    public ArrayList<StructureElement> getStructureFromDb()
    {
        return structureFromDb;
    }

    public void initStructureFromDb()
    {
        Vector<StructureProcessingFromDb> structureFromBean = getDataFromDb();
        if (structureFromBean == null)
        {
            throw new NullPointerException("Нету инфы из базы");
        }
        structureFromDb = new ArrayList<>();
        maxId = 0;
        for (StructureProcessingFromDb element : structureFromBean)
        {
            try
            {
                if (element.getId() > maxId)
                {
                    maxId = element.getId();
                }
                structureFromDb.add(new StructureElement(element.getId(), element.getNameDepartment(), element.getParent_id()));
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        structureForPrint = new ArrayList<>();
        int level = 0;
        int parentId = 0;
        initElement(level, parentId);
    }

    /**
     * Рекурсивное добавление элементов дерева в лист с указанием уровня
     * @param level - уровень погружения
     * @param parentId - предок
     */
    private void initElement(int level, int parentId)
    {
        StructureElement el;
        for (int i = 0; i < structureFromDb.size(); i++)
        {
            el = structureFromDb.get(i);

            if (el.getParent_id() == parentId)
            {
                StructureElement elementForAdd = new StructureElement(el.getId(), el.getNameDepartment(), el.getParent_id());
                elementForAdd.setLevel(level);
                structureForPrint.add(elementForAdd);
                initElement(level+1, el.getId());
            }
        }
    }

    /**
     * Формирование HTML страницы структуры для вывода из уже проинциализированной структуры из БД
     *
     * @param command Добавленная ссылка на команду действия с элементом
     * @return Сформированная HTML страница
     */
    public String printStructure(String command)
    {
        StringBuilder pw = new StringBuilder("");
        try
        {
            initStructureFromDb();

            if (command == null)
            {
                command = "";
            }
            for (StructureElement el : structureForPrint)
            {
                pw.append(addSpaces(el.getLevel()));
                pw.append(addImageForActionList(el.getId()));
                pw.append("&nbsp<span><a href=\"/PrintElementJsp.jsp?id=" + String.valueOf(el.getId()) + "\">" + el.getNameDepartment() + "</a>&nbsp");//pw.append("&nbsp<span><a href=\"/laba3/Servlets.PrintElement?id=" + String.valueOf(el.getId()) + "\">" + el.getNameDepartment() + "</a>&nbsp");
                if (!command.equals("") && !(command.equals("delete") && el.getId() == 1))
                {
                    pw.append("<a href=\"/Structure.jsp?command=" + command + "&element=" + el.getId() + "\"style=\"color:#FF0000\">[" + getStringCommand(command) + "]</a>");
                }
                pw.append("</span><br><br>");
            }
        }
        catch (NullPointerException e)
        {
            System.out.println(e.getMessage());
        }
        return pw.toString();
    }

    public boolean checkNeedChangeState(String id)
    {
        if (id == null) { return false;}
        int idElement = Integer.valueOf(id);
        StateOfElements state = getStateOfElement(idElement);
        if (state.getState() == StateOfElements.NO_CHILD) { return false; }
        if (state.getState() == StateOfElements.CLOSE)
        {
            openList(state, idElement);
            return true;
        }
        if (state.getState() == StateOfElements.OPEN)
        {
            closeList(idElement, idElement);
            return true;
        }

        return false;
    }

    private void closeList(int idElement, int firstElement)
    {
        for (StructureElement element : structureFromDb)
        {
            if (element.getParent_id() == idElement)
            {
                closeList(element.getId(), firstElement);
            }
        }
        if (idElement != firstElement)
        {
            statesOfElements.remove(getStateOfElement(idElement));
        }
        else
        {
            getStateOfElement(idElement).setState(StateOfElements.CLOSE);
        }
    }

    private void openList(StateOfElements state, int idElement)
    {
        Vector<StructureProcessingFromDb> children = getChildForElementFromDb(idElement);
        if (children != null && children.size() > 0)
        {
            logger.info("size=" + children.size());
            for (StructureProcessingFromDb structureProcessingFromDb : children)
            {
                try
                {
                    statesOfElements.add(new StateOfElements(structureProcessingFromDb.getId(), StateOfElements.CLOSE));
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
            state.setState(StateOfElements.OPEN);
        }
        else
        {
            logger.info("not found");
            state.setState(StateOfElements.NO_CHILD);
        }
    }

    private StateOfElements getStateOfElement(int id)
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

    private String addImageForActionList(int idElement)
    {
        StateOfElements state = getStateOfElement(idElement);
        if (state != null)
        {
            if (state.getState() != StateOfElements.NO_CHILD)
            {
                if (state.getState() == StateOfElements.OPEN)
                {
                    return "<a href=\"Structure.jsp?open=" + String.valueOf(idElement) + "\"><img src=\"images/minus.png\" width=\"14\" height=\"14\" align = \"bottom\" alt=\"Свернуть список\"</a>";
                } else
                {
                    return "<a href=\"Structure.jsp?open=" + String.valueOf(idElement) + "\"><img src=\"images/plus.png\" width=\"14\" height=\"14\" align = \"bottom\" alt=\"Раскрыть список\"</a>";
                }
            }
            else
            {
                return "<img src=\"images/blank.png\" width=\"14\" height=\"14\" align = \"bottom\" alt=\"Элемент\"";
            }
        }
        return "";
    }

    private String addSpaces(int level)
    {
        String spaces = "";
        for (int i = 0; i < level; i++)
        {
            spaces = spaces + "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp";
        }
        return spaces;
    }

    private String getStringCommand(String command)
    {
        if (command.equals("add"))
        {
            return "Добавить";
        }
        else if (command.equals("edit"))
        {
            return "Редактировать";
        }
        else if (command.equals("delete"))
        {
            return "Удалить";
        }
        else
        {
            return "";
        }
    }

    public Vector getDataFromDb()
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/StructureProcessingFromDbEJB");//"laba4-ejb/ru.elleriumsoft.structureForPrint.StructureProcessingFromDbHome");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            Vector<StructureProcessingFromDb> dataFromDb = new Vector<>();
            for (StateOfElements stateOfElements : statesOfElements)
            {
                 dataFromDb.add(structureProcessingFromDbHome.findByPrimaryKey(stateOfElements.getId()));
            }
            return  dataFromDb;//return (Vector) structureProcessingFromDbHome.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Vector getChildForElementFromDb(int id)
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/StructureProcessingFromDbEJB");//"laba4-ejb/ru.elleriumsoft.structureForPrint.StructureProcessingFromDbHome");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            Vector<StructureProcessingFromDb> dataFromDb = new Vector<>();
            return (Vector) structureProcessingFromDbHome.findParentKeys(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public PrintStructureBean()
    {
    }

    public void ejbCreate() throws CreateException
    {
        statesOfElements = new ArrayList<>();
        statesOfElements.add(new StateOfElements(1, StateOfElements.CLOSE));
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
