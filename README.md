# Neocore

Yay!

*// TODO Add more explanation.*

## Layout

These modules are responsible for the following components of the system:

* api : Developer-facing API for interacting with Neocore's components.
Akin to Bukkit for dealing with CraftBukkit's components.

* common : Common classes used in game servers when implementing a
Neocore host plugin.

* platform-bukkit : **Host plugin for Bukkit/Spigot servers.**  Use this on
your Bukkit/Spigot server.

* platform-bungeecord : **Host plugin for BungeeCord servers.** Use this if
you're running a BungeeCord server.

* db-jdbc : Micromodule for running your database on a any kind of JDBC
backend.**You'll probably need this.**

* network: Management project for dealing with syncronization across
networked servers.  Not necessary if you're running standalone servers or
isolated servers.

In Neomanage, there's 2 important projects.

* neomanage-client : Micromodule used in networked servers.

* nmd : Standalone daemon that Neomanage-enabled servers connect to to
synchronize with each other.

## Building

You need to have Maven and the JDK on your system, but once you have they you
can basically just do the following:

    $ make deps     # Makes sure that you have jzania installed, you only need to do this once.
    $ make          # Actually builds the project.

Then everything you should need will be in the `target` directory.  In order to
do a cleanup of this directory and anything else, use:

    $ make clean

