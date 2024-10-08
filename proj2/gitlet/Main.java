package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yu-Pang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // DONE: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command");
            System.exit(0);
        }
        
        Repository repo = new Repository();
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // DONE: handle the `init` command
                repo.init();
                break;
            case "add":
                // DONE: handle the `add [filename]` command
                checkArgumentsQty(args, 2);
                repo.add(args[1]);
                break;
            case "commit":
                checkArgumentsQty(args, 2);
                repo.commit(args[1]);
                break;
            case "checkout":
                handleCheckout(args, repo);
                break;
            case "log":
                repo.log();
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    public static void checkArgumentsQty(String[] args, int num) {
        if (args.length != num) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    /**
     * Handles the checkout command with various forms.
     * @param args The command-line arguments.
     */
    public static void handleCheckout(String[] args, Repository repository) {
        if (args.length == 2) { // checkout [branch name]
        } else if (args.length == 3) { // checkout -- [filename]
            repository.checkoutFile(args[2]);
        } else if (args.length == 4) { // checkout [commit id] -- [filename]
            repository.checkoutFileFromCommit(args[1], args[3]);
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
