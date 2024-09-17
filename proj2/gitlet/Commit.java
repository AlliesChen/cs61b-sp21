package gitlet;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/** Represents a gitlet commit object.
 *  @author Yu-Pang
 */
public class Commit implements Serializable {
    /** The message of this Commit. */
    private String message;
    /** The timestamp when this commit was created. */
    private String timestamp;
    /** The parent commits. For single commits, this list will have one parent. */
    private List<String> parents;
    /** A map from file name to its blob hash, sorted by file name. */
    private TreeMap<String, String> fileSnapshot;
    /** The unique identifier (SHA-1 hash) for this commit. */
    private String uid;

    /* DONE: fill in the rest of this class. */
    /** Commit constructor for initial commit (no parents). */
    public Commit(String message) {
        this.message = message;
        this.parents = new ArrayList<>();
        this.fileSnapshot = new TreeMap<>();

        // Generate a timestamp for current time
        this.timestamp = generateTimestamp();

        // Generate the unique ID (UID)
        this.uid = generateUID();
    }
    /** Commit constructor for commits with parents. */
    public Commit(String message, List<String> parents, TreeMap<String, String> snapshot) {
        this.message = message;
        this.parents = parents;
        this.fileSnapshot = snapshot;

        // Generate a timestamp for current time
        this.timestamp = generateTimestamp();

        // Generate the unique ID (UID)
        this.uid = generateUID();
    }

    /** Generate an SHA-1 hash as the commits' UID. */
    private String generateUID() {
        // Generate the SHA-1 hash (assuming Utils.sha1 exists)
        return Utils.sha1(
                message,
                timestamp,
                parents.toString(),
                fileSnapshot.toString()
        );
    }

    /** Generate a timestamp for commit */
    private String generateTimestamp() {
        Date now = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        return dateFormat.format(now);
    }

    /** Getters for commit attributes */
    public String getUid() {
        return uid;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<String> getParents() {
        return parents;
    }

    public TreeMap<String, String> getFileSnapshot() {
        return fileSnapshot;
    }
}
