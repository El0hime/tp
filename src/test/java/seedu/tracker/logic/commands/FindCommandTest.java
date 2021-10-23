package seedu.tracker.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tracker.commons.core.Messages.MESSAGE_MODULES_LISTED_OVERVIEW;
import static seedu.tracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tracker.testutil.TypicalModules.CS1231S;
import static seedu.tracker.testutil.TypicalModules.CS2030S;
import static seedu.tracker.testutil.TypicalModules.CS2101;
import static seedu.tracker.testutil.TypicalModules.CS2103T;
import static seedu.tracker.testutil.TypicalModules.GEQ1000;
import static seedu.tracker.testutil.TypicalModules.MA1521;
import static seedu.tracker.testutil.TypicalModules.getTypicalModuleTracker;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import seedu.tracker.model.Model;
import seedu.tracker.model.ModelManager;
import seedu.tracker.model.UserInfo;
import seedu.tracker.model.UserPrefs;
import seedu.tracker.model.module.ModuleContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalModuleTracker(), new UserPrefs(), new UserInfo());
    private Model expectedModel = new ModelManager(getTypicalModuleTracker(), new UserPrefs(), new UserInfo());

    @Test
    public void equals() {
        ModuleContainsKeywordsPredicate firstPredicate =
            new ModuleContainsKeywordsPredicate(Collections.singletonList("first"));
        ModuleContainsKeywordsPredicate secondPredicate =
            new ModuleContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different module -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULES_LISTED_OVERVIEW, 0);
        ModuleContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredModuleList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredModuleList());
    }

    @Test
    public void execute_codePredicate_multipleModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULES_LISTED_OVERVIEW, 4);
        ModuleContainsKeywordsPredicate predicate = preparePredicate("c/ CS");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredModuleList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2103T, CS2101, CS1231S, CS2030S), model.getFilteredModuleList());
    }

    @Test
    public void execute_titlePredicate_multipleModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULES_LISTED_OVERVIEW, 2);
        ModuleContainsKeywordsPredicate predicate = preparePredicate("t/ on");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredModuleList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2101, GEQ1000), model.getFilteredModuleList());
    }

    @Test
    public void execute_descriptionPredicate_multipleModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULES_LISTED_OVERVIEW, 2);
        ModuleContainsKeywordsPredicate predicate = preparePredicate("d/ the");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredModuleList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2103T, CS2101), model.getFilteredModuleList());
    }

    @Test
    public void execute_academicYearPredicate_multipleModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULES_LISTED_OVERVIEW, 3);
        ModuleContainsKeywordsPredicate predicate = preparePredicate("y/ 1");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredModuleList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(MA1521, CS1231S, CS2030S), model.getFilteredModuleList());
    }

    @Test
    public void execute_semesterPredicate_multipleModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULES_LISTED_OVERVIEW, 2);
        ModuleContainsKeywordsPredicate predicate = preparePredicate("s/ 1");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredModuleList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(MA1521, CS1231S), model.getFilteredModuleList());
    }

    @Test
    public void execute_multipleKeywords_multipleModulesFound() {
        String expectedMessage = String.format(MESSAGE_MODULES_LISTED_OVERVIEW, 2);
        ModuleContainsKeywordsPredicate predicate = preparePredicate("CS2103T CS2101");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredModuleList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2103T, CS2101), model.getFilteredModuleList());
    }

    /**
     * Parses {@code userInput} into a {@code ModuleContainsKeywordsPredicate}.
     */
    private ModuleContainsKeywordsPredicate preparePredicate(String userInput) {
        return new ModuleContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
