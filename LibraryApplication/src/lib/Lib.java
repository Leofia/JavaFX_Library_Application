package lib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.EntityType;
import java.util.Set;

public class Lib extends Application {

    private static User loggedInUser;
    private static EntityManagerFactory emf;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public void init() throws Exception {
        super.init();
        emf = Persistence.createEntityManagerFactory("libPU");
        seedDatabase();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Library System");
        stage.show();
    }

    private void seedDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            
            Set<EntityType<?>> entityTypes = em.getMetamodel().getEntities();
            boolean userTableExists = entityTypes.stream().anyMatch(entityType -> entityType.getJavaType().equals(User.class));
            boolean bookTableExists = entityTypes.stream().anyMatch(entityType -> entityType.getJavaType().equals(Book.class));

            if (userTableExists && bookTableExists) {

                TypedQuery<Long> userQuery = em.createQuery("SELECT COUNT(u) FROM User u", Long.class);
                long userCount = userQuery.getSingleResult();

                TypedQuery<Long> bookQuery = em.createQuery("SELECT COUNT(b) FROM Book b", Long.class);
                long bookCount = bookQuery.getSingleResult();

                if (userCount == 0 && bookCount == 0) {
                    UserDAO userDAO = new UserDAO();
                    User user1 = new User();
                    user1.setUsername("irem");
                    user1.setPassword("1234");
                    userDAO.addUser(user1);

                    User user2 = new User();
                    user2.setUsername("serra");
                    user2.setPassword("1234");
                    userDAO.addUser(user2);

                    User user3 = new User();
                    user3.setUsername("a");
                    user3.setPassword("b");
                    userDAO.addUser(user3);

                    BookDAO bookDAO = new BookDAO();
                    Book book1 = new Book();
                    book1.setTitle("The Hobbit");
                    book1.setAuthor("J.R.R. Tolkien");
                    book1.setGenre("Fantasy");
                    book1.setPublishedYear(1937);
                    book1.setBookContent("In a hole in the ground there lived a hobbit. Not a nasty, dirty, wet hole, filled with the ends of worms and an oozy smell, nor yet a dry, bare, sandy hole with nothing in it to sit down on or to eat: it was a hobbit-hole, and that means comfort. It had a perfectly round door like a porthole, painted green, with a shiny yellow brass knob in the exact middle. The door opened on to a tube-shaped hall like a tunnel: a very comfortable tunnel without smoke, with panelled walls, and floors tiled and carpeted, provided with polished chairs, and lots and lots of pegs for hats and coats—the hobbit was fond of visitors. The tunnel wound on and on, going fairly but not quite straight into the side of the hill—The Hill, as all the people for many miles round called it—and many little round doors opened out of it, first on one side and then on another. No going upstairs for the hobbit: bedrooms, bathrooms, cellars, pantries (lots of these), wardrobes (he had whole rooms devoted to clothes), kitchens, dining-rooms, all were on the same floor, and indeed on the same passage. The best rooms were all on the left-hand side (going in), for these were the only ones to have windows, deep-set round windows looking over his garden, and meadows beyond, sloping down to the river.");
                    bookDAO.addBook(book1);

                    Book book2 = new Book();
                    book2.setTitle("Pride and Prejudice");
                    book2.setAuthor("Jane Austen");
                    book2.setGenre("Romance");
                    book2.setPublishedYear(1813);
                    book2.setBookContent("It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife. However little known the feelings or views of such a man may be on his first entering a neighbourhood, this truth is so well fixed in the minds of the surrounding families, that he is considered the rightful property of some one or other of their daughters. \"My dear Mr. Bennet,\" said his lady to him one day, \"have you heard that Netherfield Park is let at last?\" Mr. Bennet replied that he had not. \"But it is,\" returned she; \"for Mrs. Long has just been here, and she told me all about it.\" Mr. Bennet made no answer. \"Do you not want to know who has taken it?\" cried his wife impatiently. \"You want to tell me, and I have no objection to hearing it.\" This was invitation enough. \"Why, my dear, you must know, Mrs. Long says that Netherfield is taken by a young man of large fortune from the north of England; that he came down on Monday in a chaise and four to see the place, and was so much delighted with it, that he agreed with Mr. Morris immediately; that he is to take possession before Michaelmas, and some of his servants are to be down before the end of next week.\"");
                    bookDAO.addBook(book2);

                    Book book3 = new Book();
                    book3.setTitle("1984");
                    book3.setAuthor("George Orwell");
                    book3.setGenre("Dystopian");
                    book3.setPublishedYear(1949);
                    book3.setBookContent("It was a bright cold day in April, and the clocks were striking thirteen. Winston Smith, his chin nuzzled into his breast in an effort to escape the vile wind, slipped quickly through the glass doors of Victory Mansions, though not quickly enough to prevent a swirl of gritty dust from entering along with him. The hallway smelt of boiled cabbage and old rag mats. At one end of it a coloured poster, too large for indoor display, had been tacked to the wall. It depicted simply an enormous face, more than a metre wide: the face of a man of about forty-five, with a heavy black mustache and ruggedly handsome features. Below the face, the caption ran in bold, uppercase letters: BIG BROTHER IS WATCHING YOU.");
                    bookDAO.addBook(book3);
                     
                    Book book4 = new Book();
                    book4.setTitle("To Kill a Mockingbird");
                    book4.setAuthor("Harper Lee");
                    book4.setGenre("Fiction");
                    book4.setPublishedYear(1960);
                    book4.setBookContent("When he was nearly thirteen, my brother Jem got his arm badly broken at the elbow. When it healed, and Jem's fears of never being able to play football were assuaged, he was seldom self-conscious about his injury. His left arm was somewhat shorter than his right; when he stood or walked, the back of his hand was at right angles to his body, his thumb parallel to his thigh. He couldn't have cared less, so long as he could pass and punt. I maintain that the Ewells started it all, but Jem, who was four years my senior, said it started long before that. He said it began the summer Dill came to us, when Dill first gave us the idea of making Boo Radley come out.");
                    bookDAO.addBook(book4);

                    Book book5 = new Book();
                    book5.setTitle("The Lord of the Rings");
                    book5.setAuthor("J.R.R. Tolkien");
                    book5.setGenre("Fantasy");
                    book5.setPublishedYear(1954);
                    book5.setBookContent("Three Rings for the Elven-kings under the sky, Seven for the Dwarf-lords in their halls of stone, Nine for Mortal Men doomed to die, One for the Dark Lord on his dark throne In the Land of Mordor where the Shadows lie. The One Ring to rule them all, One Ring to find them, One Ring to bring them all and in the darkness bind them In the Land of Mordor where the Shadows lie. It was a strange fate that my adventure should begin with a box. Or rather, with an old book. It was a present from Bilbo, my adopted father. I was very young then, only a child, so I did not know much about it. Bilbo, who was older than most hobbits and had taken me under his wing, told me that it was a book of legends and adventures and that the words held great secrets.");
                    bookDAO.addBook(book5);

                    Book book6 = new Book();
                    book6.setTitle("Jane Eyre");
                    book6.setAuthor("Charlotte Brontë");
                    book6.setGenre("Gothic Romance");
                    book6.setPublishedYear(1847);
                     book6.setBookContent("There was no possibility of taking a walk that day. We had been wandering, indeed, in the leafless shrubbery an hour in the morning; but since dinner (Mrs. Reed, when there was no company, dined early) the cold winter wind had brought with it clouds so sombre, and a rain so penetrating, that further outdoor exercise was now out of the question. I was glad of it: I never liked long walks, especially on chilly afternoons: dreadful to me was the coming home in the raw twilight, with nipped fingers and toes, and a heart saddened by the chidings of Bessie, the nurse. With a book, I was sure of a quiet evening. I was seated by the window, and had scarcely commenced reading, when the sound of the gate made me glance upwards. Mrs. Reed and her daughters were returning from their afternoon's drive. I closed my book and settled into my chair, with a feeling of resignation.");
                    bookDAO.addBook(book6);

                    Book book7 = new Book();
                    book7.setTitle("The Great Gatsby");
                    book7.setAuthor("F. Scott Fitzgerald");
                    book7.setGenre("Tragedy");
                    book7.setPublishedYear(1925);
                    book7.setBookContent("In my younger and more vulnerable years my father gave me some advice that I’ve been turning over in my mind ever since. “Whenever you feel like criticizing anyone,” he told me, “just remember that all the people in this world haven’t had the advantages that you’ve had.” He didn’t say any more, but I understood that he meant a great deal more than that. In consequence, I’m inclined to reserve all judgments, a habit that has opened up many curious natures to me and also made me the victim of not a few veteran bores. The abnormal mind is quick to detect and attach itself to this quality when it appears in a normal person, and so it came about that in college I was unjustly accused of being a politician, because I was privy to the secret griefs of wild, unknown men.");
                    bookDAO.addBook(book7);

                    Book book8 = new Book();
                    book8.setTitle("Moby Dick");
                    book8.setAuthor("Herman Melville");
                    book8.setGenre("Adventure");
                    book8.setPublishedYear(1851);
                    book8.setBookContent("Call me Ishmael. Some years ago—never mind how long precisely—having little or no money in my purse, and nothing particular to interest me on shore, I thought I would sail about a little and see the watery part of the world. It is a way I have of driving off the spleen and regulating the circulation. Whenever I find myself growing grim about the mouth; whenever it is a damp, drizzly November in my soul; whenever I find myself involuntarily pausing before coffin warehouses, and bringing up the rear of every funeral I meet; and especially whenever my hypos get such an upper hand of me, that it requires a strong moral principle to prevent me from deliberately stepping into the street, and methodically knocking people's hats off—then, I account it high time to get to sea as soon as I can. This is my substitute for pistol and ball. With a philosophical flourish Cato throws himself upon his sword; I quietly take to the ship. There is nothing surprising in this.");
                    bookDAO.addBook(book8);
                        System.out.println("Initial users and books added to the database.");
             }else {
                    System.out.println("User and Book tables are not empty. Skipping initial data insertion.");
                }
         } else {
            System.out.println("User or Book tables do not exist yet. Skipping initial data insertion.");
        }

        }  catch (Exception e) {
              System.out.println("An error occurred during initial data insertion: " + e.getMessage());
               e.printStackTrace();
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}