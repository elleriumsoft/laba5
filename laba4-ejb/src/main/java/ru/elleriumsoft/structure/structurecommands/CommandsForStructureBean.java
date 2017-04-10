package ru.elleriumsoft.structure.structurecommands;

import ru.elleriumsoft.structure.modifications.ActionForStructure;
import ru.elleriumsoft.structure.structurecommands.commands.AddCommand;
import ru.elleriumsoft.structure.structurecommands.commands.Commands;
import ru.elleriumsoft.structure.structurecommands.commands.DeleteCommand;
import ru.elleriumsoft.structure.structurecommands.commands.EditCommand;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.util.HashMap;
import java.util.Map;

import static ru.elleriumsoft.jdbc.ConnectToDb.PATH_STRUCTURE;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public class CommandsForStructureBean implements SessionBean
{
    private final static String COMMAND_ADD = "add";
    private final static String COMMAND_EDIT = "edit";
    private final static String COMMAND_DELETE = "delete";

    public String build(final ActionForStructure actionForStructure, String command, final String element)
    {
        if (element == null || command == null)
        {
            return "    <input type= \"submit\" id= \"Button1 \" onclick= \"window.location.href='" + PATH_STRUCTURE + "Structure.jsp?printcommand=add';return false; \" name= \" \" value= \"Добавить \" style= \"position:absolute;left:9px;top:51px;width:104px;height:25px;color:#FF0000; \">\n" +
                   "    <input type= \"submit\" id= \"Button2 \" onclick= \"window.location.href='" + PATH_STRUCTURE + "Structure.jsp?printcommand=edit';return false; \" name= \" \" value= \"Редактировать \" style= \"position:absolute;left:121px;top:51px;width:104px;height:25px;color:#FF0000; \">\n" +
                   "    <input type= \"submit\" id= \"Button3 \" onclick= \"window.location.href='" + PATH_STRUCTURE + "Structure.jsp?printcommand=delete';return false; \" name= \" \" value= \"Удалить \" style= \"position:absolute;left:235px;top:51px;width:104px;height:25px;color:#FF0000; \">" +
                   "    <input type=\"submit\" id=\"Button1\" onclick=\"window.location.href='" + PATH_STRUCTURE + "index.jsp';return false;\" name=\"\" value=\"Вернуться в меню\" style=\"position:absolute;left:310px;top:18px;width:184px;height:25px;\">" ;
        }

        Map<String, Commands> commands = new HashMap<String, Commands>()
        {
            {
                put(COMMAND_ADD, new AddCommand(actionForStructure));
                put(COMMAND_EDIT, new EditCommand(actionForStructure));
                put(COMMAND_DELETE, new DeleteCommand(actionForStructure));
            }
        };

        if (commands.containsKey(command))
        {
            return commands.get(command).run(Integer.valueOf(element));
        }
        return "";
    }

    public CommandsForStructureBean()
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
