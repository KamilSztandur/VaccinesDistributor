package vaccDistributor.test.TDD;

import vaccDistributor.src.algorithms.ConnectionsSorter;
import vaccDistributor.src.algorithms.QuickSortInterface.By;
import vaccDistributor.src.units.*;
import vaccDistributor.test.RandomDataGen;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.*;
import java.util.*;

public class ConnectionsSorterTest {
    private ConnectionsSorter sorter;
    private RandomDataGen gen;

    private HashMap<Integer, Supplier> suppliers;
    private HashMap<Integer, Pharmacy> pharmacies;
    private ArrayList<Connection> connections;
    
    private final int MAX_AMOUNT = 70;

    @Before
    public void setUp() {
        this.gen = new RandomDataGen(MAX_AMOUNT);
        this.suppliers = this.gen.getDummySuppliers();
        this.pharmacies = this.gen.getDummyPharmacies();
        this.connections = this.gen.getDummyConnections();
    }

    @After
    public void tearDown() {
        this.sorter = null;

        this.gen = null;
        this.suppliers = null;
        this.pharmacies = null;
        this.connections = null;
    }

    @Test
    public void testSortingNullConnectionsList() {
        this.connections = null;

        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(suppliers);

        assertThrows(
            IllegalArgumentException.class,
            () -> this.sorter.sortWholeList(this.connections)
        );
    }

    @Test
    public void testSortingNullPharmacyList() {
        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(null);
        this.sorter.setSuppliers(suppliers);

        assertThrows(
            IllegalArgumentException.class,
            () -> this.sorter.sortWholeList(connections)
        );
    }

    @Test
    public void testSortingNullSuppliersList() {
        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> this.sorter.sortWholeList(connections)
        );
    }

    @Test
    public void testSortingEmptyConnectionsList() {
        this.connections = new ArrayList<>();

        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(suppliers);

        assertThrows(
            IllegalArgumentException.class,
            () -> this.sorter.sortWholeList(connections)
        );
    }

    @Test
    public void testSortingEmptyPharmacyList() {
        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(new HashMap<Integer, Pharmacy>());
        this.sorter.setSuppliers(suppliers);

        assertThrows(
            IllegalArgumentException.class,
            () -> this.sorter.sortWholeList(connections)
        );
    }

    @Test
    public void testSortingEmptySuppliersList() {
        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(new HashMap<Integer, Supplier>());

        assertThrows(
            IllegalArgumentException.class,
            () -> this.sorter.sortWholeList(connections)
        );
    }

    @Test
    public void testSortingByRealTransferForOneElement() {
        this.suppliers = this.gen.getDummySuppliers(1);
        this.pharmacies = this.gen.getDummyPharmacies(1);
        this.connections = this.gen.getDummyConnections(1);

        this.sorter = new ConnectionsSorter(By.REALTRANSFER);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(suppliers);

        ArrayList<Connection> sortedConnections = this.sorter.sortWholeList(connections);
        assertTrue( isArraySortedByRealTransferInNonAscendingOrder(sortedConnections) );
    }

    @Test
    public void testSortingByCostForOneElement() {
        this.suppliers = this.gen.getDummySuppliers(1);
        this.pharmacies = this.gen.getDummyPharmacies(1);
        this.connections = this.gen.getDummyConnections(1);

        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(suppliers);

        ArrayList<Connection> sortedConnections = this.sorter.sortWholeList(connections);
        assertTrue( isArraySortedByCostInNonDecreasingOrder(sortedConnections) );
    }

    @Test
    public void testSortingByRealTransfer() {
        this.sorter = new ConnectionsSorter(By.REALTRANSFER);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(suppliers);

        ArrayList<Connection> sortedConnections = this.sorter.sortWholeList(connections);
        assertTrue( isArraySortedByRealTransferInNonAscendingOrder(sortedConnections) );
    }

    @Test
    public void testSortingOnlyPartOfListByCost() {
        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(suppliers);
        
        ArrayList<Connection> sortedConnections = this.sorter.sortOnlyPartOfList(connections, 5, 10);
        ArrayList<Connection> temp = new ArrayList<>();
        for(int i = 5; i < 10; i++) {
            temp.add( sortedConnections.get(i) );
        }
        isArraySortedByCostInNonDecreasingOrder(temp);
    }

    @Test
    public void testSortingOnlyPartOfListByRealTransfer() {
        this.sorter = new ConnectionsSorter(By.REALTRANSFER);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(suppliers);
        
        ArrayList<Connection> sortedConnections = this.sorter.sortOnlyPartOfList(connections, 5, 10);
        ArrayList<Connection> temp = new ArrayList<>();
        for(int i = 5; i < 10; i++) {
            temp.add( sortedConnections.get(i) );
        }
        assertTrue( isArraySortedByRealTransferInNonAscendingOrder(temp) );
    }

    @Test
    public void testSortingByCost() {
        this.sorter = new ConnectionsSorter(By.COST);
        this.sorter.setPharmacies(pharmacies);
        this.sorter.setSuppliers(suppliers);
        
        ArrayList<Connection> sortedConnections = this.sorter.sortWholeList(connections);
        assertTrue( isArraySortedByCostInNonDecreasingOrder(sortedConnections) );
    }

    private boolean isArraySortedByCostInNonDecreasingOrder(ArrayList<Connection> connections) {
        int n = connections.size();
        for(int i = 1; i < n; i++) {
            double previousCost = connections.get(i - 1).getCostPerSingleVaccine();
            double nextCost = connections.get(i).getCostPerSingleVaccine();

            if(previousCost > nextCost) {
                return false;
            }
        }

        return true;
    }

    private boolean isArraySortedByRealTransferInNonAscendingOrder(ArrayList<Connection> connections) {
        int n = connections.size();
        for(int i = 1; i < n; i++) {
            Connection previousConnection = connections.get(i - 1);
            Connection nextConnection = connections.get(i);

            double previousRealTransfer = previousConnection.getRealTransferWorthiness(
                this.pharmacies.get(previousConnection.getPharmacyID()), 
                this.suppliers.get(previousConnection.getSupplierID())
            );

            double nextRealTransfer = nextConnection.getRealTransferWorthiness(
                this.pharmacies.get(nextConnection.getPharmacyID()), 
                this.suppliers.get(nextConnection.getSupplierID())
            );
            
            if(previousRealTransfer < nextRealTransfer) {
                return false;
            }
        }

        return true;
    }
}