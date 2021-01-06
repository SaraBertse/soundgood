package se.kth.iv1351.soundgoodjdbc.controller;

import java.util.ArrayList;
import java.util.List;

import se.kth.iv1351.soundgoodjdbc.integration.SoundgoodDAO;
import se.kth.iv1351.soundgoodjdbc.integration.SoundgoodDBException;
import se.kth.iv1351.soundgoodjdbc.model.InstrumentDTO;
import se.kth.iv1351.soundgoodjdbc.model.InstrumentException;

/**
 * This is the application's only controller, all calls to the model pass here.
 * The controller is also responsible for calling the DAO. Typically, the
 * controller first calls the DAO to retrieve data (if needed), then operates on
 * the data, and finally tells the DAO to store the updated data (if any).
 */
public class Controller {

    private final SoundgoodDAO soundgoodDb;

    /**
     * Creates a new instance, and retrieves a connection to the database.
     *
     * @throws SoundgoodDBException If unable to connect to the database.
     */
    public Controller() throws SoundgoodDBException {
        soundgoodDb = new SoundgoodDAO();
    }

    /**
     * Gets all rentable instruments by type.
     *
     * @param type the type, for example piano or guitar
     * @return a list of rentable instruments.
     * @throws InstrumentException
     */
    public List<? extends InstrumentDTO> getRentableInstrumentsByType(String type)
            throws InstrumentException {
        if (type == null) {
            return new ArrayList<>();
        }

        try {
            return soundgoodDb.findRentableInstrumentsByType(type);
        } catch (Exception e) {
            throw new InstrumentException("Could not search for rentable instruments.", e);
        }
    }

    /**
     * Rents an instrument
     *
     * @param personNumber the personnumber of the student in the format
     * yyyymmddxxxx
     * @param instrumentId the id of the instrument to rent
     * @param startDate the start date in the format yyyy-mm-dd
     * @param endDate the end date in the format yyyy-mm-dd
     * @throws SoundgoodDBException if something goes wrong
     */
    public void rentInstrument(String personNumber, int instrumentId,
            String startDate, String endDate) throws SoundgoodDBException {
        soundgoodDb.rentInstrument(personNumber, instrumentId,
                startDate, endDate);
    }

    /**
     * Terminates an instrument rental
     *
     * @param instrumentId the id of the returned instrument
     * @throws SoundgoodDBException if something goes wrong
     */
    public void terminateRental(int instrumentId) throws SoundgoodDBException {
        soundgoodDb.terminateRental(instrumentId);
    }
}
