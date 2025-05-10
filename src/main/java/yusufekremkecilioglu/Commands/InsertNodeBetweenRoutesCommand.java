package yusufekremkecilioglu.Commands;

import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Helper;
import yusufekremkecilioglu.Interfaces.Command;

import java.util.List;

public class InsertNodeBetweenRoutesCommand implements Command {
    private Depot _depot;

    private int _routeIndexFrom;
    private int _routeIndexTo;

    private int _nodeIndexFrom;
    private int _nodeIndexTo;

    public InsertNodeBetweenRoutesCommand(Depot depot){
        this._depot = depot;

        //Choose 2 random routes
        int[] randomRouteIndexes = Helper.GetTwoDistinctRandomIndexes(_depot.GetRoutes());
        this._routeIndexFrom = randomRouteIndexes[0];
        this._routeIndexTo = randomRouteIndexes[1];

        //Choose 2 random nodes
        this._nodeIndexFrom = Helper.GetRandomIndex(_depot.GetRoute(_routeIndexFrom));
        this._nodeIndexTo = Helper.GetRandomIndex(_depot.GetRoute(_routeIndexTo));
    }

    @Override
    public void Execute() {
        List<Integer> routeFrom = _depot.GetRoute(_routeIndexFrom);
        List<Integer> routeTo = _depot.GetRoute(_routeIndexTo);

        if (routeFrom.size() <= 2) return;



        int node = routeFrom.get(_nodeIndexFrom);
        routeFrom.remove(_nodeIndexFrom);

        int insertIndex = _nodeIndexTo + 1;
        if (insertIndex > routeTo.size()) {
            insertIndex = routeTo.size(); // Prevent out-of-bounds insertion
        }

        routeTo.add(insertIndex, node);
    }

    @Override
    public void Undo() {
        List<Integer> routeFrom = _depot.GetRoute(_routeIndexFrom);
        List<Integer> routeTo = _depot.GetRoute(_routeIndexTo);

        int node;
        if (_nodeIndexTo + 1 < routeTo.size()) {
            node = routeTo.get(_nodeIndexTo + 1);
            routeTo.remove(_nodeIndexTo + 1);
        } else {
            // If _nodeIndexTo + 1 is out of bounds, set node to the last element
            node = routeTo.get(routeTo.size() - 1);
            routeTo.remove(routeTo.size() - 1);
        }

        routeFrom.add(_nodeIndexFrom, node);

    }

    @Override
    public String GetName() {
        return "insertNodeBetweenRoutes";
    }
}
