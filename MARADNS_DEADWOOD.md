The following instructions assume you are running maradns & deadwood on the same Windows PC as Gonespy.



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

For deadwood, this is the dwood3rc.txt file that I used (replace the IP address 192.168.2.37 with that of the DNS server)

```
upstream_servers = {}
upstream_servers["."]="8.8.8.8, 8.8.4.4" # Servers we connect to

bind_address = "192.168.2.37"
recursive_acl = "192.168.2.37/24"
root_servers = {}
root_servers["gamespy.com."] = "127.0.0.1"

# The file containing a hard-to-guess secret
random_seed_file = "secret.txt" 

# This is the file Deadwood uses to read the cache to and from disk
cache_file = "dw_cache_bin"

# By default, for security reasons, Deadwood does not allow IPs in the
# 192.168.x.x, 172.[16-31].x.x, 10.x.x.x, 127.x.x.x, 169.254.x.x,
# 224.x.x.x, or 0.0.x.x range.  If using Deadwood to resolve names
# on an internal network, uncomment the following line:
filter_rfc1918 = 0
```
