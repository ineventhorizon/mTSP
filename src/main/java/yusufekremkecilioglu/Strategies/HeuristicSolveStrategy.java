package yusufekremkecilioglu.Strategies;

import yusufekremkecilioglu.Commands.*;
import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Helper;
import yusufekremkecilioglu.Interfaces.Command;
import yusufekremkecilioglu.Interfaces.SolveStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class HeuristicSolveStrategy extends AbstractSolveStrategy {
    private static final CommandInvoker _invoker = new CommandInvoker();

    Map<String, Function<Depot, Command>> _commandMap = Map.of(
            "InsertBetween", depot -> new InsertNodeBetweenRoutesCommand(depot),
            "InsertInRoute", depot -> new InsertNodeInRouteCommand(depot),
            "SwapHub", depot -> new SwapHubWithNodeInRouteCommand(depot),
            "SwapBetween", depot -> new SwapNodesBetweenRoutesCommand(depot),
            "SwapInRoute", depot -> new SwapNodesInRouteCommand(depot)
    );


    @Override
    public int Solve(int iteration, boolean returnBest) {
        int cost = 0;
        for(int i=0;i<iteration;i++){
            Depot randDepot = getRandomDepot();
            List<String> keys = new ArrayList<>(_commandMap.keySet());
            String randomKey = keys.get(_rand.nextInt(keys.size()));
            Command command = _commandMap.get(randomKey).apply(randDepot);

            _invoker.ExecuteCommand(command);
            cost = calculateCost();
            if(cost < _bestCost){
                saveToBestSolution(cost);
                GoodMove();
            }
            else {
                BadMove();
            }
        }
        return returnBest? _bestCost : cost;
    }

    private Depot getRandomDepot(){
        int randomDepotIndex = Helper.GetRandomIndex(_depots);
        return _depots.get(randomDepotIndex);
    }

    @Override
    public void GoodMove(){
        _invoker.SuccessfulCommand();
    }
    @Override
    public void BadMove(){
        _invoker.UndoLastCommand();
    }

    @Override
    public void Print(){
        _invoker.PrintCommandCount();
    }


}
