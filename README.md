# Command Line Interpreter
A `Java console-based` application that mimics the Command Line Interpreters of operating systems. It allows the user to `Enter the Input through the Keyboard`:
* After the user **`Writes the Command`** and **Presses Enter**, the **`String is Parsed`**, and the indicated **command executed**.
* The CLI will keep accepting different commands from the user until the user writes "**`exit`**", then the **`CLI terminates`**.


## Supported Commands
### echo:
* Takes **`1 Argument`** and **Prints it**.

### pwd:
* Takes **`No Arguments`** and **Prints the Current Path**.

### cd:
* Takes **`No Arguments`** and **Changes the Current Path** to the **`Home Directory Path`**.
* Takes **`1 Argument`** which is **`..`** and **Changes the Current Directory** to the **`Previous Directory`**.
* Takes **`1 Argument`** which is either the **Full Path or Relative Path** and **`Changes the Current Path`** to **`that Path`**.

### ls:
* Takes **`No Arguments`** and **Lists the Contents of the Current Directory `Sorted Alphabetically`**.

### ls -r:
* Takes **`No Arguments`** and **Lists the Contents of the Current Directory in `Reverse Order`**.

### mkdir:
* Takes **`1 or more Arguments`** and **Creates a Directory for Each Argument**. Each argument can be:  
  - **`Directory Name`** in this case the new directory is created in the current directory.
  - **`Path, Full or Relative`** that ends with a directory name, in this case the new directory is created in the given path.
 
### rmdir:
* Takes **`1 Argument`** which is **`*`** and **Removes All the Empty Directories in the current directory**.
* Takes **`1 Argument`** which is either the **`Full or Relative Path`** and **Removes the Given Directory Only If it is Empty**.

### touch:
* Takes **`1 Argument`** which is either the **`Full or Relative Path that Ends with a File Name`** and **Creates this File**.

### cp:
* Takes **`2 Arguments`**, both are **`File Names`** and **Copies the First onto the Second**.

### cp -r:
* Takes **`2 Arguments`**, both are **`Directories`** (empty or not) and **Copies the First Directory with all its content into the Second one**.

### rm:
* Takes **`1 Argument`** which is a **`File Name`** that exists in the current directory and **Removes this File**.

### cat:
* Takes **`1 Argument`** which is a **`File Name`** and **Prints the file’s content**.
* Takes  **`2 Arguments`**, both are **`File Names`**  and **Concatenates the content of the two files and prints it**.

### > :
* Format: **`command > FileName`**.
* **Redirects the output of the first command to be written to a file**.
* If the file doesn’t exist, it will be created.
* If the file exists, its original content will be replaced.
<br>

### >> :
* Format: **`command >> FileName`**.
* The same functionality as '>' but **Appends to the file if it exists**.


## Project Authors:
* [Roaa EmadEldin](https://github.com/RoaaEmadEldin)
* [Osama Maher](https://github.com/osama392maher)
* [Abdullah Sadek](https://github.com/Abdul-Sadek)
* [Mariam Nasr](https://github.com/marimNasr)
