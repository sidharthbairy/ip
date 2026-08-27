# ET User Guide

ET saves tasks automatically in `data/tasks.txt`, relative to the project
root, whenever you add, mark, unmark, or delete one. On startup, ET
loads the saved tasks again. Dates and times are stored as the strings that you
enter, so you can use formats such as `Sunday`,
`11/10/2019 5pm`, or `Mon 2pm`.

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
