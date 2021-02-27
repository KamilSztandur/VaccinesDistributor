package vaccDistributor.test.TDD;

import vaccDistributor.src.algorithms.DistributionAlgorithm;
import vaccDistributor.src.algorithms.DistributionAlgorithm.TEST;
import vaccDistributor.src.units.*;
import vaccDistributor.test.RandomDataGen;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.*;
import java.util.*;

public class DistributionAlgorithmTest {
    private DistributionAlgorithm algorithm;
    private RandomDataGen dataGen;
    private HashMap<Integer, Supplier> suppliers;
    private HashMap<Integer, Pharmacy> pharmacies;
    private final int MAX_AMOUNT = 10;

    @Before
    public void setUp() {
        this.dataGen = new RandomDataGen(MAX_AMOUNT);
        this.suppliers = this.dataGen.getDummySuppliers();
        this.pharmacies = this.dataGen.getDummyPharmacies();
    }
    
    @After
    public void tearDown() {
        this.dataGen = null;
        this.suppliers = null;
        this.pharmacies = null;
    }

    @Test
    public void testTotalSortingConnections() {
        ArrayList<Connection> connections = this.dataGen.getRandomConnectionsWithDuplicatedCosts(MAX_AMOUNT);
        this.algorithm = new DistributionAlgorithm(
            this.suppliers, 
            this.pharmacies, 
            connections
        );

        this.algorithm.calculate(TEST.TOTALSORTING);

        connections = this.algorithm.getConnections();

        int n = connections.size();
        Connection previousConnection = connections.get(0);
        for(int i = 1; i < n; i++) {
            Connection thisConnection = connections.get(i);
            double previousCost = previousConnection.getCostPerSingleVaccine();
            double thisCost = thisConnection.getCostPerSingleVaccine();
            
            assertTrue(previousCost <= thisCost);
            if(previousCost == thisCost) {
                int previousRealTransfer = previousConnection.getRealTransferWorthiness(
                    this.pharmacies.get(previousConnection.getPharmacyID()), 
                    this.suppliers.get(previousConnection.getSupplierID())
                );
                int thisRealTransfer = thisConnection.getRealTransferWorthiness(
                    this.pharmacies.get(thisConnection.getPharmacyID()), 
                    this.suppliers.get(thisConnection.getSupplierID())
                );

                assertTrue(previousRealTransfer >= thisRealTransfer);
            }
            previousConnection = thisConnection;
        }
    }

    @Test
    public void testFindingExtremeIndexes() {
        ArrayList<Connection> connections = this.dataGen.getConnectionsSortedByCostWithDuplicatedCosts(10);
        this.algorithm = new DistributionAlgorithm(
            this.suppliers, 
            this.pharmacies, 
            connections
        );

        this.algorithm.calculate(TEST.FINDINGEXTREMEINDEXES);

        ArrayList<Integer> indexes = this.algorithm.getExtremeIndexes();
        int[] correctResult = {0, 1, 2, 3, 6, 8};

        int n = indexes.size();
        for(int i = 0; i < n; i++) {
            assertEquals(indexes.get(i), correctResult[i]);
        }
    }
}
