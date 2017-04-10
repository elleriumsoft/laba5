package ru.elleriumsoft.structure.structurecommands.commands;

import ru.elleriumsoft.structure.modifications.ActionForStructure;

import java.rmi.RemoteException;

import static ru.elleriumsoft.jdbc.ConnectToDb.PATH_STRUCTURE;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public class EditCommand extends Commands
{
    public EditCommand(ActionForStructure actionForStructure)
    {
        super(actionForStructure);
    }

    @Override
    public String run(Integer element)
    {
        try
        {
            actionForStructure.setIdForAction(element);
            actionForStructure.setAction("edit");
            String nameForEdit = getElement(element).getNameDepartment();
            return "Новое название для " + nameForEdit + ":<br>" +
                    "<form name=\"add\" method=\"get\" action=\"" + PATH_STRUCTURE + "Structure.jsp\">" +
                    "<input type=\"text\" id=\"Editbox1\" style=\"position:absolute;line-\" name=\"newname\" maxlength=\"200\" value=\"" + nameForEdit + "\" >" +
                    "<input type=\"submit\" id=\"Button1\" \" name=\"\" value=\"ОК\" style=\"position:absolute;left:240px;top:83px;width:104px;height:23px;z-index:0;\">" +
                    "<input type=\"submit\" id=\"Button1\" onclick=\"window.location.href='" + PATH_STRUCTURE + "Structure.jsp';return false;\" name=\"\" value=\"Отмена\" style=\"position:absolute;left:350px;top:83px;width:104px;height:23px;z-index:0;\">" +
                    "</form>";
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
