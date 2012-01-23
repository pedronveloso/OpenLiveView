package com.pedronveloso.openliveview.Utils;

public class CommandResult {

    private int commandID;
    private int commandSuccessCode;

    public CommandResult(int commandSuccessCode, int commandID) {
        this.commandSuccessCode = commandSuccessCode;
        this.commandID = commandID;
    }

    public int getCommandID() {
        return commandID;
    }

    public int getCommandSuccessCode() {
        return commandSuccessCode;
    }
}
