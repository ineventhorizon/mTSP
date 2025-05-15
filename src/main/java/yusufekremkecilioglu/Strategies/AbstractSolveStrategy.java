package yusufekremkecilioglu.Strategies;

import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Interfaces.SolveStrategy;
import yusufekremkecilioglu.TurkishNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AbstractSolveStrategy implements SolveStrategy {
    protected static final Random _rand = new Random();
    protected List<Depot> _depots;
    protected int _totalCitiesCount = TurkishNetwork.cities.length;
    protected int[][] _distances = TurkishNetwork.distance;
    protected int _numberOfDepots, _numberOfSalesmen;
    protected int _bestCost = Integer.MAX_VALUE;
    protected List<Depot> _bestSolution = null;

    @Override
    public int Solve(int iteration, boolean returnBest) {
        return -1;
    }
    protected int calculateCost() {
        int totalCost = 0;
        for (Depot depot : _depots) {
            totalCost += depot.RouteCost(_distances);
        }
        return totalCost;
    }
    @Override
    public void ConfigureStrategy(int numberOfDepots, int numberOfSalesmen, List<Depot> depots){
        this._numberOfDepots = numberOfDepots;
        this._numberOfSalesmen = numberOfSalesmen;
        this._depots = depots;
    }
    @Override
    public void GoodMove() {
    }
    @Override
    public void BadMove() {

    }

    @Override
    public List<Depot> GetBestSolution() {
        return  _bestSolution;
    }
    @Override
    public void Print() {

    }

    protected void saveToBestSolution(int totalCost){
        _bestCost = totalCost;

        // Deep copy of best solution
        _bestSolution = new ArrayList<>();
        for (Depot depot : _depots) {
            Depot copy = new Depot(depot.GetDepotNumber(), _numberOfSalesmen);
            for (int i = 0; i < _numberOfSalesmen; i++) {
                copy.GetRoutes().set(i, new ArrayList<>(depot.GetRoutes().get(i)));
            }
            _bestSolution.add(copy);
        }
    }
}
