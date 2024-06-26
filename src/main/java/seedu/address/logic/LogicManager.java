package seedu.address.logic;

import static seedu.address.logic.Messages.MESSAGE_CONFIRMATION;
import static seedu.address.logic.Messages.MESSAGE_CONFIRMATION_CANCELLED;
import static seedu.address.logic.Messages.MESSAGE_CONFIRMATION_ERROR_AUTO_CANCELLED;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    private Command prevCommand;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText, boolean isConfirmation) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;

        if (isConfirmation) {
            commandResult = handleConfirmation(commandText);
        } else {
            Command command = addressBookParser.parseCommand(commandText);
            prevCommand = command;

            Confirmation confirmation = new Confirmation(command);
            if (!confirmation.isToProceed()) {
                return new CommandResult(MESSAGE_CONFIRMATION, true, false, false);
            }

            commandResult = command.execute(model);
        }

        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Assignment> getFilteredAssignmentList() {
        return model.getFilteredAssignmentList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    /**
     * Handles command confirmation stage.
     *
     * @param commandText The confirmation text.
     * @return the result of the command execution.
     */
    public CommandResult handleConfirmation(String commandText) {
        try {
            if (commandText.equalsIgnoreCase("y")) {
                return prevCommand.execute(model);
            }

            return new CommandResult(MESSAGE_CONFIRMATION_CANCELLED);
        } catch (CommandException e) {
            return new CommandResult(
                    e.getMessage()
                    + "\n"
                    + MESSAGE_CONFIRMATION_ERROR_AUTO_CANCELLED
            );
        }
    }
}
