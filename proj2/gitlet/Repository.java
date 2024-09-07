package gitlet;

import jdk.jshell.execution.Util;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
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

        // Serialize and store the initial commit
        File commitFile = new File(OBJECTS_DIR, initialCommit.getUid());
        Utils.writeObject(commitFile, initialCommit);

        // Set up the HEAD to point to the master branch reference
        File masterBranch = new File(HEADS_DIR, "master");
        Utils.writeContents(masterBranch, initialCommit.getUid());
        Utils.writeContents(HEAD_FILE, "refs/heads/master");
    }

    public void add(String fileName) {
        loadStagingArea(); // Load the staging area from disk

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
                : new TreeMap();

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

        // Stage the file for the next commit
        stagingArea.stageFile(fileName, currentFileHash);
        saveStagingArea();
    }

    // Retrieve the latest commit from the current branch
    public Commit getLatestCommit() {
        // Read the current branch from the HEAD file
        String branchName = Utils.readContentsAsString(HEAD_FILE).trim();

        // Find the file corresponding to the branch in GITLET_DIR
        File branchFile = join(GITLET_DIR, branchName);
        // Read the commit ID stored in the branch file
        String latestCommitId = Utils.readContentsAsString(branchFile).trim();
        // Retrieve the corresponding commit object from the objects directory
        File commitFile = join(OBJECTS_DIR, latestCommitId);

        return Utils.readObject(commitFile, Commit.class);
    }

    // Load the staging area from disk
    public void loadStagingArea() {
        if (STAGING_AREA_FILE.exists()) {
            stagingArea = Utils.readObject(STAGING_AREA_FILE, StagingArea.class);
        } else {
            stagingArea = new StagingArea(); // New staging area if none exists yet
        }
    }

    // Save the staging area to disk
    public void saveStagingArea() {
        Utils.writeObject(STAGING_AREA_FILE, stagingArea);
    }
}
