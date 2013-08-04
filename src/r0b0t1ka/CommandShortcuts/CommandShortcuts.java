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
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
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
                writer.println("      permission-message: You don't have <permission>!");
            }
            writer.println("permissions:");
            for (int counter = 0; counter < cmdName.length; counter++)
            {
                writer.println("  CommandShortcuts." + cmdPerm[counter] + ":");
                writer.println("    description: " + cmdDesc[counter]);
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
        ArrayList<String> permA = new ArrayList<>(); // Permission for actual command
        while(in.hasNextLine())
        {
            String cmds = in.nextLine();
            int index = cmds.indexOf("|");
            String cmdN = cmds.substring(0, index);
            commandS.add(cmdN);
            cmds = cmds.substring(index + 1);
            index = cmds.indexOf("|");
            String cmdActual = cmds.substring(0, index);
            permA.add(permissionAnalyzer(cmdActual));
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
        permA.trimToSize();
        for (int counter = 0; counter < commandU.size(); counter++)
        {
            int index = commandU.get(counter).indexOf("<");
            if (!(index == -1))
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
                    if (player.isOp() && playerT.get(counter))
                    {
                        player.performCommand(commandA.get(counter));
                        command = true;
                    }
                    else if (player.hasPermission(perm.get(counter)) && playerT.get(counter) && player.hasPermission(permA.get(counter)))
                    {
                        player.performCommand(commandA.get(counter));
                        command = true;
                    }
                    else if (!(playerT.get(counter)))
                    {
                        sender.sendMessage("This command is for the console only");
                        command = false;
                    }
                    else if (player.hasPermission(perm.get(counter)) && !player.hasPermission(permA.get(counter)))
                    {
                        sender.sendMessage("You don't have permission to use the original command.");
                        command = false;
                    }
                    else if (!player.hasPermission(perm.get(counter)) && !player.hasPermission(permA.get(counter)))
                    {
                        sender.sendMessage("You don't have permission to use the original command and/or this command shortcut:");
                        command = false;
                    }
                    else if (!player.hasPermission(perm.get(counter)) && player.hasPermission(permA.get(counter)))
                    {
                        sender.sendMessage("You don't have permission to use this command shortcut.");
                        command = false;
                    }
                }
            }
        }
        in.close();
        return command;
    }
    private String permissionAnalyzer(String cmdActual)
    {
        String permissionz = cmdActual;
        String permissionString = "";
        int index = permissionz.indexOf("<");
        Plugin[] pl = Bukkit.getServer().getPluginManager().getPlugins();
        ArrayList<String> permissionOverLayList = new ArrayList<>(pl.length);
        ArrayList<String> commandOverLayList = new ArrayList<>(pl.length);
        SimpleCommandMap commandMap = new SimpleCommandMap(getServer());
        Collection<Command> cmdCollection = commandMap.getCommands();
        Object[] commandArray = cmdCollection.toArray();
        for (int counter = 0; counter < pl.length; counter++)
        {
            List permissionUnderLayList = pl[counter].getDescription().getPermissions();
            if (!permissionUnderLayList.isEmpty())
            {
                String[] permissionBeta = new String[permissionUnderLayList.size()];
                for (int bounter = 0; bounter < permissionBeta.length; bounter++)
                {
                    Permission permeator = (Permission)permissionUnderLayList.get(bounter);
                    String permissionAlpha = permeator.getName();
                    permissionOverLayList.add(permissionAlpha);
                }
            }
        }
        permissionOverLayList.trimToSize();
        if (index == -1)
        {
            if (permissionz.equals("version") || permissionz.equals("plugins") || permissionz.equals("reload") || permissionz.equals("timings") || permissionz.equals("kill") || permissionz.equals("me") || permissionz.equals("banlist") || permissionz.equals("help") || permissionz.equals("?") || permissionz.equals("banlist") || permissionz.equals("stop") || permissionz.equals("save-all") || permissionz.equals("save-off") || permissionz.equals("save-on") || permissionz.equals("list") || permissionz.equals("whitelist list") || permissionz.equals("whitelist reload") || permissionz.equals("toggledownfall") || permissionz.equals("seed") || permissionz.equals("gamerule"))
            {
                permissionString = "bukkit.command.";
                switch (permissionz) {
                    case "version":
                        permissionString += "version";
                        break;
                    case "plugins":
                        permissionString += "plugins";
                        break;
                    case "reload":
                        permissionString += "reload";
                        break;
                    case "timings":
                        permissionString += "timings";
                        break;
                    case "kill":
                        permissionString += "kill";
                        break;
                    case "me":
                        permissionString += "me";
                        break;
                    case "banlist":
                        permissionString += "ban.list";
                        break;
                    case "stop":
                        permissionString += "stop";
                        break;
                    case "save-all":
                        permissionString += "save.perform";
                        break;
                    case "save-off":
                        permissionString += "save.disable";
                        break;
                    case "save-on":
                        permissionString += "save.enable";
                        break;
                    case "list":
                        permissionString += "list";
                        break;
                    case "whitelist list":
                        permissionString += "whitelist.list";
                        break;
                    case "whitelist reload":
                        permissionString += "whitelist.reload";
                        break;
                    case "toggledownfall":
                        permissionString += "toggledownfall";
                        break;
                    case "seed":
                        permissionString += "seed";
                        break;
                    case "gamerule":
                        permissionString += "gamerule";
                        break;
                }
            }
            else
            {
                for(int counter = 0; counter < commandArray.length; counter++)
                {
                    if(permissionz.equals(commandArray[counter]))
                        permissionString = permissionOverLayList.get(counter);
                }
            }
        }
        else 
        {
            permissionz = permissionz.substring(0, index);
            if (permissionz.equals("tell") || permissionz.equals("kick") || permissionz.equals("ban") || permissionz.equals("pardon") || permissionz.equals("ban-ip") || permissionz.equals("pardon-ip") || permissionz.equals("op") || permissionz.equals("deop") || permissionz.equals("tp") || permissionz.equals("give") || permissionz.equals("say") || permissionz.equals("whitelist") || permissionz.equals("time") || permissionz.equals("gamemode") || permissionz.equals("defaultgamemode") || permissionz.equals("enchant") || permissionz.equals("weather") || permissionz.equals("clear") || permissionz.equals("difficulty") || permissionz.equals("spawnpoint") || permissionz.equals("effect"))
            {
                permissionString = "bukkit.command.";
                switch (permissionz) {
                    case "tell":
                        permissionString += "tell";
                        break;
                    case "kick":
                        permissionString += "kick";
                        break;
                    case "ban":
                        permissionString += "ban.player";
                        break;
                    case "pardon":
                        permissionString += "unban.player";
                        break;
                    case "ban-ip":
                        permissionString += "ban.ip";
                        break;
                    case "pardon-ip":
                        permissionString += "unban.ip";
                        break;
                    case "op":
                        permissionString += "op.give";
                        break;
                    case "deop":
                        permissionString += "op.take";
                        break;
                    case "tp":
                        permissionString += ".teleport";
                        break;
                    case "give":
                        permissionString += ".give";
                        break;
                    case "say":
                        permissionString += ".say";
                        break;
                    case "whitelist":
                        permissionString += ".whitelist";
                        break;
                    case "time":
                        permissionString += ".time";
                        break;
                    case "gamemode":
                        permissionString += ".gamemode";
                        break;
                    case "xp":
                        permissionString += ".xp";
                        break;
                    case "defaultgamemode":
                        permissionString += ".defaultgamemode";
                        break;
                    case "enchant":
                        permissionString += ".enchant";
                        break;
                    case "weather":
                        permissionString += ".weather";
                        break;
                    case "clear":
                        permissionString += ".clear";
                        break;
                    case "difficulty":
                        permissionString += ".difficulty";
                        break;
                    case "spawnpoint":
                        permissionString += ".spawnpoint";
                        break;
                    case "effect":
                        permissionString += ".effect";
                        break;
                }
            }
            else
            {
                for (int counter = 0; counter < commandArray.length; counter++)
                {
                    if(permissionz.equals(commandArray[counter]))
                        permissionString = permissionOverLayList.get(counter);
                }
            }
        }
        return permissionString;
    }
}
