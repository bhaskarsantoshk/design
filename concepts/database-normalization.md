# Normalization of Database
* It's a technique of organizing data into db.
* It's a systematic approach of decomposing tables to eliminate data redundancy (repetition)
and undesirable characteristics like Insertion, Update and Deletion anomalies.
* Multi-step process that puts data into tabular form and 
removing the data from the relation tables. 
* It's used mainly for 
  * eliminating redundant data
  * enusring data dependencies make sense i.e data is logically stored.


| rollno | name | branch | hod   | office_tel |
|--------|------|--------|-------|------------|
| 401    | Akon | CSE    | Mr. X | 53337      |
| 402    | Bkon | CSE    | Mr. X | 53337      |
| 403    | Ckon | CSE    | Mr. X | 53337      |

In the above example, you see all branch, hod and office_tel 
has been redundant for all the students. It will lead to following issues:

Insertion Anomaly: 

for every new student, until and unless he is allotted a branch, 
we cannot store hos details. else, we will have to put NULL. 

if there are 100 students in the same branch, all the branch data gets repeated.

Updation Anomaly:

What if Mr. X leaves the college? or is no longer the HOD of computer science department? 
In that case all the student records will have to be updated, and if by mistake we miss any record, it will lead to data inconsistency. This is Updation anomaly.

Deletion Anomaly

In our Student table, two different informations are kept together, 
Student information and Branch information. 
Hence, at the end of the academic year, if student records are deleted, 
we will also lose the branch information. 
This is Deletion anomaly.

Normalization Rule
Normalization rules are divided into the following normal forms:
1. First Normal Form 
2. Second Normal Form
3. Third Normal Form 
4. BCNF 
5. Fourth Normal Form