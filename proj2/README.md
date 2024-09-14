# Gitlet Design Document

**Name**: Yu-Pang

## Commands

### init

### add

Usage: `add [filename]`

Read the file content and hash the content to be the file name which is created, and stored in the `objects/` directory for controlling file versions.

### commit

Usage: `commit [message]`

Add the staged files to a `Commit` object and store it in the `objects/` directory.

### checkout

`checkout – [filename]`

## Classes and Data Structures

### *Class* Main

A controller for navigating commands to its handling method in `Repository.java`.

### *Class* Repository

Implement the methods that create and modify files.

#### Fields

1. Field 1
2. Field 2

### *Class* Commit

Define the data structure that stores metadata(message, timestamp, parent commit) and a snapshot of the working directory in the form of the file hashes (the `fileSnapshot` TreeMap). The commit itself is also saved in .gitlet/objects/ under its UID, which is the SHA-1 hash of the commit's metadata.

### *Class* StagingArea

- StagingArea: Tracking . It's stored in a file 

## Algorithms

## Persistence

