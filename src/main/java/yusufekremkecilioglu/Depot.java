package yusufekremkecilioglu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Depot {
    private List<List<Integer>> _routes;
    private int _depotNumber;


    public int getDepotNumber() {
        return _depotNumber;
    }

    public void setDepotNumber(int _depotNumber){
        this._depotNumber = _depotNumber;
    }

    public List<List<Integer>> getRoutes() {
        return _routes;
    }

    public  Depot(int depotNumber, int salesmanCount){
        this._depotNumber = depotNumber;
        _routes = new ArrayList<>();
        for (int i = 0; i < salesmanCount; i++) {
            _routes.add(new ArrayList<>());
        }
    }




}
