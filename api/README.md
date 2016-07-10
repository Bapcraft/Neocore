# Neocore API
This is the public API for the Neocore Minecraft server management library
created by Treyzania for Bapcraft.  The actual underlying implementation of
this API and its support features (such as the actual Bukkit plugin and the
MongoDB database controller) are currently private.  This may change in the
future.

# How to implement
There are two different ways one can access the API.  First, one can simply
use it as a way to interact the the core libraries from a conventional server
plugin, be it Bukkit, Spigot, BungeeCord, Sponge, or any other.  Or one could
choose to create what's called a "micromodule", which is a (typically) small
pseudo-plugin that is initialized from within a running instance of the main
Neocore Prime library.

In order to register services, you need to have a *module*.  A module is any
object that implements the `Module` interface.  If you're creating a
micromodule in Java, you should extend the `JavaMicromodule` class.  And make
sure that the jar you're compiling into has a `micromodule.conf` file in its
root, and place it in the micromodules folder wherever it's supposed to be in
your environment.

Examples:   

    // my/micromodule/package/MyMicromodule.java
    package my.micromodule.pacakage;
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

If you're simply doing an integration, then just create your Bukkit/Sponge
plugin or Forge mod or what have you and make some class that directly
implements the `Module` interface and handle things there.

You also need to do this to register listeners.  Listeners you create for your
modules and other systems that don't use the event bus should inherit from the
`SimpleListener` interface, so that they can track who is doing what.

Micromodules will be loaded automatically by Neocore-Common, but anything else
you have to load yourself.  It's assumed that if you're making anything larger
than a micromodule that someone else gives you control at some point in time,
so that's all up to you to register your module(s).  You can find that
registry in `NeocoreAPI`, so use something maybe like this:

    ModuleManager mm = NeocoreAPI.getAgent().getModuleManager();
    if (mm.isAcceptingRegistrations()) mm.registerModule(this);

Of course, in this case, you don't need to do all that stuff with the
`micromodule.conf`.

**TODO:** Explanation of host and database stuff.
