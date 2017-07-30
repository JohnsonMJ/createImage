Name: createImage.jar
Author: Matt Johnson
Date: 30/07/2017
Submission for SEQTA

-----------
Description
-----------
This program generates, then displays an image. The image contains colours
made up of combinations of RGB values in steps of 8 (starting from 7 and
ending at 255, each with a full alpha channel). The image contains each
unique colour generated this way exactly once, with no repeats and no
empty pixels. It then creates a window to display the image it has
generated.

-------
Running
-------
Open a command prompt or terminal in the same directory as
createImage.jar, then run it by typing in the following:

java -jar "createImage.jar" 

----------------------
Command line arguments
----------------------
createImage.jar allows for different options to be selected in the form of
command line arguments. To launch createImage.jar with a command line
argument, run it from the command prompt/terminal and add the command
line argument to the end of the command. Examples include:

java -jar "createImage.jar" s
java -jar "createImage.jar" s d

Multiple command line arguments can be added, however multiple uses of
the same argument are redundant.

**********
Save image
**********
Argument: s (not case sensitive)
This argument saves the generated image in the same director as
createImage.jar. Saving the image replaces displaying the image in a
window. When saved the program will display a prompt. Any input will end
the program.

****************
Check duplicates
****************
Argument: d (not case sensitive)
This argument checks the input image for duplicate colours, then outputs
the result to the terminal. This function is disabled by default to
decrease processing time.