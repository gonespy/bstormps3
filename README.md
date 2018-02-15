# Gonespy - bstormps3

Alternative Gamespy services for Bulletstorm on PS3.

Thanks to https://github.com/nitrocaster/GameSpy for the Gamespy SDK. Without the SDK code, this project would have been significantly more difficult.

# History
In 2014 the Gamespy servers were shut down. Amongst the games that used Gamespy is Bulletstorm on the PlayStation 3 (PS3) console. The server shutdown rendered the "Anarchy" co-operative game mode unplayable. It also meant that the PlayStation (PS) trophies that involved this game mode were no longer obtainable.

# Overview
This project is a collection of replacement Gamespy mini-services (hereafter referred to as the *"server"*, or by the nickname *"Gonespy"*) that allows you to play the Anarchy game mode again with limited functionality. You can create private matches and play on your own without any problems. Your level/XP is stored within PS3 saved game data, so you can level up as before, unlocking weapon and character skins along the way.

For PlayStation trophy hunters - this means that every single trophy in the game (including DLC, which is still available for purchase on the US PlayStation store) is potentially earnable again.

As a bonus, it should be possible to have Gonespy unlock the exclusive Leash from playing the demo :)

The server has no backend at the moment, meaning some features of the Anarchy game mode do not work as expected - leaderboards, game statistics, high scores etc. It simply implements the bare minimum in order for the client to not have any reason to think that any of the required Gamespy services are unavailable.

## In order to use the server, you need:
* [Java 8 or higher](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) installed on the PC that will be running Gonespy
* a DNS server that can resolve the gamespy.com domain to your Gonespy server, while serving all other DNS requests as normal. I used [maradns with deadwood](http://maradns.samiam.org/) with the settings detailed [here](MARADNS_DEADWOOD.md). If you have a router or NAS with DNS functionality, you could use that instead
* to let Gonespy and the DNS server through any relevant firewalls (eg. if running DNS & Gonespy on the same Windows PC, you will probably need to open a bunch of ports through Windows Firewall so your PS3 can connect)
* to change your PS3 network settings to use your DNS server

## What works (tested & confirmed):
* **Anarchy (private matches)**
* **Multiple PS3s running the game can connect to the same Gonespy server**
* **Inviting other people to join your private matches**
* **XP/leveling**
* **DLC maps**

## What doesn't work:
* Quick Match (matchmaking)
* Leaderboards
* Statistics (some are tracked in the game, others were tracked by Gamespy so they don't show)
* Offline Anarchy (connected to internet, but disconnected from PSN) - this didn't work before, but it was worth a shot!
* Your PSN ID is not shown correctly in-game 
** This doesn't break anything, it just looks weird!
** You can customize the server to make your nickname in-game to be whatever you want!

## Currently untested:
* Earning trophies
  * I am confident all trophies will be unlockable, aside from maybe the following ones:
    * Team Player - need at least 2 people to do this one. Plus, the tracker in the Statistics for this trophy uses data from Gamespy servers - so even if you meet the requirements, it might not unlock :(
    * Anarchy Master - this is impossible to get the required score with only one player, I believe
    * The Platinum trophy, of course!

## Do you think it would be possible to fully restore Anarchy with all functionality by running the server in a public environment?
I'm not sure if that would be a good idea. Gonespy is not secure, and the game sends some encrypted data from the PS network that might allow people to brute-force crack the cryto (Gamespy SDK is quite old and the crypto is weak by modern standards) and work out your PSN login password. 

(According to the SDK, Sony actually had to give Gamespy cipher files in order for the servers to decrypt the data mentioned above. Gonespy obviously does not have those ciphers, which, coincidentally, is why your PSN handle is not displayed in the main Anarchy screen)

Aside from the security risk above, I am no network programming expert - I doubt I would be able to restore all other functionality without help. I don't think the interest in this game is strong enough to warrant the effort.

I advise running your own Gonespy server within your own network.

## Would it be possible to resurrect the online multiplayer for other games that used Gamespy?
Yes. Others have made replacement servers for many PC games over the years (C&C Red Alert 3, Battlefield 2, Battlefield 2142...). Some work similar to this project. Some require modifications to the game client as well - and some of _those_ were shut down because the publisher issued DMCA takedown notices due to the authors modifying the game code.

As for console games - Mortal Kombat on PS Vita is one game that comes to mind that used Gamespy for ranked matchmaking. It might be possible to restore ranked matchmaking to that game - it probably works similar to the Quick Match for Bulletstorm (which is not supported by this project currently).

## Could this get you banned from PSN?
Unlikely. The Gamespy servers do not communicate directly with the PSN in any way. Only the game client communicates with the PSN.

## Do you have to mod your PS3 to use Gonespy?
Nope. As stated above, all you need to do is change your PS3 network settings to use a custom DNS server to trick the game into thinking that Gonespy is the real deal!

## Could this get your PSN account flagged on third party trophy tracking sites like truetrophies or psnprofiles?
Probably, at least until those sites are aware of Gonespy and accept that using it does not fall within their definitions of "cheating". Anyone who unlocks "unobtainable" trophies via this method may have their account flagged on those sites. I plan on releasing a full step-by-step guide and taking photos/video to prove that it works so that hopefully these sites will not take action against your account if you choose to use Gonespy.

## Is this cheating?
No. You still have to put in the same amount of effort to get the trophies as before. There is nothing that the Gonespy server current does or will ever be able to do that will fast-track or immediately unlock any trophies.

## Disclaimer
You use Gonespy at your own risk. I cannot and will not be responsible for any hardware or software damage to a computer, PS3 console, networking equipment or other device, nor banning or limitations imposed on your PSN account, whilst using the Gonespy software.

