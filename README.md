# Segment Tree Visualizer

## Learn how a Segment Tree operates

The Segment Tree Visualizer is a tool to help visualize and understand how a segment tree works. The primary focus here
is on visualizing how the operations in the tree are handled.
The tool includes support for using multiple merge functions in the tree and provides a console and GUI option to 
visualize the tree. The tree can be initialized with custom user input, with upto 32 distinct values.

This tool could be really helpful for people who are into competitive programming or just about any person looking to 
learn about Segment trees. This could also be helpful for those who are already familiar with how it works but are just
looking for a quick visualization since this could be time-consuming to draw.

### What is a Segment Tree?

Segment Tree is a powerful tree data structure that helps with both querying for instance, sum in a given range and also performing 
updates in logarithmic time. 

Segment tree has a lot of uses and I've come across it frequently in competitive programming. It is quite rare to come
across questions that necessarily need operations to be done in constant time. So in most cases, if you are familiar with
segment trees this can also be used in the place of Binary lifting for RMQ or Fenwick tree.


### What's the time complexity?

- Building the tree: O(n) - The current algorithms uses 4*n steps to build the tree, where n is the size of an array

- Querying sum in a range: O(logn)

- Updating a value: O(logn)


### Why use a Segment Tree?

Suppose an array is used to perform the same operations, the following is the time complexity:

- Querying sum in a range: O(n) (Can be brought down to O(1) with a prefix array)
- Updating a value: O(n)

It can be noticed that updating a value takes linear time and this could mean a huge difference in performance when
n >= 1e9.

### Known refactoring issues:
1) Rendering trees for the console and the gui currently has redundant code and is highly coupled to maintain
consistency between the 2 UIs. This can be solved by having an abstract renderer class which handles the core rendering
logic and the corresponding console and GUI renderers extend this class and implement the corresponding displaying logic.

2) The rendering logic currently involves explicitly having to call the renderer to render a segment tree each time an
update is made on the tree. To handle this, we could leverage the Observable pattern where the tree renderer would be
the observer and the Segment Tree would be the observable. This way any changes made to the tree would notify the
renderer and perform the corresponding render updates.

3) Adding to the above observable pattern, logging events could also be made to implement the Observable pattern.

