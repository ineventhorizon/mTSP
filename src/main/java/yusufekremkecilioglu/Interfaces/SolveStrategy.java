package yusufekremkecilioglu.Interfaces;

import yusufekremkecilioglu.Depot;

import java.util.List;

public interface SolveStrategy {
    int Solve(int iteration, boolean returnBest);
    void ConfigureStrategy(int numberOfDepots, int numberOfSalesmen, List<Depot> depots);
    void GoodMove();
    void BadMove();
    List<Depot> GetBestSolution();
    void Print();
}
