package yusufekremkecilioglu;

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

    public void RandomSolve(int iteration) {
        //Önce her depoya bir şehir atanmalı 1-81 arası
        //Atanan şehir sayıları seçilebilecek şehir sayılarından çıkarılmalı
        //Daha sonra atanmayan şehirlerden ve tüm şehirleri dolaşacak şekilde depots sayısı ve number of salesman sayısına göre her depotun routeuna bölüştürmeli

        for (int iter = 0; iter < iteration; iter++) {
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

            int totalCost = 0;
            for (Depot depot : _depots) {
                int depotCity = depot.getDepotNumber();
                for (List<Integer> route : depot.getRoutes()) {
                    if (route.isEmpty()) continue;
                    int cost = 0;
                    // Depodan ilk şehre
                    cost += _distances[depotCity][route.get(0)];
                    // Şehirler arası
                    for (int i = 0; i < route.size() - 1; i++) {
                        cost += _distances[route.get(i)][route.get(i + 1)];
                    }
                    // Son şehirden tekrar depoya
                    cost += _distances[route.get(route.size() - 1)][depotCity];
                    totalCost += cost;
                }
            }

            // 3. En iyi çözümü sakla
            if (totalCost < _bestCost) {
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
            // TODO: distance hesaplaması ve en iyiyi saklama kısmı
        }

        printBestSolution();

    }

    private void printBestSolution() {
        System.out.println("Best total cost: " + _bestCost);
        for (Depot depot : _bestSolution) {
            System.out.println("Depot at: " + _allCities[depot.getDepotNumber()]);
            int i = 1;
            for (List<Integer> route : depot.getRoutes()) {
                System.out.print("  Salesman " + i++ + ": ");
                //System.out.print(_allCities[depot.getDepotNumber()] + " -> ");
                for (int city : route) {
                    System.out.print(_allCities[city] + " -> ");
                }
                //System.out.println(_allCities[depot.getDepotNumber()]);
                System.out.println();
            }
            System.out.println();
        }
    }

}
