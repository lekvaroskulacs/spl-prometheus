package Controller;

import Model.IO_Manager;
import View.AppFrame;
import View.AppPanel;

import java.io.File;
import java.util.ConcurrentModificationException;

/**
 * A Main osztálya
 */
public class Main {
    /**
     * A program belépési pontja
     * @param args a parancssori argumentumok
     */
    public static void main(String[] args) {

        String fileToRead = null;
        String fileToWrite = null;
        AppFrame frame;
        AppPanel panel;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-d" -> Controller.deterministic = true;
                case "-i" -> fileToRead = args[++i];
                case "-o" -> {
                    fileToWrite = args[++i];
                    Controller.filetoWrite = new File(fileToWrite);
                }
                case "-g" -> {
                    IO_Manager.speak = false;
                    Controller.getInstance().getFrame().setVisible(true);
                    try {
                        Controller.getInstance().run();
                    } catch (ConcurrentModificationException e) {
                        //hát ja, ezt majd meg kéne csinálni rendesen
                    }

                }
                default -> {
                    IO_Manager.write("wrong arguments");
                    return;
                }
            }
        }


        if (fileToRead != null) {
                readFile(fileToRead, fileToWrite);
        } else {
                readStd(fileToWrite);
        }




    }

    private static void readStd(String fileToWrite) {

        if (fileToWrite != null)
            IO_Manager.openFileWrite(fileToWrite);

        String line;
        while ( !(line = IO_Manager.readLine()).equals("quit") ) {
            processCommand(line, fileToWrite != null);
        }

        IO_Manager.endReading();
        IO_Manager.closeFile();

    }

    public static void readFile(String filename, String fileToWrite) {
        IO_Manager.openFileRead(filename);
        if (fileToWrite != null)
            IO_Manager.openFileWrite(fileToWrite);

        String line;
        while ( (line = IO_Manager.readLineFile()) != null) {
            processCommand(line, fileToWrite != null);
        }

        IO_Manager.closeFile();
    }

    private static void processCommand(String command, boolean toFile) {
        Controller controller = Controller.getInstance();
        String[] cmd = command.split(" ");

        for (String s : cmd) s = s.trim();

        switch (cmd[0]) {
            case "create" -> {
                if (cmd.length != 5 && cmd.length !=3)
                    IO_Manager.writeError("create requires 2 or 4 parameters", Controller.filetoWrite != null);
                else
                    if(cmd[2].equals("pump") || cmd[2].equals("cistern") || cmd[2].equals("spring")){
                        if(cmd.length !=5)
                            IO_Manager.writeError("creating nodes requires 4 parameters", Controller.filetoWrite != null);
                        else
                            controller.create(cmd[1], cmd[2], Integer.parseInt(cmd[3]), Integer.parseInt(cmd[4]));
                    }
                    else {
                        if (cmd.length != 3)
                            IO_Manager.writeError("creating pipes or players requires 2 parameters", Controller.filetoWrite != null);
                        else
                            controller.create(cmd[1], cmd[2], 0,0);

                    }
            }
            case "connect" -> {
                if (cmd.length != 3)
                    IO_Manager.writeError("connect requires 2 parameters", Controller.filetoWrite != null);
                else
                    controller.connect(cmd[1], cmd[2]);
            }
            case "move" -> {
                if (cmd.length != 3)
                    IO_Manager.writeError("move requires 2 parameters", Controller.filetoWrite != null);
                else
                    controller.move(cmd[1], cmd[2]);
            }
            case "pierce" -> {
                if (cmd.length != 2)
                    IO_Manager.writeError("pierce requires 1 parameter", Controller.filetoWrite != null);
                else
                    controller.pierce(cmd[1]);
            }
            case "glue" -> {
                if (cmd.length != 2)
                    IO_Manager.writeError("glue requires 1 parameter", Controller.filetoWrite != null);
                else
                    controller.glue(cmd[1]);
            }
            case "lubricate" -> {
                if (cmd.length != 2)
                    IO_Manager.writeError("lubricate requires 1 parameter", Controller.filetoWrite != null);
                else
                    controller.lubricate(cmd[1]);
            }
            case "repair" -> {
                if (cmd.length != 2)
                    IO_Manager.writeError("repair requires 1 parameter", Controller.filetoWrite != null);
                else
                    controller.repair(cmd[1]);
            }
            case "redirect" -> {
                if (cmd.length != 4)
                    IO_Manager.writeError("redirect requires 3 parameters", Controller.filetoWrite != null);
                else
                    controller.redirect(cmd[1], cmd[2], cmd[3]);
            }
            case "pickup" -> {
                if (cmd.length != 3)
                    IO_Manager.writeError("pickup requires 2 parameters", Controller.filetoWrite != null);
                else
                    controller.pickup(cmd[1], cmd[2]);
            }
            case "placedown" -> {
                if (cmd.length != 2)
                    IO_Manager.writeError("placedown requires 1 parameter", Controller.filetoWrite != null);
                else
                    controller.placedown(cmd[1]);
            }
            case "state" -> {
                if (cmd.length < 2)
                    IO_Manager.writeError("available commands are:\tstate get\tstate set", Controller.filetoWrite != null);
                else if (cmd[1].equals("get"))
                    if (cmd.length != 4)
                        IO_Manager.writeError("state get requires 2 parameters", Controller.filetoWrite != null);
                    else
                        controller.stateGet(cmd[2], cmd[3]);
                else if (cmd[1].equals("set"))
                    if (cmd.length != 5)
                        IO_Manager.writeError("state set requires 3 parameters", Controller.filetoWrite != null);
                    else
                        controller.stateSet(cmd[2], cmd[3], cmd[4]);
                else
                    IO_Manager.writeError("available commands are:\tstate get\tstate set", Controller.filetoWrite != null);
            }
            case "nextTurn" -> {
                if (cmd.length != 1)
                    IO_Manager.writeError("nextTurn requires no parameters", Controller.filetoWrite != null);
                else
                    controller.nextTurn();
            }
            case "generate" -> {
                if (cmd.length != 4)
                    IO_Manager.writeError("generate requires 3 parameters", Controller.filetoWrite != null);
                else
                    controller.generate(cmd[1], cmd[2], cmd[3]);
            }
            case "waterFlow" -> {
                if (cmd.length != 2)
                    IO_Manager.writeError("waterFlow requires no parameters", Controller.filetoWrite != null);
                else
                    controller.waterFlow(cmd[1]);
            }
            case "save" -> {
                if (cmd.length != 2)
                    IO_Manager.writeError("save requires 1 parameter", Controller.filetoWrite != null);
                else
                    controller.save(cmd[1]);
            }
            case "load" -> {
                if (cmd.length != 2)
                    IO_Manager.writeError("load requires 1 parameter", Controller.filetoWrite != null);
                else
                    controller.load(cmd[1]);
            }
            case "endGame" -> {
                if (cmd.length != 1)
                    IO_Manager.writeError("endGame requires no parameters", Controller.filetoWrite != null);
                else
                    controller.endGame();
            }
            case "startGame" -> {
                if (cmd.length != 1)
                    IO_Manager.writeError("startGame requires no parameters", Controller.filetoWrite != null);
                else
                    controller.startGame();
            }
        }
    }


}

