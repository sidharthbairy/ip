package et.gui;

import et.ET;
import et.task.TaskType;

import java.util.Optional;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Displays ET as a JavaFX chat window and forwards commands to the application logic.
 */
public class MainWindow extends Application {
    /** The preferred width of the application window. */
    private static final int WINDOW_WIDTH = 720;

    /** The preferred height of the application window. */
    private static final int WINDOW_HEIGHT = 760;

    /** The maximum width of a message bubble. */
    private static final int MESSAGE_WIDTH = 480;

    /** The number of decorative stars displayed behind the conversation. */
    private static final int STAR_COUNT = 26;

    /** The time the farewell remains visible before the window closes. */
    private static final int EXIT_DELAY_MILLIS = 650;

    /** The chatbot instance shared by every interaction in this window. */
    private final ET et = new ET();

    /** Holds the conversation's messages in display order. */
    private final VBox dialogContainer = new VBox(12);

    /** Accepts the next command from the user. */
    private final TextField userInput = new TextField();

    /** Keeps the latest conversation messages visible. */
    private ScrollPane scrollPane;

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        BorderPane root = createLayout();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());

        stage.setTitle("ET • Task Companion");
        stage.setMinWidth(540);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();

        addDialog(et.getWelcomeMessage(), false);
        userInput.requestFocus();
    }

    /**
     * Creates the conversation area and command bar.
     *
     * @return the complete window layout
     */
    private BorderPane createLayout() {
        dialogContainer.setPadding(new Insets(24, 28, 28, 28));
        dialogContainer.setFillWidth(true);
        dialogContainer.getStyleClass().add("dialog-container");

        scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("dialog-scroll");
        dialogContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> scrollToLatestMessage());

        userInput.setPromptText("Send a task signal…  try “list”");
        userInput.setOnAction(event -> handleUserInput());
        userInput.getStyleClass().add("command-field");
        HBox.setHgrow(userInput, Priority.ALWAYS);

        Button sendButton = new Button("TRANSMIT");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> handleUserInput());
        sendButton.getStyleClass().add("send-button");

        HBox inputBar = new HBox(10, userInput, sendButton);
        inputBar.setAlignment(Pos.CENTER);

        Label inputHint = new Label("QUICK SIGNALS   LIST  ·  TODO  ·  DEADLINE  ·  FIND");
        inputHint.getStyleClass().add("input-hint");

        VBox composer = new VBox(8, inputHint, inputBar);
        composer.setPadding(new Insets(14, 20, 18, 20));
        composer.getStyleClass().add("composer");

        StackPane conversationArea = new StackPane(createSpaceBackdrop(), scrollPane);
        conversationArea.getStyleClass().add("conversation-area");

        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(conversationArea);
        root.setBottom(composer);
        root.getStyleClass().add("app-shell");
        return root;
    }

    /**
     * Creates the branded title and connection-status area.
     *
     * @return the application header
     */
    private VBox createHeader() {
        Label brandMark = new Label("ET");
        brandMark.getStyleClass().add("brand-mark");

        Label kicker = new Label("INTERSTELLAR TASK COMPANION");
        kicker.getStyleClass().add("app-kicker");

        Label title = new Label("ET");
        title.getStyleClass().add("app-title");

        VBox identity = new VBox(1, kicker, title);

        Label statusDot = new Label("");
        statusDot.getStyleClass().add("status-dot");
        Label statusText = new Label("SIGNAL ONLINE");
        statusText.getStyleClass().add("status-text");
        HBox status = new HBox(7, statusDot, statusText);
        status.setAlignment(Pos.CENTER);
        status.getStyleClass().add("status-pill");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleRow = new HBox(12, brandMark, identity, spacer, status);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label tagline = new Label("A little help from somewhere among the stars.");
        tagline.getStyleClass().add("tagline");

        VBox header = new VBox(9, titleRow, tagline);
        header.setPadding(new Insets(18, 22, 16, 22));
        header.getStyleClass().add("app-header");
        return header;
    }

    /**
     * Creates a decorative star field that sits behind the conversation.
     *
     * @return the non-interactive backdrop
     */
    private Pane createSpaceBackdrop() {
        Pane backdrop = new Pane();
        backdrop.setMouseTransparent(true);
        backdrop.getStyleClass().add("space-backdrop");

        Circle moonGlow = new Circle(86);
        moonGlow.centerXProperty().bind(backdrop.widthProperty().subtract(36));
        moonGlow.setCenterY(44);
        moonGlow.getStyleClass().add("moon-glow");
        backdrop.getChildren().add(moonGlow);

        for (int i = 0; i < STAR_COUNT; i++) {
            double horizontalRatio = ((i * 37) % 97 + 2) / 100.0;
            double verticalRatio = ((i * 61) % 89 + 4) / 100.0;
            Circle star = new Circle(i % 5 == 0 ? 1.5 : 0.8);
            star.centerXProperty().bind(backdrop.widthProperty().multiply(horizontalRatio));
            star.centerYProperty().bind(backdrop.heightProperty().multiply(verticalRatio));
            star.getStyleClass().add(i % 5 == 0 ? "star-bright" : "star");
            backdrop.getChildren().add(star);
        }

        return backdrop;
    }

    /** Sends a non-blank user command to ET and displays both sides of the exchange. */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        addDialog(input, true);
        ET.CommandResult result = et.getCommandResult(input);
        addDialog(result.response(), false);
        userInput.clear();
        if (result.shouldExit()) {
            closeAfterFarewell();
        }
    }

    /** Closes the application after leaving enough time for ET's farewell to appear. */
    private void closeAfterFarewell() {
        userInput.setDisable(true);
        PauseTransition exitDelay = new PauseTransition(Duration.millis(EXIT_DELAY_MILLIS));
        exitDelay.setOnFinished(event -> Platform.exit());
        exitDelay.play();
    }

    /** Scrolls the conversation to its latest message after JavaFX lays out new content. */
    private void scrollToLatestMessage() {
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    /**
     * Adds one user or ET message to the conversation.
     *
     * @param message the text to display
     * @param isUser whether the message came from the user
     */
    private void addDialog(String message, boolean isUser) {
        Label avatar = new Label(isUser ? "YOU" : "ET");
        avatar.getStyleClass().addAll("avatar", isUser ? "user-avatar" : "et-avatar");

        Region bubble = isUser ? createUserBubble(message) : createEtBubble(message);

        Label author = new Label(isUser ? "YOU  //  EARTH" : "ET  //  ORBIT");
        author.getStyleClass().addAll("message-author",
                isUser ? "user-message-author" : "et-message-author");

        VBox messageContent = new VBox(5, author, bubble);
        messageContent.setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox dialog = isUser
                ? new HBox(11, spacer, messageContent, avatar)
                : new HBox(11, avatar, messageContent, spacer);
        dialog.setAlignment(Pos.TOP_CENTER);
        dialog.getStyleClass().add("dialog-row");

        dialogContainer.getChildren().add(dialog);
    }

    /**
     * Creates a text bubble for a command entered by the user.
     *
     * @param message the command text
     * @return the styled user bubble
     */
    private Label createUserBubble(String message) {
        Label bubble = new Label(message);
        bubble.setWrapText(true);
        bubble.setMaxWidth(MESSAGE_WIDTH);
        bubble.setMinHeight(Region.USE_PREF_SIZE);
        bubble.setAlignment(Pos.CENTER_RIGHT);
        bubble.getStyleClass().add("user-bubble");
        return bubble;
    }

    /**
     * Creates an ET response bubble with graphical cards for any task lines.
     *
     * @param message ET's response text
     * @return the styled ET response bubble
     */
    private VBox createEtBubble(String message) {
        VBox bubble = new VBox(7);
        bubble.setMaxWidth(MESSAGE_WIDTH);
        bubble.setMinHeight(Region.USE_PREF_SIZE);
        bubble.getStyleClass().add("et-bubble");

        for (String line : message.split("\\R", -1)) {
            Optional<TaskDisplayParser.TaskDisplay> taskDisplay = TaskDisplayParser.parseTaskLine(line);
            if (taskDisplay.isPresent()) {
                bubble.getChildren().add(createTaskCard(taskDisplay.get()));
            } else {
                bubble.getChildren().add(createResponseLine(line));
            }
        }
        return bubble;
    }

    /**
     * Creates a wrapped line of ordinary ET response text.
     *
     * @param line the response line to display
     * @return the styled text label
     */
    private Label createResponseLine(String line) {
        Label responseLine = new Label(line.strip());
        responseLine.setWrapText(true);
        responseLine.setMaxWidth(MESSAGE_WIDTH);
        responseLine.setMinHeight(Region.USE_PREF_SIZE);
        responseLine.getStyleClass().add("response-line");
        return responseLine;
    }

    /**
     * Creates a visual card containing a task number, type, status, and description.
     *
     * @param taskDisplay the parsed task details
     * @return the graphical task card
     */
    private HBox createTaskCard(TaskDisplayParser.TaskDisplay taskDisplay) {
        HBox taskCard = new HBox(9);
        taskCard.setAlignment(Pos.CENTER_LEFT);
        taskCard.getStyleClass().add("task-card");
        if (taskDisplay.isDone()) {
            taskCard.getStyleClass().add("task-card-complete");
        }

        if (!taskDisplay.taskNumber().isEmpty()) {
            Label taskNumber = new Label(taskDisplay.taskNumber());
            taskNumber.getStyleClass().add("task-number");
            taskCard.getChildren().add(taskNumber);
        }

        taskCard.getChildren().addAll(createTaskTypeIcon(taskDisplay.taskType()),
                createStatusIndicator(taskDisplay.isDone()));

        Label description = new Label(taskDisplay.description());
        description.setWrapText(true);
        description.setMinHeight(Region.USE_PREF_SIZE);
        description.setMaxWidth(MESSAGE_WIDTH - 120);
        description.getStyleClass().add("task-description");
        HBox.setHgrow(description, Priority.ALWAYS);
        taskCard.getChildren().add(description);
        return taskCard;
    }

    /**
     * Creates the icon that distinguishes a to-do, deadline, or event.
     *
     * @param taskType the category represented by the icon
     * @return the styled task-type icon
     */
    private StackPane createTaskTypeIcon(TaskType taskType) {
        SVGPath glyph = new SVGPath();
        String styleClass;
        String accessibleText;
        switch (taskType) {
        case TODO:
            glyph.setContent("M3 4 L5 6 L8 2 M10 4 L15 4 M3 10 L5 12 L8 8 M10 10 L15 10");
            styleClass = "todo-type";
            accessibleText = "To-do";
            break;
        case DEADLINE:
            glyph.setContent("M9 2 A7 7 0 1 1 8.99 2 M9 5 L9 9 L12 11");
            styleClass = "deadline-type";
            accessibleText = "Deadline";
            break;
        case EVENT:
            glyph.setContent("M3 5 L15 5 M5 2 L5 5 M13 2 L13 5 M3 3 L15 3 L15 15 L3 15 Z");
            styleClass = "event-type";
            accessibleText = "Event";
            break;
        default:
            throw new IllegalArgumentException("Unknown task type");
        }
        glyph.getStyleClass().add("task-type-glyph");

        StackPane typeIcon = new StackPane(glyph);
        typeIcon.getStyleClass().addAll("task-type-icon", styleClass);
        typeIcon.setAccessibleText(accessibleText);
        return typeIcon;
    }

    /**
     * Creates an empty completion ring or a filled ring with a checkmark.
     *
     * @param isDone whether the task is complete
     * @return the graphical completion indicator
     */
    private StackPane createStatusIndicator(boolean isDone) {
        Circle ring = new Circle(9);
        ring.getStyleClass().add("status-ring");

        StackPane statusIndicator = new StackPane(ring);
        statusIndicator.getStyleClass().addAll("task-status",
                isDone ? "task-status-complete" : "task-status-incomplete");
        statusIndicator.setAccessibleText(isDone ? "Completed" : "Not completed");

        if (isDone) {
            SVGPath checkmark = new SVGPath();
            checkmark.setContent("M4 9 L7 12 L14 5");
            checkmark.getStyleClass().add("task-checkmark");
            statusIndicator.getChildren().add(checkmark);
        }
        return statusIndicator;
    }
}
