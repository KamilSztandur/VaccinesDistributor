package vaccDistributor.src.units;

public class Connection {
    private int supplierID;
    private int pharmacyID;
    private int maxTransfer;
    private int availableTransfer;
    private double costPerSingleVaccine;

    public Connection(int supplierID, int pharmacyID, int maxTransfer, double costPerSingleVaccine) {
        this.supplierID = supplierID;
        this.pharmacyID = pharmacyID;
        this.maxTransfer = maxTransfer;
        this.availableTransfer = maxTransfer;
        this.costPerSingleVaccine = costPerSingleVaccine;
    }

    public boolean removeFromAvailableTransfer(int amount) {
        if(this.availableTransfer >= amount) {
            this.availableTransfer -= amount;
            return true;
        } else {
            return false;
        }
    }

    public int getRealTransferWorthiness(Pharmacy pharmacy, Supplier supplier) {
        if( this.supplierID != supplier.getId() || this.pharmacyID != pharmacy.getId()) {
            throw new IllegalArgumentException(
                "Given facilities IDs don't match facilities IDs this connection connects."
            );
        }
        
        int realTransferWorthiness = Math.min( 
            this.getAvailableTransfer(), 
            Math.min(
                pharmacy.getCurrentDailyNeed(),
                supplier.getAvailableDailyProduction()
            )
        );

        return realTransferWorthiness;
    }

    public int getSupplierID() {
        return this.supplierID;
    }

    public int getPharmacyID() {
        return this.pharmacyID;
    }

    public int getMaxTransfer() {
        return this.maxTransfer;
    }

    public double getCostPerSingleVaccine() {
        return this.costPerSingleVaccine;
    }

    public int getAvailableTransfer() {
        return this.availableTransfer;
    }

    @Override
    public String toString() {
        return "[" + supplierID + " -> " + pharmacyID + "]";
    }

    @Override
    public boolean equals(Object obj) {
        Connection otherConnection = (Connection) obj;
        return this.getPharmacyID() == otherConnection.getPharmacyID() 
                && this.getSupplierID() == otherConnection.getSupplierID();
    }
}
