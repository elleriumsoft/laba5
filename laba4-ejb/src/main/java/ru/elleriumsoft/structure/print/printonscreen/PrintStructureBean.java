package ru.elleriumsoft.structure.print.printonscreen;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.print.StateOfElements;
import ru.elleriumsoft.structure.print.StructureElement;
import ru.elleriumsoft.structure.print.VariantsOfState;
import ru.elleriumsoft.structure.objectstructure.ObjectOfStructure;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;

import static ru.elleriumsoft.jdbc.ConnectToDb.PATH_STRUCTURE;

/**
 * Created by Dmitriy on 23.03.2017.
 */
public class PrintStructureBean implements SessionBean
{
    private static final Logger logger = Logger.getLogger(PrintStructureBean.class.getName());

    /**
     * Формирование HTML страницы структуры для вывода из уже проинциализированной структуры из БД
     *
     * @param command Добавленная ссылка на команду действия с элементом
     * @return Сформированная HTML страница
     */
    public String printStructure(String command, ObjectOfStructure objectOfStructure)
    {
        StringBuilder pw = new StringBuilder("");
        try
        {
            if (command == null)
            {
                command = "";
            }
            try
            {
                for (int i = 0; i < objectOfStructure.getSizeStructure(); i++)
                {
                    StructureElement el = objectOfStructure.getStructureElement(i);
                    pw.append(addSpaces(el.getLevel()));
                    pw.append(addImageForActionList(el.getId(), objectOfStructure));
                    pw.append("&nbsp<span><a href=\"/PrintElementJsp.jsp?id=" + String.valueOf(el.getId()) + "\">" + el.getNameDepartment() + "</a>&nbsp");//pw.append("&nbsp<span><a href=\"/laba3/Servlets.PrintElement?id=" + String.valueOf(el.getId()) + "\">" + el.getNameDepartment() + "</a>&nbsp");
                    if (!command.equals("") && !(command.equals("delete") && el.getId() == 1))
                    {
                        pw.append("<a href=\"" + PATH_STRUCTURE + "Structure.jsp?command=" + command + "&element=" + el.getId() + "\"style=\"color:#FF0000\">[" + getStringCommand(command) + "]</a>");
                    }
                    pw.append("</span><br><br>");
                }
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        catch (NullPointerException e)
        {
            logger.info(e.getMessage());
        }
        return pw.toString();
    }

    private String addImageForActionList(int idElement, ObjectOfStructure objectOfStructure)
    {
        StateOfElements state = null;
        try
        {
            state = objectOfStructure.getStateOfElement(idElement);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        if (state != null)
        {
            if (state.getState() != VariantsOfState.NO_CHILD)
            {
                if (state.getState() == VariantsOfState.OPEN)
                {
                    return "<a href=\"" + PATH_STRUCTURE + "Structure.jsp?open=" + String.valueOf(idElement) + "\"><img src=\"images/minus.png\" width=\"14\" height=\"14\" align = \"bottom\" alt=\"Свернуть список\"</a>";
                } else
                {
                    return "<a href=\"" + PATH_STRUCTURE + "Structure.jsp?open=" + String.valueOf(idElement) + "\"><img src=\"images/plus.png\" width=\"14\" height=\"14\" align = \"bottom\" alt=\"Раскрыть список\"</a>";
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

    public PrintStructureBean()
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
