package ru.elleriumsoft.printstructure.handlingofstates;

import ru.elleriumsoft.printstructure.objectstructure.ObjectOfStructure;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 02.04.2017.
 */
public interface HandlingOfStates extends EJBObject
{
    boolean checkNeedChangeState(String id, ObjectOfStructure objectOfStructure) throws RemoteException;
}
