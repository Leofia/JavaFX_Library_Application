package lib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainScreenController implements Initializable {

    @FXML
    private VBox searchPane;
    @FXML
    private VBox borrowPane;
    @FXML
    private VBox returnPane;
    @FXML
    private VBox friendsPane;
    @FXML
    private TextField searchField;
    @FXML
    private TextField borrowField;
    @FXML
    private TextField returnField;
    @FXML
    private TextArea resultLabel;
    @FXML
    private TextArea bookContentArea;
    @FXML
    private ListView<String> friendListView;
    @FXML
    private ListView<String> bookListView;
    @FXML
    private ListView<User> allUsersListView;
    @FXML
    private ListView<String> previouslyBookListView;
    @FXML
    private ListView<String> readOnlineListView;
    @FXML
    private Button searchButton;
    @FXML
    private Button borrowButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button borrowedBooksButton;
    @FXML
    private Button readOnlineButton;
    @FXML
    private Button friendsButton;
    @FXML
    private Button friendsBorrowedButton;
    @FXML
    private Button friendRequestsButton;

    @FXML
    private Label friendSearchResultLabel;
    @FXML
    private TextField searchFriendField;
    @FXML
    private Button removeFriendButton;
    @FXML
    private TextArea borrowResultArea;
    @FXML
    private TextArea returnResultArea;
    @FXML
    private Button previouslyBorrowedButton;
    @FXML
    private VBox bookListViewContainer;
    @FXML
    private VBox readOnlineContainer;
    @FXML
    private Label readOnlineTitleLabel;
    @FXML
    private Label bookContentTitle;
     @FXML
    private ListView<String> pendingRequestListView;
    @FXML
    private VBox pendingRequestPane;

    private User loggedInUser;
    private DatabaseManagement dbManager = new DatabaseManagement();
    private BookDAO bookDAO = new BookDAO();
    private BorrowedBooksDAO borrowedBooksDAO = new BorrowedBooksDAO();
    private UserDAO userDAO = new UserDAO();
    private PreviouslyBorrowedBooksDAO previouslyBorrowedBooksDAO = new PreviouslyBorrowedBooksDAO();
    private PendingListDAO pendingListDAO = new PendingListDAO();
    private ObservableList<String> items = FXCollections.observableArrayList();
    private ObservableList<String> bookItems = FXCollections.observableArrayList();
    private ObservableList<String> previouslyBookItems = FXCollections.observableArrayList();
    private ObservableList<User> allUsersItems = FXCollections.observableArrayList();
    private ObservableList<String> readOnlineItems = FXCollections.observableArrayList();
     private ObservableList<String> pendingRequestItems = FXCollections.observableArrayList();
    @FXML
    private VBox secondvbox;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        System.out.println("Logged in user: " + loggedInUser.getUsername());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
          friendListView.setItems(FXCollections.observableArrayList());
        bookListView.setItems(bookItems);
        allUsersListView.setItems(allUsersItems);
        previouslyBookListView.setItems(previouslyBookItems);
        readOnlineListView.setItems(readOnlineItems);
        pendingRequestListView.setItems(pendingRequestItems);
        resultLabel.setVisible(false);

        allUsersListView.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getUsername());
                }
            }
        });
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch();
            }
        });
        borrowField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performBorrow();
            }
        });
        returnField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performReturn();
            }
        });
        searchFriendField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateUserList(newValue);
        });
        readOnlineListView.setOnMouseClicked(event -> {
            String selectedBookInfo = readOnlineListView.getSelectionModel().getSelectedItem();
            if (selectedBookInfo != null) {
                String[] parts = selectedBookInfo.split(" with borrowed id: ");
                if (parts.length == 2) {
                    try {
                        Long borrowedBookId = Long.parseLong(parts[1]);
                        showBookContent(borrowedBookId);
                    } catch (NumberFormatException e) {
                        bookContentArea.setText("Invalid borrowed book ID format.");
                        bookContentArea.setVisible(true);
                    }
                }
            }
        });

    }

    @FXML
    private void searchBook() {
        setVisiblePane(searchPane);
        resultLabel.setText("");
        resultLabel.setVisible(true);
    }

    @FXML
    private void performSearch() {
        String searchTerm = searchField.getText();
        List<Book> searchResults;

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            searchResults = bookDAO.getAllBooks();
        } else {
            searchResults = bookDAO.getAllBooks().stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(searchTerm.toLowerCase())
                            || book.getAuthor().toLowerCase().contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());
        }
        resultLabel.setText("");
        if (searchResults.isEmpty()) {
            resultLabel.setText("No books found.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Book book : searchResults) {
                sb.append("ID: ").append(book.getId()).append("\n")
                        .append("Title: ").append(book.getTitle()).append("\n")
                        .append("Author: ").append(book.getAuthor()).append("\n")
                        .append("Genre: ").append(book.getGenre()).append("\n")
                        .append("Published Year: ").append(book.getPublishedYear()).append("\n\n");
            }
            resultLabel.setText(sb.toString());
        }
    }

    @FXML
    private void borrowBook() {
        setVisiblePane(borrowPane);
        borrowResultArea.setText("");
    }

    @FXML
    private void performBorrow() {
        String bookIdStr = borrowField.getText();
        borrowResultArea.setText("");
        try {
            long bookId = Long.parseLong(bookIdStr);
            Book book = bookDAO.findBookById(bookId);

            if (book != null) {
                List<BorrowedBooks> borrowedBooks = borrowedBooksDAO.getBorrowedBooksByUser(loggedInUser);
                boolean alreadyBorrowed = borrowedBooks.stream()
                        .anyMatch(bb -> bb.getBook().getId().equals(bookId) && bb.getReturnedDate() == null);

                if (alreadyBorrowed) {
                    BorrowedBooks borrowedBook = borrowedBooks.stream()
                            .filter(bb -> bb.getBook().getId().equals(bookId) && bb.getReturnedDate() == null)
                            .findFirst()
                            .orElse(null);

                    if (borrowedBook != null) {
                        borrowResultArea.setText("You have already borrowed this book and it is not returned yet!\n" +
                                "You can return it with entering the following ID to return a book section: " + borrowedBook.getId());
                    } else {
                        borrowResultArea.setText("You have already borrowed this book and it is not returned yet!\n" +
                                "Please check your borrowed books section to see your borrowed book id for this book. ");
                    }

                } else {
                    BorrowedBooks borrowedBook = new BorrowedBooks();
                    borrowedBook.setBook(book);
                    borrowedBook.setUser(loggedInUser);
                    borrowedBook.setBorrowedDate(LocalDate.now());
                    borrowedBooksDAO.addBorrowedBook(borrowedBook);

                    long borrowedBookId = borrowedBook.getId();

                    borrowResultArea.setText("Book borrowed successfully!\n" +
                            "You can return it with entering the following ID to return a book section: " + borrowedBookId);
                }

            } else {
                borrowResultArea.setText("Book not found with ID: " + bookId);
            }

        } catch (NumberFormatException e) {
            borrowResultArea.setText("Invalid book ID format. Ids must be entered Integers(1,2,3...)");
        } catch (Exception e) {
            borrowResultArea.setText("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void returnBook() {
        setVisiblePane(returnPane);
        returnResultArea.setText("");
    }

    @FXML
    private void performReturn() {
        String borrowedBookIdStr = returnField.getText();
        returnResultArea.setText("");
        try {
            long borrowedBookId = Long.parseLong(borrowedBookIdStr);
            BorrowedBooks borrowedBook = borrowedBooksDAO.findBorrowedBookById(borrowedBookId);

            if (borrowedBook != null) {
                if (borrowedBook.getUser().getId().equals(loggedInUser.getId())) {
                    if (borrowedBooksDAO.returnBorrowedBook(borrowedBook.getId())) {
                        returnResultArea.setText("Book returned successfully!");
                    } else {
                        returnResultArea.setText("This book has already been returned");
                    }
                } else {
                    returnResultArea.setText("You have not borrowed this book.");
                }
            } else {
                returnResultArea.setText("Borrowed book not found with ID: " + borrowedBookId);
            }

        } catch (NumberFormatException e) {
            returnResultArea.setText("Invalid borrowed book ID format.");
        } catch (Exception e) {
            returnResultArea.setText("Error occurred while returning the book.");
            e.printStackTrace();
        }

    }

    @FXML
    private void displayBorrowedBooks() {
        setVisiblePane(bookListViewContainer);
        List<BorrowedBooks> borrowedBooks = borrowedBooksDAO.getBorrowedBooksByUser(loggedInUser);
        bookItems.clear();
        if (borrowedBooks.isEmpty()) {
            bookItems.add("No books borrowed by you.");
            previouslyBorrowedButton.setVisible(true);

        } else {
            borrowedBooks.forEach(borrowedBook -> bookItems.add(borrowedBook.getBook().getTitle() + " borrowed on " + borrowedBook.getBorrowedDate() + " with borrowed id: " + borrowedBook.getId()));
            previouslyBorrowedButton.setVisible(true);
        }

        previouslyBookListView.setVisible(false);
    }

    @FXML
    private void displayPreviouslyBorrowedBooks() {
        List<PreviouslyBorrowedBooks> previouslyBorrowedBooks = previouslyBorrowedBooksDAO.getPreviouslyBorrowedBooksByUser(loggedInUser);
        previouslyBookItems.clear();
        if (previouslyBorrowedBooks.isEmpty()) {
            previouslyBookItems.add("No previously borrowed books.");
        } else {
            previouslyBorrowedBooks.forEach(borrowedBook -> previouslyBookItems.add(borrowedBook.getBook().getTitle() + " borrowed on " + borrowedBook.getBorrowedDate() + " returned on " + borrowedBook.getReturnedDate() + " with borrowed id: " + borrowedBook.getOriginalBorrowedBookId()));
        }
        previouslyBookListView.setVisible(true);
    }

    @FXML
    private void readOnline() {
        setVisiblePane(readOnlineContainer);
        readOnlineTitleLabel.setText("Choose The Book you want to read");
        readOnlineItems.clear();
        List<BorrowedBooks> borrowedBooks = borrowedBooksDAO.getBorrowedBooksByUser(loggedInUser);
        if (borrowedBooks.isEmpty()) {
            readOnlineItems.add("You have not borrowed a book to read online.");
        } else {
            borrowedBooks.forEach(borrowedBook -> readOnlineItems.add(borrowedBook.getBook().getTitle() + " with borrowed id: " + borrowedBook.getId()));
        }
        bookContentArea.setVisible(false);
        bookContentTitle.setVisible(false);
    }


    private void showBookContent(Long borrowedBookId) {
        BorrowedBooks borrowedBook = borrowedBooksDAO.findBorrowedBookById(borrowedBookId);
        if (borrowedBook != null) {
            Book book = borrowedBook.getBook();
            bookContentTitle.setText(book.getTitle());
            bookContentArea.setText(book.getBookContent());
            bookContentArea.setVisible(true);
            bookContentTitle.setVisible(true);
        }
    }

    @FXML
    private void displayPendingRequests() {
        setVisiblePane(pendingRequestPane);
        List<PendingList> pendingRequests = pendingListDAO.getPendingRequestsForUser(loggedInUser);
        pendingRequestItems.clear();
        if (pendingRequests.isEmpty()) {
            pendingRequestItems.add("No pending friend requests.");
        } else {
            for (PendingList pendingList : pendingRequests) {
                pendingRequestItems.add(pendingList.getSender().getUsername() + " send you a friend request");
            }
        }


    }

    @FXML
    private void displayFriends() {
        setVisiblePane(friendsPane);
         friendListView.setVisible(true);
         allUsersListView.setVisible(true);
         removeFriendButton.setVisible(true);
         loadFriendsData();
        updateUserList("");
    }


    

    private void updateUserList(String searchTerm) {
        allUsersItems.clear();
        List<User> allUsers = userDAO.getAllUsersExcept(loggedInUser);
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            allUsersItems.addAll(allUsers);
        } else {
            allUsersItems.addAll(allUsers.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList()));

        }
    }
    @FXML
    private void performAddFriend() {
        User selectedUser = allUsersListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            User targetUser = userDAO.findUserById(selectedUser.getId());
            if (targetUser == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("This user does not exist!");
                alert.showAndWait();
                return;
            }


            if (userDAO.areFriends(loggedInUser, targetUser)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("This user is already your friend!");
                alert.showAndWait();
                return;

            } else if (loggedInUser.getId().equals(selectedUser.getId())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("You cannot add yourself as a friend!");
                alert.showAndWait();
                return;
            } else {
                if (pendingListDAO.hasPendingRequestFromSender(loggedInUser, selectedUser)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("You have already sent a friend request to this user.");
                    alert.showAndWait();
                    return;

                } else {
                    try {
                        User receiver = userDAO.findUserById(selectedUser.getId());
                        if (receiver != null) {
                            PendingList pendingList = new PendingList();
                            pendingList.setSender(loggedInUser);
                            pendingList.setReceiver(receiver);
                            pendingListDAO.addPendingRequest(pendingList);
                            friendSearchResultLabel.setText("Friend request sent to " + selectedUser.getUsername());
                            friendSearchResultLabel.setStyle("-fx-text-fill: green;");
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("This user does not exist!");
                            alert.showAndWait();
                        }
                    } catch (RuntimeException e) {
                        if (e.getMessage().equals("Receiver user not found in the database")) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("This user does not exist!");
                            alert.showAndWait();

                        } else {
                            friendSearchResultLabel.setText("Error occurred: " + e.getMessage());
                            friendSearchResultLabel.setStyle("-fx-text-fill: red;");
                        }


                    } catch (Exception e) {
                        friendSearchResultLabel.setText("Error occurred: " + e.getMessage());
                        friendSearchResultLabel.setStyle("-fx-text-fill: red;");
                    }
                }
            }

        } else {
            friendSearchResultLabel.setText("Please select a user to add.");
            friendSearchResultLabel.setStyle("-fx-text-fill: red;");
        }

    }

    private void loadFriendsData() {
        if (loggedInUser == null) return;
        List<User> friends = userDAO.findFriends(loggedInUser.getId());
        ObservableList<String> friendUsernames = FXCollections.observableArrayList();
        friendUsernames.add("Friend List:");
        if (friends != null) {
            for (User friend : friends) {
                friendUsernames.add(friend.getUsername());
            }
        }
        friendListView.setItems(friendUsernames);
    }


    @FXML
    private void performAcceptFriendRequest() {
        String selectedPendingRequest = pendingRequestListView.getSelectionModel().getSelectedItem();

        if (selectedPendingRequest != null) {
            String[] parts = selectedPendingRequest.split(" send you a friend request");
            if (parts.length == 1) {
                try {
                    String senderUsername = parts[0];
                    User sender = userDAO.findUserByUsername(senderUsername);
                    if (sender != null) {
                        List<PendingList> pendingRequests = pendingListDAO.getPendingRequestsForUser(loggedInUser);
                        PendingList pendingList = pendingRequests.stream().filter(pendingList1 -> pendingList1.getSender().getId().equals(sender.getId())).findFirst().orElse(null);

                        if (pendingList != null) {
                            List<User> senderFriendList = sender.getFriends();
                            if (senderFriendList == null) {
                                senderFriendList = new ArrayList<>();
                            }
                            if (senderFriendList.stream().anyMatch(friend -> friend.getId().equals(loggedInUser.getId()))) {
                                 Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning");
                                alert.setHeaderText(null);
                                alert.setContentText("This user is already your friend!");
                                 alert.showAndWait();
                            } else {
                                 senderFriendList.add(loggedInUser);
                            }
                            sender.setFriends(senderFriendList);
                            userDAO.updateUser(sender);

                            List<User> receiverFriendList = loggedInUser.getFriends();
                            if (receiverFriendList == null) {
                                receiverFriendList = new ArrayList<>();
                            }
                           if (receiverFriendList.stream().anyMatch(friend -> friend.getId().equals(sender.getId()))) {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning");
                                alert.setHeaderText(null);
                                alert.setContentText("This user is already your friend!");
                                 alert.showAndWait();
                            }else {
                                  receiverFriendList.add(sender);
                           }

                            loggedInUser.setFriends(receiverFriendList);
                            userDAO.updateUser(loggedInUser);


                            pendingListDAO.removePendingRequest(pendingList.getId());


                            friendSearchResultLabel.setText("You are now friends with " + sender.getUsername());
                            friendSearchResultLabel.setStyle("-fx-text-fill: green;");
                            displayPendingRequests();

                            if (!loggedInUser.getId().equals(sender.getId())) {
                                refreshOtherUsersFriendList(sender);
                                if (secondvbox.getChildren().contains(friendsPane)) {
                                    MainScreenController mainScreenController = new MainScreenController();
                                    mainScreenController.setLoggedInUser(sender);
                                      mainScreenController.loadFriendsData();
                                }
                            }
                            loadFriendsData();


                        } else {
                            friendSearchResultLabel.setText("Pending request not found!");
                            friendSearchResultLabel.setStyle("-fx-text-fill: red;");
                        }

                    } else {
                        friendSearchResultLabel.setText("Sender user not found!");
                        friendSearchResultLabel.setStyle("-fx-text-fill: red;");
                    }


                } catch (Exception e) {
                    friendSearchResultLabel.setText("An error occurred: " + e.getMessage());
                    friendSearchResultLabel.setStyle("-fx-text-fill: red;");
                }
            }
        } else {
            friendSearchResultLabel.setText("Please select a friend request to accept.");
            friendSearchResultLabel.setStyle("-fx-text-fill: red;");
        }
    }


    private void refreshOtherUsersFriendList(User sender) {
        if (!secondvbox.getChildren().contains(friendsPane)) {
            MainScreenController mainScreenController = new MainScreenController();
            mainScreenController.setLoggedInUser(sender);
            mainScreenController.displayFriends();
        }

    }

    @FXML
    private void performRejectFriendRequest() {
        String selectedPendingRequest = pendingRequestListView.getSelectionModel().getSelectedItem();

        if (selectedPendingRequest != null) {
            String[] parts = selectedPendingRequest.split(" send you a friend request");
            if (parts.length == 1) {
                try {
                    String senderUsername = parts[0];
                    User sender = userDAO.findUserByUsername(senderUsername);
                    if (sender != null) {
                        List<PendingList> pendingRequests = pendingListDAO.getPendingRequestsForUser(loggedInUser);
                        PendingList pendingList = pendingRequests.stream().filter(pendingList1 -> pendingList1.getSender().getId().equals(sender.getId())).findFirst().orElse(null);

                        if (pendingList != null) {

                            pendingListDAO.removePendingRequest(pendingList.getId());
                            friendSearchResultLabel.setText("Friend request rejected from " + sender.getUsername());
                            friendSearchResultLabel.setStyle("-fx-text-fill: green;");
                            displayPendingRequests();
                        } else {
                            friendSearchResultLabel.setText("Pending request not found!");
                            friendSearchResultLabel.setStyle("-fx-text-fill: red;");
                        }

                    } else {
                        friendSearchResultLabel.setText("Sender user not found!");
                        friendSearchResultLabel.setStyle("-fx-text-fill: red;");
                    }


                } catch (Exception e) {
                    friendSearchResultLabel.setText("An error occurred: " + e.getMessage());
                    friendSearchResultLabel.setStyle("-fx-text-fill: red;");
                }
            }
        } else {
            friendSearchResultLabel.setText("Please select a friend request to reject.");
            friendSearchResultLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void performRemoveFriend() {
        String selectedFriendUsername = friendListView.getSelectionModel().getSelectedItem();
        if (selectedFriendUsername != null) {
            List<User> friends = loggedInUser.getFriends();
            User friendToRemove = null;
            for (User friend : friends) {
                if (friend.getUsername().equals(selectedFriendUsername)) {
                    friendToRemove = friend;
                    break;
                }
            }

            if (friendToRemove != null) {
                friends.remove(friendToRemove);
                loggedInUser.setFriends(friends);
                userDAO.updateUser(loggedInUser);
                loadFriendsData();
                friendSearchResultLabel.setText("Friend removed successfully!");
                friendSearchResultLabel.setStyle("-fx-text-fill: green;");

            } else {
                friendSearchResultLabel.setText("Friend not found in your list.");
                friendSearchResultLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            friendSearchResultLabel.setText("Please select a friend to remove.");
            friendSearchResultLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void displayFriendsBorrowedBooks() {
        setVisiblePane(bookListViewContainer);
        List<User> friends = loggedInUser.getFriends();
        if (friends == null || friends.isEmpty()) {
            bookItems.clear();
            bookItems.add("No friends yet");
        } else {
            bookItems.clear();
            for (User friend : friends) {
                List<BorrowedBooks> borrowedBooks = borrowedBooksDAO.getBorrowedBooksByUser(friend);
                if (borrowedBooks == null || borrowedBooks.isEmpty()) {
                    bookItems.add(friend.getUsername() + " has not borrowed books yet.");
                } else {
                    for (BorrowedBooks borrowedBook : borrowedBooks) {
                        bookItems.add(friend.getUsername() + " borrowed " + borrowedBook.getBook().getTitle() + " on " + borrowedBook.getBorrowedDate()
                                + (borrowedBook.getReturnedDate() == null ? "" : " returned on " + borrowedBook.getReturnedDate()));
                    }
                }

            }
        }
        previouslyBookListView.setVisible(false);
    }

    private void hideAllPanes() {
        searchPane.setVisible(false);
        borrowPane.setVisible(false);
        returnPane.setVisible(false);
        friendsPane.setVisible(false);
         pendingRequestPane.setVisible(false);
        resultLabel.setVisible(false);
        friendListView.setVisible(false);
        allUsersListView.setVisible(false);
        bookListViewContainer.setVisible(false);
        friendSearchResultLabel.setVisible(false);
        removeFriendButton.setVisible(false);
        borrowResultArea.setVisible(false);
        previouslyBookListView.setVisible(false);
        previouslyBorrowedButton.setVisible(false);
        returnResultArea.setVisible(false);
        readOnlineContainer.setVisible(false);
        bookContentArea.setVisible(false);
        bookContentTitle.setVisible(false);
        readOnlineTitleLabel.setVisible(false);
         pendingRequestListView.setVisible(false);
    }

    private void setVisiblePane(javafx.scene.Node pane) {
        secondvbox.getChildren().clear();
        hideAllPanes();
        if (pane != null) {

            secondvbox.getChildren().add(pane);
            pane.setVisible(true);
            if (pane == borrowPane) {
                borrowResultArea.setVisible(true);
            }
            if (pane == bookListViewContainer) {
                bookListViewContainer.setVisible(true);
            }
            if (pane == readOnlineContainer) {
                readOnlineContainer.setVisible(true);
                readOnlineTitleLabel.setVisible(true);
            }
             if (pane == pendingRequestPane) {
                   pendingRequestListView.setVisible(true);
             }
            if (pane == returnPane) {
                returnResultArea.setVisible(true);
            }


        }
    }
}