package yusufekremkecilioglu;

import java.util.ArrayList;
import java.util.List;

public class Depot {
    private List<List<Integer>> _routes;
    private int _depotNumber;


    public int GetDepotNumber() {
        return _depotNumber;
    }
    public void SetDepotNumber(int _depotNumber){
        this._depotNumber = _depotNumber;
    }
    public List<List<Integer>> GetRoutes() {
        return _routes;
    }
    public List<Integer> GetRoute(int index){
        if(_routes.get(index).isEmpty() || index > _routes.size()) return new ArrayList<>();
        return _routes.get(index);
    }
    public  Depot(int depotNumber, int salesmanCount){
        this._depotNumber = depotNumber;
        _routes = new ArrayList<>();
        for (int i = 0; i < salesmanCount; i++) {
            _routes.add(new ArrayList<>());
        }
    }
    public int RouteCost(int[][] distanceMatrix){
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

        //System.out.println("Depot" + _depotNumber + " cost is " + totalCost);
        return totalCost;
    }
    public String GetName(){
        return TurkishNetwork.cities[_depotNumber];
    }
}
