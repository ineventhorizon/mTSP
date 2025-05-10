package yusufekremkecilioglu;

import yusufekremkecilioglu.Commands.CommandInvoker;
import yusufekremkecilioglu.Commands.SwapNodesInRouteCommand;
import yusufekremkecilioglu.Interfaces.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Solver {
    private String[] _allCities = TurkishNetwork.cities;
    private int[][] _distances = TurkishNetwork.distance;
    private int _totalCities = TurkishNetwork.cities.length;
    private Random _rand = new Random();

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

        _depots = new ArrayList<>();
        for (Depot depot : _bestSolution) {
            Depot copy = new Depot(depot.getDepotNumber(), _numberOfSalesmen);
            for (int i = 0; i < _numberOfSalesmen; i++) {
                copy.getRoutes().set(i, new ArrayList<>(depot.getRoutes().get(i)));
            }
            _depots.add(copy);
        }

        CommandInvoker invoker = new CommandInvoker();
        for(int i=0;i<iteration;i++){
            int moveType = _rand.nextInt(5);
            // Randomly choose a move type
            switch (moveType) {
                case 0:
                    Depot randDepot = getRandomDepot();
                    Command command = new SwapNodesInRouteCommand(randDepot);
                    invoker.ExecuteCommand(command);
                break;
                case 1:
                    randDepot = getRandomDepot();
                    command = new SwapNodesInRouteCommand(randDepot);
                    invoker.ExecuteCommand(command);
                break;
                case 2:
                    randDepot = getRandomDepot();
                    command = new SwapNodesInRouteCommand(randDepot);
                    invoker.ExecuteCommand(command);;
                break;
                case 3:
                    randDepot = getRandomDepot();
                    command = new SwapNodesInRouteCommand(randDepot);
                    invoker.ExecuteCommand(command);
                break;
                case 4:
                    randDepot = getRandomDepot();
                    command = new SwapNodesInRouteCommand(randDepot);
                    invoker.ExecuteCommand(command);
                break;
            }


            int cost = calculateCost();
            if(cost < _bestCost){
                saveToBestSolution(cost);
            }
            else {
                invoker.UndoLastCommand();
            }
        }

    }
    public void RandomSolve(int iteration) {
        //Önce her depoya bir şehir atanmalı 1-81 arası
        //Atanan şehir sayıları seçilebilecek şehir sayılarından çıkarılmalı
        //Daha sonra atanmayan şehirlerden ve tüm şehirleri dolaşacak şekilde depots sayısı ve number of salesman sayısına göre her depotun routeuna bölüştürmeli

        for (int iter = 0; iter < iteration; iter++) {
            //int prevCost = calculateCost();
            List<Integer> allCitiesList = new ArrayList<>();
            for (int i = 0; i < _totalCities; i++) {
                allCitiesList.add(i);
            }

            Collections.shuffle(allCitiesList, _rand);

            // 1. Depo şehirlerini seç
            List<Integer> depotCities = allCitiesList.subList(0, _numberOfDepots);
            List<Integer> remainingCities = new ArrayList<>(allCitiesList.subList(_numberOfDepots, allCitiesList.size()));

            // 2. Depo nesnelerine şehirleri ata
            for (int i = 0; i < _numberOfDepots; i++) {
                _depots.get(i).setDepotNumber(depotCities.get(i));
            }

            // 3. Kalan şehirleri satıcılara dağıt
            Collections.shuffle(remainingCities, _rand);
            int salesmanTotal = _numberOfDepots * _numberOfSalesmen;
            List<List<Integer>> allRoutes = new ArrayList<>();
            for (int i = 0; i < salesmanTotal; i++) {
                allRoutes.add(new ArrayList<>());
            }

            for (int i = 0; i < remainingCities.size(); i++) {
                allRoutes.get(i % salesmanTotal).add(remainingCities.get(i));
            }

            // 4. Satıcı rotalarını depolara dağıt
            int routeIndex = 0;
            for (Depot depot : _depots) {
                for (int i = 0; i < _numberOfSalesmen; i++) {
                    depot.getRoutes().set(i, allRoutes.get(routeIndex++));
                }
            }

            int cost = calculateCost();
            if(cost < _bestCost) saveToBestSolution(cost);

        }

    }
    private int calculateCost(){
        int totalCost = 0;
        for (Depot depot : _depots) {
            totalCost += depot.routeCost(_distances);
        }
        return totalCost;
    }
    private void saveToBestSolution(int totalCost){
        _bestCost = totalCost;

        // Deep copy of best solution
        _bestSolution = new ArrayList<>();
        for (Depot depot : _depots) {
            Depot copy = new Depot(depot.getDepotNumber(), _numberOfSalesmen);
            for (int i = 0; i < _numberOfSalesmen; i++) {
                copy.getRoutes().set(i, new ArrayList<>(depot.getRoutes().get(i)));
            }
            _bestSolution.add(copy);
        }
    }
    public void printBestSolution(boolean verbose) {
        int depotNumber = 0;
        for (Depot depot : _bestSolution) {
            depotNumber++;
            System.out.println("Depot" +depotNumber+": " + (verbose ? _allCities[depot.getDepotNumber()] : depot.getDepotNumber()));
            int i = 1;
            for (List<Integer> route : depot.getRoutes()) {
                System.out.print("  Route " + i++ + ": ");
                //System.out.print(_allCities[depot.getDepotNumber()] + " -> ");
                int j = 0;
                for (int city : route) {
                    j++;
                    System.out.print(verbose ? _allCities[city] : city);
                    if(j < route.size()) System.out.print(" , ");
                }
                //System.out.println(_allCities[depot.getDepotNumber()]);
                System.out.println();
            }
            System.out.println();

            //System.out.println(depot.getRoutes().get(0)  + " "+ depot.getRoutes().get(depot.getRoutes().size()-1) + " -> " + depot.getDepotNumber());
        }
        System.out.println("Total cost is " + _bestCost);
    }
    private Depot getRandomDepot(){
        int randomDepotIndex = Helper.GetRandomIndex(_depots);
        return _depots.get(randomDepotIndex);
    }

    //Şuanda _depots ve cost fonksiyonu değiştirilecek heuristic en iyi üstünden gitmesi gerekiyor



}
