# Gitlet
A Github-like Version Control System for storing file backups.

## Getting started
Compile all the java classes:
`javac gitlet/Main.java gitlet/Staging.java gitlet/Repo.java gitlet/Branch.java gitlet/Commit.java gitlet/Head.java gitlet/Status.java`

## How to use Gitlet

To start a repo:
`java gitlet.Main init`

To add a file:
`java gitlet.Main add [file name]`

To make a commit:
`java gitlet.Main commit [message]`

To remove a file:
`java gitlet.Main rm [file name]`

To see the commit history:
`java gitlet.Main log`

To see the global commit history:
`java gitlet.Main global-log`

To find commits that contain a given message:
`java gitlet.Main find [commit message]`

To see the status (current branch, staged files, removed files, modified not staged for files, and untracked files):
`java gitlet.Main status`

To checkout a file:
`java gitlet.Main checkout -- [file name]`

To checkout to a commit:
`java gitlet.Main checkout [commit id] -- [file name]`

To checkout to a branch:
`java gitlet.Main checkout [commit id] -- [file name]`

To create a branch:
`java gitlet.Main branch [branch name]`

To remove a branch:
`java gitlet.Main rm-branch [branch name]`

To reset to a commit:
`java gitlet.Main reset [commit id]`

To merge files from the given branch into the current branch:
`java gitlet.Main merge [branch name]`

## Acknowledgement
This implementation follows the design of Gitlet[https://cs61bl.org/su20/projects/gitlet/#acknowledgments] owned by the staff of the EECS department at University of California, Berkeley.

To future CS61B/BL students, don't copy this implementation. You can come up with your own design.
