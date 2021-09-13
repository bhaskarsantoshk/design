# Question

Design a simple model of Facebook where people can add other people as friends. 
In addition, where people can post messages and that messages are visible on their friend's page. 
The design should be such that it can handle 10M of people. There may be, on an average 100 friends each person has. 
Every day each person posts around 10 messages on an average.

# Answer

## Use Cases

1. User can create profile
2. User can add friends
3. User can post to his timeline
4. User can see post of his friends on a timeline/diaplay board
5. User can like a post
6. User can share his/her freinds posts on their timeline.


## Constraints

1. Consider whole network of people as Graph, each person is a node, if two people are friends, tehre will be an edge indicating the relationship. 
2. Total number of distinct users/nodes : 10 M
3. Total number of distinct freinds relationships/ edges : 100 * 10 M (assuming each person has 100 friends) 
4. Number of posts of each User: 10/day
5. Total number of posts: 10 * 10 M

Basic Design:





