package ru.elleriumsoft.structure;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.modifications.ActionForStructure;

import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 23.04.2017.
 */
public class CreateButtons
{
    private static final Logger logger = Logger.getLogger(CreateButtons.class.getName());

    public String selectButtons(ActionForStructure actionForStructure, String command, String element)
    {
        if (command == null || element == null)
        {
            return "";//"'#finder'";
        }

        logger.info("command=" + command);
        try
        {
            actionForStructure.setIdForAction(Integer.valueOf(element));
            actionForStructure.setAction(command);
            return "'#" + command + "action'";
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
