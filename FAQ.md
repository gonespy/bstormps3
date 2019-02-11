* Why can't Gonespy work with games that don't use Unreal Engine? eg. FEAR 2

Games that use GameSpy perform a remote authentication check, sending a digital certificate to the GameSpy server.

The game expects the server to respond with a similar certificate, with an additional signature field that is created by encrypting 
the certificate data with an unknown private key.

The game attempts to decrypt the signature field with the matching public key (which is known). I don't know what the 
private key is, so it is impossible to generate a signature field that the game expects.

Games that use Unreal Engine seem to have a bug/feature where game doesn't really care if the certificate signature is wrong. 
This is why Gonespy works with games like Bulletstorm and Unreal Tournament III.

* Can't you just "crack" the private key?

The key pair is 1024 bit RSA which would take a very long time to brute force crack. You would need the computing resources of a
first-world government to do it in a reasonable amount of time.

* Can't you just use a different key pair?

To use a different key pair, you would have to hack the game code to use a different public key. To do this you would need custom 
firmware, and even then I'm not sure how easy this is.

Using custom firmware is against Sony's TOS, and also not allowed on trophy leaderboard sites for unlocking unobtainable trophies,
so this is not a good solution.

Hacking the game code on PC to use a different public key is comparatively quite easy, so this method works well for PC games. 
However, hacking the game code is currently deemed a DMCA violation in the United States - some projects have been shut down 
because they have been threatened with legal action.

* Can further work be done to get Unreal Engine games working with matchmaking etc?

Yes, anyone with programming experience and knows their way around Wireshark can use Gonespy as a starting point, and test games -
checking to see what the game client is sending, and using the GameSpy SDK to work out how the server should respond.

For example, MK vs DC (PS3) requires GameSpy's chat service to be running, as text chat lobbies are part of the online experience. 
The chat service has not been implemented yet.

* Can Gonespy support platforms other than PlayStation?

Potentially, but some work would be required to handle the initial authentication, as this differs for other platforms.

