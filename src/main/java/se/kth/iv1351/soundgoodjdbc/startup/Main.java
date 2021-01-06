package se.kth.iv1351.soundgoodjdbc.startup;


import se.kth.iv1351.soundgoodjdbc.controller.Controller;
import se.kth.iv1351.soundgoodjdbc.integration.SoundgoodDBException;
import se.kth.iv1351.soundgoodjdbc.view.BlockingInterpreter;

/**
 * Starts the bank client.
 */
public class Main {
    /**
     * @param args There are no command line arguments.
     */
    public static void main(String[] args) {
        try {
        new BlockingInterpreter(new Controller()).handleCmds();
        } catch(SoundgoodDBException bdbe) {
            System.out.println("Could not connect to Soundgood db.");
            bdbe.printStackTrace();
        }
    }
}
