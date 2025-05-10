package yusufekremkecilioglu.Commands;

import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Helper;
import yusufekremkecilioglu.Interfaces.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwapNodesInRouteCommand implements Command {

    private Depot _depot;
    private int _routeIndex;
    private int _nodeIndexFrom;
    private int _nodeIndexTo;
    private List<Integer> _nodesBeforeSwap;

    public SwapNodesInRouteCommand(Depot depot) {
        this._depot = depot;
        this._routeIndex = Helper.GetRandomIndex(_depot.GetRoutes());
        int[] randomIndexes  = Helper.GetTwoDistinctRandomIndexes(_depot.GetRoute(_routeIndex));
        this._nodeIndexFrom = randomIndexes[0];
        this._nodeIndexTo = randomIndexes[1];
        this._nodesBeforeSwap = new ArrayList<>(_depot.GetRoute(_routeIndex));
    }

    @Override
    public void Execute() {
        List<Integer> route = _depot.GetRoute(_routeIndex);
        Collections.swap(route, _nodeIndexFrom, _nodeIndexTo);
        //System.out.println(_depot.GetName() +" in route " + _routeIndex+1 +" Executed" + _nodeIndexFrom + " to " + _nodeIndexTo);
    }

    @Override
    public void Undo() {
        List<Integer> route = _depot.GetRoute(_routeIndex);
        route.set(_nodeIndexFrom, _nodesBeforeSwap.get(_nodeIndexFrom));
        route.set(_nodeIndexTo, _nodesBeforeSwap.get(_nodeIndexTo));
        //System.out.println("Undo-ed" + _nodeIndexFrom + " to " + _nodeIndexTo);
    }
}
