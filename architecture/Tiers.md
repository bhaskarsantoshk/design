# Different Tiers in Architecture

### Tier
* Both logical and physical separation of components in an application/service.
* Separation is at component level, not at code level. 
* Components: 
	* DB
	* Application Server
	* UI
	* Messaging
	* Caching
### Single Tier Architecture
* UI, Backend buisness logic and DB reside in same machine.
* Examples: Photoshop, MS Office, PC Games etc.
* Pros:
	* no network latency
	* high security of data
* Cons:
	* application publisher has no control, customer needs to manually update if the new changes are needed
	* software needs to be thourougly tested since the buggy code can't be changed once the software is shipped
	* code can be tweaked / reverse engineered/ modified for profit
	* app rendering largely depends on user's machine/hardware.

### Two Tier
* involves a client and server
* client contains UI and business logic on one machine
* backend server and db are in a different machine (publisher has a control over these)
* a to-do app/ planer app etc are the examples
* business logic will be within the client , hence fewer network calls to backend server (less money spent to keep the servers running - economical)
* code is vulnerable but not much harm to business.

### Three Tier
* UI, Business logic and DB - all reside on different machines
* Blog is an example, UI will be written in HTML, JS and CSS. Backend application logic will be run on a server like Apache and database will be MySQL. 

### N Tier
* Examples: Instagram, Amazon, Pokemon Go etc. 
* involves more than 3 components ( Cache, MQs, Load Balancers, Search Servers, Microservices etc. )
* Need for so many tiers: 
	* Single Responsibility Principle: 
		* dedicated responsibility to a component ( seemless execution )
		* dedicated teams and code repos for individual components. (flexibility )
		* less probability of regression due to loose coupling
		* upgrade/patch to one component won't affect other components ( or rest of the components can still do fine) 
		* DB will not have business logic ( stored procedures ), a new db can be added easily if required , also DBs are purely responsible for persistence
	* Separation of Concerns:
		* be concerned about a individual component not the whole system
		* having a loosely coupled architecture eases scaling when required
* Diff between layers and tiers:
	* Layers are at code level, represent conceptual / logical organization of code
	* ui, business, service and data access layers ( all these can be used at any tier application)
	


