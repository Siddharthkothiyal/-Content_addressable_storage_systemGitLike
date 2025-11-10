# Content Addressable Storage System (Git-Like)  

## Table of Contents  
- [Overview](#overview)  
- [Key Features](#key-features)  
- [Motivation & Why](#motivation-&-why)  
- [Architecture & Design](#architecture-&-design)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Installation](#installation)  
  - [Usage](#usage)  
- [Project Structure](#project-structure)  
- [Configuration & Customization](#configuration-&-customization)  
- [Testing & Validation](#testing-&-validation)  
- [Limitations & Known Issues](#limitations-&-known-issues)  
- [Future Roadmap](#future-roadmap)  
- [Contributing](#contributing)  
- [License](#license)  
- [Acknowledgements](#acknowledgements)  

---

## Overview  
This project implements a minimal **content-addressable storage system**, inspired by how Git organizes objects. It supports storing blobs, trees, and commits where each object is referenced by its content hash. The goal is to demonstrate the core concepts of content-addressable storage — immutability, deduplication, and referencing by hash — within a Java implementation.

## Key Features  
- Blob storage: store raw file contents as objects addressed by their SHA-1 or chosen hash.  
- Tree objects: represent directory structures mapping names to blobs or other trees.  
- Commit objects: reference a tree snapshot, include metadata (author, message, date), and optionally parent commits.  
- Repository initialization: sets up a dedicated storage directory (e.g., `.minigit`) to hold object files.  
- Simplified CLI or API interface to add, commit, and inspect objects.

## Motivation & Why  
Many developers use Git without necessarily understanding the underlying storage model. This project was built to:  
- Provide a clear, educational implementation of a Git-like storage system.  
- Enable experimentation with content-addressable storage concepts in a simple environment.  
- Serve as a foundation for further exploration (e.g., custom version control systems, content deduplication engines).

## Architecture & Design  
### Object Types  
- **Blob**: raw file content, stored and addressed by its content hash.  
- **Tree**: directory listing mapping filenames to blob/tree hashes.  
- **Commit**: includes a reference to a tree hash, metadata (author, timestamp, message), and optionally one or more parent commits.

### Storage Layout  
The repository root contains a `.minigit` folder with subfolders for object storage (e.g., by hash prefix) similar to Git’s design.

### Workflow  
1. `init` — initialise repository in working directory  
2. `add <file>` — convert file into a blob, write object file  
3. `commit -m "<message>"` — capture tree snapshot, store commit object  
4. `log` / `show <hash>` — inspect commit history or object content

## Getting Started  

### Prerequisites  
- Java Development Kit (JDK) version 8 or above  
- Apache Maven (or Gradle) depending on build setup  
- Git (optional, for versioning this project)  

### Installation  
```bash
# Clone the repository
git clone https://github.com/Siddharthkothiyal/-Content_addressable_storage_systemGitLike.git

# Change directory
cd Content_addressable_storage_systemGitLike

# Build the project (Maven example)
mvn clean install
