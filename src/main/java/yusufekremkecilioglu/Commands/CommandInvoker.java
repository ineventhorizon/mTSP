package yusufekremkecilioglu.Commands;

import yusufekremkecilioglu.Interfaces.Command;

import java.util.Stack;

public class CommandInvoker {
    private Stack<Command> _commandHistory = new Stack<>();

    public void ExecuteCommand(Command command) {
        command.Execute();
        _commandHistory.push(command);
    }
    public void UndoLastCommand() {
        if (!_commandHistory.isEmpty()) {
            Command lastCommand = _commandHistory.pop();
            lastCommand.Undo();
        }
    }
}
