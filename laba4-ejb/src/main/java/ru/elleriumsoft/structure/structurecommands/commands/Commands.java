package ru.elleriumsoft.structure.structurecommands.commands;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.modifications.ActionForStructure;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDb;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDbHome;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 26.03.2017.
 */
public abstract class Commands
{
    ActionForStructure actionForStructure;

    private static final Logger logger = Logger.getLogger(Commands.class.getName());

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
            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
            structureProcessingFromDb = structureProcessingFromDbHome.findByPrimaryKey(id);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return structureProcessingFromDb;
    }
}
