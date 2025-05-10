package yusufekremkecilioglu.Commands;

import yusufekremkecilioglu.Interfaces.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CommandInvoker {
    private Stack<Command> _commandHistory = new Stack<>();
    private Map<String, Integer> _commandCount = new HashMap<>();

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

    public void SuccessfulCommand(){
        String name = _commandHistory.getLast().GetName();
        if(_commandCount.containsKey(name)){
            _commandCount.put(name, _commandCount.get(name) + 1);
        }
        else{
            _commandCount.put(name, 1);
        }
    }
    public void PrintCommandCount(){
        // Create a StringBuilder to construct the output in the desired format
        StringBuilder sb = new StringBuilder("{\n");

        // Iterate over the commandCount map to format the output
        for (Map.Entry<String, Integer> entry : _commandCount.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(",\n");
        }

        // Remove the last comma and newline for clean output
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("\n}");

        // Print the final formatted result
        System.out.println(sb.toString());
    }
}
