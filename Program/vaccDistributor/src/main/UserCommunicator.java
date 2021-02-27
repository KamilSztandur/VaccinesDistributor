package vaccDistributor.src.main;

public class UserCommunicator { 
    private enum SetColorTo {
        /* Based on ANSI escape codes */
        DEFAULT("\u001B[0m"),           // White
        SUCCESS("\u001b[32m"),          // Green
        FAILURE("\u001B[31m"),          // Red
        WARNING("\u001B[33m");          // Yellow

        private String value;

        SetColorTo(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static void printSystem(String message) {
        System.out.println("[Vacc Distributor] " + message);
    }

    public static void printStatus(String message) {
        System.out.print(message + "... ");
    }

    public static void signalSuccess() {
        System.out.print(SetColorTo.SUCCESS);
        System.out.println("Sukces");
        System.out.print(SetColorTo.DEFAULT);
    }

    public static void signalSuccess(String message) {
        System.out.print(SetColorTo.SUCCESS);
        System.out.println(message);
        System.out.print(SetColorTo.DEFAULT);
    }

    public static void signalFailure() {
        System.out.print(SetColorTo.FAILURE);
        System.out.println("Porażka");
        System.out.print(SetColorTo.DEFAULT);
    }

    public static void signalFailure(String message) {
        System.out.print(SetColorTo.FAILURE);
        System.out.println(message);
        System.out.print(SetColorTo.DEFAULT);
    }

    public static void printFailureDescription(String description) {
        System.out.print(SetColorTo.FAILURE);
        System.out.println(description);
        System.out.print(SetColorTo.DEFAULT);
    }

    public static void printWarning(String message) {
        System.out.print(SetColorTo.WARNING);
        System.out.println("[Ostrzeżenie] " + message);
        System.out.print(SetColorTo.DEFAULT);
    }

    public static void printEmptyLine() {
        System.out.println();
    }
}
