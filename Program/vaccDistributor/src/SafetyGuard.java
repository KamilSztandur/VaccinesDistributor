package vaccDistributor.src;

import vaccDistributor.src.units.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SafetyGuard {
    public static boolean areConnectionsEfficientEnoughForThisPharmacy(Pharmacy pharmacy, ArrayList<Connection> connections) {
        int connectionsPower = 0;
        for (var connection : connections) {
            connectionsPower += connection.getAvailableTransfer();
        }

        return pharmacy.getCurrentDailyNeed() <= connectionsPower;
    }

    public static void checkIfSupplierDataLineCorrect(String[] dataLine, int lineNumber) {
        if (dataLine.length != 3) {
            throw new IllegalArgumentException("Nieprawidłowa liczba danych w linii nr. " + lineNumber + ".");
        }

        int id, production;
        try {
            id = Integer.parseInt(dataLine[0]);
            production = Integer.parseInt(dataLine[2]);
            if( id < 0 || production < 0 ) { 
                throw new IllegalArgumentException("Dane liczbowe nie mogą być mniejsze niż zero w linii nr. " + lineNumber + ".");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Nieprawidłowy format danych liczbowych w linii nr. " + lineNumber + ".");
        }
    }

    public static void checkIfPharmacyDataLineCorrect(String[] dataLine, int lineNumber) {
        checkIfSupplierDataLineCorrect(dataLine, lineNumber);
    }

    public static void checkIfConnectionDataLineCorrect(String[] dataLine, int lineNumber) {
        if (dataLine.length != 4) {
            throw new IllegalArgumentException("Nieprawidłowa liczba danych w linii nr. " + lineNumber + ".");
        }

        int supplierID, pharmacyID, maxTransfer;
        double costPerSingleVaccine;
        try {
            supplierID = Integer.parseInt(dataLine[0]);
            pharmacyID = Integer.parseInt(dataLine[1]);
            maxTransfer = Integer.parseInt(dataLine[2]);
            costPerSingleVaccine = Double.parseDouble(dataLine[3]);

            if( supplierID < 0 || pharmacyID < 0 || maxTransfer < 0 || costPerSingleVaccine < 0) {
                throw new IllegalArgumentException("Dane liczbowe nie mogą być mniejsze niż zero w linii nr. " + lineNumber + ".");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Nieprawidłowy format danych liczbowych w linii nr. " + lineNumber + ".");
        }
    }

    public static boolean isThisPharmacyDuplicated(Pharmacy newPharmacy, HashMap<Integer, Pharmacy> pharmacies) {
        for (var pharmacy : pharmacies.values() ) {
            if (pharmacy.equals(newPharmacy)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isThisSupplierDuplicated(Supplier newSupplier, HashMap<Integer, Supplier> suppliers) {
        for (var supplier : suppliers.values()) {
            if (supplier.equals(newSupplier)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isThisConnectionDuplicated(Connection newConnection, ArrayList<Connection> connections) {
        for (var connection : connections) {
            if (connection.equals(newConnection) ) {
                return true;
            }
        }

        return false;
    }

    public static boolean isThisTransactionDuplicated(Transaction newTransaction, ArrayList<Transaction> transactions) {
        for (var transaction : transactions) {
            if (transaction.equals(newTransaction) ) {
                return true;
            }
        }

        return false;
    }

    public static boolean isThisConnectionPharmacyRegistered(Connection connection, HashMap<Integer, Pharmacy> pharmacies) {
        int pharmacyID = connection.getPharmacyID();
        for(var registeredPharmacyID : pharmacies.keySet()) {
            if( pharmacyID == registeredPharmacyID ) {
                return true;
            }
        } 
        return false;
    }

    public static boolean isThisConnectionSupplierRegistered(Connection connection, HashMap<Integer, Supplier> suppliers) {
        int supplierID = connection.getSupplierID();
        for(var registeredSupplierID : suppliers.keySet()) {
            if( supplierID == registeredSupplierID ) {
                return true;
            }
        } 
        return false;
    }
}