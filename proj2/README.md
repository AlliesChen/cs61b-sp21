# Gitlet Design Document

**Name**: Yu-Pang

## Getting started

To set up the project, navigate to the root directory of the project in your command line interface and run:

```bash
make
```

> Note: If you don't have `make` installed, please follow [these instructions](https://sp21.datastructur.es/materials/guides/make-install.html) to install it.

Once the build is complete, you can run the Gitlet application using:

```bash
java gitlet.Main [command] [args...]
```

For example:

```bash
java gitlet.Main init
java gitlet.Main add README.md
java gitlet.Main commit 'Add README'
java checkout -- README.md
```

## Commands

### init

Usage: `init`

Initializes a new Gitlet version-control system in the current directory. Creates a .gitlet directory that will store all of the version control information.

### add

Usage: `add [filename]`

Stages the file specified by `[filename]` for the next commit. The file's content is read, hashed to create a unique blob identifier, and stored in the `objects/` directory. This allows Gitlet to track changes to the file over time.

### commit

Usage: `commit [message]`

Creates a new commit object that records the current state of the staging area, with the provided commit `[message]`. The commit object is saved in the `objects/` directory.

### checkout

- `checkout -- [filename]`    
  Restores the version of the file specified by `[filename]` from the latest commit in the current branch.
- `checkout [commit id] -- [filename]`    
  Restores the version of the file from the specified `[commit id]`. The file from the commit is retrieved from the objects store and written to the working directory.
- `checkout [branch name]`    
  Switches to the specified [branch name], updating the working directory to match the state of that branch.

### log

Usage: `log`

Displays the commit history starting from the current HEAD commit, going back through its parents. Each commit is printed with its ID, timestamp, and message in reverse chronological order. The log displays:

```bash
===
commit <commit-id>
Date: <timestamp>
<commit-message>
```

## Classes and Data Structures

### Class `Main`

Acts as the controller for the application, parsing command-line arguments and delegating commands to the appropriate methods in the `Repository` class.

### Class `Repository`

Implements methods that handle core version control functionality, such as initializing the repository, adding files, committing changes, and checking out files and branches.

#### Fields

1. `CWD`: Represents the current working directory.
2. `GITLET_DIR`: The main directory for the Gitlet repository (.gitlet).
3. `OBJECTS_DIR`: Directory where all objects (commits, blobs) are stored.
4. `REFS_DIR`: Directory that contains heads and remotes references.
5. `HEADS_DIR`: Directory that contains references to heads of branches.
6. `HEAD_FILE`: A file that stores the reference to the current branch.
7. `STAGING_AREA_FILE`: A file that stores the staged files and files to be removed for the next commit.
8. `DEFAULT_BRANCH_NAME`: "master"
9. `DEFAULT_HEAD_POINTER`: "refs/heads/master"
10. `stagingArea`: To be assigned the StagingArea base on the information stored in STAGING_AREA_FILE.

### Class `Commit`

Represents a single commit in the version control system.

#### Fields

- message: The commit message.
- timestamp: The time when the commit was created.
- parents: A list of parent commit IDs (supports merges).
- fileSnapshot: A TreeMap<String, String> that maps filenames to their blob IDs, representing the state of the files at the time of the commit.
- uid: A unique identifier for the commit, generated using SHA-1 hashing of the commit's contents.

The commit object is serialized and saved in `.gitlet/objects/` under its UID.

### Class `StagingArea`

Tracks files that have been added or marked for removal in preparation for the next commit.

#### Fields

- `stagedFiles`: A `TreeMap<String, String>` mapping filenames to blob IDs for files added to the staging area.
- `removedFiles`: A `HashSet<String>` of filenames that are marked for removal.

The staging area is serialized and stored in a file within the `.gitlet` directory.

## Algorithms

### Commit Creation

- Initialization: If there is a parent commit, start with its file snapshot; otherwise, start with an empty snapshot.
- Apply Staged Changes: Update the snapshot with files from the staging area.
- Apply Removals: Remove any files marked for deletion.
- Generate UID: Create a unique ID for the commit by hashing its contents.
- Persistence: Serialize and store the commit object in `objects/`.

### File Checkout

- Locate Commit: Retrieve the commit object from `objects/` using the commit ID (or latest commit for the current branch).
- Retrieve Blob ID: Get the blob ID for the specified file from the commit's file snapshot.
- Restore File: Read the blob from `objects/` and write its contents to the working directory.

### Log Traversal

- **Retrieve HEAD Commit**: Start from the latest commit in the current branch.

- **Print Commit Details**: For each commit, display its ID, timestamp, and message.

- **Traverse Parent Commits**: Move through the commit history by following each commit's parent(s).

## Persistence

- Objects Directory (`objects/`): Stores serialized commit and blob objects, named by their SHA-1 UIDs.
- References Directory (`refs/`): Contains branch pointers that reference the latest commit ID in each branch.
- HEAD File (`HEAD`): Stores the reference to the current branch, typically in the format `refs/heads/[branch name]`.
- Staging Area: Serialized staging area object stored in `.gitlet/staging_area`.

## Additional Notes

- Hashing and Uniqueness: SHA-1 hashing is used to ensure that commits and blobs have unique identifiers based on their contents.
- Branches: Branches are implemented by storing pointers to the latest commit in the `refs/heads/` directory.
- Error Handling: The application provides informative error messages when commands fail due to incorrect usage or invalid repository states.