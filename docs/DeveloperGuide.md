---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **User profile and value proposition**

The product is intended for insurance agents, advertisers or anyone in adjacent roles to manage their customers' information efficiently.

Users will enjoy the advantages of a command-line interface, as managing contacts can be done more quickly by typing, while a graphical user interface allows the user to visualise their contacts intuitively.

--------------------------------------------------------------------------------------------------------------------

## **Non-functional requirements**

This application should be usable on mainstream OSes as long as Java 11 is installed.

The application should be sufficiently easy to use, that the average owner of a desktop computer should be able to use it efficiently.

--------------------------------------------------------------------------------------------------------------------


## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/elister-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2223S2-CS2103T-T17-3/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2223S2-CS2103T-T17-3/tp/blob/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2223S2-CS2103T-T17-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2223S2-CS2103T-T17-3/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2223S2-CS2103T-T17-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2223S2-CS2103T-T17-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `EListerParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `EListerParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `EListerParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2223S2-CS2103T-T17-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `ELister`, which `Person` references. This allows `ELister` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2223S2-CS2103T-T17-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `EListerStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.elister.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo/Redo

The `undo` command supports:

* Reverting the most recent `Command` that affected the `Model` state (by changing a `Person` within it
or its list of displayed `Persons`).
* Reverting a specified number of such `Commands` at once.

The `redo` command supports:

* Reinstating any undone `Command`, or a specified number of such `Commands`.

#### Implementation

Both commands operate using the `StateHistory` class, which is a container
that records - and can ultimately reconstruct - previous `Model` states.

Externally, `StateHistory` listens to the `CommandResult` of each executing command.
It also requires commands to declare two new fields in `CommandResult`:

* `affectsModel`  —  Whether the `Command` modifies `Model` by modifying a person or its list of displayed people.
A command will be reverted upon an `Undo` if and only if this is `true`.
* `deterministic`  —  Whether the modification to `Model` that was just made by `Command`
was the sole possible outcome of its execution.

By default, `StateHistory` points to the latest version of `Model` and offers a copy of its state.
This pointer can be moved backward or forward to retrieve copies of past states, or copies of undone future states:

* `StateHistory#undo(int n)` — Moves the pointer _n_ commands backward.
* `StateHistory#redo(int n)` — Moves the pointer _n_ commands forward.
* `StateHistory#presentModel()` — Retrieves a copy of the pointer's `Model` state.

The `Model` objects produced by `StateHistory#presentModel()` are state-detached copies of the originals -
fields related to containing or displaying `Persons` are deep copies, whereas fields involving UI preferences
reuse the original objects (thus including any mutations done later).

`StateHistory` listens for `CommandResults` like so:

![StateHistorySequenceDiagram](images/StateHistorySequenceDiagram.png)

In turn, the sequence diagram of `UndoCommand` is:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

#### Design considerations:

Given the inclusion of `import` and `export` commands, implementing a specific
reversal for each individual command was deemed impractical. Instead, the approach chosen was
to store previous `Model` states in their entirety.

However, to avoid excessive memory usage, only select seed copies of `Model` are actually stored.
Instead, previous `Commands` are also kept, and most `Model` states
are recreated by applying the relevant commands to seed states.

Seed states are captured

* upon program startup
* after a non-`deterministic` command
* every 10 commands, to avoid rerunning too many commands

These details are handled transparently by `StateHistory`.

### Input Log
This is a GUI enhancement we implemented to provide users with more information. A new UI component `HistoryDisplay` was created along with some changes and additions to other high-level components (`Model`, `Storage`, `Commons`) in order to record the executed commands (noted that unsuccessfully executed inputs will not be recorded) and store it in a local `.txt` file and read from that `.txt` file and show to user.

#### Implementation

The feature implementation involves in almost all high-level components which are `UI`, `Model`, `Storage` and `Commons`:

* `HistoryDisplay` class in the `UI` encapsulates the visual display of the saved data onto the GUI.
* `History` class in the `Model` represent the list of executed commands.
* `TxtHistoryStorage` and `HistoryStorage` from the `Storage` represents the `.txt` file and the action of reading/writing from/to that file.

1. Initially, when E-Lister is run, a new `TxtInputHistoryStorage` will be initialized along with other parts of high-level components.
2. Then, the history will be read from the `.txt` file after the `Elister` is read.
3. It will then be passed to `Logic` where the users' commands in `String` type are going to be executed and written into the `.txt` file if the commands were succesfully executed.
4. After the execution, the new `String` from `.txt` file will be read and display the updated history list to the users.

