package vaccDistributor.src.algorithms;

import vaccDistributor.src.algorithms.QuickSortInterface.By;
import vaccDistributor.src.units.*;
import java.util.HashMap;
import java.util.ArrayList;

public class DistributionAlgorithm {
    private ConnectionsSorter sorter;
    private HashMap<Integer, Supplier> suppliers;
    private HashMap<Integer, Pharmacy> pharmacies;
    private ArrayList<Connection> connections;
    private ArrayList<Transaction> transactions;
    private ArrayList<Integer> extremeIndexes;

    public DistributionAlgorithm( 
            HashMap<Integer, Supplier> suppliers, 
            HashMap<Integer, Pharmacy> pharmacies, 
            ArrayList<Connection> connections
    ) {
        this.suppliers = suppliers;
        this.pharmacies = pharmacies;
        this.connections = connections;
        this.transactions = new ArrayList<>();
    }

    public void calculate() {
        sortConnectionsByVaccineCost();
        findFirstAndLastIndexesOfEqualParts();
        sortEqualPartsOfConnectionsListByRealTransfer();

        finalizeBestConnections();
    }

    public enum TEST {
        SORTINGBYCOST(0),
        FINDINGEXTREMEINDEXES(1),
        TOTALSORTING(2);

        private final int value;
        private TEST(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public void calculate(TEST mode) {
        switch(mode.getValue()) {
            case 0:
                sortConnectionsByVaccineCost();
                break;

            case 1:
                sortConnectionsByVaccineCost();
                findFirstAndLastIndexesOfEqualParts();
                break;

            case 2:
                sortConnectionsByVaccineCost();
                findFirstAndLastIndexesOfEqualParts();
                sortEqualPartsOfConnectionsListByRealTransfer();
                break;
        }
    }

    public HashMap<String, Integer> getNotFullyStockedPharmaciesInfo() {
        HashMap<String, Integer> info = new HashMap<>();
        for(var pharmacy : this.pharmacies.values()) {
            if( ! pharmacy.isFullyStocked() ){
                info.put(
                    pharmacy.getName(),
                    pharmacy.getCurrentDailyNeed()
                );
            }
        }

        return info;
    }

    public ArrayList<Integer> getExtremeIndexes() {
        return this.extremeIndexes;
    }

    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    public ArrayList<Connection> getConnections() {
        return this.connections;
    }

    private void finalizeBestConnections() {
        int n = connections.size();
        for(int i = 0; i < n; i++) {
            Connection currentConnection = this.connections.get(i);
            Pharmacy pharmacy = pharmacies.get(
                currentConnection.getPharmacyID()
            );
            Supplier supplier = suppliers.get(
                currentConnection.getSupplierID()
            );

            int amount = currentConnection.getRealTransferWorthiness(
                pharmacy, 
                supplier
            );
            
            if(amount > 0) {
                pharmacy.removeFromCurrentDailyNeed(amount);
                supplier.removeFromAvailableDailyProduction(amount);
            }

            this.transactions.add(
                new Transaction(
                    pharmacy.getName(),
                    supplier.getName(), 
                    amount, 
                    currentConnection.getCostPerSingleVaccine()
                )
            );
        }
    }

    private void sortConnectionsByVaccineCost() {
        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(this.pharmacies);
        this.sorter.setSuppliers(this.suppliers);
        this.connections = this.sorter.sortWholeList(this.connections);

        this.sorter = null;
    }

    private void sortEqualPartsOfConnectionsListByRealTransfer() {
        this.sorter = new ConnectionsSorter(By.REALTRANSFER);
        this.sorter.setPharmacies(this.pharmacies);
        this.sorter.setSuppliers(this.suppliers);

        int n = this.extremeIndexes.size();
        for(int i = 1; i < n; i += 2) {
            int firstIndex = this.extremeIndexes.get(i - 1);
            int lastIndex = this.extremeIndexes.get(i);

            this.connections = this.sorter.sortOnlyPartOfList(
                this.connections, 
                firstIndex, 
                lastIndex
            );
        }

        this.sorter = null;
    }

    /* It is mandatory to have already sorted Connections by cost!!! */
    private void findFirstAndLastIndexesOfEqualParts() {
        ArrayList<Integer> extremeIndexes = new ArrayList<>();
        int first, last;

        int n = this.connections.size();
        int i = 1;
        while(i < n) {
            if( areTheseConnectionsCostValuesEqual(i - 1, i) ) {
                first = i - 1;
                while(i < n && areTheseConnectionsCostValuesEqual(i - 1, i)) {
                    i++;
                }
                last = i - 1;
                
                if(last - first > 0) {
                    extremeIndexes.add(first);
                    extremeIndexes.add(last);
                }
            }
            i++;
        }

        this.extremeIndexes = extremeIndexes;
    }

    private boolean areTheseConnectionsCostValuesEqual(int firstIndex, int secondIndex) {
        double firstValue = this.connections.get(firstIndex).getCostPerSingleVaccine();
        double secondValue = this.connections.get(secondIndex).getCostPerSingleVaccine();

        return firstValue == secondValue;
    }
}