# Library Management System

## System Requirements

* A Library member should be able to search Books by Title, Author, Category and Publication Date
* Each Book will have Unique ID , and Rack Number and other details to physically locate the book.
* There can be more than one copy of book, let's call it a book item, library members should be able to checkout and reserve any copy.
* System should be able to give the details of who took the book, also the system should give details about all the books taken by a library member.
* Limit on max books a user can take:5 , Limit on the max days a user can keep a book: 10
* Systems should be able to collect fines for books returned after due date.
* Members should be able to reserve books that are currently not available.
* System should be able to send notifications when the book is available.
* Each Book and Member card will have barcodes and system should able to read the barcodes


## Use Case Diagram

Actors: Librarian, Member , System

### Use Cases:

* Add/Remove/Edit Book item from catalog.
* Search Catalog
* Register a new member / cancel membership.
* Checkout a book
* Reserve a book
* Renew a book
* Return a book

### Use Case Diagram:

https://app.diagrams.net/#G1-QyBEN70wo5jAsfLLiNkxliFfyTNEcEp


## Class Diagram

* Main Classes of Library Management System

* Library ( name, address )
* Book ( Title, Category, Author, Publisher, ISBN, etc.
* BookItem ( copy of the book - it has unique bar code)
* Account : Librarian, Member types
* LibraryCard
* BookReservation
* BookLending
* Catalog : will have List of books and search methods
* Fine
* Author
* Rack
* Notification

* Class Diagram: https://app.diagrams.net/#G1-QyBEN70wo5jAsfLLiNkxliFfyTNEcEp


### Code

* Enums, Constants and Basic Data Types

```java
public enum BookFormat{
HARDCOVER,
PAPERBACK,
AUDIO_BOOK,
EBOOK,
JOURNAL,
MAGAZINE,
NEWSPAPER
}

public enum BookStatus{
AVAILABLE,
RESERVED,
LOANED,
LOST
}

public enum ReservationStatus{
  WAITING,
  PENDING,
  CANCELED,
  NONE
}

public enum AccountStatus{
  ACTIVE,
  CLOSED,
  CANCELED,
  BLACKLISTED,
  NONE
}
public class Adress{
  private String streetAddress;
  private String city;
  private String state;
  private String zipCode;
  private String country;

}

public class Person {
private String name;
private Address address;
private string email;
privte string phone;
}

public class constants{
    public static final int MAX_BOOKS_ISSUES_TO_USER = 5;
    public static final int MAX_LENDING_DAYS= 10;
}
```

* Account , Member and Librarian

```java
public abstract class Account {
    private string id;
    private string password;
    private AccountStatus status;
    private Person person;

    public boolean resetPassword();
}

public class Librarian extends Account {
    public boolean addBookItem(BookItem bookItem);

    public boolean blockMember(Member member);

    public boolean unBlockMember(Member member);
}

public class Member extends Account {
    private Date dateOfMembership;
    private int totalBooksCheckedOut;
    public int getTotalBooksCheckedOut();

    public boolean reserveBookItem(BookItem bookItem );

    public boolean checkOutBookItem( BookItem bookItem ){
        if ( this.getTotalBooksCheckedOut() >= Constants.MAX_BOOKS_ISSUES_TO_USER ) {
           ShowError("Max Books Limit Reached");
           return false;
        }

        BookReservation br = BookReservation.fetchRservationDetails(bookItem.barCode);
        if ( br != null && br.getId() != this.getId() ) {
           ShowError("Reserved by another user";
        } else if ( br != null ){
           br.updateStatus(ReservationStatus.COMPLETED );
        }
        if ( !bookItem.checkout() ){
        return false;
        }
        br.setId(this.getId());
        this.incrementTotalBooksCheckedout();
        return true;

    }

    private void checkForFine(String bookItemBarcode) {

    }

    public void returnBookItem(BookItem bookItem) {

    }

    public bool renewBookItem(BookItem bookItem) {

    }
    }
```

* BookRservation, BookLending, Fine

```java
public class BookReservation {
  private Date creationDate;
  private ReservationStatus status;
  private String bookItemBarcode;
  private String memberId;

  public static BookReservation fetchReservationDetails(String barcode);
}

public class BookLending {
  private Date creationDate;
  private Date dueDate;
  private Date returnDate;
  private String bookItemBarcode;
  private String memberId;

  public static boolean lendBook(String barcode, String memberId);
  public static BookLending fetchLendingDetails(String barcode);
}

public class Fine {
  private Date creationDate;
  private double bookItemBarcode;
  private String memberId;

  public static void collectFine(String memberId, long days) {}
}
```

* Book, BookItem

```java
public abstract class Book {
  private String ISBN;
  private String title;
  private String subject;
  private String publisher;
  private String language;
  private int numberOfPages;
  private List<Author> authors;
}

public class BookItem extends Book {
  private String barcode;
  private boolean isReferenceOnly;
  private Date borrowed;
  private Date dueDate;
  private double price;
  private BookFormat format;
  private BookStatus status;
  private Date dateOfPurchase;
  private Date publicationDate;
  private Rack placedAt;

  public boolean checkout(String memberId) {
    if(bookItem.getIsReferenceOnly()) {
      ShowError("This book is Reference only and can't be issued");
      return false;
    }
    if(!BookLending.lendBook(this.getBarCode(), memberId)){
      return false;
    }
    this.updateBookItemStatus(BookStatus.LOANED);
    return true;
  }
}

public class Rack {
  private int number;
  private String locationIdentifier;
}

```

* Search Interface:

```java
public interface Search {
  public List<Book> searchByTitle(String title);
  public List<Book> searchByAuthor(String author);
  public List<Book> searchBySubject(String subject);
  public List<Book> searchByPubDate(Date publishDate);
}

public class Catalog implements Search {
  private HashMap<String, List<Book>> bookTitles;
  private HashMap<String, List<Book>> bookAuthors;
  private HashMap<String, List<Book>> bookSubjects;
  private HashMap<String, List<Book>> bookPublicationDates;

  public List<Book> searchByTitle(String query) {
    // return all books containing the string query in their title.
    return bookTitles.get(query);
  }

  public List<Book> searchByAuthor(String query) {
    // return all books containing the string query in their author's name.
    return bookAuthors.get(query);
  }
}
```