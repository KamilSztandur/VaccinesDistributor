package vaccDistributor.src.inputOutputManagment;

import vaccDistributor.src.units.*;
import java.util.ArrayList;
import java.io.*;

public class OutputDataWriter {
    private String directoryName;
    private String filename;

    public OutputDataWriter(String filename) {
        this.directoryName = "output";
        this.filename = filename;
    }

    public void createOutputDir() throws IOException {
        File directory = new File(this.directoryName);

        try {
            if(!directory.exists()) {
                directory.mkdirs();
            }
        } catch (Exception exception) {
            throw new IOException("Nie udało się utworzyć katalogu.\n");
        }
    }

    public void save(ArrayList<Transaction> transactions) throws Exception {
        File output = new File(this.directoryName + File.separator + filename);
        double totalCost = 0;

        deleteFileIfExists(output);
        try (   FileWriter fw = new FileWriter(output); 
                BufferedWriter myWriter = new BufferedWriter(fw)) 
        {
            for (var transaction : transactions) {
                totalCost += transaction.getTotalCost();
                myWriter.write(transaction + "\n");
            }
            myWriter.write("\nOpłaty całkowite: " + String.format("%.2f", totalCost) + "zł\n");
        } catch (IOException e) {
            throw new Exception("Nie udało się zapisać pliku z wynikiem.\n");
        }
    }

    private void deleteFileIfExists(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
