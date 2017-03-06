# Neocore

Yay!

*// TODO*

## Layout

These modules are responsible for the following components of the system:

* api : Developer-facing API for interacting with Neocore's components.
Akin to Bukkit for dealing with CraftBukkit's components.

* common : Common classes used in game servers when implementing a
Neocore host plugin.

* platform-bukkit : **Host plugin for Bukkit/Spigot servers.**  Use this on your
Bukkit/Spigot server.

* platform-bungee : **Host plugin for BungeeCord servers.** Use this if
you're running a BungeeCord server.

* db-jdbc : Micromodule for running your database on a any kind of JDBC
backend.**You'll probably need this.**

* netw ork: Management project for dealing with syncronization across
networked servers.  Not necessary if you're running standalone servers or
isolated servers.

In Neomanage, there's 2 important projects.

* neomanage-client : Micromodule used in networked servers.

* nmd : Standalone daemon that Neomanage-enabled servers connect to to
synchronize with each other.

## Building

For now, you can't build it too easily.  I'll fix that in a few minutes.

