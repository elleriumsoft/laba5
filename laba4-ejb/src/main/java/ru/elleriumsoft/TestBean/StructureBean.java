package ru.elleriumsoft.TestBean;

/**
 * Created by Dmitriy on 18.03.2017.
 */
public class StructureBean implements javax.ejb.SessionBean
{
    public StructureBean()
    {
    }

    public void ejbCreate() throws javax.ejb.CreateException
    {
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) throws javax.ejb.EJBException
    {
    }

    public void ejbRemove() throws javax.ejb.EJBException
    {
    }

    public void ejbActivate() throws javax.ejb.EJBException
    {
    }

    public void ejbPassivate() throws javax.ejb.EJBException
    {
    }

    public String say(String word)throws javax.ejb.EJBException
    {
        return "<h3>" + word + "</h3>";
    }

}
