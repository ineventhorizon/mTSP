package yusufekremkecilioglu.Commands;

import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Helper;
import yusufekremkecilioglu.Interfaces.Command;

import java.util.ArrayList;
import java.util.List;

public class InsertNodeInRouteCommand implements Command {
    private Depot _depot;
    private int _routeIndex;
    private int _nodeIndexFrom;
    private int _nodeIndexTo;
    private List<Integer> _nodesBeforeSwap;

    public InsertNodeInRouteCommand(Depot depot){
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

        if (route.size() <= 2) return;

        int node = route.get(_nodeIndexFrom);
        route.remove(_nodeIndexFrom);
        int insertIndex = _nodeIndexTo;
        if (_nodeIndexFrom < _nodeIndexTo) {
            insertIndex--; // Çünkü bir eleman silindi, hedef sola kaydı
        }
        route.add(insertIndex + 1, node);

    }

    @Override
    public void Undo() {
        List<Integer> route = _depot.GetRoute(_routeIndex);
        route.clear();
        route.addAll(_nodesBeforeSwap);
    }
}
