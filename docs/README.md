# ET User Guide

ET saves tasks automatically in `data/tasks.txt`, relative to the project
root, whenever you add, mark, unmark, or delete one. On startup, ET
loads the saved tasks again.

## Date and time formats

ET accepts dates with `-`, `/`, or `.` separators, such as `2019-01-05`,
`2019/1/5`, and `5/1/2019`. You can also use month names, such as
`5 Jan 2019`, `January 5, 2019`, or `5-Jan-2019`. Ambiguous numeric dates are
interpreted as day-first.

An optional time must follow the date. Both 24-hour and 12-hour forms are
accepted, including `1800`, `18:00`, `6pm`, and `6:30 PM`.

## Adding tasks

### ToDos

Use `todo` for a task without a date or time:

```
todo borrow book
```

ET displays it as:

```
[T][ ] borrow book
```

### Deadlines

Use `/by` to specify when a task must be completed:

```
deadline return book /by Sunday
```

ET displays it as:

```
[D][ ] return book (by: Sunday)
```

### Events

Use `/from` and `/to` to specify an event's start and end:

```
event project meeting /from Mon 2pm /to 4pm
```

ET displays it as:

```
[E][ ] project meeting (from: Mon 2pm to: 4pm)
```

## Listing and completing tasks

Use `list` to display every task. Use `mark <number>` to complete a task and
`unmark <number>` to mark it as not done again.

## Sorting tasks

Use `sort` to display a temporary chronological view of your tasks. Incomplete
tasks appear before completed tasks. Within each group, deadlines are ordered
by their due date, events by their start date, and ToDos appear after tasks
with dates.

The numbers shown are the tasks' original numbers, so you can use them with
`mark`, `unmark`, or `delete`. Sorting does not change the order shown by
`list` or `find`, and it does not change the saved task order.

## Finding tasks

Use `find <keyword>` to display tasks whose descriptions contain the keyword.
The search is case-insensitive and preserves the matching tasks' list order:

```
find book
```
