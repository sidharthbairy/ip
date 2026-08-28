# ET UI Test Plan

## Test configuration

- Required Java version: `25`
- Compile command: `javac -Xlint:all -d out src/main/java/*.java`
- Run command: `java -cp out ET`

## Test cases

### Parse and display dates and times

Aim: Verify that single-digit date parts are accepted without a time, while date-times are stored and displayed in a readable format.

Inputs:
```text
deadline submit assignment /by 2019-1-5
event project meeting /from 2/1/2019 /to 3/1/2019
deadline return book /by 2/12/2019 1800
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
       [D][ ] submit assignment (by: Jan 05 2019)
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Jan 02 2019 to: Jan 03 2019)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Dec 02 2019 6:00 PM)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] submit assignment (by: Jan 05 2019)
     2.[E][ ] project meeting (from: Jan 02 2019 to: Jan 03 2019)
     3.[D][ ] return book (by: Dec 02 2019 6:00 PM)
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Reject invalid or reversed event dates

Aim: Verify that invalid date text and an event ending before it starts do not add tasks, while a valid event still uses formatted times.

Inputs:
```text
todo read notes
deadline report /by Friday
event backwards /from 2/12/2019 1800 /to 2/12/2019 1700
event planning /from 2/12/2019 0900 /to 2/12/2019 1000
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
     Please use yyyy-M-d or d/M/yyyy, optionally followed by HHmm, for example 2019-1-5, 2/1/2019, or 2/12/2019 1800.
____________________________________________________________
____________________________________________________________
     The event end date and time cannot be before its start.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] planning (from: Dec 02 2019 9:00 AM to: Dec 02 2019 10:00 AM)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read notes
     2.[E][ ] planning (from: Dec 02 2019 9:00 AM to: Dec 02 2019 10:00 AM)
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```
