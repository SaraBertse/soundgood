
package se.kth.iv1351.soundgoodjdbc.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import se.kth.iv1351.soundgoodjdbc.model.Instrument;

/**
 * This data access object (DAO) encapsulates all database calls in the bank
 * application. No code outside this class shall have any knowledge about the
 * database.
 */
public class SoundgoodDAO {
    private static final String HOLDER_TABLE_NAME = "holder";
    private static final String HOLDER_PK_COLUMN_NAME = "holder_id";
    private static final String HOLDER_COLUMN_NAME = "name";
    private static final String ACCT_TABLE_NAME = "account";
    private static final String ACCT_NO_COLUMN_NAME = "account_no";
    private static final String BALANCE_COLUMN_NAME = "balance";
    private static final String HOLDER_FK_COLUMN_NAME = HOLDER_PK_COLUMN_NAME;

    private Connection connection;

    private PreparedStatement listInstrumentsStmt;
    private PreparedStatement findAllRentableInstrumentsByTypeStmt;
    private PreparedStatement findStudentByPersonNumber;
    private PreparedStatement rentInstrumentStmt;
    private PreparedStatement findInstrumentById;
    private PreparedStatement changeInstrumentRentalId;
    private PreparedStatement getLatestIdFromInstrumentRental;
    private PreparedStatement findInstrumentRentalIdfromSchoolsInstrument;
    private PreparedStatement enterTerminatedRental;
    private PreparedStatement changeRentalEndDateStmt;
    private PreparedStatement findNumberOfRentalsStmt;

    /**
     * Constructs a new DAO object connected to the database.
     */
    public SoundgoodDAO() throws SoundgoodDBException {
        try {
            connectToBankDB();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException exception) {
            throw new SoundgoodDBException("Could not connect to datasource.", exception);
        }
    }

