package vaccDistributor.src.units;

public class Pharmacy {
    private int id;
    private String name;
    private int dailyNeed;
    private int currentDailyNeed;

    public Pharmacy(int id, String name, int dailyNeed) {
        this.id = id;
        this.name = name;
        this.dailyNeed = dailyNeed;
        this.currentDailyNeed = dailyNeed;
    }

    public boolean removeFromCurrentDailyNeed(int amount) {
        if(this.currentDailyNeed >= amount) {
            this.currentDailyNeed -= amount;
            return true;
        } else {
            return false;
        }
    }

    public boolean isFullyStocked() {
        return this.currentDailyNeed == 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDailyNeed() {
        return dailyNeed;
    }

    public int getCurrentDailyNeed() {
        return currentDailyNeed;
    }

    @Override
    public String toString() {
        return "[" + name + " (id = " + id + ")]";
    }

    @Override
    public boolean equals(Object obj) {
        Pharmacy otherPharmacy = (Pharmacy) obj;
        return this.getId() == otherPharmacy.getId() || this.getName().equals(otherPharmacy.getName() );
    }
}
