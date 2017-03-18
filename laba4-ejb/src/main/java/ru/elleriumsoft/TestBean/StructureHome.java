package ru.elleriumsoft.TestBean;

import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public interface StructureHome extends javax.ejb.EJBHome
{
    ru.elleriumsoft.TestBean.Structure create() throws RemoteException, javax.ejb.CreateException;
}
