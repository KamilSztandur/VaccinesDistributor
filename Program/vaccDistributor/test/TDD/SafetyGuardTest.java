package vaccDistributor.test.TDD;

import vaccDistributor.src.SafetyGuard;
import vaccDistributor.src.units.*;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.HashMap;


public class SafetyGuardTest {
    private HashMap<Integer, Pharmacy> pharmacies;
    private HashMap<Integer, Supplier> suppliers;
    private ArrayList<Connection> connections;
    private ArrayList<Transaction> transactions;

    @Before 
    public void setUp() {
        this.pharmacies = new HashMap<>();
        this.suppliers = new HashMap<>();
        this.connections = new ArrayList<>();
        this.transactions = new ArrayList<>();

        this.pharmacies.put(0, new Pharmacy(0, "Fapteka Papteka", 750));
        this.suppliers.put(0, new Supplier(0, "Jerzy Zięba Basement Lab", 1000));
        this.connections.add(new Connection(0, 0, 800, 70.5));
        this.transactions.add(new Transaction("Apteka Papteka", "Producent JakisTam", 600, 70.5));
    }

    @After
    public void tearDown() {
        this.pharmacies = null;
        this.suppliers = null;
        this.connections = null;
        this.transactions = null;
    }

    @Test
    public void shouldDiagnoseDuplicatedPharmacies() {
        assertTrue( 
            SafetyGuard.isThisPharmacyDuplicated(new Pharmacy(0, "Fapteka Papteka", 750), this.pharmacies)
        );

        assertTrue( 
            SafetyGuard.isThisPharmacyDuplicated(new Pharmacy(1, "Fapteka Papteka", 750), this.pharmacies)
        );

        assertTrue( 
            SafetyGuard.isThisPharmacyDuplicated(new Pharmacy(0, "Papteka", 750), this.pharmacies)
        );
    }

    @Test
    public void shouldDiagnoseDuplicatedSuppliers() {
        assertTrue( 
            SafetyGuard.isThisSupplierDuplicated(new Supplier(0, "Jerzy Zięba Basement Lab", 1000), this.suppliers)
        );

        assertTrue( 
            SafetyGuard.isThisSupplierDuplicated(new Supplier(1, "Jerzy Zięba Basement Lab", 1550), this.suppliers)
        );

        assertTrue( 
            SafetyGuard.isThisSupplierDuplicated(new Supplier(0, "Politechnika Warszawska", 100), this.suppliers)
        );
    }

    @Test
    public void shouldDiagnoseDuplicatedConnections() {
        assertTrue( 
            SafetyGuard.isThisConnectionDuplicated(new Connection(0, 0, 600, 70.5), this.connections)
        );

        assertFalse( 
            SafetyGuard.isThisConnectionDuplicated(new Connection(0, 1, 600, 70.5), this.connections)
        );

        assertFalse( 
            SafetyGuard.isThisConnectionDuplicated(new Connection(1, 0, 600, 70.5), this.connections)
        );
    }

    @Test
    public void shouldDiagnoseDuplicatedTransactions() {
        assertTrue( 
            SafetyGuard.isThisTransactionDuplicated(new Transaction("Apteka Papteka", "Producent JakisTam", 600, 70.5), this.transactions)
        );

        assertFalse( 
            SafetyGuard.isThisTransactionDuplicated(new Transaction("Apteka Papteka2", "Producent JakisTam", 600, 70.5), this.transactions)
        );

        assertFalse( 
            SafetyGuard.isThisTransactionDuplicated(new Transaction("Apteka Papteka", "Producent JakisTam2", 600, 70.5), this.transactions)
        );

        assertFalse( 
            SafetyGuard.isThisTransactionDuplicated(new Transaction("Apteka Papteka2", "Producent JakisTam2", 600, 70.5), this.transactions)
        );
    }

    @Test
    public void shouldDetectNonexistentDestinationsInConnection() {
        var connectionWithFalsePharmacy = new Connection(0, 123, 600, 70.5);
        var connectionWithFalseSupplier = new Connection(123, 0, 600, 70.5);

        assertFalse(
            SafetyGuard.isThisConnectionPharmacyRegistered(connectionWithFalsePharmacy, this.pharmacies)
        );

        assertFalse(
            SafetyGuard.isThisConnectionSupplierRegistered(connectionWithFalseSupplier, this.suppliers)
        );
    }

