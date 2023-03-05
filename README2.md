# candlesticks

## Running

Run with docker-compose:

```shell
docker compose up -d
```

The project consists of:

1. The partner service
2. The candlestick service
3. A postgres instance
4. pgAdmin4, to query the database

Admin username and passport for the pgAdmin can be found inside docker-compose.yaml. In order to connect from pgAdmin to
postgres,
the database host should be specified as: `host.docker.internal`

## Description

So this project was rather interesting because I did a very similar thing at least three times before. (I used to work
for a bank) I had several ideas, the first one I toyed around with was writing two different microservices, one in
Scala, utilizing fs2 to read and create a websocket stream and persist the data to a postgres instance with doobie,
end to end streamified; and the other microservice was going to be a regular old Spring boot application, and it is only
purpose was going to be calculating and serving candlesticks. In an ideal scenario, candlesticks should be persisted in
a caching solution like Hazelcast or redis and quotes/instruments should've been distributed by kafka or something
similar (so that application won't block itself with heavy io). However, I thought having kafka around would be overkill
since _simplicity is favored_. Also scala does not work well with gradle (esp. since docker is also involved) so there
goes that idea.

Ah, forgot to mention that when I first read the project description, I immediately thought this would have been super
easy to implement with node :) But then again, the framework constraints had to be obeyed so yeah.

So the second idea was having Spring, R2DBC and Webflux. We (as in, the bank) did not like to use reactive programming
at all,
because:

1. Low latency is much more preferable than scalability. A bank can always up the resources but if your request is a
   second off your customers will start gathering a crowd at your door.
2. The reactive stack back then was simply not mature enough.

So after prototyping with Spring Boot 3 and related stack, I still think it is not mature enough. Especially the testing
was super ineffective and had some weird behavior. So I ditched that idea as well.

I am not super comfortable with kotlin yet so did not really think about modifying the existing code. Thus, I wrote
good-old Servlet Spring. It is not fancy nor flashy, but gets the job done rather quick (with testing completing the
task
took something around 6-7 hours) I tried to follow up some basic DDD convention, data-centric packages, testing
behaviour
rather than units (there are still unit tests) etc. But I could not say this is pure convention. Also, I did not want
to do configuration (other than injecting some variables to set up jdbc context) because there are simply two things to
configure:

1. Endpoints
2. The hard cap of 30 candlesticks

Besides, I don't like how Spring handles Configuration, even though I understand convention over configuration, there
should be a convenient way to load configuration from a file to a _*TYPE*_ so yeah. Also, whenever I start writing
Spring,
I find myself doing more configuration than any other framework and language out there so yeah.

As for the future development, if we want to have more scalable system in the future, working reactive would make sense.
also, distributing the load to other services would be better, delegate db io to one service, consume sockets with
another
and calculate and serve candlesticks with a third one. make them all communicate with a bus.

## TODO

* Integrate proper configuration for endpoints inside configuration classes
* Integrate vavr to quote and instrument packages