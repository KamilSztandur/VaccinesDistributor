package vaccDistributor.test;

import vaccDistributor.src.algorithms.DistributionAlgorithm;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.junit.*;

public class DistributionSpeedTest {
    private int[] sizesOfTestVectors;
    private int numberOfAttempts;
    private DecimalFormatSymbols formatWithSpace;
    private DecimalFormat formatter;
    
    @Before
    public void setUp() {
        initializeSizesOfTestVectors();
        this.numberOfAttempts = 10;
        initializeOutputFormatters();
    }

    @After
    public void tearDown() {
        this.sizesOfTestVectors = null;
        this.formatWithSpace = null;
        this.formatter = null;
    }

    @Test
    public void testDistrAlgorithm() {
        System.out.println("[SPEEDTEST] Starting benchmark for distribution algorithm.");

        int n = this.sizesOfTestVectors.length;
        for (int i = 0; i < n; i++) {
            long sum = 0L;
            
            int size = this.sizesOfTestVectors[i];
            for (int j = 0; j < this.numberOfAttempts; j++) {
                sum += getTimeForNElements(size);
            }

            printResult(i + 1, size, sum);
        }
        System.out.println("[SPEEDTEST] Benchmark completed.");
    }

    private long getTimeForNElements(int n) {
        DistributionAlgorithm algorithm = prepareAlgorithmForNElements(n);

        long start = System.nanoTime();
        algorithm.calculate();
        long end = System.nanoTime();

        return (end - start);
    }

    private DistributionAlgorithm prepareAlgorithmForNElements(int n) {
        RandomDataGen dataGen = new RandomDataGen(n);
        return new DistributionAlgorithm(
            dataGen.getDummySuppliers(),
            dataGen.getDummyPharmacies(), 
            dataGen.getDummyConnections()
        );
    }

    private void printResult(int thisTestNumber, int size, long result) {
        int totalTestsAmount = this.sizesOfTestVectors.length;

        String scoreFormatted = this.formatter.format(
            (result / this.numberOfAttempts)
        );

        System.out.println("[PERFORMED TEST] " + thisTestNumber + "/" + totalTestsAmount );
        System.out.println("\tPharmacies = " + size);
        System.out.println("\tSuppliers = " + size);
        System.out.println("\tConnections = " + (size * size));
        System.out.println(
            "\tAverage result (" + this.numberOfAttempts + " attempts) = " + scoreFormatted + " nanoseconds"
        );
    }

    private void initializeSizesOfTestVectors() {
        this.sizesOfTestVectors = new int[5];
        this.sizesOfTestVectors[0] = 100;
        this.sizesOfTestVectors[1] = 200;
        this.sizesOfTestVectors[2] = 300;
        this.sizesOfTestVectors[3] = 400;
        this.sizesOfTestVectors[4] = 500;
    }

    private void initializeOutputFormatters() {
        this.formatWithSpace = new DecimalFormatSymbols();
        this.formatWithSpace.setGroupingSeparator(' ');
        this.formatter = new DecimalFormat("###,###", formatWithSpace);
    }
}