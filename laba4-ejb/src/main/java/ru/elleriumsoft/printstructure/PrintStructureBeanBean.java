package ru.elleriumsoft.printstructure;

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
public class PrintStructureBeanBean implements SessionBean
{
    private ArrayList<StructureElement> structureFromDb;
    private ArrayList<StructureElement> structure;

    //private StructureProcessingFromDbHome structureProcessingFromDbHome = null;

    public void initStructureFromDb()
    {
        Vector<StructureProcessingFromDb> structureFromBean = getDataFromDb();
        if (structureFromBean == null)
        {
            throw new NullPointerException("Нету инфы из базы");
        }
        structureFromDb = new ArrayList<>();
        for (StructureProcessingFromDb element : structureFromBean)
        {
            try
            {
                structureFromDb.add(newElement(element.getId(), element.getNameDepartment(), element.getParent_id()));
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        structure = new ArrayList<>();
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
                StructureElement elementForAdd = newElementInt(el.getId(), el.getNameDepartment(), el.getParent_id());
                elementForAdd.setLevel(level);
                structure.add(elementForAdd);
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
            for (StructureElement el : structure)
            {
                //if (isElementOpen(el.getParent_id()) || el.getLevel() == 0)
                {
                    pw.append(addSpaces(el.getLevel()));
                    //    pw.append(addImageForActionList(isElementOpen(el.getId()), el.getId()));
                    pw.append("&nbsp<span><a href=\"/laba3/PrintElementJsp.jsp?id=" + String.valueOf(el.getId()) + "\">" + el.getNameDepartment() + "</a>&nbsp");//pw.append("&nbsp<span><a href=\"/laba3/Servlets.PrintElement?id=" + String.valueOf(el.getId()) + "\">" + el.getNameDepartment() + "</a>&nbsp");
                    if (!command.equals("") && !(command.equals("delete") && el.getId() == 1))
                    {
                        pw.append("<a href=\"/laba3/PrintStructure.jsp?command=" + command + "&element=" + el.getId() + "\"style=\"color:#FF0000\">[" + getStringCommand(command) + "]</a>");
                    }
                    pw.append("</span><br><br>");
                }
            }
        }
        catch (NullPointerException e)
        {
            System.out.println(e.getMessage());
        }
        return pw.toString();
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

    private StructureElement newElement(String id, String name, String parent_id)
    {
        StructureElement element = new StructureElement();
        element.setId(Integer.valueOf(id));
        element.setNameDepartment(name);
        element.setParent_id(Integer.valueOf(parent_id));
        return element;
    }

    private StructureElement newElementInt(int id, String name, int parent_id)
    {
        StructureElement element = new StructureElement();
        element.setId(Integer.valueOf(id));
        element.setNameDepartment(name);
        element.setParent_id(Integer.valueOf(parent_id));
        return element;
    }

    public Vector getDataFromDb()
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/StructureProcessingFromDbEJB");//"laba4-ejb/ru.elleriumsoft.structure.StructureProcessingFromDbHome");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            return (Vector) structureProcessingFromDbHome.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public PrintStructureBeanBean()
    {
    }

    public void ejbCreate() throws CreateException
    {
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
