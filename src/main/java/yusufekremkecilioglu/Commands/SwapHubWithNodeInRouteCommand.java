package yusufekremkecilioglu.Commands;

import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Helper;
import yusufekremkecilioglu.Interfaces.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwapHubWithNodeInRouteCommand implements Command {
    private Depot _depot;
    private int _depotNumber; //Number of depot
    private int _routeIndex; //Which route
    private int _randomIndexFromRoute; //Random point in a selected route
    private List<Integer> _nodesBeforeSwap;
    public SwapHubWithNodeInRouteCommand(Depot depot){
        this._depot = depot;
        this._depotNumber = _depot.GetDepotNumber();
        this._routeIndex = Helper.GetRandomIndex(_depot.GetRoutes());
        this._randomIndexFromRoute = Helper.GetRandomIndex(_depot.GetRoute(_routeIndex));
        this._nodesBeforeSwap = new ArrayList<>(_depot.GetRoute(_routeIndex));
    }

    @Override
    public void Execute() {
        List<Integer> route = _depot.GetRoute(_routeIndex);
        _depot.SetDepotNumber(route.get(_randomIndexFromRoute));
        route.set(_randomIndexFromRoute, _depotNumber);
    }

    @Override
    public void Undo() {
        List<Integer> route = _depot.GetRoute(_routeIndex);
        _depot.SetDepotNumber(_depotNumber);
        route.set(_randomIndexFromRoute, _nodesBeforeSwap.get(_randomIndexFromRoute));
    }
}
