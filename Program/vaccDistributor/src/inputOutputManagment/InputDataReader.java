package vaccDistributor.src.inputOutputManagment;

import vaccDistributor.src.SafetyGuard;
import vaccDistributor.src.units.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class InputDataReader {
    private String inputFilename;

    private HashMap<Integer, Supplier> suppliers;
    private HashMap<Integer, Pharmacy> pharmacies;
    private ArrayList<Connection> connections;

    private String nowReading;
    private boolean suppliersListHasBeenRead;
    private boolean pharmaciesListHasBeenRead;
    private boolean connectionsListHasBeenRead;

    public InputDataReader(String inputFilename) {
        this.inputFilename = inputFilename;

        this.suppliers = new HashMap<Integer, Supplier>();
        this.pharmacies = new HashMap<Integer, Pharmacy>();
        this.connections = new ArrayList<Connection>();

        this.suppliersListHasBeenRead = false;
        this.pharmaciesListHasBeenRead = false;
        this.connectionsListHasBeenRead = false;
    }

    public void read() throws Exception {
        File input = getInputFile();

        try ( FileReader fw = new FileReader(input); 
              BufferedReader myReader = new BufferedReader(fw)) 
        {
            int lineNumber = 0;
            String line = null;

            while ((line = myReader.readLine()) != null) {
                lineNumber++;

                switch (line) {
                    case "# Producenci szczepionek (id | nazwa | dzienna produkcja)":
                        signalizeSuppliersSectionReading();
                        break;

                    case "# Apteki (id | nazwa | dzienne zapotrzebowanie)":
                        signalizePharmaciesSectionReading();
                        break;

                    case "# Połączenia producentów i aptek (id producenta | id apteki | dzienna maksymalna liczba dostarczanych szczepionek | koszt szczepionki [zł] )":
                        signalizeConnectionsSectionReading();
                        break;

                    default:
                        addRecordFor(line, lineNumber);
                }
            }

            if (this.connectionsListHasBeenRead == false) {
                throw new IllegalArgumentException("Nie odnaleziono pola z połączeniami.");
            }
        } catch (IOException e) {
            throw new Exception("Nie udało się odczytać pliku " + this.inputFilename);
        }
    }

    public ArrayList<String> detectAnyLogicalProblemsInData() {
        ArrayList<String> impossibleToSupplyPharmacies = new ArrayList<>();
        for(Pharmacy pharmacy : this.pharmacies.values() ) {
            if( ! SafetyGuard.areConnectionsEfficientEnoughForThisPharmacy(pharmacy, this.connections) ) {
                impossibleToSupplyPharmacies.add(pharmacy.getName());
            }
        }

        return impossibleToSupplyPharmacies;
    }

    public ArrayList<Connection> getConnections() {
        return this.connections;
    }

    public HashMap<Integer, Supplier> getSuppliers() {
        return this.suppliers;
    }

    public HashMap<Integer, Pharmacy> getPharmacies() {
        return this.pharmacies;
    }

    private File getInputFile() {
        File input = new File(this.inputFilename);

        if( input.exists() ) {
            return input;
        } else {
            throw new IllegalArgumentException("Podany plik nie istnieje.");
        }
    }

    private void signalizeSuppliersSectionReading() {
        if (this.suppliersListHasBeenRead == true) {
            throw new IllegalArgumentException("Zduplikowane pola producentów.");
        } else {
            this.nowReading = "Suppliers";
            this.suppliersListHasBeenRead = true;
        }
    }

    private void signalizePharmaciesSectionReading() {
        if (this.suppliersListHasBeenRead == false) {
            throw new IllegalArgumentException("Nie odnaleziono pola z producentami.");
        }

        if (this.pharmaciesListHasBeenRead == true) {
            throw new IllegalArgumentException("Zduplikowane pola aptek.");
        } else {
            this.nowReading = "Pharmacies";
            this.pharmaciesListHasBeenRead = true;
        }
    }

    private void signalizeConnectionsSectionReading() {
        if (this.suppliersListHasBeenRead == false) {
            throw new IllegalArgumentException("Nie odnaleziono pola z producentami");
        }

        if (this.pharmaciesListHasBeenRead == false) {
            throw new IllegalArgumentException("Nie odnaleziono pola z aptekami.");
        }
    
        if (this.connectionsListHasBeenRead == true) {
            throw new IllegalArgumentException("Zduplikowane pola z połączeniami.");
        } else {
            this.nowReading = "Connections";
            this.connectionsListHasBeenRead = true;
        }
    }

    private void addRecordFor(String line, int lineNumber) {
        if(this.nowReading == null) {
            throw new IllegalArgumentException("Nieprawidłowy początek pliku.");
        }

        switch (this.nowReading) {
            case "Suppliers":
                Supplier supplier = readSupplier(line, lineNumber);
                this.suppliers.put(supplier.getId(), supplier);
                break;

            case "Pharmacies":
                Pharmacy pharmacy = readPharmacy(line, lineNumber);
                this.pharmacies.put(pharmacy.getId(), pharmacy);
                break;

            case "Connections":
                Connection connection = readConnection(line, lineNumber);
                this.connections.add(connection);
                break;
        }
    }

    private Supplier readSupplier(String line, int lineNumber) {
        String[] data = line.split(" \\| ");
        SafetyGuard.checkIfSupplierDataLineCorrect(data, lineNumber);

        Supplier supplier = new Supplier(
            Integer.parseInt(data[0]), 
            data[1], 
            Integer.parseInt(data[2])
        );

        if (SafetyGuard.isThisSupplierDuplicated(supplier, suppliers)) {
            throw new IllegalArgumentException(
                "Zduplikowany dostawca \'" + supplier.getName() + "\' w linii nr. " + lineNumber + "."
            );
        } 

        return supplier;
    }

    private Pharmacy readPharmacy(String line, int lineNumber) {
        String[] data = line.split(" \\| ");
        SafetyGuard.checkIfPharmacyDataLineCorrect(data, lineNumber);

        Pharmacy pharmacy = new Pharmacy(
            Integer.parseInt(data[0]), 
            data[1], 
            Integer.parseInt(data[2])
        );

        if (SafetyGuard.isThisPharmacyDuplicated(pharmacy, pharmacies)) {
            throw new IllegalArgumentException(
                "Zduplikowana apteka \'" + pharmacy.getName() + "\' w linii nr. " + lineNumber + "."
            );
        } 

        return pharmacy;
    }

    private Connection readConnection(String line, int lineNumber) {
        String[] data = line.split(" \\| ");
        SafetyGuard.checkIfConnectionDataLineCorrect(data, lineNumber);

        Connection connection = new Connection(
            Integer.parseInt(data[0]), 
            Integer.parseInt(data[1]),
            Integer.parseInt(data[2]),
            Double.parseDouble(data[3])
        );
        
        if ( ! SafetyGuard.isThisConnectionPharmacyRegistered(connection, pharmacies)) {
            throw new IllegalArgumentException(
                "Apteka z połączenia \'" + connection + "\' w linii nr. " + lineNumber + " nie istnieje."
            );
        } else if ( ! SafetyGuard.isThisConnectionSupplierRegistered(connection, suppliers) ) {
            throw new IllegalArgumentException(
                "Producent z połączenia \'" + connection + "\' w linii nr. " + lineNumber + " nie istnieje."
            );
        } else if (SafetyGuard.isThisConnectionDuplicated(connection, connections)) {
            throw new IllegalArgumentException(
                "Zduplikowane połączenie \'" + connection + "\' w linii nr. " + lineNumber + "."
            );
        } 

        return connection;
    }
}
