# 🤖 Robot Worlds – The Ultimate Terminal Robot Battle  

Welcome to **Robot Worlds**, where your code-controlled warriors fight for digital dominance in a **Java-powered client-server battlefield**! Think **Tron**, but with mountains, bottomless pits, and **absolutely zero mercy**.  

## 🚀 What Is It?  

- A **client-server game** where players control robots.  
- The **server** generates a **grid world** with obstacles like mountains and bottomless pits.  
- The **clients** launch robots and **control them** to move, shoot, and (hopefully) survive.  
- The goal? **Blast your opponents into oblivion** while avoiding death traps.  

## 🛠️ Tech Stack  

- **Java** – The backbone of our robotic mayhem.  
- **Sockets & Networking** – Because robots need to communicate with their owners.  
- **Multithreading** – To keep multiple robots fighting at once.  
- **2D Grid System** – The battlefield of legends.  
- **Maven** – Because manually managing dependencies is for mere mortals.  

## 📦 Setup & Installation  

### Option 1: Using **Maven** (Recommended)  

```sh
git clone https://github.com/adudumayo/robot-worlds.git  
cd robot-worlds  
mvn package  
java -jar target/robot-worlds.jar  
```

### Option 2: Using IntelliJ or VS Code  

- Open the project in **IntelliJ** or **VS Code**.  
- If prompted, **import as a Maven project**.  
- Build and run the **Server** first, then the **Client**.  

## 🎮 How to Play  

### Starting the Server  

```sh
java -jar target/robot-worlds.jar SimpleServer  
```

- This launches the **Robot Worlds** server, generating a battlefield with random obstacles.  

### Starting a Client  

```sh
java -jar target/robot-worlds.jar SimpleClient  
```

- This connects a **robot controller** to the server.  
- You can **send movement commands**, **shoot at enemies**, and **avoid hazards**.  

## 🕹️ Controls  

- **MOVE [direction]** – Moves your robot (directions: `NORTH`, `SOUTH`, `EAST`, `WEST`).  
- **SHOOT** – Fires a projectile straight ahead.  

## 🌍 The Battlefield  

- **Mountains** – Block movement and bullets.  
- **Bottomless Pits** – Step in, and it's game over.  
- **Open Spaces** – Perfect for **robot shootouts**.  

## 🤝 Contribution  

- Feel free to **fork** and improve robot AI!  
- Add new **obstacles** and **power-ups** if you’re feeling creative.  
- Submit **pull requests** to upgrade the battleground.  

## 📄 License  

MIT – Because **even killer robots should have freedom**.  
