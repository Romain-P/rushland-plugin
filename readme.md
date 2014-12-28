Rushland plugin
================

### General

Rushland plugin is a plugin which was used by Rushland ^_^ 
It's a plugin which runs with BungeeCore & allows load-balancing. 
Indeed, you can anyway configure players per lobby in rushland.yml.

To work, you have to compile the plugin in each servers & configure his config; you have to write at the line 'plugin.type' the server type (lobby, pvp or main server) 
You have to have too, a good path configuration. Here an example which describe how to place your folders: 

Rushland-folder
 - rushland.yml
 - main-server-subfolder
 - lobby1-subfolder
 - lobby2-subfolder
 - lobby3-subfolder
 - pvp-subfolder

I just want to specify that only the rush gamemod's running.I have made a good code & you can create your gamemod easily.
You have to finishe item & bags managers too, i had not the time to finish that and really, i don't want to finish that fucking code XD
It was so long to develop this plugin but i have made in my opinion a good project using guice.

What else ? Ah yes, i've created a network per plugin using Mina (i wanted to use Netty but.. fck) to manage load-balancing communication (Bungee is a fucking thing which doesn't work (plugin messages) ).
You have so to compile Rushland-Network (sorry for his dirty code, writed quickly ^^), look at my projects.

Enjoy, good use :)
