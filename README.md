# BPlusTree

1. INTRODUCTION
The project aims to implement B+ tree to store the data in form of pairs (key, value). The tree should be memory resident that is it should reside in the main memory. The tree should support the following operations:
1) Initialization: Creating a tree of order as specified.
2) Insert (key, value): It should be able to insert the given key and if the key is
duplicate, it should store its multiple values as well.
3) Search(key): If given a key it should be able to return its value if exists,
otherwise return “Null”.
4) Search (key1, key2): It should return all the key value pairs that will lie
within the range of keys specified, that is, key1<= key <= key2.
The programming environment used is JAVA.
2. ALGORITHM
The B+ trees can be of any order and hence, known as N-ary tree. If the order given is m, then it could have m-1 nodes and m children. It has two types of nodes:
1) Index Node 2) Leaf Node
The Index nodes are used to direct the searches while the leaf node contains the data entry. Each pair that needs to be inserted is treated as a leaf node. E.g.:
 
In the above figure, the order is 4 and thus each node can be of maximum size of 3 i.e. (m-1), Each node can have maximum number of 4 children which is the order of the tree specified.
The algorithm supports the following operations:
Insert:
Initially, the root will be null, so when first pair is given for insertion, this initializes the root node. Thus, in further update the node gets updated accordingly. For insertion, a search is to be performed to determine that under which range/ bucket the new key is to be inserted. Like in above example the keys smaller than 3 are inserted to the left subtree, while greater than 5 are inserted to right subtree, else if the key is in between the keys on given node specified, it will be inserted in the middle range/bucket accordingly.
The overflow condition for each node is if the size of node exceeds order – 1. So, if the node bucket is not full i.e. less than order -1 then add in the bucket, otherwise the overflow happens. In this case, the node is split by calculating the middle index which will become the new parent now. This will have the right subtree with nodes as itself and the keys greater than it. If the parent overflow, then split the parent in the same manner and add the middle key to its parent. This will continue until there is no more overflow. If the root splits than a new root is created with the middle key and will have two pointers. So, when order -1 is odd there will be equal split, but it is even then split will be unequal with one side containing more number of keys.
When the same key comes multiple times to be inserted it will just stores it values, since in the insert function it first checks if the given key already exists or not. If yes, then insert its value in the Array List of the key otherwise, insert in the similar manner as descried above.
During insertion, the leaf node is implemented/stored in the form of doubly linked list. So, each node will point to its next node. This will be useful while searching for the value of a given key.
Function Definition:
To split the node the function definition is:
To point to the next sibling that is creating doubly linked list for leaf the method is
Search:
Search function takes a key as input and returns its associated values in the output file. If the key is not present, then it will simply return Null. The search starts from the root and with the help of the index node pointers it searches for the leaf node to which the key belongs and once it finds that it simply returns the values which were stored during insertion.
Function Definition:
    
Range Search:
This function takes two keys as the input, in which we need to find all the key-value pairs that lie within this key range. So, the format is Search (key1, key2). Key1 will be the minimum key out of two, hence, we will search for the leaf node where this key1 belongs, once we find the leaf node, we will proceed in the list by following the next pointer as created during insertion and find the other values within the range until some value greater than key2 is observed.
Function Definition:
 
2. PROGRAM STRUCTURE
The implementation consists of 6 classes.
 NodeStruct
This defines the overflow condition and the basic structure for keys.
    IndexNodeStruct
This extends the NodeStruct class and defines the structure for the index node which is the internal node. It defines how the keys store in an internal node.
LeafNodeStruct
This extends the NodeStruct class and defines the structure for the index node which is the internal node. It defines how the keys store in an internal node.
 
BPlusTree
This class has all the operations i.e. insert, search and search range. It has methods to split the node as well if the key size becomes greater than the order – 1. It uses the class nodeTuple to
