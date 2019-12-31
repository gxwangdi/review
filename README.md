A simple program to help you decide which problem you want to review. 

The general idea is that, the less recent problem will get bigger chances 
to get selected for review, and once a problem is reviewed, it is removed 
from the repository unless you init the repo again. 

When running "init -new" command, it only check in problems you worked on 
with bigger last modified timestamp.   

Command list:
init  -all  scan all problems and init repo
      -new  scan new problems and update repo
      -dir  update problem repo dir
next        pick the next problem to review
help        print the command list
quit        quit and persist all problems into a file, which will be resumed 
            the next time the program is up. 
            
            
