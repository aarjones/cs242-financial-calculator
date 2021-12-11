# Clarkson University CS242: Financial Calculator  

### Authors: Matthew Sprague, Aaron Jones  

This is the repo for our implementation of a basic Financial Calculator GUI, developed using JavaFX as a bonus project for our CS242 class.

This financial calculator can take in an arbitrary principal value and a duration, given in months.  The user then chooses between two different types of interest calculations: Simple Interest or Compound Interest.  This can be done by either interacting with the drop-down menu listed in the GUI or by pressing the 'I' key.  The user can then specify a monthly interest rate ranging from 0.00% to 100.00% using a slider.  Every time that the inputs are modified, the final amount after interest is re-calculated.  This saves the user from having to press a separate button to re-compute the new amount, thereby making for a better user experience.

The financial calculator is designed with a "backend" `FinCalc.java` and a "frontend" `FinCalcGUI.java`.  The interest calculations are handled by the backend class, and the different types of interest calculation are given as an enumeration.  In this way, additional types of interest calculations could be added quickly by adding an additional entry to the enumeration modifying the `compute()` function.  The frontend application then handles the creation of the GUI and the parsing of inputs from the user.  
