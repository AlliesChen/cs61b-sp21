package gitlet;

// TODO: any imports you need here
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yu-Pang
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

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

    /* TODO: fill in the rest of this class. */
    /** Commit constructor for initial commit (no parents). */
    public Commit(String message, List<String> parents, TreeMap<String, String> snapshot) {
        this.message = message;

        // Ensure parents is never null; use an empty ArrayList if no parents
        if (parents == null) {
            this.parents = new ArrayList<>();
        } else {
            this.parents = parents;
        }

        this.fileSnapshot = snapshot != null ? snapshot : new TreeMap<>();

        // get the current time
        Date now = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        this.timestamp = dateFormat.format(now);

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
