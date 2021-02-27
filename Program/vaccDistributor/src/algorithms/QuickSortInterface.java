package vaccDistributor.src.algorithms;

import vaccDistributor.src.units.*;
import java.util.ArrayList;

public interface QuickSortInterface {
    public enum By {
        COST,
        REALTRANSFER
    }

    public ArrayList<Connection> sortWholeList(ArrayList<Connection> connections);
    
    public ArrayList<Connection> sortOnlyPartOfList(
        ArrayList<Connection> connections, int start, int end
    );
}
