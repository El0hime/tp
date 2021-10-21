package seedu.tracker.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tracker.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tracker.testutil.Assert.assertThrows;
import static seedu.tracker.testutil.TypicalModules.getTypicalModuleTracker;

import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.tracker.model.Model;
import seedu.tracker.model.ModelManager;
import seedu.tracker.model.UserPrefs;
import seedu.tracker.model.calendar.AcademicCalendar;
import seedu.tracker.model.calendar.AcademicYear;
import seedu.tracker.model.calendar.Semester;
import seedu.tracker.model.module.Code;
import seedu.tracker.model.module.Description;
import seedu.tracker.model.module.Mc;
import seedu.tracker.model.module.Module;
import seedu.tracker.model.module.ModuleInSpecificSemesterPredicate;
import seedu.tracker.model.module.Title;
import seedu.tracker.model.tag.Tag;

class ClearCommandTest {
    private Model model = new ModelManager(getTypicalModuleTracker(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalModuleTracker(), new UserPrefs());

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ClearCommand(null));
    }

    @Test
    public void equals() {
        AcademicYear year = new AcademicYear(2);
        Semester semester = new Semester(1);
        AcademicCalendar firstAcademicCalendar = new AcademicCalendar(year, semester);

        year = new AcademicYear(1);
        semester = new Semester(1);
        AcademicCalendar secondAcademicCalendar = new AcademicCalendar(year, semester);

        ClearCommand clearY2S1Command = new ClearCommand(firstAcademicCalendar);
        ClearCommand clearY1S1Command = new ClearCommand(secondAcademicCalendar);

        // same object -> returns true
        assertTrue(clearY2S1Command.equals(clearY2S1Command));

        // same values -> returns true
        ClearCommand clearY2S1CommandCopy = new ClearCommand(firstAcademicCalendar);
        assertTrue(clearY2S1Command.equals(clearY2S1CommandCopy));

        // different types -> returns false
        assertFalse(clearY2S1Command.equals(1));

        // null -> returns false
        assertFalse(clearY2S1Command.equals(null));

        // different academic year -> returns false
        assertFalse(clearY2S1Command.equals(clearY1S1Command));
    }

    @Test
    public void execute_allFieldsSpecified_clear() {
        AcademicYear year = new AcademicYear(1);
        Semester semester = new Semester(1);
        AcademicCalendar academicCalendar = new AcademicCalendar(year, semester);

        model.updateFilteredModuleList(preparePredicate(1, 1));
        ObservableList<Module> filteredList = model.getFilteredModuleList();
        model.updateFilteredModuleList(x->true);

        for (Module module : filteredList) {
            expectedModel.setModule(module, createUnscheduledModule(module));
        }

        String expectedMessage = String.format(ClearCommand.MESSAGE_SUCCESS, academicCalendar);

        ClearCommand command = new ClearCommand(academicCalendar);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    /**
     * Creates a {@code Module} without an academicCalendar attribute.
     * @param module to be unscheduled.
     * @return {@code Module} without the academicCalendar attribute.
     */
    private Module createUnscheduledModule(Module module) {
        Code code = module.getCode();
        Title title = module.getTitle();
        Description desc = module.getDescription();
        Mc mc = module.getMc();
        Set<Tag> tag = module.getTags();
        return new Module(code, title, desc, mc, tag);
    }

    private ModuleInSpecificSemesterPredicate preparePredicate(int year, int sem) {
        AcademicYear academicYear = new AcademicYear(year);
        Semester semester = new Semester(sem);
        AcademicCalendar academicCalendar = new AcademicCalendar(academicYear, semester);
        return new ModuleInSpecificSemesterPredicate(academicCalendar);
    }
}
