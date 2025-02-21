package seedu.tracker.model;

import static java.util.Objects.requireNonNull;
import static seedu.tracker.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.tracker.commons.core.GuiSettings;
import seedu.tracker.commons.core.LogsCenter;
import seedu.tracker.model.calendar.AcademicCalendar;
import seedu.tracker.model.module.Mc;
import seedu.tracker.model.module.Module;

/**
 * Represents the in-memory model of the mod tracker data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ModuleTracker moduleTracker;
    private final UserPrefs userPrefs;
    private final FilteredList<Module> filteredModules;

    /**
     * Initializes a ModelManager with the given moduleTracker and userPrefs.
     */
    public ModelManager(ReadOnlyModuleTracker moduleTracker, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(moduleTracker, userPrefs);

        logger.fine("Initializing with mod tracker: " + moduleTracker + " and user prefs " + userPrefs);

        this.moduleTracker = new ModuleTracker(moduleTracker);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredModules = new FilteredList<>(this.moduleTracker.getModuleList());
    }

    public ModelManager() {
        this(new ModuleTracker(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getModuleTrackerFilePath() {
        return userPrefs.getModuleTrackerFilePath();
    }

    @Override
    public void setModuleTrackerFilePath(Path moduleTrackerFilePath) {
        requireNonNull(moduleTrackerFilePath);
        userPrefs.setModuleTrackerFilePath(moduleTrackerFilePath);
    }

    //=========== ModuleTracker ================================================================================

    @Override
    public void setModuleTracker(ReadOnlyModuleTracker moduleTracker) {
        this.moduleTracker.resetData(moduleTracker);
    }

    @Override
    public ReadOnlyModuleTracker getModuleTracker() {
        return moduleTracker;
    }

    @Override
    public void setCurrentSemester(AcademicCalendar academicCalendar) {
        this.moduleTracker.setCurrentSemester(academicCalendar);
    }

    @Override
    public AcademicCalendar getCurrentSemester() {
        return moduleTracker.getCurrentSemester();
    }

    @Override
    public void setMcGoal(Mc mcGoal) {
        this.moduleTracker.setMcGoal(mcGoal);
    }

    @Override
    public Mc getMcGoal() {
        return moduleTracker.getMcGoal();
    }

    @Override
    public boolean hasModule(Module module) {
        requireNonNull(module);
        return moduleTracker.hasModule(module);
    }

    @Override
    public void deleteModule(Module target) {
        moduleTracker.removeModule(target);
    }

    @Override
    public void addModule(Module module) {
        moduleTracker.addModule(module);
        updateFilteredModuleList(PREDICATE_SHOW_ALL_MODULES);
    }

    @Override
    public void setModule(Module target, Module editedModule) {
        requireAllNonNull(target, editedModule);

        moduleTracker.setModule(target, editedModule);
    }

    //=========== Filtered Module List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Module} backed by the internal list of
     * {@code versionedModuleTracker}
     */
    @Override
    public ObservableList<Module> getFilteredModuleList() {
        return filteredModules;
    }

    @Override
    public void updateFilteredModuleList(Predicate<Module> predicate) {
        requireNonNull(predicate);
        filteredModules.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return moduleTracker.equals(other.moduleTracker)
                && userPrefs.equals(other.userPrefs)
                && filteredModules.equals(other.filteredModules);
    }

}
