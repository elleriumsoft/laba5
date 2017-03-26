package ru.elleriumsoft.structurecommands.commands;

import ru.elleriumsoft.actionforstucture.ActionForStructure;
import ru.elleriumsoft.structure.StructureProcessingFromDb;
import ru.elleriumsoft.structure.StructureProcessingFromDbHome;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public abstract class Commands
{
    ActionForStructure actionForStructure;

    public Commands(ActionForStructure actionForStructure)
    {
        this.actionForStructure = actionForStructure;
    }

    abstract public String run(Integer element);

    StructureProcessingFromDb getElement(Integer id)
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        StructureProcessingFromDb structureProcessingFromDb = null;
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/StructureProcessingFromDbEJB");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            structureProcessingFromDb = structureProcessingFromDbHome.findByPrimaryKey(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return structureProcessingFromDb;
    }
}
