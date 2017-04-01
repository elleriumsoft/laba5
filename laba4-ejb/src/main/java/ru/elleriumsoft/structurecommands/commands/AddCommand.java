package ru.elleriumsoft.structurecommands.commands;

import ru.elleriumsoft.actionforstucture.ActionForStructure;

import java.rmi.RemoteException;

import static ru.elleriumsoft.jdbc.ConnectToDb.PATH_APP;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public class AddCommand extends Commands
{
    public AddCommand(ActionForStructure actionForStructure)
    {
        super(actionForStructure);
    }

    @Override
    public String run(Integer element)
    {
        try
        {
            actionForStructure.setIdForAction(element);
            actionForStructure.setAction("add");
            String nameForAdd = getElement(element).getNameDepartment();
            return "Добавьте элемент в " + nameForAdd + ":<br>" +
                    "<form name=\"add\" method=\"get\" action=\"" + PATH_APP + "Structure.jsp\">" +
                    "<input type=\"text\" id=\"Editbox1\" style=\"position:absolute;line-\" name=\"newname\" value=\"\"  maxlength=\"200\">" +
                    "<input type=\"submit\" id=\"Button1\" \" name=\"\" value=\"ОК\" style=\"position:absolute;left:240px;top:83px;width:104px;height:23px;z-index:0;\">" +
                    "<input type=\"submit\" id=\"Button1\" onclick=\"window.location.href='" + PATH_APP + "Structure.jsp';return false;\" name=\"\" value=\"Отмена\" style=\"position:absolute;left:350px;top:83px;width:104px;height:23px;z-index:0;\">" +
                    "</form>";
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