        public List<Instrument> findRentableInstrumentsByType(String type) throws SoundgoodDBException {
        String failureMsg = "Could not search for specified instruments.";
        ResultSet result = null;
        List<Instrument> instruments = new ArrayList<>();
        try {
            findAllRentableInstrumentsByTypeStmt.setString(1, type);
            result = findAllRentableInstrumentsByTypeStmt.executeQuery();
            while (result.next()) {
                instruments.add(new Instrument(result.getInt("schools_instrument_id"),
                                               result.getString("brand"),
                                               result.getString("instrument_type"),
                                               result.getInt("rental_fee_per_month")));
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        } finally {
            closeResultSet(failureMsg, result);
        }
        return instruments;
    }
        
    public List<Instrument> listInstruments() throws SoundgoodDBException {
        String failureMsg = "Could not list instruments.";
        List<Instrument> instruments = new ArrayList<>();
        try (ResultSet result = listInstrumentsStmt.executeQuery()) { 
            while (result.next()) {
                instruments.add(new Instrument(result.getInt("schools_instrument_id"),
                                                result.getString("brand"),
                                               result.getString("instrument_type"),
                                               result.getInt("rental_fee_per_month")));
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
        return instruments;
    }
                
                 //Parameters: Personnr, instrument_id, start-date, end-date.
    public void rentInstrument(String personNumber, int instrumentId,
            String startDate, String endDate) throws SoundgoodDBException {
        
 
        String failureMsg = "Could not rent the instrument " + instrumentId; 
        int updatedRows = 0;
        try { 
            findStudentByPersonNumber.setString(1, personNumber);
            ResultSet result = findStudentByPersonNumber.executeQuery();          

            int studentId = 0;
                    if (result.next()) {
                        studentId = result.getInt("student_id");
                }
  
            findNumberOfRentalsStmt.setInt(1, studentId);
            int i = 0;
            result = findNumberOfRentalsStmt.executeQuery();
                while (result.next()) {
                    i++;
                }
     
            if (i >= 2){
                System.out.println("You're already renting two instruments, which is the max limit");
                handleException(failureMsg, null);
            }
            
            String checkedStartDate = startDate.replace("-", "");
            String checkedEndDate = endDate.replace("-", "");
            
            int intStartDate = Integer.parseInt(checkedStartDate);
            int intEndDate = Integer.parseInt(checkedEndDate);
        
            if ((intStartDate + 10001) <= intEndDate){
                System.out.println("You can only rent an instrument for a year");
                handleException(failureMsg, null);
            }
            
            rentInstrumentStmt.setString(1, startDate);
            rentInstrumentStmt.setString(2, endDate); 
            rentInstrumentStmt.setInt(3, studentId);
            
            updatedRows = rentInstrumentStmt.executeUpdate();
            if (updatedRows != 1) {
                handleException(failureMsg, null);
            }
  
            findInstrumentById.setInt(1, instrumentId);

            int latestRentalId = 0;
            result = getLatestIdFromInstrumentRental.executeQuery();
                                if (result.next()) {
                                latestRentalId = result.getInt("max");
                }
            changeInstrumentRentalId.setInt(1, (latestRentalId)); 
            changeInstrumentRentalId.setInt(2, instrumentId);
            
            updatedRows = changeInstrumentRentalId.executeUpdate(); 
            if (updatedRows != 1) {
                handleException(failureMsg, null);
            }  
            
            connection.commit();
            System.out.println("Instrument rented successfully");
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
    }
    
        public void terminateRental(int instrumentId) throws SoundgoodDBException {
            
            String failureMsg = "Could not terminate the rental for instrument " + instrumentId; 
            
            int updatedRows = 0;
        try{
            findInstrumentRentalIdfromSchoolsInstrument.setInt(1, instrumentId); 
            int rentalId = 0;
     
            ResultSet result = findInstrumentRentalIdfromSchoolsInstrument.executeQuery();
            
                        if (result.next()) {
                        rentalId = result.getInt("instrument_rental_id");
                }
                        
            enterTerminatedRental.setInt(1, rentalId);
            enterTerminatedRental.setInt(2, instrumentId);
            updatedRows = enterTerminatedRental.executeUpdate();
            if (updatedRows != 1) {
                handleException(failureMsg, null);
            }
            
            changeInstrumentRentalId.setString(1, null); 
            changeInstrumentRentalId.setInt(2, instrumentId);
            
            updatedRows = changeInstrumentRentalId.executeUpdate();
            if (updatedRows != 1) {
                handleException(failureMsg, null);
            }

            changeRentalEndDateStmt.setString(1, LocalDate.now().toString());
            changeRentalEndDateStmt.setInt(2, rentalId);
            
            updatedRows = changeRentalEndDateStmt.executeUpdate();
            if (updatedRows != 1) {
                handleException(failureMsg, null);
            }

           connection.commit();
           System.out.println("The rental has been terminated.");
          } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
            
        }

    private void connectToBankDB() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/throwaway_sg1",
                                                 "postgres", "postgres");
        // connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankdb",
        //                                          "root", "javajava");
        connection.setAutoCommit(false);
    }

    private void prepareStatements() throws SQLException {
        
        listInstrumentsStmt = connection.prepareStatement("SELECT brand"
            + ", instrument_type, rental_fee_per_month FROM schools_instrument");
        
        findAllRentableInstrumentsByTypeStmt = connection.prepareStatement("SELECT schools_instrument_id, brand"
                + ", instrument_type, rental_fee_per_month FROM schools_instrument "
                + "WHERE instrument_type = ? AND instrument_rental_id IS NULL");
        
        findInstrumentById = connection.prepareStatement("SELECT schools_instrument_id"
                + " FROM schools_instrument WHERE schools_instrument_id = ?");
        
        findStudentByPersonNumber = connection.prepareStatement("SELECT * FROM student"
                + " INNER JOIN person ON student.person_id = person.person_id "
                + "WHERE person.person_number = ?");
       
        rentInstrumentStmt = connection.prepareStatement("INSERT INTO instrument_rental"
            + " (rental_start_date, rental_end_date, student_id)" 
            + " VALUES (?::date, ?::date, ?)");
       
        changeInstrumentRentalId = connection.prepareStatement("UPDATE schools_instrument"
            + " SET instrument_rental_id = ?::int WHERE schools_instrument_id = ?"); 
        
        getLatestIdFromInstrumentRental = connection.prepareStatement("SELECT MAX(instrument_rental_id)"
            + " FROM instrument_rental");
        
        findInstrumentRentalIdfromSchoolsInstrument = connection.prepareStatement("SELECT"
        + " instrument_rental_id FROM schools_instrument WHERE schools_instrument_id = ?");
        
        enterTerminatedRental = connection.prepareStatement("INSERT INTO terminated_rental"
            + " (instrument_rental_id, schools_instrument_id) VALUES (?, ?)");
        
        changeRentalEndDateStmt = connection.prepareStatement("UPDATE instrument_rental"
            + " SET rental_end_date = ?::date WHERE instrument_rental_id = ?");
        
        findNumberOfRentalsStmt = connection.prepareStatement("SELECT"
                + " schools_instrument.instrument_rental_id, COUNT(*)"
                + " FROM schools_instrument INNER JOIN instrument_rental"
                + " ON schools_instrument.instrument_rental_id = instrument_rental.instrument_rental_id"
                + " WHERE student_id = ? GROUP BY schools_instrument.instrument_rental_id");
    }
    
    private void handleException(String failureMsg, Exception cause) throws SoundgoodDBException {
        String completeFailureMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            completeFailureMsg = completeFailureMsg + 
            ". Also failed to rollback transaction because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new SoundgoodDBException(failureMsg, cause);
        } else {
            throw new SoundgoodDBException(failureMsg);
        }
    }

    private void closeResultSet(String failureMsg, ResultSet result) throws SoundgoodDBException {
        try {
            result.close();
        } catch (Exception e) {
            throw new SoundgoodDBException(failureMsg + " Could not close result set.", e);
        }
    }
}
