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

#### Which scalability is right for our app

	* Pros of Vertical Scaling: 
		* doesn't require any code change / complex system configurations. 
		* takes less administrative/monitoring/management efforts than managing a distributes system.
	* Cons of VS:
		* Avaialability Risk

	#### Why does the code need to change in when the app has to run on multiple machines ?

		* If you intend to run the code in distrubted env, it need to be stateless. 
		* Means, there should be no static instances in the class, static instances hold application data, when the server is down, all the static data/state is lost. The app is left in an inconsistent state.
		* In object-oriented programming, the instance variables hold object state in them. Static variables moreover hold state that spans across multiple objects. They generally hold state per classloader. Now, if the server instance running that classloader goes down, all the data is lost.
		* Also, whatever data static variables hold, it’s not application-wide. For this reason, distributed memory like Redis, Memcache, etc., are used to maintain a consistent state application-wide. When writing applications for distributed systems, it’s a good practice to avoid using static instances in the class. The state is typically persisted in a distributed memory store; this facilitates components to be stateless.	
		* This is why functional programming got popular with distributed systems. The functions don’t retain any state. However, the same behavior can also be achieved with prominent OOP languages.
	* If the app has minimum predictable traffic ( internal tool and not mission critical ) - single server could possibly be enough
	* If your app is a public-facing social app like a social network, a fitness app, an online game, or something similar, where the traffic is unpredictable. Both high availability and horizontal scalability are important to you.



