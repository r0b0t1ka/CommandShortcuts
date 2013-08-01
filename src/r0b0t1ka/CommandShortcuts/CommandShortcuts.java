/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package r0b0t1ka.CommandShortcuts;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.*;
import org.bukkit.Bukkit;
/**
 *
 * @author r0b0t1ka
 */
public class CommandShortcuts extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getLogger().info("Command shortcuts has been enabled!");
        try {
            writethem();
        } catch (IOException ex) {
            Logger.getLogger(CommandShortcuts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void onDisable()
    {
        getLogger().info("Command shortcuts has been disabled!");
    }
    @SuppressWarnings("empty-statement")
    public void writethem() throws IOException
    {
        File file = new File("wtf.txt");
        String filepath = file.getCanonicalPath();
        filepath = filepath.replace("\\wtf.txt", "");
        boolean dirMake = new File(filepath + "\\plugins\\CommandShortcuts").mkdir();
        if (!dirMake)
        {
            getLogger().info("Plugin folder already exits or couldn't be created");
        }
        if (dirMake)
        {
            getLogger().info("Plugin folder successfully created!");
            dirMake = new File(filepath + "\\plugins\\CommandShortcuts\\commands.yml").createNewFile();
            if (!dirMake)
                getLogger().info("OK, something is wrong, CommandShortcuts couldn't create commands.yml");
            dirMake = new File(filepath + "\\plugins\\CommandShortcuts\\plugin.yml").createNewFile();
            if (!dirMake)
                getLogger().info("OK, something is wrong, CommandShortcuts couldn't create plugin.yml");
            try (PrintWriter writing = new PrintWriter(new File (filepath + "\\plugins\\CommandShortcuts\\commands.yml"))) {
                writing.println("s|save-all|s|console|player|");
            }
        }
        Scanner in;
        try (PrintWriter writer = new PrintWriter(new File(filepath + "\\plugins\\CommandShortcuts\\plugin.yml"))) {
            writer.println("name: CommandShortcuts");
            writer.println("main: r0b0t1ka.CommandShortcuts.CommandShortcuts");
            writer.println("version: 1.2");
            writer.println("author: r0b0t1ka");
            writer.println("commands:");
            ArrayList<String> commands = new ArrayList<>();
            in = new Scanner(new File(filepath + "\\plugins\\CommandShortcuts\\commands.yml"));
            while(in.hasNextLine())
            {
                String fileIn = in.nextLine();
                commands.add(fileIn);
            }
            commands.trimToSize();
            String[] cmds = new String[commands.size()];
            String[] cmdName = new String[commands.size()];
            String[] cmdDesc = new String[commands.size()];
            String[] cmdUsag = new String[commands.size()];
            String[] cmdPerm = new String[commands.size()];
            String[] cmdPerM = new String[commands.size()];
            for (int counter = 0; counter < cmds.length; counter++)
            {
                cmds[counter] = commands.get(counter);
            }
            for (int counter = 0; counter < cmds.length; counter++)
            {
                int index = cmds[counter].indexOf("|");
                cmdName[counter] = cmds[counter].substring(0, index);
                cmds[counter] = cmds[counter].substring(index + 1);
                index = cmds[counter].indexOf("|");
                cmdDesc[counter] = cmds[counter].substring(0, index);
                cmds[counter] = cmds[counter].substring((index) + 1);
                index = cmds[counter].indexOf("|");
                cmdUsag[counter] = ("/" + cmds[counter].substring(0, index));
                cmds[counter] = cmds[counter].substring(index + 1);
                cmdPerm[counter] = ("CommandShortcuts." + cmdName[counter]);
                cmdPerM[counter] = ("You don't have <permission>!");
            }
            for (int counter = 0; counter < cmdName.length; counter++)
            {
                writer.println("   " + cmdName[counter] + ":");
                writer.println("      description: " + cmdDesc[counter]);
                writer.println("      usage: " + cmdUsag[counter]);
                writer.println("      permission: " + cmdPerm[counter]);
                writer.println("      permission-message: " + cmdPerM[counter]);
            }
            getLogger().info("CommandShortcuts has successfully written the plugin.yml file!");
            getLogger().info("Place plugin.yml in jar file if it has been newly generated.");
        }
        in.close();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        File file = new File("wtf.txt");
        String filepath = null;
        try {
            filepath = file.getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(CommandShortcuts.class.getName()).log(Level.SEVERE, null, ex);
        }
        filepath = filepath.replace("\\wtf.txt", "");        
        Scanner in = null;
        try {
            in = new Scanner(new File(filepath + "\\plugins\\CommandShortcuts\\commands.yml"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CommandShortcuts.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean command = false;
        ArrayList<String> commandS = new ArrayList<>();//Command shortcut name
        ArrayList<String> commandA = new ArrayList<>();//Command actual name
        ArrayList<Boolean> consoleT = new ArrayList<>();//Console truth
        ArrayList<Boolean> playerT = new ArrayList<>();//Player truth
        ArrayList<String> perm = new ArrayList<>();//Permissions CommandShortcuts.blahblah
        ArrayList<String> commandU = new ArrayList<>();//Command usage
        ArrayList<String> commandP = new ArrayList<>();//Command parameters contains values if paramaters specified in usage
        ArrayList<Boolean> commandPP = new ArrayList<>();//Command parameters present truth
        while(in.hasNextLine())
        {
            String cmds = in.nextLine();
            int index = cmds.indexOf("|");
            String cmdN = cmds.substring(0, index);
            commandS.add(cmdN);
            cmds = cmds.substring(index + 1);
            index = cmds.indexOf("|");
            String cmdActual = cmds.substring(0, index);
            commandA.add(cmdActual);
            cmds = cmds.substring(index + 1);
            perm.add("CommandShortcuts." + cmdN);
            index = cmds.indexOf("|");
            cmds = cmds.substring(1 + index);
            String cmdUsage = cmds.substring(0, index);
            commandU.add(cmdUsage);
            index = cmds.indexOf("|");
            String consoletruth = cmds.substring(0, index);
            if (consoletruth.equals("console"))
                consoleT.add(true);
            cmds = cmds.substring(1 + index);
            index = cmds.indexOf("|");
            String playertruth = cmds.substring(0, index);
            if (playertruth.equals("player"))
                playerT.add(true);
        }
        commandS.trimToSize();
        commandA.trimToSize();
        perm.trimToSize();
        consoleT.trimToSize();
        playerT.trimToSize();
        commandU.trimToSize();
        for (int counter = 0; counter < commandU.size(); counter++)
        {
            int index = commandU.get(counter).indexOf("<");
            if (!(index == -1) && commandU.get(counter).substring(0, index).equals(commandS.get(counter)))
            {
                String cmditself = commandU.get(counter);
                String param = commandU.get(counter);
                cmditself = cmditself.replaceAll(" ", "");
                index = cmditself.indexOf("<");
                cmditself = cmditself.substring(0, index);
                index = param.indexOf("<");
                param = param.substring(index);
                commandP.add(param);
                commandU.set(counter, cmditself);
                commandPP.add(true);
                if (!commandU.get(counter).equals(commandS.get(counter)))
                    getLogger().info("CommandShortcuts has failed to properly set parameters :/");
            }
            else
            {
                commandPP.add(false);
            }
        }
        commandPP.trimToSize();
        commandP.trimToSize();
        for (int counter = 0; counter < commandS.size(); counter++)
        {
            if (cmd.getName().equalsIgnoreCase(commandS.get(counter)))
            {
                if (!(sender instanceof Player) && consoleT.get(counter))
                {
                    boolean swag = getServer().dispatchCommand(sender, commandA.get(counter));
                    command = true;
                }
                else if (!(sender instanceof Player) && !(consoleT.get(counter)))
                {
                    sender.sendMessage("This command is for players only -_-");
                    command = false;
                }
                else
                {
                    Player player = (Player) sender;
                    if (player.hasPermission(perm.get(counter)) && playerT.get(counter))
                    {
                            player.performCommand(commandA.get(counter));
                            command = true;
                    }
                    else if (player.hasPermission(perm.get(counter)) && !(playerT.get(counter)))
                    {
                        sender.sendMessage("This command is for the console only");
                        command = false;
                    }
                    else if (!player.hasPermission(perm.get(counter)) && !(playerT.get(counter)))
                    {
                        sender.sendMessage("You don't have permission to use this and it's for the console only.");
                        command = false;
                    }
                    else if (!player.hasPermission(perm.get(counter)) && !(playerT.get(counter)))
                    {
                        sender.sendMessage("You don't have permission to use this command.");
                        command = false;
                    }
                }
            }
        }
        in.close();
        return command;
    }    
}
