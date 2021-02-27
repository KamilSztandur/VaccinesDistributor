package vaccDistributor.src.main;

import vaccDistributor.src.inputOutputManagment.*;
import vaccDistributor.src.algorithms.DistributionAlgorithm;

public class VaccDistributor {
    private String[] args;
    private InputDataReader inputDataReader;
    private OutputDataWriter outputDataWriter;
    private DistributionAlgorithm distrAlgorithm;

    public VaccDistributor(String[] args) {
        this.args = args;
    }

    public void launch() {
        UserCommunicator.printSystem("Uruchomiono program.");
        try {
            String inputFilename = getInputFilename();

            readData(inputFilename);
            detectAnyLogicalProblemsInData();
            calculateBestTransactions();
            printNotFullyStockedPharmacies();
            saveData();

            UserCommunicator.printEmptyLine();
            UserCommunicator.printSystem("Praca programu zakończona pomyślnie.");
        } catch (Exception exception) {
            UserCommunicator.signalFailure();
            UserCommunicator.printFailureDescription(exception.getMessage());

            UserCommunicator.printEmptyLine();
            UserCommunicator.printSystem("Praca programu zakończona z błędem.");
        }
    }

    private String getInputFilename() {
        if (this.args.length == 0) {
            throw new IllegalArgumentException("Brak podanego pliku z danymi. Poprawne wywołanie:\n"
                    + "\tjava -jar vaccDistributor.jar input.txt\n"
                    + "gdzie input.txt to nazwa pliku z danymi, który znajduje się w biężącym katalogu.");
        } else {
            return args[0];
        }
    }

    private void readData(String inputFilename) throws Exception {
        UserCommunicator.printStatus("Czytanie danych wejściowych");

        this.inputDataReader = new InputDataReader(inputFilename);
        this.inputDataReader.read();

        UserCommunicator.signalSuccess();
    }

    private void detectAnyLogicalProblemsInData() {
        UserCommunicator.printStatus("Szukanie błędów logicznych w danych wejściowych");

        var pharmacies = this.inputDataReader.detectAnyLogicalProblemsInData();
        if(pharmacies.size() != 0) {
            UserCommunicator.signalFailure("Odnaleziono.");
            for(var pharmacy : pharmacies) {
                UserCommunicator.printWarning(
                    "Połączenia i producenci nie są dostatecznie wydajni dla tak wymagającej apteki: " + pharmacy + "."
                );
            }
        } else {
            UserCommunicator.signalSuccess("Nie odnaleziono.");
        }
    }

    private void printNotFullyStockedPharmacies() {
        var pharmacies = this.distrAlgorithm.getNotFullyStockedPharmaciesInfo();
        
        for(var pharmacy : pharmacies.entrySet()) {
            String name = pharmacy.getKey();
            int needed = pharmacy.getValue();

            UserCommunicator.printWarning(
                "Aptece \"" + name + "\" brakuje \"" + needed + "\" szczepionek."
            );
        }
    }

    private void calculateBestTransactions() {
        UserCommunicator.printStatus("Obliczanie najlepszych połączeń");

        this.distrAlgorithm = new DistributionAlgorithm( 
            inputDataReader.getSuppliers(),
            inputDataReader.getPharmacies(), 
            inputDataReader.getConnections()
        );
        
        this.distrAlgorithm.calculate();

        UserCommunicator.signalSuccess();
    }

    private void saveData() throws Exception {
        this.outputDataWriter = new OutputDataWriter("connections.txt");

        UserCommunicator.printStatus("Tworzenie katalogu z rozwiazaniem");
        this.outputDataWriter.createOutputDir();
        UserCommunicator.signalSuccess();

        UserCommunicator.printStatus("Zapisywanie danych do pliku");
        this.outputDataWriter.save(
            this.distrAlgorithm.getTransactions()
        );

        UserCommunicator.signalSuccess();
    }
}