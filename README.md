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

    $ maven clean install

And then you'll have to look around in the subdirectories for whichever jars
you want, but I'll be writing a Makefile to make it easier to find what you
want soon.
