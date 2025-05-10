package yusufekremkecilioglu.Commands;

import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Helper;
import yusufekremkecilioglu.Interfaces.Command;

import java.util.ArrayList;
import java.util.List;

public class SwapNodesBetweenRoutesCommand implements Command {
    private Depot _depot;

    private int _routeIndexFrom;
    private int _routeIndexTo;

    private int _nodeIndexFrom;
    private int _nodeIndexTo;

    private List<Integer> _nodesBeforeSwapFrom;
    private List<Integer> _nodesBeforeSwapTo;

    public SwapNodesBetweenRoutesCommand(Depot depot){
        this._depot = depot;

        //Choose 2 random routes
        int[] randomRouteIndexes = Helper.GetTwoDistinctRandomIndexes(_depot.GetRoutes());
        this._routeIndexFrom = randomRouteIndexes[0];
        this._routeIndexTo = randomRouteIndexes[1];

        //Choose 2 random nodes
        this._nodeIndexFrom = Helper.GetRandomIndex(_depot.GetRoute(_routeIndexFrom));
        this._nodeIndexTo = Helper.GetRandomIndex(_depot.GetRoute(_routeIndexTo));

        //Set before nodes
        this._nodesBeforeSwapFrom = new ArrayList<>(_depot.GetRoute(_routeIndexFrom));
        this._nodesBeforeSwapTo = new ArrayList<>(_depot.GetRoute(_routeIndexTo));

    }


    @Override
    public void Execute() {
        List<Integer> routeFrom = _depot.GetRoute(_routeIndexFrom);
        List<Integer> routeTo = _depot.GetRoute(_routeIndexTo);
        int fromValue = routeFrom.get(_nodeIndexFrom);
        int toValue = routeTo.get(_nodeIndexTo);

        if (fromValue == toValue) return;

        routeFrom.set(_nodeIndexFrom, toValue);
        routeTo.set(_nodeIndexTo, fromValue);

        //System.out.println(_depot.GetName() +" in route " + _routeIndexFrom+1  +" ->" + _routeIndexTo + " to " + _nodeIndexFrom + _nodeIndexTo);

    }

    @Override
    public void Undo() {
        List<Integer> routeFrom = _depot.GetRoute(_routeIndexFrom);
        List<Integer> routeTo = _depot.GetRoute(_routeIndexTo);

        routeFrom.set(_nodeIndexFrom, _nodesBeforeSwapFrom.get(_nodeIndexFrom));
        routeTo.set(_nodeIndexTo, _nodesBeforeSwapTo.get(_nodeIndexTo));

    }
}
