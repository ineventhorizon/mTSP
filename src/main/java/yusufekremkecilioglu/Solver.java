package yusufekremkecilioglu;

import yusufekremkecilioglu.Commands.*;
import yusufekremkecilioglu.Interfaces.Command;
import yusufekremkecilioglu.Strategies.AbstractSolveStrategy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Solver {
    private String[] _allCities = TurkishNetwork.cities;
    private AbstractSolveStrategy _currentStrategy;

    private List<Depot> _depots;

    private int _bestCost = Integer.MAX_VALUE;
    private List<Depot> _bestSolution = null;


    private int _numberOfDepots;
    private int _numberOfSalesmen;

    public Solver(int d, int s){
        this._numberOfDepots = d;
        this._numberOfSalesmen = s;

        _depots = new ArrayList<>();

        for (int i = 0; i < _numberOfDepots; i++) {
            Depot depot = new Depot(-1, _numberOfSalesmen);
            _depots.add(depot);
        }
    }
    public void SetStrategy(AbstractSolveStrategy strategy){
        this._currentStrategy = strategy;
        _currentStrategy.ConfigureStrategy(_numberOfDepots, _numberOfSalesmen, _depots);
    }
    public void Solve(int iteration){
        _bestCost = _currentStrategy.Solve(iteration, true);
        _bestSolution = _currentStrategy.GetBestSolution();
    }
    public void PrintBestSolution(boolean verbose) {
        int depotNumber = 0;
        for (Depot depot : _bestSolution) {
            depotNumber++;
            System.out.println("Depot" +depotNumber+": " + (verbose ? _allCities[depot.GetDepotNumber()] : depot.GetDepotNumber()));
            int i = 1;
            for (List<Integer> route : depot.GetRoutes()) {
                System.out.print("  Route " + i++ + ": ");
                int j = 0;
                for (int city : route) {
                    j++;
                    System.out.print(verbose ? _allCities[city] : city);
                    if(j < route.size()) System.out.print(" , ");
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("Total cost is " + _bestCost);
        _currentStrategy.Print();
    }
    public void WriteToJson(int d, int s, boolean verbose) {
        try {
            saveSolutionAsJson(_bestSolution,verbose, _allCities, d, s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveSolutionAsJson(List<Depot> bestSolution,boolean verbose, String[] allCities, int depotCount, int salesmenCount) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"solution\": [\n");

        for (int d = 0; d < bestSolution.size(); d++) {
            Depot depot = bestSolution.get(d);
            json.append("    {\n");
            json.append("      \"depot\": \"").append(verbose ? allCities[depot.GetDepotNumber()] : depot.GetDepotNumber()).append("\",\n");
            json.append("      \"routes\": [\n");

            List<List<Integer>> routes = depot.GetRoutes();
            for (int r = 0; r < routes.size(); r++) {
                List<Integer> route = routes.get(r);
                String routeStr = route.stream()
                        .map(city -> verbose ? allCities[city] : String.valueOf(city))
                        .collect(Collectors.joining(" "));
                json.append("        \"").append(routeStr).append("\"");
                if (r < routes.size() - 1) json.append(",");
                json.append("\n");
            }

            json.append("      ]\n");
            json.append("    }");
            if (d < bestSolution.size() - 1) json.append(",");
            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}");

        try (FileWriter file = new FileWriter("solution_d"+depotCount+"s"+salesmenCount+".json")) {
            file.write(json.toString());
        }

        System.out.println("Formatted solution saved");
    }

}
