package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_INDEX_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_NAME_USAGE = COMMAND_WORD
            + ": Deletes the person identified with specified {@code targetName} in the displayed person list.\n"
            + "Parameters: NAME (must be a non-empty and non-blank string)\n"
            + "Example: " + COMMAND_WORD + " Alex Tan";

    public static final String MESSAGE_UNKNOWN_PERSON_NAME = "The person named %s is not found.";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;
    private final String targetName;

    /**
     * Creates an DeleteCommand to delete the person identified with specified {@code targetIndex}
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetName = null;

    }

    /**
     * Creates an DeleteCommand to delete the person identified with specified {@code targetName}
     */
    public DeleteCommand(String targetName) {
        this.targetName = targetName;
        this.targetIndex = null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        return targetIndex == null ? executeDeleteByName(model) : executeDeleteByIndex(model);
    }

    /** Deletes the person identified with specified {@code targetIndex}. */
    private CommandResult executeDeleteByIndex(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        assert targetIndex != null : "targetName should not be null";
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    /** Deletes the person named {@code targetName}. */
    private CommandResult executeDeleteByName(Model model) throws CommandException {
        assert targetName != null : "targetName should not be null";
        String removedFirstSpaceName = targetName.substring(1);
        Predicate<Person> hasExactSameName = (person) -> person.getName().fullName.equals(removedFirstSpaceName);

        model.updateFilteredPersonList(hasExactSameName);
        List<Person> filteredList = model.getFilteredPersonList();
        if (filteredList.isEmpty()) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            throw new CommandException(String.format(MESSAGE_UNKNOWN_PERSON_NAME, removedFirstSpaceName));
        }

        for (int i = 0; i < filteredList.size(); i++) {
            Person personToDelete = filteredList.get(i);
            model.deletePerson(personToDelete);
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, targetName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true; // short circuit if same object
        }

        if (!(other instanceof DeleteCommand)) {
            return false; // instanceof handles nulls
        }

        // state check
        if (targetIndex == null) {
            assert targetName != null : "targetName should not be null";
            return targetName.equals(((DeleteCommand) other).targetName);
        } else {
            return targetIndex.equals(((DeleteCommand) other).targetIndex);
        }
    }
}
