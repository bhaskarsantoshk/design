## Web architecture
* involves multiple components to form an online service

### Client- Server architecture:
* fundamental building block of the web
* works on a request- reponse model
* all websites are built on client-server architecture
#### Client
* Client holds the UI.
* Client can be a mobile app or a web based console
* Popular technologies that are used to write client/front end: vanilla javascript, JQuery, React, Angular, Vue, etc. 
* Types of clients: Thin Client, Thick Client
* Thin client: contains only UI, no business logic , sends req to server for every action. (3 tier)
* Thick client: contains UI and some business logic ( 2 tier)

#### Server
* receives requests from client, executes business logic based on req params, sends the responds back to client.
* servers running web applications are commonly known as application server.
* other kinds of servers with specified tasks assigned: Proxy Server, Mail server, File server, virtual server etc. 
* server side rendering: rendering ui at the backend and sending the generated data to client.

#### Communication between client and server
* HTTP : a protocol for data exchange over world wide web
* HTTP is a stateless protocol, every process is independently executed, and has no knowledge of previous processes.
* More details: https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview
* backend application has rest api implemented. 
* every client has to hit rest end points to receive response.

#### REST
* REST API adhers to REST architectural constraints.
* communication between client and server happens over HTTP.
* REST takes the advantage of HTTP methodologies.
* enables servers to cache responses in order to improve application performance.
* end point: simply a URL
* having rest end points available, this enables to decouple client and server implementations.
* rest api acts as a gateway or a single entry point into the system. encapsulates business logic, takes care authentication, authorization and sanitizing of input data.

#### HTTP Push and Pull
* two modes of data transfer between client and server: HTTP Push and HTTP Pull
* HTTP Pull: 
	* default mode of http comunication
	* client sends the request, server responds to it.
	* client pulls data from server whenever required, it's possible that the data may not updated often , so it costs resources if client keeps on sending requests and also adds load on server.
* HTTP Push:
	* client sends req for certain information just once, server keeps pushing the new updates whenever they are available.
	* also known as callback.
* client uses async javascript and xml ( ajax) to send requests in both http pull and push.

* HTTP Pull - Polling with AJAX:
	* there are two ways of fetching/pulling data from server: 1. constant manual triggering of HTTP GET 2. polling data dynamically at regular intervals with AJAX without any human intervention.
	* AJAX is used for adding asynch behaviour to web page. it enables to fetch updated data from sever automatically sending requests at continous intervals. 
	* AJAX uses XMLHttpRequest object to send requests to server, this object is built in the browser and uses javscript to update the HTML DOM.
	* commonly used with jQuery framework.
