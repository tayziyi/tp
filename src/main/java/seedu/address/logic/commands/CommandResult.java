package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** User should confirm if they want to execute the command. */
    private final boolean confirmation;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** Application List Panel Selection */
    // ListPanelView to switch views is inspired by https://github.com/AY2324S1-CS2103T-W08-4/tp
    public enum ListPanelView {
        NO_EFFECT,
        PERSON,
        ASSIGNMENT
    }

    private final ListPanelView listPanelView;
    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean confirmation, boolean showHelp, boolean exit,
                         ListPanelView listPanelView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.confirmation = confirmation;
        this.showHelp = showHelp;
        this.exit = exit;
        this.listPanelView = listPanelView;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields, and ListPanelView to the
     * default value.
     * @param feedbackToUser
     * @param confirmation
     * @param showHelp
     * @param exit
     */
    public CommandResult(String feedbackToUser, boolean confirmation, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.confirmation = confirmation;
        this.showHelp = showHelp;
        this.exit = exit;
        this.listPanelView = ListPanelView.NO_EFFECT;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false,
                ListPanelView.NO_EFFECT);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * {@code listPanelView} and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser, ListPanelView listPanelView) {
        this(feedbackToUser, false, false, false, listPanelView);
    }


    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isConfirmation() {
        return confirmation;
    }
    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public ListPanelView getView() {
        return listPanelView;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && confirmation == otherCommandResult.confirmation
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && listPanelView == otherCommandResult.listPanelView;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, confirmation, showHelp, exit);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("confirmation", confirmation)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("listPanelView", listPanelView)
                .toString();
    }

}
