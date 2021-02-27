package vaccDistributor.src.algorithms;

import vaccDistributor.src.units.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionsSorter implements QuickSortInterface {
    private HashMap<Integer, Supplier> suppliers;
    private HashMap<Integer, Pharmacy> pharmacies;
    private ArrayList<Connection> connections;
    private By comparator;

    private int start;
    private int end;

    public ConnectionsSorter(By comparator) {
        this.comparator = comparator;
    }

    public ArrayList<Connection> sortWholeList(ArrayList<Connection> connections) {
        if(connections == null) {
            throw new IllegalArgumentException(
                "Input data cannot be null. Don't forget to fully initialize suppliers, pharmacies and connections."
            );
        };

        return sortOnlyPartOfList(connections, 0, connections.size() - 1);
    }

    public ArrayList<Connection> sortOnlyPartOfList(ArrayList<Connection> connections, int start, int end) {
        if(connections == null || this.pharmacies == null || this.suppliers == null) {
            throw new IllegalArgumentException(
                "Input data cannot be null. Don't forget to fully initialize suppliers, pharmacies and connections."
            );
        };

        if(connections.size() == 0 || this.pharmacies.size() == 0 || this.suppliers.size() == 0) {
            throw new IllegalArgumentException(
                "Input data cannot be empty."
            );
        };

        this.start = start;
        this.end = end;
        this.connections = new ArrayList<>(connections);
        if (this.comparator == By.COST) {
            quicksortConnectionsNonDecreasinglyByCost();
        } else {
            quicksortConnectionsNonDecreasinglyByRealTransfer();
        }

        return this.connections;
    }

    public void setSuppliers(HashMap<Integer, Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void setPharmacies(HashMap<Integer, Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
    }

    private void quicksortConnectionsNonDecreasinglyByCost() {
        int vectorSize = this.connections.size();
        int[] starts = new int[vectorSize];
        int[] ends = new int[vectorSize];

        int left = this.start;
        int right = this.end;

        starts[0] = left;
        ends[0] = right;

        int n = 1;
        int pivot;

        if(left < right) {
            while(n > 0) {
                n--;
                left = starts[n];
                right = ends[n];

                pivot = splitDataForCost(left, right);

                if(pivot - 1 > left) {
                    starts[n] = left;
                    ends[n] = pivot - 1;
                    n++;
                }

                if (pivot + 1 < right) {
                    starts[n] = pivot + 1;
                    ends[n] = right;
                    n++;
                }
            }
        }
    }

    private void quicksortConnectionsNonDecreasinglyByRealTransfer() {
        int vectorSize = this.connections.size();
        int[] starts = new int[vectorSize];
        int[] ends = new int[vectorSize];

        int left = this.start;
        int right = this.end;

        starts[0] = left;
        ends[0] = right;

        int n = 1;
        int pivot;

        if(left < right) {
            while(n > 0) {
                n--;
                left = starts[n];
                right = ends[n];

                pivot = splitDataForRealTransfer(left, right);

                if(pivot - 1 > left) {
                    starts[n] = left;
                    ends[n] = pivot - 1;
                    n++;
                }

                if (pivot + 1 < right) {
                    starts[n] = pivot + 1;
                    ends[n] = right;
                    n++;
                }
            }
        }
    }

    private int splitDataForCost(int start, int end) {
        int left = start + 1;
        int right = end;

        getPivotByMedianAndSetAsFirstElementForCost(start, end);

        while (left < right) { 
            while (left < right && connections.get(left).getCostPerSingleVaccine() < connections.get(start).getCostPerSingleVaccine() ) {
                left++;
            }

            while (left < right && connections.get(right).getCostPerSingleVaccine() >= connections.get(start).getCostPerSingleVaccine()) {
                right--;
            }

            swap(left, right);
        }

        if (connections.get(left).getCostPerSingleVaccine() >= connections.get(start).getCostPerSingleVaccine()) {
            left--;
        }

        swap(start, left);

        return left;
    }

    private int splitDataForRealTransfer(int start, int end) {
        int left = start + 1;
        int right = end;

        getPivotByMedianAndSetAsFirstElementForRealTransfer(start, end);
        while (left < right) {
            while (left < right && getRealTransferWorthiness(left) >= getRealTransferWorthiness(start)) {
                left++;
            }

            while (left < right && getRealTransferWorthiness(right) < getRealTransferWorthiness(start)) {
                right--;
            }

            swap(left, right);
        }

        if (getRealTransferWorthiness(left) < getRealTransferWorthiness(start)) {
            left--;
        }

        swap(start, left);

        return left;
    }

    private void getPivotByMedianAndSetAsFirstElementForCost(int start, int end) {
        int randIndex = (int) (Math.random() * (end - start) + start);

        double firstElement = this.connections.get(start).getCostPerSingleVaccine();
        double randElement = this.connections.get(randIndex).getCostPerSingleVaccine();
        double lastElement = this.connections.get(end).getCostPerSingleVaccine();

        double median = Math.max(
            Math.min(
                firstElement, 
                randElement
            ),
            Math.min(
                Math.max(
                    firstElement, 
                    randElement
                ), 
                lastElement
            ))
        ;

        int pivotIndex = start;
        if (median == firstElement) {
            pivotIndex = start;
        } else if (median == randElement) {
            pivotIndex = randIndex;
        } else if (median == lastElement) {
            pivotIndex = end;
        }

        swap(start, pivotIndex);
    }

    private void getPivotByMedianAndSetAsFirstElementForRealTransfer(int start, int end) {
        int randIndex = (int) (Math.random() * (end - start) + start);

        int firstElement = getRealTransferWorthiness(start);
        int randElement = getRealTransferWorthiness(randIndex);
        int lastElement = getRealTransferWorthiness(end);

        int median = Math.max(
            Math.min(
                firstElement, 
                randElement
            ),
            Math.min(
                Math.max(
                    firstElement, 
                    randElement
                ), 
                lastElement
            ))
        ;

        int pivotIndex = start;
        if (median == firstElement) {
            pivotIndex = start;
        } else if (median == randElement) {
            pivotIndex = randIndex;
        } else if (median == lastElement) {
            pivotIndex = end;
        }

        swap(start, pivotIndex);
    }

    private void swap(int firstId, int secondId) {
        if (firstId != secondId) {
            Connection firstValue = this.connections.get(firstId);
            this.connections.set(firstId, this.connections.get(secondId) );
            this.connections.set(secondId, firstValue);
        }
    }

    private int getRealTransferWorthiness(int connectionID) {
        Connection connection = this.connections.get(connectionID);
        Pharmacy pharmacy = this.pharmacies.get(
            connection.getPharmacyID()
        );

        Supplier supplier = this.suppliers.get(
            connection.getSupplierID()
        );

        int realTransfer = connection.getRealTransferWorthiness(
            pharmacy,
            supplier
        );

        return realTransfer;
    }
}