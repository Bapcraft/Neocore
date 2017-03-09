# How to setup Neocore with your server

Neocore is annoyingly difficult to get configured, but anyone is capable of it.

You need to have the relevant server jar sitting in your plugins folder, and
then add in `neocore.conf` and `databases.conf` to the plugin data directory,
which usually is a folder called Neocore inside your plugins folder.  Make sure
that you fill in the relevant values in these directories, as it's really easy
to mess everything up without realizing it if you don't do things right, then
you'll come over to GitHub and make an issue and get angry with me.

If you're using Neomanage (aka anything in the `network` Maven module), then
you also need to drop `Neomanage.conf` in the root of the server directory.

