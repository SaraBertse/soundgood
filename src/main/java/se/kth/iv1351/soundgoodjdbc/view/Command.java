package se.kth.iv1351.soundgoodjdbc.view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {
    /**
     * Lists all instruments of the specified type.
     */
    INSTRUMENT,
    /**
     * Rents an instrument.
     */
    RENT,
    /**
     * Terminates the rental of the specified instrument.
     */
    TERMINATE,
    /**
     * Lists all commands.
     */
    HELP,
    /**
     * Leave the chat application.
     */
    QUIT,
    /**
     * None of the valid commands above was specified.
     */
    ILLEGAL_COMMAND,
}
