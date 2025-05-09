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
    public int routeCost(int[][] distanceMatrix){

        int totalCost = 0;
        for (List<Integer> route : _routes){
            if(_routes.isEmpty()) continue;
            int cost = 0;
            cost += distanceMatrix[_depotNumber][route.get(0)];

            for (int i = 0; i < route.size() - 1; i++) {
                cost += distanceMatrix[route.get(i)][route.get(i + 1)];
            }
            cost += distanceMatrix[route.get(route.size() - 1)][_depotNumber];
            totalCost += cost;
        }


        return totalCost;
    }




}
