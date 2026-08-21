# ET UI Test Plan

## Test configuration

- Required Java version: `25`
- Compile command: `javac -Xlint:all -d out src/main/java/*.java`
- Run command: `java -cp out ET`

## Test cases

### Add and list every task type

Aim: Verify that ToDos, deadlines, and events are added and displayed through one polymorphic task list.

Inputs:
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output:
```text
____________________________________________________________
 _____ _____
| ____|_   _|
|  _|   | |
| |___  | |
|_____| |_|
Hello, friend! I'm ET, a gentle visitor from far away.
I may be a little lost, but I would be happy to help with your tasks.
What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] borrow book
     2.[D][ ] return book (by: Sunday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Mark and unmark an event

Aim: Verify that common Task completion behaviour works polymorphically for an Event and preserves its date strings.

Inputs:
```text
event orientation week /from 4/10/2019 /to 11/10/2019
mark 1
unmark 1
list
bye
```

Expected output:
```text
____________________________________________________________
 _____ _____
| ____|_   _|
|  _|   | |
| |___  | |
|_____| |_|
Hello, friend! I'm ET, a gentle visitor from far away.
I may be a little lost, but I would be happy to help with your tasks.
What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [E][X] orientation week (from: 4/10/2019 to: 11/10/2019)
____________________________________________________________
____________________________________________________________
     OK, I've marked this task as not done yet:
       [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```
