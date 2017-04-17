package ru.elleriumsoft.department.object;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 16.04.2017.
 */
public interface ObjectDept extends EJBObject
{
    void readAllEmployeeFromDept(Integer idDepartment) throws RemoteException;
    Integer getSizeObject() throws RemoteException;
    Integer getId(Integer idEmployee) throws RemoteException;
    String getNameEmployee(Integer idEmployee) throws RemoteException;
    String getProfession(Integer idEmployee) throws RemoteException;
    String getEmploymentDate(Integer idEmployee) throws RemoteException;
    String getDateForEdit(Integer idEmployee) throws RemoteException;
    Integer getIdDepartment() throws RemoteException;
    String getNameDepartment() throws RemoteException;
    void setNameDepartment(String nameDepartment) throws RemoteException;
}
