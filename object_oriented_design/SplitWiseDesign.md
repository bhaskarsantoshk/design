# Splitwise System Design

## Requirements
* Add an expense
* Modify 
* Settle
* Add/Modify/Settle group expense
* Comments
* Activity Log

## E-R:
* Expense object -> Id, Users, Balance, GroupID
* Id, Map<User, Balance>, Title, Timestamp, isSettled, GroupID
* User -> Id, Image, Bio
* Balance -> Integer, Currency
* Group -> Id, Iamge, Description, ImageURL, List<Users>
* Simplified Settling Algorithm

* Group expense -> Select * from EXPENSE where GROUP_ID="Trip" and settled is not true
* Map<User, Balance> -> getGroupBalances(group_id)
* getPaymentGraph(group id)



