package ru.elleriumsoft.xml.exchange.exportxml;

import ru.elleriumsoft.xml.exchange.Exchange;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 14.05.2017.
 */
public interface Export extends EJBObject
{
    void exportToXml(int idDept) throws RemoteException;

    void setWithChildrenDept(boolean withChildrenDept) throws RemoteException;
    void setWithEmployees(boolean withEmployees) throws RemoteException;
    void setWithOccupations(boolean withOcc) throws RemoteException;


    Exchange getExchange() throws RemoteException;

}
