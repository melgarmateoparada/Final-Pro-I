package org.proIII.appManejoImagenes.command;

import org.proIII.appManejoImagenes.image.ImagePixels;
import org.proIII.appManejoImagenes.linkedlist.Stack;

public class Invoker {
    private CommandExcecute command;
    private Stack<CommandExcecute> history = new Stack<>();
    public Invoker(){

    }
    public void excecute(CommandExcecute command){
        this.command = command;
        command.execute();
        history.push(command);

    }
    public void undo(){
            if (!history.isEmpty()){
                CommandExcecute command = history.pop();
                command.undo();
            }
    }

}
