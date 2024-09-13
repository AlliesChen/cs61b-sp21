package gitlet;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    public static final File STAGING_AREA_FILE = join(GITLET_DIR, "staging_area");
    public static final String DEFAULT_BRANCH_NAME = "master";
    public static final String DEFAULT_HEAD_POINTER = "refs/heads/" + DEFAULT_BRANCH_NAME;
    private StagingArea stagingArea;

    /* TODO: fill in the rest of this class. */
    // create a .gitlet directory
    public void init() {
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        // Create the directory structure for the Gitlet VCS
        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        REFS_DIR.mkdir();
        HEADS_DIR.mkdir();

        // Create the initial commit with no parents
        Commit initialCommit = new Commit("initial commit");
        saveCommit(initialCommit);

        // Set default branch reference
        File branchFile = new File(HEADS_DIR, DEFAULT_BRANCH_NAME);
        updateBranchReference(branchFile, initialCommit.getUid());

        // Set up the HEAD to point to the master branch reference
        Utils.writeContents(HEAD_FILE, DEFAULT_HEAD_POINTER);
    }

    public void add(String fileName) {
        this.stagingArea = loadStagingArea(); // Load the staging area from disk

        // Check if the file path is absolute or relative
        File fileToAdd = Paths.get(fileName).isAbsolute()
                ? new File(fileName)
                : join(CWD, fileName);

        // Check if the file exists in the working directory
        if (!fileToAdd.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        // Generate the file's blob hash
        byte[] fileContents = Utils.readContents(fileToAdd);
        String currentFileHash = Utils.sha1(fileContents);

        // Get the latest commit and its snapshot
        Commit latestCommit = getLatestCommit();
        TreeMap<String, String> latestSnapshot = latestCommit != null
                ? latestCommit.getFileSnapshot()
                : new TreeMap<>();

        // Compare the current file hash with the latest commit's file hash
        if (latestSnapshot.containsKey(fileName)) {
            String latestFileHash = latestSnapshot.get(fileName);

            // If the file hasn't changed, skip adding it to the staging area
            if (currentFileHash.equals(latestFileHash)) {
                stagingArea.getStagedFiles().remove(fileName); // Unstage if it was staged
                saveStagingArea(); // Save the updated staging area;
                return;
            }
        }
        // Store the blob (file contents) in the object directory
        File blobFile = Utils.join(OBJECTS_DIR, currentFileHash);
        if (!blobFile.exists()) {
            Utils.writeContents(blobFile, fileContents); // Store the file content under its hash
        }
        // Stage the file for the next commit
        stagingArea.stageFile(fileName, currentFileHash);
        saveStagingArea();
    }

    public void commit(String message) {
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0); // Abort the commit
        }

        // Load the staging area
        this.stagingArea = loadStagingArea();

        if (stagingArea.getStagedFiles().isEmpty() && stagingArea.getRemovedFiles().isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        // Get the current HEAD commit
        Commit parentCommit = getLatestCommit();
        TreeMap<String, String> fileSnapshot = new TreeMap<>();
        List<String> parentUids = new ArrayList<>();

        if (parentCommit != null) {
            fileSnapshot.putAll(parentCommit.getFileSnapshot());
            parentUids.add(parentCommit.getUid());
        }
        fileSnapshot.putAll(stagingArea.getStagedFiles());

        for (String fileName : stagingArea.getRemovedFiles()) {
            fileSnapshot.remove(fileName);
        }

        Commit newCommit = new Commit(
                message,
                parentUids,
                fileSnapshot
        );

        saveCommit(newCommit);
        File branchFile = getCurrentBranchReference();
        updateBranchReference(branchFile, newCommit.getUid());
        removeStagingArea();
    }

    public void checkoutFile(String fileName) {
        Commit commit = getLatestCommit();
        TreeMap<String, String> snapshot = commit.getFileSnapshot();
        // Check if the file exists in the commit
        if (!snapshot.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        // Retrieve the blob hash and find the blob file
        String blobHash = snapshot.get(fileName);
        File blobFile = Utils.join(OBJECTS_DIR, blobHash);

        // Handle missing blob files
        if (!blobFile.exists()) {
            System.out.println("Blob not found for file: " + fileName);
            System.exit(0);
        }
        // Read the blob contents
        byte[] blobContents = Utils.readContents(blobFile);

        // Write the contents back to the working directory, overwriting any existing file
        Utils.writeContents(new File(CWD,fileName), blobContents);
    }

    public File getCurrentBranchReference() {
        // Read the relative path of the branch reference from the HEAD file
        String branchRef = Utils.readContentsAsString(HEAD_FILE).trim();

        // Find the file corresponding to the branch in GITLET_DIR
        return join(GITLET_DIR, branchRef);
    }

    // Retrieve the latest commit from the current branch
    public Commit getLatestCommit() {
        File branchFile = getCurrentBranchReference();

        // Read the commit ID stored in the branch file
        String latestCommitId = Utils.readContentsAsString(branchFile).trim();
        // Retrieve the corresponding commit object from the objects directory
        File commitFile = join(OBJECTS_DIR, latestCommitId);

        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Serializes and saves a commit to the objects directory.
     * @param commit The commit object to be saved.
     */
    public void saveCommit(Commit commit) {
        File commitFile = new File(OBJECTS_DIR, commit.getUid());
        Utils.writeObject(commitFile, commit);
    }

    public void updateBranchReference(File branchFile, String commitId) {
        Utils.writeContents(branchFile, commitId);
    }

    // Load the staging area from disk
    public StagingArea loadStagingArea() {
        return STAGING_AREA_FILE.exists()
            ? Utils.readObject(STAGING_AREA_FILE, StagingArea.class)
            : new StagingArea(); // New staging area if none exists yet
    }

    public void removeStagingArea() {
        String pathToBeRemoved = STAGING_AREA_FILE.getAbsolutePath();
        boolean isRemoved = STAGING_AREA_FILE.delete();
        if (isRemoved) {
            // System.out.println("File removed: " + pathToBeRemoved);
        } else {
            System.out.println("File is not removed: " + pathToBeRemoved);
        }
    }

    // Save the staging area to disk
    public void saveStagingArea() {
        Utils.writeObject(STAGING_AREA_FILE, stagingArea);
    }
}

