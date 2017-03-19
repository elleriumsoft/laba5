package ru.elleriumsoft.printstructure;

import ru.elleriumsoft.structure.StructureProcessingFromDb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Dmitriy on 19.03.2017.
 */
public class PrintStructureForJsp
{
    private ArrayList<StructureElement> structureFromDb;
    private ArrayList<StructureElement> structure;
     /**
     * Инициализация структуры из БД в виде дерева
     *
     * @throws SQLException
     */
    public void initStructureFromDb(Vector<StructureProcessingFromDb> structureFromBean) throws RemoteException
    {
        structureFromDb = new ArrayList<>();
        for (StructureProcessingFromDb element : structureFromBean)
        {
            structureFromDb.add(newElement(element.getId(), element.getNameDepartment(), element.getParent_id()));
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
    public String printStructure(Vector<StructureProcessingFromDb> structureFromBd, String command) throws RemoteException
    {
        initStructureFromDb(structureFromBd);

        if (command == null){command = "";}
        StringBuilder pw = new StringBuilder("");
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
}
