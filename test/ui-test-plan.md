# ET UI Test Plan

## Test configuration

- Required Java version: `25`
- Compile command: `./gradlew classes`
- Run command: `java -cp build/classes/java/main et.ConsoleLauncher`

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

### Accept common date and time formats

Aim: Verify that year-first, month-name, 12-hour, and 24-hour inputs are accepted when the time follows the date.

Inputs:
```text
deadline file taxes /by 2019/4/15 18:30
deadline attend launch /by April 16, 2019 6:45 pm
event workshop /from 17-Apr-2019 9am /to Apr 17 2019 10:30 AM
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
       [D][ ] file taxes (by: Apr 15 2019 6:30 PM)
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] attend launch (by: Apr 16 2019 6:45 PM)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] workshop (from: Apr 17 2019 9:00 AM to: Apr 17 2019 10:30 AM)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] file taxes (by: Apr 15 2019 6:30 PM)
     2.[D][ ] attend launch (by: Apr 16 2019 6:45 PM)
     3.[E][ ] workshop (from: Apr 17 2019 9:00 AM to: Apr 17 2019 10:30 AM)
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
     Please enter a valid date, optionally followed by a time. Examples: 2019-01-05, 5/1/2019, 5 Jan 2019, or Jan 5, 2019 6:30 PM.
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

### Mark and unmark a task

Aim: Verify that task-status commands update and display the selected task.

Inputs:
```text
todo read notes
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
       [T][ ] read notes
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [T][✓] read notes
____________________________________________________________
____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read notes
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read notes
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Find tasks by keyword

Aim: Verify that find displays every task whose description contains the keyword, regardless of letter case.

Inputs:
```text
todo read book
deadline return Book /by 6/6/2019
todo buy groceries
mark 1
mark 2
find book
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
       [D][ ] return Book (by: Jun 06 2019)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] buy groceries
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [T][✓] read book
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [D][✓] return Book (by: Jun 06 2019)
____________________________________________________________
____________________________________________________________
     Here are the matching tasks in your list:
     1.[T][✓] read book
     2.[D][✓] return Book (by: Jun 06 2019)
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Delete a task

Aim: Verify that deleting a task removes it from the list and reports the remaining count.

Inputs:
```text
todo feed cat
delete 1
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
       [T][ ] feed cat
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] feed cat
     Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Reject invalid task numbers

Aim: Verify that malformed and unavailable task numbers show the existing helpful errors.

Inputs:
```text
todo read notes
mark first
delete 2
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
     Please give a valid task number after mark.
____________________________________________________________
____________________________________________________________
     That task number is not in the current list.
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```

### Sort tasks without changing their original numbers

Aim: Verify that sort groups incomplete tasks before completed tasks, orders dated tasks chronologically, preserves canonical task numbers, and does not reorder the task list.

Inputs:
```text
deadline later /by 10/1/2027
deadline earlier /by 3/1/2027
todo undated
mark 1
sort
mark 2
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
       [D][ ] later (by: Jan 10 2027)
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] earlier (by: Jan 03 2027)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] undated
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [D][✓] later (by: Jan 10 2027)
____________________________________________________________
____________________________________________________________
     Here are your tasks sorted chronologically:
     2.[D][ ] earlier (by: Jan 03 2027)
     3.[T][ ] undated
     1.[D][✓] later (by: Jan 10 2027)
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [D][✓] earlier (by: Jan 03 2027)
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[D][✓] later (by: Jan 10 2027)
     2.[D][✓] earlier (by: Jan 03 2027)
     3.[T][ ] undated
____________________________________________________________
____________________________________________________________
     Bye. Hope to see you again soon!
____________________________________________________________
```
