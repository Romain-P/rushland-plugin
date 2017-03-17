Rushland plugin
================

### General

Rushland plugin is a free release of rushland-plugin.
It's a plugin running with BungeeCore & allows load-balancing. 

To work, you have to compile the plugin for each server and configure its config; you have to write at the line 'plugin.type' the server type (lobby, pvp or main server) 
You gotta have a good path configuration. Here an example which describe how to place your folders: 

Rushland-folder
 - rushland.yml
 - main-server-subfolder
 - lobby1-subfolder
 - lobby2-subfolder
 - lobby3-subfolder
 - pvp-subfolder

I just want to specify that only the rush gamemod's running.
Network using Mina to manage load-balancing communication.
You have so to compile Rushland-Network. (check @my projects)
