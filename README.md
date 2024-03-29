# java-text-moba-part2

## Overview
Text based MOBA with functionalities like strategies: each champion can choose every time the right strategy in order to have the highest chances to survive and win.

## Objective:
The objective of this home assignment is to extend the functionalities
of an already written code, learn new Java design patterns and build
scalable and maintainable code.

## Implementation:
For the Strategy pattern I created an interface for each type of
champion. Then, every champion has two classes that implement the
corresponding interface. These two classes represent the strategies
the champions can choose. Every type of champion has a method that
calculates the hp limits and chooses the right strategy.

I used the Visitor pattern to apply the effects of the angels. The
Angel class is extended by a class for every type of angel. Each angel
has four methods, one for every champion and the champion classes have one
method to let the angel apply its effects.

The Observer pattern is useful to write the results in the file. I
opted to create a single observer with different update methods because
these methods require different parameters and I do not think that it makes
sense to create a new Observer interface for every type of update. I
strongly believe that my solution is clean and solves the problem in an
elegant way. The update methods are called in the notify methods which are
implemented in the angel classes.

## Observations:
The requirements were confusing and the checker suffered too many
changes. Not all the functionalities from the stage 1 worked the
same: damage is rounded after every multiplier now, compared to the stage 1
when we would apply Math.round() on the two abilities just before
adding the two damages together. This assignment lacked continuity. In the
end, everything was solved.
