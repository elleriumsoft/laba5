package ru.elleriumsoft.TestBean;

import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public interface Structure extends javax.ejb.EJBObject
{
    String say(String word) throws RemoteException;
}
