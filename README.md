# game-demo
Short Demo for the interview

# Testing The API
Go to http://localhost:8080/swagger-ui/index.html to test the provided APIs.


# Why use redis for the leaderboard impl
https://medium.com/@sandeep4.verma/building-real-time-leaderboard-with-redis-82c98aa47b9f

# Why activeMq?
What would happen if we had 2 instances of this project running?
I needed an external message broker for the websocket implementations
to work with across all the instance nodes.

# Sources used to present this demo
https://betterprogramming.pub/building-a-websocket-server-in-a-microservice-architecture-50c6c6432e2b

# Database Design

![](../../OneDrive/Masaüstü/uml-diagram.png)