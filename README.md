<br>
<p align="center">
<img src="http://img.benkeoghcgd.co.uk/Spigot/AxiusCoreHeader.png">
</p>
<br>
<p align="center">
    <img src="https://img.shields.io/github/issues/BenKeoghCGD/AxiusCore"/>
    <img src="https://img.shields.io/github/forks/BenKeoghCGD/AxiusCore"/>
    <img src="https://img.shields.io/github/stars/BenKeoghCGD/AxiusCore"/>
    <img src="https://img.shields.io/github/license/BenKeoghCGD/AxiusCore"/>
</p>
<p align="center">
    <img src="https://pluginbadges.glitch.me/api/v1/dl/Spigot-%23ff9100.svg?spigot=axiuscore.102852&style=flat"/>
</p>

# Welcome to AxiusCore
Hey! thanks for being interested in using AxiusCore. As of now, the core is in its early development, and therefore lacks
many of the intended launch features of the project.

## Features
- Data handling, with files stored as .yml files for easy configuration;
- Custom plugin framework, which breaks down the core functionality to 4 functions;
- Custom command handling / registration. Eliminating the need to add commands and permissions to the plugin.yml;
- GUI generation and manipulation classes;
- Player head auto-cache, used in unison with GUIs to provide next-level customisation;

## Installation
Navigate to the [AxiusCore spigot page](https://www.spigotmc.org/resources/axiuscore.102852/), or the [releases page on GitHub](https://github.com/BenKeoghCGD/AxiusCore/releases), and download the latest stable build of AxiusCore.
The core will be downloaded as a `.jar` file, and must be placed in the `plugins` folder of your server.

### Adding to IntelliJ
The plugin should be added to your projects libraries, by selecting `FILE > PROJECT STRUCTURE` and navigating to `LIBRARIES`.

From here, select the `+` at the top of the dialogue box, and locate your AxiusCore build.

### Adding to Eclipse
We don't use Eclipse unfortunately! Luckily for you, though, Spigot has a [wiki page](https://www.spigotmc.org/wiki/creating-a-blank-spigot-plugin-in-eclipse/) dedicated to the first steps of plugin development; the process is the same, add both the latest Spigot API build
along-side your download of AxiusCore.

### Prerequisites
Make sure you're using `Java 17` and load your server as usual. Check console for any errors before developing with the downloaded build.
If all steps have been followed correctly, your server should boot up normally; This is when you can begin development.

## Usage
Using AxiusCore was designed to be quick and efficient - but this is on the premise you have decent understanding of
Java as a language, as well as common programming practices, such as inheritance.

### Creating the Plugin
The first steps as normal would be to implement a plugin.yml, such as this:
```yaml
name: ExamplePlugin
main: uk.co.benkeoghcgd.api.ExamplePlugin.ExampleMain
version: 1.0.0
api-version: 1.18
author: You!
```

As far as the plugin.yml is concerned, this is LITERALLY all you need to add to begin development. Commands are added at run-time, and don't need to be declared here - the same with permissions.

Create a package leading to the main class, in this case `uk.co.benkeoghcgd.api.ExamplePlugin`, and create your main class within, in this case `ExampleMain` would be the class name.
Extend this blank class with `AxiusPlugin`, as such:

```java
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;

public class ExampleMain extends AxiusPlugin {
```

Your IDE should prompt you to implement the following 4 functions, which break up different sections of the plugins start-up and shut-down process to co-incide with the core's functionality. For example, it is a good practice to save data on plugin disable, but some data may be saved specifically to the core. In this case, you would save your data in the `Stop()` function, prior to the plugin de-registering from the core.

With the new classes implemented, your class should look something like this:
```java
package uk.co.benkeoghcgd.api.AxiusCore.Examples;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;

public class ExampleMain extends AxiusPlugin {
    @Override
    protected void Preregister() {
        // Runs BEFORE the plugin registers to the core
    }

    @Override
    protected void Postregister() {
        // Runs AFTER the plugin registers to the core
    }

    @Override
    protected void Stop() {
        // Runs BEFORE the plugin de-registers to the core
    }

    @Override
    protected void FullStop() {
        // Runs AFTER the plugin de-registers to the core
    }
}
```

### The Two MOST Important Functions
#### Preregister
The pre-register task runs before the plugin registers to the core. This section should be mainly used for dependencies, files, etc.
Basically, if it can break on launch, you want it to break here.

Example Pre-register (from PremiumJoinMessage):
```java
@Override
protected void Preregister() {
    Logging.Log(this, "Running plugin pre-registry tasks");
    if(core == null) errors.add(new MissingDependException("AxiusCore"));

    Logging.Log(this, "Registering Data Files");
    cyml = new ConfigYML(this);
    pyml = new PlayersYML(this);
    gyml = new GroupsYML(this);

    Logging.Log(this, "Collecting Commands");
    commands.add(new DefaultCommand(this));
}
```

#### Postregister
The post-register task runs after the plugin registers to the core, and should be used for defining variables that could otherwise be null, registering listeners, and calling the core to register commands.

Example Post-register (from PremiumJoinMessage):
```java
@Override
protected void Postregister() {
    guiIcon = GUI.createGuiItem(GUIAssets.DECORHEADS.get("arrowdown"), "§d§lPremiumJoinMessage");
    setFormattedName("&x&f&6&0&0&f&b&lP&x&f&1&0&8&f&b&lr&x&e&c&1&0&f&b&le&x&e&7&1&8&f&b&lm&x&e&2&2&0&f&b&li&x&d&d&2&9&f&c&lu&x&d&8&3&1&f&c&lm&x&d&3&3&9&f&c&lJ&x&c&e&4&1&f&c&lo&x&c&9&4&9&f&c&li&x&c&4&5&1&f&c&ln&x&b&f&5&9&f&c&lM&x&b&a&6&1&f&c&le&x&b&5&6&a&f&d&ls&x&b&0&7&2&f&d&ls&x&a&b&7&a&f&d&la&x&a&6&8&2&f&d&lg&x&a&1&8&a&f&d&le§7 ");

    Logging.Log(this, "Registering Commands");
    registerCommands();

    Logging.Log(this, "Registering Listeners");
    getServer().getPluginManager().registerEvents(new JoinLeaveListeners(this), this);
}
```

### Creating a Command
Creating a command through AxiusCore is simple, and easily broken down into 2 steps;
#### Creating the Class
Create a new sub-package called `Commands`, this is a common practice for myself, you do you. Create a class within - in this example, i will call it `ExampleCommand` - extending AxiusCommand.

```java
import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;

public class ExampleCommand extends AxiusPlugin {
```
As with before, your IDE will prompt you to implement the required functions. Compared to a normal CommandExecutor, AxiusCommand classes are identical, besides the addition of a constructor containing a super.

Here is an example of an AxiusCommand (from GUIHomes):
```java
public class homeCommand extends AxiusCommand {

    private HomesYML homesYML;

    public homeCommand(AxiusPlugin instance, HomesYML homeData) {
        // AxiusCommand(AxiusPlugin instance, boolean canConsoleRun, String Command,
        //              String description, String usage, String... aliases);
        super(instance, false,
                "home",
                "Command to view and teleport to homes",
                instance.getNameFormatted() + "§7 Incorrect Syntax: /home",
                "h", "homes");

        homesYML = homeData;
        setPermission("guihomes.home");
    }

    @Override
    public boolean onCommand(CommandSender sndr, Command command, String s, String[] args) {
        if(sndr instanceof ConsoleCommandSender) {
            sndr.sendMessage("§cThis command can only be run by players");
            return false;
        }

        List<String> homes = homesYML.getPlayerHomes((Player)sndr);

        if(homes.isEmpty()) {
            sndr.sendMessage(plugin.getNameFormatted() + "§7 You don't have any homes set.");
            return true;
        }

        HomesGUI gui = new HomesGUI(plugin, homesYML, (Player)sndr, GUIAssets.getInventorySize(homes.size()) / 9);
        gui.show((Player) sndr);

        return true;
    }
}
```
#### Adding to Collection and Register to Core
Once you have your command file created and filled, add your command to the `commands` collection, like so:
```java 
commands.add(new ExampleCommand());
```

And in your postregister function, in your main class:
```java
registerCommands();
```
### GUIs!
GUIs are a staple-point of most plugins, and a headache for all developers. Well... besides you now, obviously.
#### Creating the GUI class
Create a new class, preferably in a new sub-package called "GUIs", and make this new class extend the `GUI` class, and your IDE will prompt you to import the required functions:
```java
public class ExampleGUI extends GUI {
    public ExampleGUI(AxiusPlugin instance, int rows, String title) {
        super(instance, rows, title);
        
        // must include yourself, at the end:
        Populate();
    }

    @Override
    protected void Populate() {
        
    }

    @Override
    protected void onInvClick(InventoryClickEvent e) {

    }
}
```
In the constructor, you must specify the plugin, number of rows and the title of the window, and AT THE END you MUST populate the window.

Running the super generates a new Inventory type called `container` which can be called upon at any time as a normal inventory. Inside the populate function, you would call
```java
container.addItem(ItemStack... items);
```

#### Creating a GUI Item
Unless you have your own way of making items, or have a really specific need to generate an item a certain way (we've all been there), I have included an itemstack generation function found from StackOverflow at some point in the past which ive used for years. Its really numbed down, and great for first time users:
```java
public static ItemStack createGuiItem(final Material material, final String name, final String... lore);
```

#### Example GUI Class (GUIHomes)
```java
public class HomesGUI extends GUI {
    // Removed variables to conserve space

    public HomesGUI(AxiusPlugin plugin, HomesYML homesYML, Player sndr, int i) {
        super(plugin, i, plugin.getNameFormatted() + "§7 Your homes.");

        p = sndr;
        main = (GUIHomes) plugin;
        data = homesYML;
        Populate();
    }

    @Override
    protected void Populate() {
        data.refresh();
        for(String s : data.getPlayerHomes(p)) {
            String[] parts = s.split(";");
            Location loc = HomesYML.stringToLocation(parts[1]);
            Material mat = Material.BARRIER; // DEFAULT MATERIAL SHOULD ONLY SHOW ON ERROR
            
            // removed per-world material type to conserve space

            String[] loreLinesRaw = LocationToString(loc).split(", ");
            ItemStack itm = createGuiItem(mat, "§3§l" + parts[0].toUpperCase(), "§7" + loreLinesRaw[0], "§7" + loreLinesRaw[1], "§7" + loreLinesRaw[2], "", "§aLeft-Click to Teleport", "§cRight-Click to Delete");
            homes.put(itm, loc);
            container.addItem(itm);
        }
    }

    @Override
    protected void onInvClick(InventoryClickEvent inventoryClickEvent) {
        // removed inventory events to conserve space. this function works as
        // you'd expect. google how to run inventory events.
    }
}
```
#### Showing the player a GUI
This example below shows how to show a player a GUI, in the instance of a command:
```java
// inventory size calculated prior to super()
HomesGUI gui = new HomesGUI(plugin, homesYML, (Player)sndr, GUIAssets.getInventorySize(homes.size()) / 9);
// #show(Player player);
gui.show((Player) sndr);
```

#### GUI Assets
Included in the library are multiple GUI-related functions,
##### Head Generation
```java
public static ItemStack GenHead(String name);
public static ItemStack GenHead(Player p);
```
##### PlayerHead Cache
The following collections contain the playerheads of the players on the server, 
```java
public final static HashMap<Player, ItemStack> PLAYERHEADS = new HashMap<>();
public final static HashMap<ItemStack, Player> PLAYERHEADS_INV = new HashMap<>();
```
In all honesty - this isn't tested outside of AxiusCore itself, but should still work. If not;
create a new listener to listen to join and leave events, and register/deregister the player there;
```java
public static void registerPlayer(Player p);
public static void unregisterPlayer(Player p);
```

##### Decorative heads
the GUIAssets class contains a function for generating decorative heads, and stores them here:
```java
public final static HashMap<String, ItemStack> DECORHEADS = new HashMap<>();
```

```md
Decorative heads:
- question
- exclamation
- arrowright
- arrowleft
- arrowup
- arrowdown
```

##### Issues with GUIAssets
Any issues with the GUIAssets class, please make an issue request and i'll take a look as soon as I can.

### Creating a Data Handler
#### Creating the class
Create a new class, extending the DataHandler class. your IDE will prompt you to import the required functions, as such:
```java
public class ExampleDataHandler extends DataHandler {
    public ExampleDataHandler(JavaPlugin instance) {
        super(instance, "Example"); // File saves as Example.yml
    }

    @Override
    protected void saveDefaults() {
        
    }
}
```
#### Setting data
```java
public void setData(String Key, Object value); // Override defaults to true
public void setData(String key, Object value, boolean override);
```

When setting default data, say when you generate a new data file, always specify FALSE as the override. otherwise, your file will forever reset to default.

#### Getting data
##### Lists
```java
FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
var = fileConfig.getList(path);
```

##### Standard Types
```java
YourDataHandlerClass.data.get("path");
```
Returns an Object type, that can be cast to boolean, String, int, float, double, etc.

##### Anything else
```java
FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
var = fileConfig.getYourDataType(path);
```