    @Test
    public void shouldThrowExceptionsWhenReadingInvalidSuppliersDataLines() {
        int dummyLineNumber = 0;
        String[] correctDataLine = {"0", "BioTech 2.0", "900"};
        String[] incorrectDataLine_tooFewArguments = {"0", "BioTech 2.0"};
        String[] incorrectDataLine_tooManyArguments = {"0", "BioTech 2.0", "900", "additional argument"};
        String[] incorrectDataLine_negativeNumericArguments = {"0", "BioTech 2.0", "-900"};
        String[] incorrectDataLine_nonNumericArguments = {"0", "BioTech 2.0", "dziewięć"};

        /* Should not throw any exception */
        SafetyGuard.checkIfSupplierDataLineCorrect(correctDataLine, dummyLineNumber);

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfSupplierDataLineCorrect(incorrectDataLine_tooFewArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfSupplierDataLineCorrect(incorrectDataLine_tooManyArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfSupplierDataLineCorrect(incorrectDataLine_negativeNumericArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfSupplierDataLineCorrect(incorrectDataLine_nonNumericArguments, dummyLineNumber)
        );
    }

    @Test
    public void shouldThrowExceptionsWhenReadingInvalidPharmaciesDataLines() {
        int dummyLineNumber = 0;
        String[] correctDataLine = {"0", "BioTech 2.0", "900"};
        String[] incorrectDataLine_tooFewArguments = {"0", "BioTech 2.0"};
        String[] incorrectDataLine_tooManyArguments = {"0", "BioTech 2.0", "900", "additional argument"};
        String[] incorrectDataLine_negativeNumericArguments = {"0", "BioTech 2.0", "-900"};
        String[] incorrectDataLine_nonNumericArguments = {"0", "BioTech 2.0", "dziewięć"};

        /* Should not throw any exception */
        SafetyGuard.checkIfPharmacyDataLineCorrect(correctDataLine, dummyLineNumber);

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfPharmacyDataLineCorrect(incorrectDataLine_tooFewArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfPharmacyDataLineCorrect(incorrectDataLine_tooManyArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfPharmacyDataLineCorrect(incorrectDataLine_negativeNumericArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfPharmacyDataLineCorrect(incorrectDataLine_nonNumericArguments, dummyLineNumber)
        );
    }

    @Test
    public void shouldThrowExceptionsWhenReadingInvalidConnectionsDataLines() {
        int dummyLineNumber = 0;
        String[] correctDataLine = {"0", "0", "800", "70.5"};
        String[] incorrectDataLine_tooFewArguments = {"0", "0", "800"};
        String[] incorrectDataLine_tooManyArguments = {"0", "0", "800", "70.5", "additional argument"};
        String[] incorrectDataLine_negativeNumericArguments = {"0", "0", "800", "-70.5"};
        String[] incorrectDataLine_nonNumericArguments = {"0", "zero", "800", "70.5"};

        /* Should not throw any exception */
        SafetyGuard.checkIfConnectionDataLineCorrect(correctDataLine, dummyLineNumber);

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfConnectionDataLineCorrect(incorrectDataLine_tooFewArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfConnectionDataLineCorrect(incorrectDataLine_tooManyArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfConnectionDataLineCorrect(incorrectDataLine_negativeNumericArguments, dummyLineNumber)
        );

        assertThrows( 
            IllegalArgumentException.class,
            () -> SafetyGuard.checkIfConnectionDataLineCorrect(incorrectDataLine_nonNumericArguments, dummyLineNumber)
        );
    }

    @Test
    public void testDetectingInsufficientConnections() {
        this.pharmacies.put(5, new Pharmacy(5, "Fapteka Papteka", 15000) );
        this.suppliers.put(5, new Supplier(5, "Jerzy Zięba Basement Lab", 15000) );
        this.connections.add(new Connection(5, 5, 600, 70) );

        assertFalse(
            SafetyGuard.areConnectionsEfficientEnoughForThisPharmacy(pharmacies.get(5), connections)
        );

        assertTrue(
            SafetyGuard.areConnectionsEfficientEnoughForThisPharmacy(pharmacies.get(0), connections)
        );
    }
}