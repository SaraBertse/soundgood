
package se.kth.iv1351.soundgoodjdbc.model;

public class Instrument implements InstrumentDTO {
    private int instrumentId;
    private String brand;
    private int rentalId;
    private String type;
    private int rentalFee;
    
    /**
     * Constructor for instantiating a new instrument.
     * 
     * @param instrumentId
     * @param brand
     * @param rentalId
     * @param type
     * @param rentalFee 
     */
    public Instrument(int instrumentId, String brand, int rentalId, String type, int rentalFee){
        this.instrumentId = instrumentId;
        this.brand = brand;
        this.rentalId = rentalId;
        this.type = type;
        this.rentalFee = rentalFee;
    }
        public Instrument(String brand, String type){
        this.instrumentId = 0;
        this.brand = brand;
        this.rentalId = 0;
        this.type = type;
        this.rentalFee = 0;
    }
        
        public Instrument(String brand, String type, int rentalFee){
        this.instrumentId = 0;
        this.brand = brand;
        this.rentalId = 0;
        this.type = type;
        this.rentalFee = rentalFee;
    }
        
        public Instrument(int instrumentId, String brand, String type, int rentalFee){
        this.instrumentId = instrumentId;
        this.brand = brand;
        this.rentalId = 0;
        this.type = type;
        this.rentalFee = rentalFee;
    }
        
    public String getBrand(){
        return brand;
    }

    public String getType(){
        return type;
    }
    
    public int getRentalFee(){
        return rentalFee;
    }
    
    public int getInstrumentId(){
        return instrumentId;
    }
}
    
 