#### Design Consideration:
1. Instead of saving the history of commands in the same `.json` file, I personally believe that it would be better in this case to have a separate `.txt` file to store the commands, it would be much more convenient and fewer methods invoking among high-level components because:
    * The expected behavior is that it displays exactly the commands that the user inputted before, so if we use `.txt` file, we only need to check the command is successfully executed before write the whole `String` command into the `txt` file.
    * On the other hand, using `.json` file would require a lot of data conversion which is likely to be more error-prone and the `HistoryDisplay` from the `Ui` must trace through `Logic`, `Model`, `Storage` to read the `.json` file and vice versa since the data conversion happens in `Storage` or `Model`. Below is the code snippet in `LogicManager` where the history is read.
```
   historyStringOptional = storage.readHistoryString();
   if (!historyStringOptional.isPresent()) {
       logger.info("History file not found. Will be starting with the default file");
   }
   initialHistory = new History(historyStringOptional.orElse(""));
```
2. Inspired by the `Optional<ReadOnlyAdressBook` from the read and write process to the `.json` file, I also implement the read/write process of history such that the content will be encapsulated with an `Optional<String>` instead of `String`. This is useful since `Optional<T>` helps to avoid `NullPointException` and also lead to cleaner codes.

### Displaying filters that are currently applied
This is another GUI enhancement that is similar to the display of executed inputs from the previous section. Instead of displaying executed commands, this new UI component displays those filters that are applied on the contact list.

#### Implementation
In order to implement this new feature, there are some changes and additions to `Ui`, `Model` and `Logic`:
* `FiltersDisplay` class in the `Ui` encapsulates the visual display of those applying filters.
* `Filter` class in the `Model` represent a filter coming from user inputs with the `filter` command.

A brief description of how E-Lister keep track of which filters are applied on its contact list is:
1. `MainWindow#executeCommand()` in the `Ui` will invoke a call to `ModelManager#getApplyingFilterList()` and hence it will returns an `ObservableList<filter>` and be passed into `FiltersDisplay#setApplyingFilters()`.
2. A `ModelManager` object keeps updating the list of filters to be shown to user along with its `filteredPerson` through `ModelManager#updateFilteredPersonList()`.

### Creating shortcuts using the command `shortcut`

`shortcut` can be used to create user-defined shortcuts in order to input commands more quickly. This section will describe the implementation of this command, if the user enters `shortcut edit e` as an example.

Step 1. The user enters the command `shortcut edit e` and presses the Enter key. `ElisterParser` parses the user's command and returns a `ShortcutCommandParser`.

Step 2. `ShortcutCommandParser` parses the user's command, picking out the command that is to be shortened, and the shortcut to be added. A `ShortcutCommand` object is then added.

Step 3. The `ShortcutCommand` object is executed by `MainWindow#executeCommand`, returning a `CommandResult`. This signifies the completion of execution.

The following activity diagram summarizes what happens when a user executes this command:


### Importing and Exporting CSV Files

This is a useful feature which allows users to export all contact details in a CSV File of their choice, or import an existing dataset.

#### Implementation

The implementation for `import` and `export` are found in `ImportCommand` and `ExportCommand` respectively. Once the `execute(Model model)` method is invoked, a `FileChooser` is displayed to the user. This file chooser has a `FileChooser.ExtensionFilter` applied to it, where the file description is "CSV Files" and the allowed file extension is "*.csv".

Only for the `import` command, the system checks that the selected file is a valid CSV file. If it is invalid, an `Alert` is displayed to inform the user. This check is not required for the `export` command since users are allowed to write to a new file that does not yet exist.

Beyond the UI level, the commands operate using the `CsvElisterStorage` class. This class interacts between the `CsvUtil` and `CsvSerializableElister` classes in order to convert between E-Lister data and a CSV-friendly format. The `CsvSerializableElister` class helps to convert each `Person` instance in the list to it's corresponding CSV String and vice-versa. Using the `export` command for example, this interaction is illustrated in the following sequence diagram:

##### Export

![ExportSequenceDiagram](images/ExportSequenceDiagram.png)

### Design Considerations

Given the complexity of the CSV file format, it was deemed impractical to support every single valid expression or notation which CSV allows. Instead, only two expressions were considered: the `,` and `"` symbols. Since CSV is comma-delimited, the following special considerations were made to handle these characters when converting from E-Lister to CSV-friendly format.

* If any number of `,` appears in a field, the field must be wrapped with quotation marks `""`.
* If a field is wrapped in quotation marks `""` (such as due to the previous rule), any existing `"` within the field is converted to `""`.

The `CsvUtil` class provides a method to handle these rules:
```java
public static String toCsvField(String str) {
   if (str.contains(",")) {
      str = str.replaceAll("\"", "\"\"");
      str = "\"" + str + "\"";
   }
   return str;
}
```
### Tag feature

#### Implementation

The Tag mechanism is facilitated by `ELister`. It extends `ELister` with an Tag , stored internally as an `Tag` and  Additionally, it implements the following operations:

* `ELister#Tag()` — Adds a Tag to the person based on the index in the list.

These operations are exposed in the `Model` interface as `Model#addTag()`

