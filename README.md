# CS4600 Final Project
The goal of this final project was to implement a simulation of secure communication between two parties (Sender and Receiver) using AES, RSA, and MAC. See project instructions for more detailed information.

## Usage
After cloning the repository, run the following commands in the root directory of the project in order to compile and run the program (assuming Java is correctly installed and configured on your system):
- Compile the program: `javac -cp src src/project/*/*.java src/project/Simulation.java`
- Run the program: `java -cp src project.Simulation`

## Project Structure
The root directory of the project contains three folders: `receiver`, `sender`, and `src`. The `receiver` folder is meant to be accessed by the Receiver, and will contain the transmitted data from the Sender after the program is ran. The `sender` folder is meant to be accessed by the Sender, and contains the original message that the Sender wishes to send to the Receiver. The `src` folder contains all of the source code (explained in more detail in the **Code Structure** section below).

## Code Structure
Inside of the `src` folder is a `project` folder. All of the Java source files for this project are part of the `project` package. Located directly in the `project` folder is the file `Simulation.java`. This is the main file in the project that is meant to be executed once compiled. This file manages all of the other Java classes to create one seamless simulation for the user. Inside the `project` folder are three more folders: `receiver`, `sender`, and `security`. Inside the `receiver` and `sender` folder are `Receiver.java` and `Sender.java` respectively. These Java classes handle the logic specific to the Receiver or Sender party. Inside the `security` folder are the files `AES.java`, `MAC.java`, and `RSA.java`. These files hold the logic for their associated encryption/decryption or authentication functions, and are used by the previously mentioned Java classes to implement the simulation.
