package yusufekremkecilioglu.Strategies;

import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Interfaces.SolveStrategy;
import yusufekremkecilioglu.TurkishNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomSolveStrategy extends AbstractSolveStrategy {
    @Override
    public int Solve(int iteration, boolean returnBest) {
        int cost = 0;
        for (int iter = 0; iter < iteration; iter++){
            List<Integer> allCitiesList = new ArrayList<>();
            for (int i = 0; i < _totalCitiesCount; i++) {
                allCitiesList.add(i);
            }
            Collections.shuffle(allCitiesList, _rand);

            List<Integer> depotCities = allCitiesList.subList(0, _numberOfDepots);
            List<Integer> remainingCities = new ArrayList<>(allCitiesList.subList(_numberOfDepots, allCitiesList.size()));

            for (int i = 0; i < _numberOfDepots; i++) {
                _depots.get(i).SetDepotNumber(depotCities.get(i));
            }

            Collections.shuffle(remainingCities, _rand);
            int salesmanTotal = _numberOfDepots * _numberOfSalesmen;
            List<List<Integer>> allRoutes = new ArrayList<>();
            for (int i = 0; i < salesmanTotal; i++) {
                allRoutes.add(new ArrayList<>());
            }
            for (int i = 0; i < remainingCities.size(); i++) {
                allRoutes.get(i % salesmanTotal).add(remainingCities.get(i));
            }
            int routeIndex = 0;
            for (Depot depot : _depots) {
                for (int i = 0; i < _numberOfSalesmen; i++) {
                    depot.GetRoutes().set(i, allRoutes.get(routeIndex++));
                }
            }

            cost = calculateCost();

            if(cost < _bestCost){
                saveToBestSolution(cost);
            }
        }
        return returnBest? _bestCost : cost;
    }
}
