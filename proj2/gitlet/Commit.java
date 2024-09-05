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
    private final String uid;
    private final String message;
    private final String timeStamp;
    private final Map<String, String> pathToBlobId;
    private final List<String> parents;

    /* TODO: fill in the rest of this class. */
    public Commit(String message, Map<String, String> pathToBlobId, List<String> parents) {
        this.message = message;
        // get the current time
        Date now = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        this.timeStamp = dateFormat.format(now);
        this.pathToBlobId = pathToBlobId;
        this.parents = parents;
        this.uid = Utils.sha1(
                message,
                this.timeStamp,
                this.pathToBlobId.toString(),
                this.parents.toString()
        );
    }
}
