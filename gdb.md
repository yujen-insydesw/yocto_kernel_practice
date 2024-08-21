## GDB command for debugging

### Starting GDB:
```console
gdb <executable>
```
### Setting Breakpoints:
```console
break <line_number> or b <line_number>
break <function>
break <file|swap.c>:<line_number>
```
### Setting arguments:
```console
set variable x = 10
set args <arg1> <arg2>
set environment PATH = /usr/local/bin
```
### Running the Program (until breakpoint or finish):
```console
run or r

# with arguments
r <arg1> <arg2> ...

# stop at main()
start <arg1> <arg2> ...
```
### Stepping Through Code:
```console
next or n
nexti or ni

step or s
stepi or si

continue or c

# continue running until finish
finish
```
### Examining the Program State:
```console
# print
print <variable> or p <variable>

# Show the global and static variable names
info variables

# Show the values of local variables in the current scope
info locals

# List all breakpoints that have been set
info breakpoints
info b

# Print the current call stack of the program
backtrace or bt
```
### Managing Breakpoints:
```console
delete <id>
disable <id>
enable <id>
clear <line_num>
clear <file>:<line_num>
clear
```
### Others:
```console
quit or q
help or h

# List source code around the current line
list or l
```
