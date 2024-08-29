
# Low-Level Design (LLD) for Windows Search Functionality

## Overview
This document provides the Low-Level Design (LLD) for a Windows Search functionality that supports searching based on a string pattern. The search results are matched against all file and folder names as well as file contents (if they are text-based). The results are ordered based on heuristic logic.

---

## 1. Class Structures

### 1.1 Driver Class
#### Diagram
\`\`\`plaintext
+----------------------------+
|          Driver             |
+----------------------------+
| + search(searchTerm: String): List<Result> |
| + handleAFile(file: File): void            |
+----------------------------+
\`\`\`

#### Explanation
- **Driver Class:** The main entry point for the search functionality. It has two main responsibilities:
  1. `search`: Takes a search term as input and uses the `Index` class to search the inverted index.
  2. `handleAFile`: Responsible for handling the creation, update, or deletion of a file by delegating to the `FileSystem`.

### 1.2 FileSystem Class
#### Diagram
\`\`\`plaintext
+----------------------------+
|         FileSystem          |
+----------------------------+
| + createAFile(docName: String, text: String): void |
| + updateAFile(docName: String, text: String): void |
| + deleteAFile(docName: String): void               |
+----------------------------+
\`\`\`

#### Explanation
- **FileSystem Class:** Responsible for file operations such as creation, updating, and deletion of files. It interacts with the `Index` class to update the index whenever a file is created, updated, or deleted.

### 1.3 File Class
#### Diagram
\`\`\`plaintext
+----------------------------+
|            File             |
+----------------------------+
| - path: String              |
| - content: String           |
+----------------------------+
\`\`\`

#### Explanation
- **File Class:** Represents a file in the system with attributes `path` and `content`. The `path` stores the file's location, and `content` holds the text-based content of the file.

### 1.4 Index Class
#### Diagram
\`\`\`plaintext
+----------------------------+
|            Index            |
+----------------------------+
| - invertedIndex: Map<String, List<IndexEntry>> |
| + searchIndex(term: String): List<Result> |
| + updateOrBuildIndex(file: File): void    |
+----------------------------+
\`\`\`

#### Explanation
- **Index Class:** Manages the inverted index, which maps search terms to a list of `IndexEntry` objects. It supports searching the index and updating the index when files are created, updated, or deleted.

### 1.5 IndexEntry Class
#### Diagram
\`\`\`plaintext
+----------------------------+
|         IndexEntry          |
+----------------------------+
| - file: File                |
| - position: int             |
| - frequency: int            |
+----------------------------+
\`\`\`

#### Explanation
- **IndexEntry Class:** Represents an entry in the inverted index. It contains a reference to the `File`, the `position` of the search term within the file (or -1 if it's a folder name), and the `frequency` of the term in the file.

---

## 2. Design Patterns

### 2.1 Singleton Pattern for Index Class
#### Why Chosen?
The `Index` class is a central component that should be shared across different parts of the system. The Singleton pattern ensures that only one instance of the `Index` class exists, which is accessible globally.

#### How Implemented?
The `Index` class has a private constructor and a static method `getInstance()` that returns the single instance of the class.

---

## 3. SOLID Principles

### 3.1 Single Responsibility Principle (SRP)
Each class has a single responsibility:
- `Driver`: Manages the overall search process.
- `FileSystem`: Handles file operations.
- `File`: Represents a file with its attributes.
- `Index`: Manages the inverted index.
- `IndexEntry`: Represents an entry in the index.

### 3.2 Open/Closed Principle (OCP)
The design is open for extension but closed for modification. New file operations or search algorithms can be added without modifying existing code.

### 3.3 Liskov Substitution Principle (LSP)
The classes in the design can be extended or replaced with subclasses without affecting the correctness of the system.

### 3.4 Interface Segregation Principle (ISP)
Classes do not depend on methods they do not use. Each class has a well-defined purpose and minimal interface.

### 3.5 Dependency Inversion Principle (DIP)
High-level modules like `Driver` depend on abstractions (e.g., `Index`) rather than concrete implementations.

---

## 4. Search Logic

### 4.1 Building an Inverted Index
The `Index` class builds and maintains an inverted index that maps each search term to the files and positions where it occurs. This index is updated asynchronously via a daemon process that monitors file system changes.

### 4.2 Updating the Index
The `FileSystem` class triggers the index update process whenever a file is created, updated, or deleted. The `Index` class then rebuilds the relevant parts of the index.

### 4.3 Searching the Index
The `Driver` class uses the `Index` class to search for a term in the inverted index. The results are ranked based on a heuristic logic that considers factors like term frequency, position, and file recency.

---

## 5. Example Java Code

### 5.1 Driver Class
\`\`\`java
import java.util.List;

public class Driver {
    public List<Result> search(String searchTerm) {
        return Index.getInstance().searchIndex(searchTerm);
    }

    public void handleAFile(File file) {
        FileSystem.handle(file);
    }
}
\`\`\`

### 5.2 FileSystem Class
\`\`\`java
public class FileSystem {
    public void createAFile(String docName, String text) {
        File file = new File(docName, text);
        Index.getInstance().updateOrBuildIndex(file);
    }

    public void updateAFile(String docName, String text) {
        File file = new File(docName, text);
        Index.getInstance().updateOrBuildIndex(file);
    }

    public void deleteAFile(String docName) {
        Index.getInstance().removeFromIndex(docName);
    }
}
\`\`\`

### 5.3 File Class
\`\`\`java
public class File {
    private String path;
    private String content;

    public File(String path, String content) {
        this.path = path;
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }
}
\`\`\`

### 5.4 Index Class
\`\`\`java
import java.util.*;

public class Index {
    private static Index instance;
    private Map<String, List<IndexEntry>> invertedIndex;

    private Index() {
        invertedIndex = new HashMap<>();
    }

    public static Index getInstance() {
        if (instance == null) {
            instance = new Index();
        }
        return instance;
    }

    public List<Result> searchIndex(String term) {
        return invertedIndex.getOrDefault(term, new ArrayList<>());
    }

    public void updateOrBuildIndex(File file) {
        String[] terms = file.getContent().split(" ");
        for (int i = 0; i < terms.length; i++) {
            invertedIndex.computeIfAbsent(terms[i], k -> new ArrayList<>())
                .add(new IndexEntry(file, i, Collections.frequency(Arrays.asList(terms), terms[i])));
        }
    }

    public void removeFromIndex(String docName) {
        // Logic to remove a file from the index
    }
}
\`\`\`

### 5.5 IndexEntry Class
\`\`\`java
public class IndexEntry {
    private File file;
    private int position;
    private int frequency;

    public IndexEntry(File file, int position, int frequency) {
        this.file = file;
        this.position = position;
        this.frequency = frequency;
    }

    public File getFile() {
        return file;
    }

    public int getPosition() {
        return position;
    }

    public int getFrequency() {
        return frequency;
    }
}
\`\`\`

### 5.6 Result Class
\`\`\`java
public class Result {
    private File file;
    private int rank;

    public Result(File file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public File getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }
}
\`\`\`

---

## Conclusion
This LLD outlines a robust design for a Windows Search functionality. The use of design patterns, adherence to SOLID principles, and a well-thought-out search logic ensure that the system is scalable, maintainable, and efficient.
