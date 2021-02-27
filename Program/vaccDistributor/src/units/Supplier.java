package vaccDistributor.src.units;

public class Supplier {
    private int id;
    private String name;
    private int dailyProduction;
    private int availableDailyProduction;

    public Supplier(int id, String name, int dailyProduction) {
        this.id = id;
        this.name = name;
        this.dailyProduction = dailyProduction;
        this.availableDailyProduction = dailyProduction;
    }

    public boolean removeFromAvailableDailyProduction(int amount) {
        if(this.availableDailyProduction >= amount) {
            this.availableDailyProduction -= amount;
            return true;
        } else {
            return false;
        }
    }

    public boolean isFullySoldOut() {
        return this.availableDailyProduction == 0;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getDailyProduction() {
        return this.dailyProduction;
    }

    public int getAvailableDailyProduction() {
        return this.availableDailyProduction;
    }

    @Override
    public String toString() {
        return "[" + name + " (id = " + id + ")]";
    }

    @Override
    public boolean equals(Object obj) {
        Supplier otherSupplier = (Supplier) obj;
        return this.getId() == otherSupplier.getId() || this.getName().equals(otherSupplier.getName() );
    }
}
