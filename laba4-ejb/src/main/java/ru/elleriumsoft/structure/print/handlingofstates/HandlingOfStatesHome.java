package ru.elleriumsoft.structure.print.handlingofstates;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 02.04.2017.
 */
public interface HandlingOfStatesHome extends EJBHome
{
    ru.elleriumsoft.structure.print.handlingofstates.HandlingOfStates create() throws RemoteException, CreateException;
}
