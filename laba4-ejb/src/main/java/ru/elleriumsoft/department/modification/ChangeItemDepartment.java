package ru.elleriumsoft.department.modification;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 18.04.2017.
 */
public interface ChangeItemDepartment extends EJBObject
{
    void changeItem(String action, int id,  int id_dept, String name, String date, String id_occ) throws RemoteException;
}
