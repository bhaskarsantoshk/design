Tiny URL

Functional Requirements:

Given a URL , Our service should generate a short and unique Alias
Given a short link , our service should redirect it to the right URL
Users should be able to select a custom URL
Default expiry for each link, also Users should be able to provide the expiry


Non Functional Requirements

System should be highly available
Less latency for redirection
Links should not be guessable


Extended Requirements

Analytics
Service should be accessible by other services through REST API


Capacity Estimations:

System will be read heavy ( More reads than writes ), let's assume 100:1 READ : WRITE ratio

Traffic Estimates: 500 million writes per month, 500 * 100 -> 50,000 Million reads -> 50 B reads(redirections) per month

QPS: 500 Million/ 30 days * 24 hours * 3600 seconds = 200 URLs/sec. ( Writes )

200 * 100 = 20k/Sec ( Redirections )

Storage Estimate:

500 M URLs every month
assume we store each URL upto 5 Years
500 M * 12 * 5 = 30000 M -> 30 Billion URLs
Each takes 500 Bytes
30 Billion * 500 Bytes -> 30000 Million * 500 Bytes -> 30 Million * 500 KB -> 30000 * 500 MB -> 30 * 500 GB -> 15 TB

Bandwidth Estimate:

Write Requests: 200 URL per second -> 200 * 500 B -> 100 KB / sec 
Read Resquests: 20K Redirections per sec -> 20K * 500 B -> 2000 * 5 KB -> 10 MB/sec

Memory Estimates:

80-20 Rule i.e 20% of URLs generate 80% of traffic, so we can cache this 20% URLs

20K requests / sec -> 10 MB/sec -> 10 MB * 24 * 3600 / day 
20% -> 0.2 * 10 MB * 24 * 3600 -> 48 MB * 3600 -> 48 GB * 3.6 -> 170 GB approx. 


High level estimates:

Type of URL - estimates
New URLs 				:200/sec
URL redirections 		:20000/sec
Incoming data 			:100 KB/sec
Outgoing Data 			:10 MB/sec
Storage for 5 Years     :15 TB
Memory in cache         :170 GB


## System APIs:

```createURL(api_dev_key, original_url, custom_alias=None, user_name=None, expiry_date=None)```

Returns: ( string )
A succesful insertion returns the shortened URL, else an error code

```deleteURL(api_dev_key, url_key)```

* To detect and prevent abuse: 
A user can take all our keys by sending too many requests, we can assign dev key for each user and can limit the number of requests 

### DB Design

* Few observations:
	* we need to store Billions of records
	* each record is less than 1KB
	* not many relationships ( other than user and it's created URLs)
	* read heavy service

#### DB Schema:


URL:
----------------------
PK: Hash ( varchar 16)
----------------------
Original_URL:varchar
CreationDate:datetime
ExpirationDate:datetime
UserId:int

User Data:
---------------
PK: UserId: int
----------------
Name:varchar
Email:varchar
CreationDate:varchar
LastLogin: datetime

* What kind of database we need to use ?
Since we need to store billions of records, and we don't need to use relationships between objects - a NoSQL store like DynamoDB, Cassandra or Riak is a better choice. 


### Basic System Design and Algorithm

tinyurl.com/<6-8 characters >

a:

We will first convert the long URL into md5 Hash ( 128-bit ) then we do base64 encoding.
If we take base64 ( a-zA-Z0-9+/)
If we use 6 letter keys- 64^6 -> 68 Billion possible strings
when we encode md5 hash into base64 , we get a 21+ characters above string since each base64 character encodes 6 bits of the hash value - we take 6 characters out of it ( uniquely to avoid repetition )

Different issues with our solution:

If multiple users enter same URL, they can get same shortened URL: ( if not acceptible, we will keep generating until we find a unique value)

b:

Generating keys offline

### Data Partitioning and Replication


Range Based partitionsing, Hash based partitioning

### Cache

20% URLs

LRU eviction policy


Telemetry:

Some analytics on how many times a url is used etc. 

Security and Permissions:







