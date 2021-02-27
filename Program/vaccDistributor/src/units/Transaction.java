package vaccDistributor.src.units;

public class Transaction {
    private String pharmacy;
    private String supplier;
    private int transfer;
    private double costPerSingleVaccine;
    private double totalCost;

    public Transaction(String pharmacy, String supplier, int transfer, double costPerSingleVaccine) {
        this.pharmacy = pharmacy;
        this.supplier = supplier;
        this.transfer = transfer;
        this.costPerSingleVaccine = costPerSingleVaccine;

        this.totalCost = costPerSingleVaccine * transfer;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getTransfer() {
        return transfer;
    }

    public double getCostPerSingleVaccine() {
        return costPerSingleVaccine;
    }

    public double getTotalCost() {
        return totalCost;
    }
    
    @Override
    public String toString() {
        String connectionData = supplier + " -> " + pharmacy;
        String singleCostFormatted = String.format("%.2f", costPerSingleVaccine);
        String totalCostFormatted = String.format("%.2f", totalCost);
        String costData = "[Koszt = " + transfer + " * " + singleCostFormatted + " = " + totalCostFormatted + "zł]";
        
        return connectionData + " " + costData;
    }

    @Override
    public boolean equals(Object obj) {
        Transaction otherTransaction = (Transaction) obj;
        return this.pharmacy.equals(otherTransaction.pharmacy)
                && this.supplier.equals(otherTransaction.supplier);
    }
}