Given below is an example usage scenario and how the tag mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedELister` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

Step 2. The user executes `Tag 5 good` command to Tag the 5th person in the address book with a new Tag `good`. The `Tag` command calls `Model#addTag()`, causing the modified state of the address book after the `Tag 5 good` command executes to be saved in the `eListerStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![TagSequenceDiagram](images/TagCommandSequenceDiagram.png)

### Mass feature

#### Implementation

The mass mechanism is facilitated by `Elister`. It extends `tag`/`delete` person/`delete_tag` /`edit` by providing a way to use those functions to the current list of people in the addressbook.
* `ELister#MassOp()` — MassOp to every Person in the list.

Given below is an example usage scenario and how the MassOp mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedELister` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

Step 2. The user executes `mass tag good` command to tag all the people in the address book with a new Tag `good`. The `mass` command creates `tag` command in a loop and each `tag` command calls `Model#addTag()`, causing the modified state of the address book after the `mass tag good` command executes to be saved in the `eListerStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state. The end result is that
every person in the addressbook list is `tag` with good.

![MassOpSequenceDiagram](images/MassOpSequenceDiagram.png)

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: Insurance salesmen who want to obtain a list of contacts who meet a certain criteria.

**Value proposition**: tag, filter, and obtain contacts faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | new user                                   | see usage instructions         | refer to instructions in case I forget how to use the App              |
| `* * *`  | user                                       | add a potential customer       |                                                                        |
| `* * *`  | user                                       | delete a potential customer    | remove entries that I no longer need                                   |
| `* * *`  | user                                       | find a potential customer by name | locate details of persons without having to go through the entire list |
| `* *`    | user                                       | hide private contact details   | minimize chance of someone else seeing them by accident                |
| `*`      | salsman with many potential customers in the E-Lister| sort potential customers by name | locate a potential customer easily                         |
| `* * *`  | Insurance salesman                         | see usage instructions         | Add someone to a category to manage them better                        |
| `* * *`  | Insurance salesman                         | add the user's income          | tell whether he's someone actually worth selling insurance to          |
| `* * *`  | Insurance salesman                         | delete all tags                | reset all the data                                                     |
| `* *`    | Insurance salesman                           | add the user's income          | tell whether he's someone actually worth selling insurance to            |
| `* *`    | Insurance salesman                           | filter by traits (e.g. income level) | selectively target certain insurance products to people who are more likely to buy it |
| `*`      | Insurance salesman                           | undo what I did                  | easily correct unecessary mistakes                                     |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `ELister` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete a person**

**MSS**

1.  User requests to list persons
2.  ELister shows a list of persons
3.  User requests to delete a specific person in the list
4.  ELister deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. ELister shows an error message.

      Use case resumes at step 2.

**Use Case UC1: Tag a person**

**MSS**

1.  User adds a tag with a specified label
2.  ELister adds the tag to the person.

    Use case ends.

**Extensions**

* 1a. The tag already exists for the given person.

    * 1a1. ELister shows an info message.

      Use case ends.

* 1b. The given index is invalid.

    * 1b1. ELister shows an error message.

      Use case ends.

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should not lag much when undoing a previous move.

*{More to be added}*

### Glossary

* **Command-line interface**: any changes to the state of the ELister is done via a text command
* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

1. Currently, the default parse failure message is displayed when `delete`, `delete_tag`, `undo`, or `redo` is
called with an index argument which is non-positive, or greater than `Integer.MAX_VALUE`, 2^31 - 1.<br>
A common user complaint is that this leads to confusion,
as it does not point out the index argument as a problem - in an often otherwise-correct command.<br>
We plan to make these error messages mention this cause of failure: `The index entered must be a positive integer below 2^31`.
3. The Input Log displays commands entered in previous sessions identically to those entered this session.<br>
This can cause confusion as the `undo` command is able to revert the latter, but not the former, leading to
`undo` appearing to fail on commands without an obvious reason.<br>
We plan to color the Input Log text of commands entered in previous sessions
<span style="color:#3CDFFF">light blue</span> to distinguish them and indicate them as non-undoable.
4. The Input Log cannot be cleared from within the program, causing it to grow cluttered over time and
increasingly difficult to scroll. We plan to add a command `wipelog` to clear the Input Log of its contents.
5. Command shortcuts cannot be deleted from within the program, making it difficult to remove an alias once
it is no longer required. We plan to add a command to delete such shortcuts: `delete_shortcut SHORTCUT`. To avoid making
commands inaccessible, shortcuts will only be deletable when more than one alias exists for the
command in question; an error message shall be raised otherwise.
6. Fix the filters display to work properly after some commands like "undo". For example, after 2 input "filter t/abc t/cde" and "filter t/mnb", the Ui display the applying filters are "t/mnb", but after a "redo" the Ui display no filters that are being applied while it supposes to show "t/abc" and "t/cde" to user.
