# idmanager
Repository for IdManager open source project

# The problem

Imagine that you want to have an array of numbers, starting from 1,2,3...N. Let say N = 1 000 000.  
There are many excellent data structures for it like lists, maps etc.

Then somebody removed some of the numbers from your list. For example 10000 of them, from any position. So you have a list with a gaps and you don't know where the gaps are located.
  
Now, you would like to put the numbers back to the list, but you want to put
them back in ascending order.    

Let inspect how we can store such information about storing the numbers and
the gaps (with some information about its limitations):

## LinkedList

## ArrayList
## Tree
## BitSet
## BitSet with additional data structure to store removed elements


```mermaid
  graph TD;
      A-->B;
      A-->C;
      B-->D;
      C-->D;
```