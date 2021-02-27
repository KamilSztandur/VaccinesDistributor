package vaccDistributor.test;

import vaccDistributor.src.units.*;
import java.util.*;

public class RandomDataGen {
    private long seed;
    private Random constGenerator;
    private int size;

    public RandomDataGen(int size) {
        this.seed = 25_11_2020L;
        this.constGenerator = new Random(this.seed);
        
        this.size = size;
    }


    public ArrayList<Connection> getDummyConnections() {
        return getDummyConnections(this.size);
    }
    
    public HashMap<Integer, Pharmacy> getDummyPharmacies() {
        return getDummyPharmacies(this.size);
    }

    public HashMap<Integer, Supplier> getDummySuppliers() {
        return getDummySuppliers(this.size);
    }
    

    public HashMap<Integer, Supplier> getDummySuppliers(int n) {
        HashMap<Integer, Supplier> suppliers = new HashMap<>();

        for(int i = 0; i < n; i++) {
            String supplierName = getRandDummyName(i);
            int dailyProduction = generateRandTransfer();
            int supplierID = i;
            suppliers.put(supplierID, new Supplier(supplierID, supplierName, dailyProduction));
        }

        return suppliers;
    }

    public HashMap<Integer, Pharmacy> getDummyPharmacies(int n) {
        HashMap<Integer, Pharmacy> pharmacies = new HashMap<>();

        for(int i = 0; i < n; i++) {
            String pharmacyName = getRandDummyName(i);
            int dailyNeed = generateRandTransfer();
            int pharmacyID = i;
            pharmacies.put(pharmacyID, new Pharmacy(pharmacyID, pharmacyName, dailyNeed));
        }

        return pharmacies;
    }

    public ArrayList<Connection> getDummyConnections(int n) {
        ArrayList<Connection> connections = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                int maxTransfer = generateRandTransfer();
                double costPerSingleVaccine = generateRandVaccineCost();
                connections.add(new Connection(i, j, maxTransfer, costPerSingleVaccine));
            }
        }

        return connections;
    }

    
    public ArrayList<Connection> getRandomConnectionsWithDuplicatedCosts(int n) {
        double costPerSingleVaccine;

        ArrayList<Connection> connections = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            int maxTransfer = generateRandTransfer();

            if( constGenerator.nextDouble() > 0.5) {
                costPerSingleVaccine = generateRandVaccineCost();
            } else {
                costPerSingleVaccine = 70.5;
            }
            connections.add(new Connection(i, i, maxTransfer, costPerSingleVaccine));
        }

        return connections;
    }

    public ArrayList<Connection> getConnectionsSortedByCostWithDuplicatedCosts(int n) {
        ArrayList<Connection> connections = new ArrayList<>();
        double costPerSingleVaccine = 50.5;
        for(int i = 1; i < n; i++) {
            int maxTransfer = generateRandTransfer();

            if( constGenerator.nextDouble() > 0.5) {
                // Assign the same value by ignoring
            } else {
                costPerSingleVaccine++;
            }
            connections.add(new Connection(i, i, maxTransfer, costPerSingleVaccine));
        }

        return connections;
    }

    
    private int generateRandTransfer() {
        return this.constGenerator.nextInt(10)*100;
    }

    private String getRandDummyName(int n) {
        String dummyPharmacyName = "";
        for(int j = 0; j < n; j++) {
            dummyPharmacyName += (char) ('A' + this.constGenerator.nextInt(26));
        }

        return dummyPharmacyName;
    }

    private double generateRandVaccineCost() {
        return Math.round(this.constGenerator.nextDouble()*10000.0)/100.0;
    }
}
