package ru.elleriumsoft.structure.structurecommands.commands;

import ru.elleriumsoft.structure.modifications.ActionForStructure;

import java.rmi.RemoteException;

import static ru.elleriumsoft.jdbc.ConnectToDb.PATH_STRUCTURE;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public class DeleteCommand extends Commands
{
    public DeleteCommand(ActionForStructure actionForStructure)
    {
        super(actionForStructure);
    }

    @Override
    public String run(Integer element)
    {
        try
        {
            actionForStructure.setIdForAction(element);
            actionForStructure.setAction("delete");
            String nameForAdd = getElement(element).getNameDepartment();
            return  "Вы уверены, что хотите удалить " + nameForAdd + "?<br><br><br>" +
                    "    <input type= \"submit\" id= \"Button1 \" onclick= \"window.location.href='" + PATH_STRUCTURE + "Structure.jsp?newname=delete';return false; \" name= \" \" value= \"ДА\" style= \"position:absolute;left:9px;top:100px;width:104px;height:25px;color:#FF0000; \">\n" +
                    "    <input type= \"submit\" id= \"Button1 \" onclick= \"window.location.href='" + PATH_STRUCTURE + "Structure.jsp';return false; \" name= \" \" value= \"НЕТ\" style= \"position:absolute;left:109px;top:100px;width:104px;height:25px;color:#FF0000; \">\n";
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return "";
        //return answer + "<br>" + "    <input type=\"submit\" id=\"Button1\" onclick=\"window.location.href='" + PATH_STRUCTURE + "Structure.jsp';return false;\" name=\"\" value=\"ОК\" style=\"position:absolute;left:310px;top:18px;width:184px;height:25px;\">" ;
    }

}
