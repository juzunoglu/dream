# game-demo
Short Demo for the interview

# Testing The API
Go to http://localhost:8080/swagger-ui/index.html to test the provided APIs.

# Why use redis for the leaderboard impl
https://medium.com/@sandeep4.verma/building-real-time-leaderboard-with-redis-82c98aa47b9f

# Sources used to present this demo
https://betterprogramming.pub/building-a-websocket-server-in-a-microservice-architecture-50c6c6432e2b

# Database Design
![db-schema](https://user-images.githubusercontent.com/38230713/197417480-07b0e57c-cdfe-4787-9d95-2f68058a79fa.png)

# Get Rank Request
Get Rank request works with the redis hashes. Updating the relational-db every time a position is changed seemed hard
and unnecessarily complicated. So I went with Redis's built-in sorted set implementation and commands. Jedis (https://github.com/redis/jedis)
is used for the redis client on top of spring's built in redis implementation.

# Redis Current Group Leaderboard
When users join to the STARTED tournament, a new set for a user group and a hash for user is created in the redis server.
the group leaderboard is always updated when the user levels up thanks to the Redis's built-in sorted set implementation.
zrevrangeWithScores(groupName, min, max) is used to calculate the positions in a group. works in 
O(log(N)+M)  with N being the number of elements in the sorted set and M the number of elements being returned

# Redis Global Leaderboard
Almost the same logic. Except, this time every user who are in the tournament gets saved to the sorted-set.

# Claim Reward Request
Additional table was required to keep track of the reward-claims that keeps the tournament_id and user_id. Could be a lot simpler maybe but I just dont know :)

# Enter Tournament Request
Normally this one is a scheduled job that works across all the nodes if I decide to add
this implementation -> https://www.baeldung.com/shedlock-spring, but there was simply no time

When a tournament starts, the groups are automatically created for users to be in based on their level.
after each scheduled tournament ends, the redis server is cleared for the sorted-sets (group leaderboard and global leaderboard)
but the hashes for Leaderboard remains for further usage of the GET RANK request. The clean-up happens
async.

# What could have I done better?
The DB schemas could definitely use some improvements, as I'm not happy with all the 
complexity, but given the time, this was the best I could do.
There could be definitely some improvements on the queries as they seem rather complex and could
potentially slow the application down.
Team solution does not really scale. Should've found a better way to assign users to a group based on their levels.
Could not write unit-tests.

# Further Possible Implementations and Why there is a websocket class
### Redis Async
Can make some functions async with @async annotation, but I don't want to risk it at this time :D
### Why websockets?
It could be the case that the notification server can be built upon this implementation with redis pub-sub.

### Why activeMq?
What would happen if we had 2 instances of this project running?
I needed an external message broker for the websocket implementations
to work with across all the instance nodes.



