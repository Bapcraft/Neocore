# Neocore API
This is rhe public API for the Neocore Minecraft server management library
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
