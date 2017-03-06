# Neocore API
This is the public API for the Neocore Minecraft server management library
created by Treyzania for Bapcraft.  The actual underlying implementation of
this API and its support features (the MongoDB database controller we use on
our server) are currently private.  There is an alternative JSON database
controller for testing purposes.  This may change in the future.

**The whole API is in a state of flux at the moment, so please don't expect
any code you write against this project to be binary-compatible for a while.**

# How to *use*
There are two different ways one can access the API.  First, one can simply
use it as a way to interact the the core libraries from a conventional server
plugin, be it Bukkit, Spigot, BungeeCord, Sponge, or any other.  Or one could
choose to create what's called a "micromodule", which is a (typically) small
pseudo-plugin that is initialized from within a running instance of the main
Neocore-Common library.

In order to register services, you need to have a *module*.  A module is any
object that implements the `Module` interface.  If you're creating a
micromodule in Java, you should extend the `JavaMicromodule` class.  And make
sure that the jar you're compiling into has a `micromodule.conf` file in its
root, and place it in the micromodules folder wherever it's supposed to be in
your environment.

Examples:

    // my/micromodule/package/MyMicromodule.java
    package my.micromodule.package;
    public class MyMicromodule extends JavaMicromodule {
        // your code
    }

...as well as...

    # micromodule.conf
    module {
        name="MyMicromodule"
        main="my.micromodule.pacakage.MyMicromodule"
        version="1.0.0"
    }

If you want to have some kind of automatic configuration, then things are a
little more different.  Since micromodules aren't supposed to be things that
do a *ton* of things, no support is currently available to automatically
place configs into relevant locations.  There *is* however a method called
`Micromodule:configure(Config)` that gets passed a HOCON config object before
any calls to `onEnable` are made.  This object can be null if no config for
your micromodule is found.  The names of these configs are the same as the name
of your micromodule with ".conf" appended to the end.  So if your micromodule
is called "MyMicromodule", there would be a config for it in the root of the
server called "MyMicromodule.conf".  These **must** be valid HOCON or it won't
parse, and your micromodule won't ever get a call to *either* `configure` or
`onEnable`.  If an exception gets thrown during the call to `configure` for any
reason, then the call to `onEnable` will not be made and the stack trace will
be logged to the console.  If you want to change this config loading behavior,
then make a fork of Neocore-Common and check out the classes in the
`io.neocore.common.module.micro` package, or something.

Since Neocore is designed to be for larger, more scalable networks, I highly
suggest finding a way to resolve configurations from a remote, centralized
repository instead of loading it locally.  Or at least make an option to do
it remotely somehow.

If you're simply doing an integration, then just create your Bukkit/Sponge
plugin or Forge mod or what have you and make some class that directly
implements the `Module` interface and handle things there.

Micromodules will be loaded automatically by Neocore-Common, but anything else
you have to load yourself.  It's assumed that if you're making anything larger
than a micromodule that someone else gives you control at some point in time,
so that's all up to you to register your module(s).  You can find that
registry in `NeocoreAPI`, so use something maybe like this:

    ModuleManager mm = NeocoreAPI.getAgent().getModuleManager();
    if (mm.isAcceptingRegistrations()) mm.registerModule(this);

Of course, in this case, you don't need to do all that stuff with the
`micromodule.conf`.

# Databases
In order to set up a database contorller, you need a class implementing the
interface `DatabaseController`.  You also should either be doing this in a
micromodule of some integrator plugin.  Preferably the former as it integrates
a lot more smoothly.

The database controller can be registered using some code along the lines of
the following...

    DatabaseManager dbm = NeocoreAPI.getAgent().getDatabaseManager();
    dbm.registerController("My-Fancy-Controller", MyFancyController.class);

The database contorller must have a contructor accepting a single argument,
a HOCON `Config` object.  The controller should derive all configuration
from this object.  As it is taken from the `database.conf` file that the host
plugin provides.  If you do not have a constructor like this, the database
manager will throw an exception at runtime.

# Event Listeners
Events in Neocore are handled slightly differently to how they are handled in
Bukkit, though it may seem similar at first glance.  In order to get to the
Event Manager, you can do something like this:

    EventManager em = NeocoreAPI.getAgent().getEventManager();

And this is about as far the similarities go.  In Neocore, we have a much more
direct priority system than Bukkit.  Our priorities are explicit numbers
instead of the general ranges that Bukkit has.  We also use the nice Java 8
features that makes our job just a little bit easier.  Here's roughly how we'd
register a listener in Neocore.

    Module mod = myModule;
    em.registerListener(mod, SomeEvent.class, event -> {
        
        // Example
        event.setCancelled(true);
        
    }, 1000);

(Of course, you don't *have* to use a lambda expression there, but it helps.)
The value of `1000` there is just a "medium" value.  The higher the number, the
higher the priority.  If you want something akin the the "monitor" priority
that Bukkit has, use `Integer.MAX_VALUE`.  High values should be around 1
million.  And low values would be under 100.  And please don't use values
*less than or equal to* 0 unless you're doing something really special.  The
class mentioned in there is the name of the events you want to listen for.
Please don't do anything fancy like trying to capture all `PlayerEvent`s or
it'll probably cause something to get all mucked up or at least thrown an
exception somewhere in there.

**TODO:** Explanation of host stuff.
