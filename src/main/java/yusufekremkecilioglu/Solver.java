package yusufekremkecilioglu;

import org.json.JSONArray;
import org.json.JSONObject;
import yusufekremkecilioglu.Commands.*;
import yusufekremkecilioglu.Interfaces.Command;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Solver {
    private String[] _allCities = TurkishNetwork.cities;
    private int[][] _distances = TurkishNetwork.distance;
    private int _totalCities = TurkishNetwork.cities.length;
    private static final Random _rand = new Random();
    private static final CommandInvoker _invoker = new CommandInvoker();

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
    public void HeuristicSolve(int iteration){
        copyBestToDepots();
        for(int i=0;i<iteration;i++){
            int moveType = _rand.nextInt(5);
            Depot randDepot = getRandomDepot(); // Hepsinde ortaksa dışarı alabilirsin
            Command command = null;

            switch (moveType) {
                case 0:
                    command = new InsertNodeBetweenRoutesCommand(randDepot);
                break;
                case 1:
                    command = new InsertNodeInRouteCommand(randDepot);
                break;
                case 2:
                    command = new SwapHubWithNodeInRouteCommand(randDepot);
                break;
                case 3:
                    command = new SwapNodesBetweenRoutesCommand(randDepot);
                break;
                case 4:
                    command = new SwapNodesInRouteCommand(randDepot);
                break;
            }

            _invoker.ExecuteCommand(command);
            int cost = calculateCost();

            if(cost < _bestCost){
                saveToBestSolution(cost);
                _invoker.SuccessfulCommand();
            }
            else {
                _invoker.UndoLastCommand();
            }
        }
    }
    public void RandomSolve(int iteration) {
        for (int iter = 0; iter < iteration; iter++) {
            List<Integer> allCitiesList = new ArrayList<>();
            for (int i = 0; i < _totalCities; i++) {
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

            int cost = calculateCost();
            if(cost < _bestCost) saveToBestSolution(cost);

        }
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
    }
    public void PrintMoveCount(){
        _invoker.PrintCommandCount();
    }


    private int calculateCost(){
        int totalCost = 0;
        for (Depot depot : _depots) {
            //System.out.println("Calculating" +depot.GetName() + "");
            totalCost += depot.RouteCost(_distances);
        }
        return totalCost;
    }
    private void saveToBestSolution(int totalCost){
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
    private Depot getRandomDepot(){
        int randomDepotIndex = Helper.GetRandomIndex(_depots);
        return _depots.get(randomDepotIndex);
    }
    private void copyBestToDepots(){
        _depots = new ArrayList<>();
        for (Depot depot : _bestSolution) {
            Depot copy = new Depot(depot.GetDepotNumber(), _numberOfSalesmen);
            for (int i = 0; i < _numberOfSalesmen; i++) {
                copy.GetRoutes().set(i, new ArrayList<>(depot.GetRoutes().get(i)));
            }
            _depots.add(copy);
        }
    }
    public static void SaveSolutionAsJson(List<Depot> bestSolution, double bestCost, boolean verbose, String[] allCities, int depotCount, int salesmenCount) throws IOException {
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

        // Write to file
        try (FileWriter file = new FileWriter("solution_d"+depotCount+"s"+salesmenCount+".json")) {
            file.write(json.toString());
        }

        System.out.println("Formatted solution saved. Total cost is: " + bestCost);
    }

    public void WriteToJson(int d, int s, boolean verbose) {
        try {
            SaveSolutionAsJson(_bestSolution, _bestCost, verbose, _allCities, d, s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
