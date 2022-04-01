#### Scalability

* Scalability means the application's ability to handle and withstand increased workload without sacrifcing performance. 
* For example, if your app takes x seconds to respond to a user's request, It should take the same x seconds to respond to each of your app's million concurrent user requests. 
* The app’s back-end infrastructure should not crumble under a load of a million concurrent requests. It should scale well when subjected to a heavy traffic load and maintain the system’s latency.
* Latency means the time system takes to respond to a user's request. 
* Latency is devided into two parts: Application latency and Network latency
* Network latency: time taken by network to send a data packet from point A to point B. network should be efficient enough to handle the increased load on the website. 
* To cut down the network latency, businesses use a CDN (Content Delivery Network) to deploy their servers across the globe as close to the end-user as possible. These close to the user locations are also known as Edge locations 
* https://www.educative.io/courses/cloud-computing-101-master-the-fundamentals
* Application latency is the time the application takes to process a user request


#### Types

* Vertical Scaling: Adding more power to our server. It's also called scaling up. augmenting the power of hardware. 
* Horizontal scaling: adding more hardware to existing hardware pools. It's also called scaling out.there is no limit on how much we can scale horizontally. 
* Horizontal scaling also allows us to scale dynamically when the traffic increases. dynamic scaling is not possible with vertical scaling.
* Cloud elasticity: The process of adding and removing the servers, stretching and returning to the original infrastructual capacitancy on the fly. https://www.educative.io/courses/cloud-computing-101-master-the-fundamentals
