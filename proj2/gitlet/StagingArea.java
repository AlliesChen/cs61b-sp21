package gitlet;

import java.io.Serializable;
import java.util.*;

public class StagingArea implements Serializable {
    private TreeMap<String, String> stagedFiles; // Maps file name to blob hash
    private HashSet<String> removedFiles; // Stores files marked for removal

    public StagingArea() {
        this.stagedFiles = new TreeMap<>();
        this.removedFiles = new HashSet<>();
    }

    // Add a file to the staging area
    public void stageFile(String fileName, String fileHash) {
        stagedFiles.put(fileName, fileHash);
        removedFiles.remove(fileName); // If it's marked for removal, unmark it
    }

    // Mark a file for removal
    public void removeFile(String fileName) {
        stagedFiles.remove(fileName); // If it's staged, unstage it
        removedFiles.add(fileName); // Mark it for removal
    }

    // Get the staged files
    public TreeMap<String, String> getStagedFiles() {
        return stagedFiles;
    }

    // Get the removed files
    public HashSet<String> getRemovedFiles() {
        return removedFiles;
    }

    // Clear the staging area after a commit
    public void clear() {
        stagedFiles.clear();
        removedFiles.clear();
    }

}
