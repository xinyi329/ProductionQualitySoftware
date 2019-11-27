# Connect Four

Author: Xinyi Liu (xl2700), xinyi.liu@nyu.edu

## Design Patterns

* Observer Pattern: `Model` & `Listener` & `Viewer`
* Builder Pattern: `Move`, `HumanPlayer`, `MachinePlayer`
* Factory Pattern: `Player` & `HumanPlayer` & `MachinePlayer` & `PlayerFactory`
* Singleton Pattern: `Model`, `Board`

## Notes

### Game

* Configurations like names and colors in `ConnectFourSettings` can be changed but the game requires `ConnectFourSettings.PLAYER_ONE_COLOR` and `ConnectFourSettings.PLAYER_TWO_COLOR` to be different.

### Implementation

* If `equals()` and `hashCode()` methods are not rewritten, then either the inherited ones satisfy the needs or classes returning a static instance don't need those.
* All the classes are not comparable.
* Overriden `toString()` methods are not tested because these methods are overwritten for the ease when reading logs or debugging, and there is no good way to set the correct answer to compare.
* The test coverage within the package `edu.nyu.pqs.ps4.model` and its subpackages is 100% excluding `toString()` methods.
