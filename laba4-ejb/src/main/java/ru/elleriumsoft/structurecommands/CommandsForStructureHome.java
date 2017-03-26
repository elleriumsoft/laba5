package ru.elleriumsoft.structurecommands;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public interface CommandsForStructureHome extends EJBHome
{
    ru.elleriumsoft.structurecommands.CommandsForStructure create() throws RemoteException, CreateException;
}
