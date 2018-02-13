This is the "mararc" file I used to configure maradns

```
ipv4_bind_addresses = "127.0.0.1"
timestamp_type = 2
random_seed_file = "secret.txt"

csv2 = {}
csv2["gamespy.com."] = "dns.gamespy.com"
```

You also need a file called "dns.gamespy.com" in your maradns directory, containing the following (replace the IP address 192.168.2.37 with that of the machine on your network that is hosting the server)

```
*.gamespy.com.  A		192.168.2.37 ~
*.gamespy.net.  A		192.168.2.37 ~
```
