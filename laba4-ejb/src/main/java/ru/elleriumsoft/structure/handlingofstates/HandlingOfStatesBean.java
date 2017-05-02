package ru.elleriumsoft.structure.handlingofstates;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.StateOfElements;
import ru.elleriumsoft.structure.StructureElement;
import ru.elleriumsoft.structure.VariantsOfState;
import ru.elleriumsoft.structure.objectstructure.ObjectOfStructure;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDb;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDbHome;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.util.Vector;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 02.04.2017.
 */
public class HandlingOfStatesBean implements SessionBean
{
    private static final Logger logger = Logger.getLogger(HandlingOfStatesBean.class.getName());

    private ObjectOfStructure objectOfStructure;

    public boolean checkNeedChangeState(String id, ObjectOfStructure objectOfStructure)
    {
        this.objectOfStructure = objectOfStructure;

        if (id == null) { return false;}
        int idElement = Integer.valueOf(id);
        StateOfElements state = null;
        try
        {
            state = objectOfStructure.getStateOfElement(idElement);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        if (state.getState() == VariantsOfState.NO_CHILD) { return false; }
        if (state.getState() == VariantsOfState.CLOSE)
        {
            try
            {
                openList(idElement);
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
            return true;
        }
        if (state.getState() == VariantsOfState.OPEN)
        {
            try
            {
                closeList(idElement, idElement);
                objectOfStructure.removeDeleted();
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    private void closeList(int idElement, int firstElement) throws RemoteException
    {
        for (int i = 0; i < objectOfStructure.getSizeStructure(); i++)
        {
            StructureElement element = objectOfStructure.getStructureElement(i);
            if (element.getParent_id() == idElement)
            {
                closeList(element.getId(), firstElement);
            }
        }

        if (idElement != firstElement)
        {
            objectOfStructure.setStateOfElement(idElement, VariantsOfState.DELETED);//objectOfStructure.removeStateElement(idElement);
        }
        else
        {
            objectOfStructure.setStateOfElement(idElement, VariantsOfState.CLOSE);
        }
    }

    private void openList(int idElement) throws RemoteException
    {
        Vector<StructureProcessingFromDb> children = getChildForElementFromDb(idElement);
        if (children != null && children.size() > 0)
        {
            logger.info("size=" + children.size());
            for (StructureProcessingFromDb structureProcessingFromDb : children)
            {

                objectOfStructure.addStateElement(structureProcessingFromDb.getId(), VariantsOfState.CLOSE);

            }
            objectOfStructure.setStateOfElement(idElement, VariantsOfState.OPEN);
        } else
        {
            logger.info("not found");
            objectOfStructure.setStateOfElement(idElement, VariantsOfState.NO_CHILD);
        }
    }

    private Vector getChildForElementFromDb(int id)
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");//"laba4-ejb/ru.elleriumsoft.structureForPrint.StructureProcessingFromDbHome");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            return (Vector) structureProcessingFromDbHome.findParentKeys(id);
        } catch (Exception e) {

            logger.info(e.getStackTrace());
        }
        return null;
    }

    public HandlingOfStatesBean()
    {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
    {
    }

    public void ejbCreate() throws CreateException
    {
    }

    public void ejbRemove() throws EJBException
    {
    }

    public void ejbActivate() throws EJBException
    {
    }

    public void ejbPassivate() throws EJBException
    {
    }
}
