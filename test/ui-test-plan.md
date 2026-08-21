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

### Delete a task and renumber the remaining list

Aim: Verify that deleting a task removes the requested task, reports the new count, and renumbers the remaining tasks.

Inputs:
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo borrow book
delete 3
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
       [T][ ] read book
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: June 6th)
     3.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Report invalid commands without ending the session

Aim: Verify that invalid commands are reported with ET-specific exceptions and that ET continues accepting later valid commands.

Inputs:
```text
todo
blah
todo buy milk
mark one
mark 2
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
     Please provide a description for the ToDo.
____________________________________________________________
____________________________________________________________
     I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] buy milk
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Please give a valid task number after mark.
____________________________________________________________
____________________________________________________________
     That task number is not in the current list.
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Invalid deadline commands preserve the task list

Aim: Verify that malformed deadlines do not add incomplete tasks, while valid deadlines added between errors remain in the correct order.

Inputs:
```text
todo read notes
deadline /by Friday
list
deadline submit report /by
list
deadline submit report /by Friday
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
       [T][ ] read notes
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Please provide a description for the deadline.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read notes
____________________________________________________________
____________________________________________________________
     Please provide a date or time after /by.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read notes
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] submit report (by: Friday)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read notes
     2.[D][ ] submit report (by: Friday)
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Invalid event commands and task references preserve state

Aim: Verify that invalid event fields and an out-of-range unmark leave the existing event and its completion status unchanged.

Inputs:
```text
event demo /from Mon /to Tue
mark 1
event /from Wed /to Thu
list
event prep /from /to Thu
list
event prep /from Wed /to
list
unmark 2
list
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
       [E][ ] demo (from: Mon to: Tue)
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [E][X] demo (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
     Please provide a description for the event.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[E][X] demo (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
     Please provide a starting date or time after /from.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[E][X] demo (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
     Please provide an ending date or time after /to.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[E][X] demo (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
     That task number is not in the current list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[E][X] demo (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
     OK, I've marked this task as not done yet:
       [E][ ] demo (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[E][ ] demo (from: Mon to: Tue)
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```
