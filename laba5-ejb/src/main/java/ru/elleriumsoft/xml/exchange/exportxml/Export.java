package ru.elleriumsoft.xml.exchange.exportxml;

import ru.elleriumsoft.xml.exchange.Exchange;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 14.05.2017.
 */
public interface Export extends EJBObject
{
    /**
     * Создает из данных отдела в базе данных объект Exchange, готовый для выгрузки в xml через JAXB
     * @param idDept - номер отдела в БД
     */
    Exchange createExchangeForExportToXml(int idDept) throws RemoteException;

    /**
     * Устанавливает параметр выгрузки вложенных отделов
     */
    void setWithChildrenDept(boolean withChildrenDept) throws RemoteException;

    /**
     * Устанавливает параметр выгрузки сотрудников отделов
     */
    void setWithEmployees(boolean withEmployees) throws RemoteException;

    /**
     * Устанавливает параметр выгрузки профессий
     */
    void setWithOccupations(boolean withOcc) throws RemoteException;
}
