package vaccDistributor.test.TDD;

import vaccDistributor.src.inputOutputManagment.OutputDataWriter;
import vaccDistributor.src.units.Transaction;
import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class OutputDataWriterTest {
    private OutputDataWriter writer;
    private String outputFilename;
    
    @Before 
    public void setUp() {
        this.outputFilename = "output.txt";
        this.writer = new OutputDataWriter(this.outputFilename);
    }

    @After
    public void tearDown() {
        this.writer = null;
    }

    @Test
    public void testCreatingOutputDir() throws IOException {
        this.writer.createOutputDir();

        File outputDirectory = new File("output");
        assertTrue( outputDirectory.exists() );
    }

    @Test
    public void testCreatingOutputFile() throws IOException, Exception {
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add( new Transaction("Apteka", "Producent", 500, 70.5) );

        this.writer.createOutputDir();
        this.writer.save(transactions);

        File outputFile = new File("output" + File.separator + this.outputFilename);
        assertTrue(outputFile.exists());
    }

    @Test
    public void testCreatingOutputDirWhenAlreadyExists() throws IOException {
        this.writer.createOutputDir();

        this.writer.createOutputDir();

        File outputDirectory = new File("output");
        assertTrue( outputDirectory.exists() );
    }
}
