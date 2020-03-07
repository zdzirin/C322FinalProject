# C322FinalProject
A task-management android application with many neat features, a collaborative effort between me and classmate Chun Lai.

* Sign in and out of different 'accounts' to use notes associated with that account.
* get reminded for tasks based on time and location, the app opening to a task when one of the tasks start times is reached, and 
  sending a notification when a user comes within a mile of the task
* view a google map implemented using google maps api with the locations of all your tasks marked with tooltips on the map.
* pull up a calender to select a date and view a list of all tasks scheduled for that date

The app was created in android studio and coded in Java. The app is supported by a mySQL databases with a table for users and tasks. 
A background services polls location to see if within range of a task.
