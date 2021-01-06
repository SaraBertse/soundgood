
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
        
        
                public List<? extends InstrumentDTO> getAllInstruments() throws InstrumentException {
        try {
            return soundgoodDb.listInstruments();
        } catch (Exception e) {
            throw new InstrumentException("Unable to list instruments.", e);
        }
    }
            
                //Parameters: Personnr, instrument_id, start-date, end-date.
            public void rentInstrument(String personNumber, int instrumentId,
                    String startDate, String endDate) throws SoundgoodDBException {
                    soundgoodDb.rentInstrument(personNumber, instrumentId,
                    startDate, endDate);
            }
            
            
            public void terminateRental(int instrumentId) throws SoundgoodDBException{
                soundgoodDb.terminateRental(instrumentId);
            }
}
 
