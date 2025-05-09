package yusufekremkecilioglu.Commands;

import yusufekremkecilioglu.Depot;
import yusufekremkecilioglu.Interfaces.Command;

public class SwapNodesInRouteCommand implements Command {

    private Depot _depot;
    private int _routeIndex;
    private int _nodeIndexFrom;
    private int _nodeIndexTo;

    @Override
    public void Execute() {

    }

    @Override
    public void Undo() {

    }
}
