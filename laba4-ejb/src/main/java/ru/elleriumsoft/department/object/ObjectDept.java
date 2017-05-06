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
    int getMaxId() throws RemoteException;
    AllDepartments getAllDept() throws RemoteException;

    void setCommandForModification(String command) throws RemoteException;
    String getCommandForModification() throws RemoteException;
    void setIdForModification(Integer id) throws RemoteException;
    Integer getIdForModification() throws RemoteException;
    void setPositionForModification(Integer positionForModification) throws RemoteException;
    Integer getPositionForModification() throws RemoteException;
}
