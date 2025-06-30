# wordle-solver
A program that helps play Wordle optimally.

# How to run
```
java WordleSolver.java
```

# How to use
## Word input
Enter the word used in Wordle (Not case-sensitive, must be 5 letters long and must not contain any numbers or special characters).

## Result input
Enter the result for that word in the form of 5 numbers with no space:
- Gray is 0
- Yellow is 1
- Green is 2
  
E.g: If the result is "Gray Yellow Gray Green Gray", then the input is ```01020```.

## Result file
After both inputs, check the ```result.txt``` file and choose a word to enter in as the next guess in Wordle (Note that not all words are valid guesses).

## Start new round and exit the program
- To start a new round without restarting the program, type in ```new``` for either of the inputs.
- To exit the program, type in ```exit``` for either of the inputs or use ```CTRL + C```.



