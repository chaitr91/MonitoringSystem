#Documentation

##Design Consideration :

1)Ip Address generation

192.168.[0-7].[0-124]  
Therefore, 125*8=1000 servers.
Each Server has two CPU_ID 0/1.


2)Data stored in the file 
Format: [Epoch Value, IP Address , CPU_ID, CPU_USAGE]
Eg:1414689783 192.168.1.10 0 87  


##Working:

###Generate:
 - The user inputs a start-date and start time. Data is generated for 24 hours from that point.
 - The generation is done using Java threads owing to the fact that number of entries is large.
 - 8 threads are run each writing data into its individual out[number].txt file for 125 Servers each.
 - Finally all the temp out files are concated into provided Data_Path.

###Query:
 - The data is read from the input path and written into HashMaps.
 - The data Structure maintained is HashMap<IpAddress, HashMap<CPU_ID, List<Obj(TimeStamp, CPU_Usage)>>>
 - Indexing of a HashMap is O(1) and iterating through a list is O(n).
 - While querying for the data we sort the List, leading to O(nlogn) Complexity. The range is then found by iterating from the start and end of this sorted list, simultaneously. 

##Usage:

1)To run the Generator
./generate.sh DATA_PATH

2)To run the Query tool:
./query.sh DATA_PATH

3)To query for CPU_Usage Log for a given data range.
format:
>QUERY IP Address CPU_ID start_time[yyyy-MM-dd HH:MM]  end_time[yyyy-MM-dd HH:MM]
Eg:>QUERY 192.168.0.120 1 2017-02-05 17:00 2017-02-05 17:05

4)To Exit from the tool
>EXIT

##Time:

###Generation: 
First Approach:
Brute force Method 30 minutes.
Parrallel Approach 20 minutes.

###Query:
Query 5 hours.
Time Complexity O(NLogN) 

##Scope Of Improvement
1) Validation of input Arguments for the Query.








