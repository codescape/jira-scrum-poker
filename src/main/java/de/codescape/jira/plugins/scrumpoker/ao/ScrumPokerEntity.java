package de.codescape.jira.plugins.scrumpoker.ao;

import net.java.ao.RawEntity;
import net.java.ao.schema.AutoIncrement;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;

/**
 * Base Entity for Active Objects that provides a primary key with a {@link Long} value.
 */
public interface ScrumPokerEntity extends RawEntity<Long> {

    /**
     * The technical unique identifier for a concrete entity extending this interface.
     */
    @AutoIncrement
    @NotNull
    @PrimaryKey("ID")
    Long getID();

}
