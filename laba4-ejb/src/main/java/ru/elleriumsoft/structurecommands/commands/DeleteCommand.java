package ru.elleriumsoft.structurecommands.commands;

import ru.elleriumsoft.actionforstucture.ActionForStructure;
import ru.elleriumsoft.printstructure.StructureElement;
import ru.elleriumsoft.structure.StructureProcessingFromDb;
import ru.elleriumsoft.structure.StructureProcessingFromDbHome;

import javax.ejb.RemoveException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public class DeleteCommand extends Commands
{
    private String answer;
    ArrayList<StructureElement> structureFromDb;

    public DeleteCommand(ActionForStructure actionForStructure)
    {
        super(actionForStructure);
    }

    @Override
    public String run(Integer element)
    {
        answer = "";
        structureFromDb = new ArrayList<>();
        for (StructureProcessingFromDb structureElement : getAllElements())
        {
            try
            {
                structureFromDb.add(new StructureElement(structureElement.getId(), structureElement.getNameDepartment(), structureElement.getParent_id()));
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        deleteElement(element);

        return answer + "<br>" + "    <input type=\"submit\" id=\"Button1\" onclick=\"window.location.href='/Structure.jsp';return false;\" name=\"\" value=\"ОК\" style=\"position:absolute;left:310px;top:18px;width:184px;height:25px;\">" ;
    }

    private void deleteElement(Integer id)
    {
        try
        {
            StructureProcessingFromDb structureProcessingFromDb = getElement(id);
            answer = answer + "Элемент " + structureProcessingFromDb.getNameDepartment() + " удален из структуры!" + "<br>";
            structureProcessingFromDb.remove();
        } catch (RemoteException e)
        {
            e.printStackTrace();
            answer = answer + "Не удалось удалить элемент!" + "<br>";
        } catch (RemoveException e)
        {
            e.printStackTrace();
        }
        for (StructureElement structureElement : structureFromDb)
        {
            if (structureElement.getParent_id() == id)
            {
                deleteElement(structureElement.getId());
            }
        }
    }

    Vector<StructureProcessingFromDb> getAllElements()
    {
        Vector<StructureProcessingFromDb> vector = new Vector<>();
        StructureProcessingFromDb structureProcessingFromDb = null;
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/StructureProcessingFromDbEJB");
            StructureProcessingFromDbHome structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            vector = (Vector) structureProcessingFromDbHome.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return vector;
    }
}
