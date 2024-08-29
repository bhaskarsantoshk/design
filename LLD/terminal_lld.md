
# Low-Level Design (LLD) for Terminal-Based Prompt for File and Directory Management

## Overview

This document provides a low-level design for a terminal-based prompt system that supports the creation and updating of files and directories. The system supports basic commands like `mkdir`, `cd`, and `touch`, and it handles folder names with absolute paths, relative paths, and paths starting with `~` using the Chain of Responsibility design pattern.

## 1. Class Diagram

### 1.1. Classes

#### 1.1.1. `FileSystemElement` (Abstract Class)
- **Attributes:**
  - `name`: `String` - The name of the file or directory.
  - `path`: `String` - The path to this element.
  - `parent`: `Directory` - Reference to the parent directory.
- **Methods:**
  - `getFullPath() -> String`: Returns the full path of the element.

#### 1.1.2. `File` (Subclass of `FileSystemElement`)
- **Attributes:**
  - Inherits all attributes from `FileSystemElement`.
  - `content`: `String` - The content of the file.
- **Methods:**
  - `File(String name, Directory parent)`: Constructor to initialize a file.
  - `writeContent(String content)`: Writes content to the file.
  - `readContent() -> String`: Returns the content of the file.

#### 1.1.3. `Directory` (Subclass of `FileSystemElement`)
- **Attributes:**
  - Inherits all attributes from `FileSystemElement`.
  - `children`: `List<FileSystemElement>` - List of files and directories contained within this directory.
- **Methods:**
  - `Directory(String name, Directory parent)`: Constructor to initialize a directory.
  - `addElement(FileSystemElement element)`: Adds a file or directory to this directory.
  - `getElement(String name) -> FileSystemElement`: Retrieves a file or directory by name.
  - `listElements() -> List<FileSystemElement>`: Lists all files and directories in this directory.

#### 1.1.4. `Command` (Interface)
- **Methods:**
  - `execute(String[] args)`: Executes the command with the given arguments.

#### 1.1.5. `MkdirCommand` (Implements `Command`)
- **Attributes:**
  - `currentDirectory`: `Directory` - The current directory context.
- **Methods:**
  - `MkdirCommand(Directory currentDirectory)`: Constructor to initialize the command with the current directory.
  - `execute(String[] args)`: Executes the `mkdir` command to create a new directory.

#### 1.1.6. `CdCommand` (Implements `Command`)
- **Attributes:**
  - `currentDirectory`: `Directory` - The current directory context.
- **Methods:**
  - `CdCommand(Directory currentDirectory)`: Constructor to initialize the command with the current directory.
  - `execute(String[] args)`: Executes the `cd` command to change the current directory.

#### 1.1.7. `TouchCommand` (Implements `Command`)
- **Attributes:**
  - `currentDirectory`: `Directory` - The current directory context.
- **Methods:**
  - `TouchCommand(Directory currentDirectory)`: Constructor to initialize the command with the current directory.
  - `execute(String[] args)`: Executes the `touch` command to create a new file.

#### 1.1.8. `PathHandler` (Abstract Class in Chain of Responsibility)
- **Attributes:**
  - `next`: `PathHandler` - Reference to the next handler in the chain.
- **Methods:**
  - `handle(String path) -> Directory`: Abstract method to handle the given path.
  - `setNext(PathHandler next)`: Sets the next handler in the chain.

#### 1.1.9. `AbsolutePathHandler` (Implements `PathHandler`)
- **Methods:**
  - `handle(String path) -> Directory`: Handles absolute paths starting with `/`.

#### 1.1.10. `RelativePathHandler` (Implements `PathHandler`)
- **Methods:**
  - `handle(String path) -> Directory`: Handles relative paths.

#### 1.1.11. `HomePathHandler` (Implements `PathHandler`)
- **Methods:**
  - `handle(String path) -> Directory`: Handles paths starting with `~` (home directory).

#### 1.1.12. `FileSystemDriver`
- **Attributes:**
  - `rootDirectory`: `Directory` - The root directory of the file system.
  - `currentDirectory`: `Directory` - The current directory context.
  - `commandMap`: `Map<String, Command>` - A map of available commands.
  - `pathHandlerChain`: `PathHandler` - The head of the chain of responsibility for path handling.
- **Methods:**
  - `FileSystemDriver()`: Constructor to initialize the file system.
  - `executeCommand(String commandLine)`: Parses and executes the given command line.
  - `initializePathHandlers()`: Initializes the chain of responsibility for path handling.

## 2. Sequence Diagram

### 2.1. Sequence of Operations

1. **Input:** User enters a command (`mkdir`, `cd`, `touch`) in the terminal.
2. **Command Parsing:** The `FileSystemDriver` parses the command line to determine the command and arguments.
3. **Path Handling:** The path is processed by the `PathHandler` chain to resolve the target directory.
4. **Command Execution:** The appropriate command object is invoked to execute the command in the target directory context.
5. **Output:** The command's effect is applied, such as creating a directory, changing the current directory, or creating a file.

## 3. Example Usage

### 3.1. Creating Directories and Files

```java
FileSystemDriver fsDriver = new FileSystemDriver();
fsDriver.executeCommand("mkdir /home/user/docs");
fsDriver.executeCommand("cd /home/user/docs");
fsDriver.executeCommand("touch file1.txt");
```

### 3.2. Handling Different Path Types

```java
fsDriver.executeCommand("mkdir ~/projects");
fsDriver.executeCommand("cd ~/projects");
fsDriver.executeCommand("mkdir ./java");
fsDriver.executeCommand("cd java");
fsDriver.executeCommand("touch Main.java");
```

## 4. Chain of Responsibility Example

### 4.1. Path Handling

- **Absolute Path (`/home/user/docs`)**: The `AbsolutePathHandler` will process this and return the appropriate directory.
- **Relative Path (`./java`)**: The `RelativePathHandler` will process this relative to the current directory.
- **Home Path (`~/projects`)**: The `HomePathHandler` will expand the `~` to the user's home directory and process it.

## 5. Summary

This LLD outlines the design of a terminal-based file system command prompt with support for basic file and directory operations. The use of the Chain of Responsibility pattern allows for flexible and modular handling of different path types, ensuring the system can manage various file system scenarios effectively.
