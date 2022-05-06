# Segment Tree Visualizer

## Learn how a Segment Tree operates

The Segment Tree Visualizer is a tool to help visualize and understand how a segment tree works. 
The tool includes support for using multiple merge functions in the tree and provides a console and GUI option to 
visualize the tree. The tree can be initialized with custom user input, with upto 32 distinct values.

This tool could be really helpful for people who are into competitive programming or just about any person looking to 
learn about Segment trees. This could also be helpful for those who are already familiar with how it works but are just
looking for a quick visualization since this could be time-consuming to draw.

Segment tree has a lot of uses and I've come across it frequently in competitive programming. This is a very flexible
data structure and is something that I really find fascinating in the way it works. It seems simple to understand
but provides a lot of advantages if implemented well.
 
### What is a Segment Tree?

Segment Tree is a powerful tree data structure that helps with both querying sum in a given range and also performing 
updates in logarithmic time. 

### What's the time complexity?

- Building the tree: O(n) - This requires a maximum of 4n steps though there are algorithms with great 
optimization that could bring it down to 2n steps

- Querying sum in a range: O(logn)

- Updating a value: O(logn)


### Why use a Segment Tree?

Suppose an array is used to perform the same operations, the following is the time complexity:

- Querying sum in a range: O(n) (Can be brought down to O(1) with a prefix array)
- Updating a value: O(n)

It can be noticed that updating a value takes linear time and this could mean a huge difference in performance when
n >= 1e9.


### User stories
- As a user, I want to be able to use default values / add (multiple) custom values to generate a segment tree
- As a user, I want to be able to update any value in the segment tree
- As a user, I want to be able to query sum/product in a given range
- As a user, I want to be able to specify the merge function(addition/multiplication) being used in the segment tree
- As a user, I want to be able to save the current state of the segment tree
- As a user, I want to be able to load the most recently saved segment tree

### Phase 4: Task 2
Thu Nov 25 22:51:40 PST 2021
Generated new segment tree with 16 values - 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16


Thu Nov 25 22:51:43 PST 2021
Updated segment tree at node index - 0 to value - 2


Thu Nov 25 22:51:48 PST 2021
Updated merge function from 'Addition' to 'Product'


Thu Nov 25 22:51:50 PST 2021
Updated merge function from 'Product' to 'Addition'

### Phase 4: Task 3
1) Rendering trees for the console and the gui currently has redundant code and is highly coupled to maintain
consistency between the 2 UIs. This can be solved by having an abstract renderer class which handles the core rendering
logic and the corresponding console and GUI renderers extend this class and implement the corresponding displaying logic.

2) The rendering logic currently involves explicitly having to call the renderer to render a segment tree each time an
update is made on the tree. To handle this, we could leverage the Observable pattern where the tree renderer would be
the observer and the Segment Tree would be the observable. This way any changes made to the tree would notify the
renderer and perform the corresponding render updates.

3) Adding to the above observable pattern, logging events could also be made to implement the Observable pattern.