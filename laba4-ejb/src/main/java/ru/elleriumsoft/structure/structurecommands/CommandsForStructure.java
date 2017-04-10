package ru.elleriumsoft.structure.structurecommands;

import ru.elleriumsoft.structure.modifications.ActionForStructure;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public interface CommandsForStructure extends EJBObject
{
    String build(ActionForStructure actionForStructure, final String element, String command) throws RemoteException;
}
