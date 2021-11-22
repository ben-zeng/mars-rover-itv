ITV Mars Rover Technical Challenge

Libraries used:
- Circe
- Circe Enum

I actually got as far as Part 3: Putting it all together without using any libraries (apart from Scalatest), but bought Circe and Circe Enum to replace the standard Enums in to make it easier for JSON serialisation.

---

Not completed although probably can with a bit more time:
- Autopiloting for when moving off the grid to reach the opposite side if faster
- Avoiding mountain ranges

---

Some improvements that can be made:
- Use refined types for the Grid, right now there's nothing stopping the grid from being initialised with negative numbers. 
- If and when using refined types, will likely need to error handle the case of when the Grid is attempted to be instantiated with negative numbers, possibly with Eithers or Options.
- MarsRoverTest tests both MarsRover and RoverMovements, this can be seperated out.
- Property based testing using Generators.