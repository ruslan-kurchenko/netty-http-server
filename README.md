# Netty HTTP Server


## Description

It is a HTTP server based on NIO framework Netty that provide handle HTTP requests and 
store information about them in the data base.

### In default server have next handlers:
- [`ServerInitializer`](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/handler/ServerInitializer.java) - provides helpful server initialization and some configurations.
- [`ServerHttpRequestHandler`](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/handler/ServerHttpRequestHandler.java) - main server handler which process HTTP requests and based on that construct responses.
- [`ServerConnectionCountHandler`](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/handler/ServerConnectionCountHandler.java) - provides counting of all and current connections to the server.
- [`ServerTrafficHandler`](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/handler/ServerTrafficHandler.java) - provides a count of input/output data.
- [`ServerDataBaseCleaner`](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/handler/ServerDataBaseCleaner.java) - monitors the "purity" of the database.
- [`HttpObjectAggregator`](http://netty.io/4.0/api/io/netty/handler/codec/http/HttpObjectAggregator.html) - provides handle only full HTTP messages and be okay with some memory overhead.
- [`HttpServerCodec`](http://netty.io/4.0/api/io/netty/handler/codec/http/HttpServerCodec.html) - provides decode HTTP requests from clients/decode HTTP response for clients. 

##### `ServerHttpRequestHandler` - provides four situations "how are requests processed?":
- `http://localhost:{port}/hello` server wait 10 second and that sends response with HTML page which contains "Hello World" string.
- `http://localhost:{port}/redirect?url=<url>` server redirect client to specified `<url>`.
- `http://localhost:{port}/status` server sends response with HTML page which contains server status.
- `http://localhost:{port}/{smth.another}` server sends response with ERROR 404 HTML page.

>To configure the server port use [`~/config/server-config.properties`](https://github.com/henko-okdev/netty-http-server/blob/master/config/server-config.properties).
>All path registration occurs in [PathRegistry](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/controller/PathRegistry.java) interface.


To provide all of these functions, the handler uses a [Controller](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/controller/Controller.java). 
The controller gets a path that a client specified and returns a content of 
a corresponds page. There are three type of [Page](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/view/Page.java): 

- [HelloPage](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/view/HelloPage.java) contains *"Hello Word!"* string.
- [StatusPage](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/view/StatusPage.java) contains server statistic: number of all requests, number of unique requests from IP, 
number of current connections, table with unique requests, table with redirect information 
and table with information about last connections(by default table have 3, 3, 16 rows corresponds).

Also if the path is `/redirect?url=<url>`, the controller can
prepare a `<url>` for handler. All responses sends the handler.

##### `ServerConnectionCountHandler` - concurrent count of connections
The counter based on [ChannelTrafficShapingHandler](http://netty.io/4.0/api/io/netty/handler/traffic/ChannelTrafficShapingHandler.html).
It is has two [AtomicInteger](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/atomic/AtomicInteger.html)
variables: `CURRENT_CONNECTION_COUNT` and `ALL_CONNECTION_COUNT` which count current connections and 
all connections to server corresponds. Also handler provides two methods that provides getting numbers from this 
inner counters.
 

##### `ServerDataBaseCleaner` - very important handler "why"?
This handler provides database cleaning. It is the child of [ChannelTrafficShapingHandler](http://netty.io/4.0/api/io/netty/handler/traffic/ChannelTrafficShapingHandler.html). 
When a method `doAccounting(...)` works, inner [DBManager](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/db/DBManager.java) 
cleans storage. Also you can configure cleaning interval(`cleanInterval`) and how many rows a cleaner left in table(`leftRows`) 
via handler constructor. By default `cleanInterval = 1000 milliseconds`, `leftRows = 500`.

#####A little bit about server storage
The server uses **[H2DataBase](http://www.h2database.com/)** 
[in-memory](http://en.wikipedia.org/wiki/In-memory_database) mode to store data about server status. 
To store and retrieve data server uses **[HikariCP](https://github.com/brettwooldridge/HikariCP)**, 
it is a "zero-overhead" production ready JDBC connection pool and very comfortable in work.
Also you can configure server storage by [`~/config/db-config.properties`](https://github.com/henko-okdev/netty-http-server/blob/master/config/db-config.properties). 
For example - switch DB mode to [embedded](http://www.h2database.com/html/quickstart.html), 
configure path to DB, user name/password, max connection pool size. To provide more flexibility, the server
have a DAO layer based on the Factory Method design pattern. The DAO layer have two dao interfaces - ConnectDao 
and RedirectDao implementations of which provide communication with DB([H2ConnectDao](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/dao/impl/H2ConnectDao.java), [H2RedirectDao](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/dao/impl/H2RedirectDao.java)).
>Each time before the server start, [DBManager](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/db/DBManager.java)
prepares the database.

##### `ServerTrafficHandler` - count server I/O data
This counter specialized on counting received and sent data by server. Also processor calculates the speed of each 
connection and stores all data in the database using [H2ConnectDao](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/dao/impl/H2ConnectDao.java). 


##### Additional
Also server has some classes helpers:

- [ConfigReader](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/util/ConfigReader.java) - 
provides reading `.properties` configurations files.
- [DBUtil](https://github.com/henko-okdev/netty-http-server/blob/master/src/main/java/com/henko/server/db/DBUtil.java) - 
provides static methods that helps to close the database [Connection](http://docs.oracle.com/javase/7/docs/api/java/sql/Connection.html), 
[PreparedStatement](http://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html), 
[Statement](http://docs.oracle.com/javase/7/docs/api/java/sql/Statement.html) and
[ResultSet](http://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html).

