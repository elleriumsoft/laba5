package ru.elleriumsoft.department.entity;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 12.04.2017.
 */
public interface EntityDept extends EJBObject
{
    Integer getId() throws RemoteException;
    String getNameEmployee() throws RemoteException;
    String getEmploymentDate() throws RemoteException;
    String getNameProfession() throws RemoteException;
    Integer getIdProfession() throws RemoteException;
    void setIdDepartment(Integer idDepartment) throws RemoteException;
    void setNameEmployee(String nameEmployee) throws RemoteException;
    void setIdProfession(Integer idProfession) throws RemoteException;
    void setEmploymentDate(String employmentDate) throws RemoteException;
    void setNeedUpdate() throws RemoteException;
}
