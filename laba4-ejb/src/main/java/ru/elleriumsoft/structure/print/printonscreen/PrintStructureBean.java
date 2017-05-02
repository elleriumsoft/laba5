package ru.elleriumsoft.structure.print.printonscreen;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.objectstructure.ObjectOfStructure;
import ru.elleriumsoft.structure.StateOfElements;
import ru.elleriumsoft.structure.StructureElement;
import ru.elleriumsoft.structure.VariantsOfState;

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
     * @return Сформированная HTML страница
     */
    public String printStructure(ObjectOfStructure objectOfStructure)
    {
        StringBuilder pw = new StringBuilder("");
        try
        {
            try
            {
                for (int i = 0; i < objectOfStructure.getSizeStructure(); i++)
                {
                    StructureElement el = objectOfStructure.getStructureElement(i);
                    pw.append(addSpaces(el.getLevel()));
                    pw.append(addImageForActionList(el.getId(), objectOfStructure));
                    pw.append("&nbsp<span><a href=\"" + "/app/department/" + "Department.jsp?id=" + String.valueOf(el.getId()) + "&name=" + el.getNameDepartment() + "\">" + el.getNameDepartment() + "</a>&nbsp");
                    {
                        pw.append(addImagesForCommand(el.getId()));
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

    private String addImagesForCommand(int id)
    {
        String htmlImages = "&nbsp&nbsp";
        htmlImages = htmlImages + "<a href=\"Structure.jsp?command=add&element=" + id + "\"><img src=\"images/create.png\" width=\"17\" height=\"17\" align = \"center\" alt=\"Добавить элемент\"></a>" + "&nbsp";
        htmlImages = htmlImages + "<a href=\"Structure.jsp?command=edit&element=" + id + "\"><img src=\"images/edit.png\" width=\"17\" height=\"17\" align = \"center\" alt=\"Редактировать элемент\"></a>" + "&nbsp";
        if (id != 1)
        {
            htmlImages = htmlImages + "<a href=\"Structure.jsp?command=delete&element=" + id + "\"><img src=\"images/delete.png\" width=\"17\" height=\"17\" align = \"center\" alt=\"Удалить элемент\"></a>";
        }
        return htmlImages;
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
