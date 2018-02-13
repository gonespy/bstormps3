# bstormps3

Alternative Gamespy services for Bulletstorm on PS3.

Thanks to https://github.com/nitrocaster/GameSpy for the Gamespy SDK

In 2014 the Gamespy servers were shut down. Amongst the games that used these servers is the game Bulletstorm on the PS3 console. The server shutdown rendered the "Anarchy" co-operative game mode unplayable. It also meant that the PlayStation trophies that involved this game mode were no longer obtainable.

This project is a collection of dummy Gamespy mini-services (hereafter referred to as the "server") that allows you to play the Anarchy game mode again with limited functionality. You can create private matches and play on your own without any problems. Your level/XP is stored in the PS3 game data so you can level up as before.

This means that every single trophy in the game (including DLC, which is still up on the US store) is potentially earnable again.

As a bonus, it should be possible to make the dummy server tell the game to give you the exclusive Leash from playing the demo :)

The server has no backend at the moment, meaning some features of the Anarchy game mode do not work as expected - leaderboards, game statistics, high scores etc.

## In order to use the server, you have to:
* Run the dummy server on a PC in your home network (I plan on releasing the source code soon after more testing & cleaning up, along with instructions on how to run it)
* Run a DNS server that can resolve the gamespy.com domain to the dummy server, while serving all other DNS requests as normal. I used [maradns with deadwood](http://maradns.samiam.org/). If you have a router or NAS with custom DNS features, you could just use that
* change your PS3 network settings to set your DNS server to the one running on your PC

## What works (tested & confirmed):
* Anarchy (private matches)
* XP/leveling

## What doesn't work:
* Quick Match (matchmaking)
* Leaderboards
* Statistics (some are tracked in the game, others were tracked by Gamespy so they don't show)

## Currently untested:
* Inviting friends to your private match (game invites are sent but I don't have a second copy of the game yet to test that they can be accepted & the game joined)
* Connecting multiple PS3s running the game to the same dummy server
* Earning trophies
  * I am confident all trophies will be unlockable, aside from possibly the following ones:
    * Team Player - need at least 2 people to do this one. Plus, the tracker in the Statistics for this trophy uses data from Gamespy servers - so even if you meet the requirements, it might not unlock :(
    * Anarchy Master - this is impossible to get the required score with only one player, I believe
    * The Platinum, of course!
* DLC (I have purchased it but haven't tried playing a DLC map in-game)

## Do you think it would be possible to fully restore Anarchy with all functionality by running the server in a public environment?

I'm not sure if that would be a good idea. The dummy server is not very secure, and the game sends some encrypted data from the PS network that might allow people to brute-force crack the cryto (Gamespy SDK is quite old and the crypto is weak by modern standards) and work out your PSN login password. 

(According to the SDK, Sony actually had to give Gamespy cipher files in order for the servers to decrypt the data mentioned above. The dummy server obviously does not have those ciphers.)

Aside from the security risk above, I am no network programming expert - I doubt I would be able to restore all other functionality without help. I don't think the interest in this game is strong enough to warrant the effort.

I think it would be safest to just run your own server within your own network.

## Could this get you banned from PSN?
Unlikely. The Gamespy servers do not communicate directly with the PSN in any way. The game does all that.

## Do you have to mod your PS3 to use the dummy server?
Nope. As stated above, all you need to do is change your PS3 network settings to use a custom DNS server to trick the game into thinking the dummy server is the real one!

## Is this cheating?
Not really. You still have to put in the same amount of effort to get the trophies as before. There is nothing that the dummy server does that will fast-track or immediately unlock any trophies.

## Could this get you banned from trophy tracking sites like truetrophies or psnprofiles?
Possibly. Anyone who unlocks "unobtainable" trophies via this method may have their account flagged on those sites. I plan on releasing a full step-by-step guide and taking photos/video to prove that it works so that hopefully these sites will not take action against your account if you choose to use the dummy server.
