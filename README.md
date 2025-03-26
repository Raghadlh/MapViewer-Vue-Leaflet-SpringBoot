## 🗺️ MapViewer-Vue-Leaflet-SpringBoot 
<img width="1582" alt="Image" src="https://github.com/user-attachments/assets/f0bb434f-334a-4762-acfa-5bda2f9a1c92" />
An interactive map viewer built using Vue.js, Leaflet, and Spring Boot. This project allows users to search locations, view maps, and authenticate securely using Spring Security.

## Features  
### **Frontend (Vue.js + Leaflet)**  
-   **Interactive Map** – Browse and navigate locations dynamically.  
-   **Search Functionality** – Search places using the Nominatim API.  
-   **Geolocation** – Use the "Use My Location" feature to find your current location.  
-   **Theming** – Toggle between light/dark modes.  
-   **User Authentication** – Login, Signup, and Logout.  

### **Backend (Spring Boot + Security)**  
- *User Authentication** – Secure login & signup using Spring Security.  
- **JWT & Session Handling** – Manages user sessions effectively.  
- **CORS Configured** – Ensures cross-origin requests from the frontend.  


##  Setup Instructions  

### **1. Clone the Repository**

### **2. Backend  Setup**  
 **Make sure you have Java 17+ and Maven installed.**  

1. Go to `backend/src/main/resources/` and create `application.yml`
2. Add database credentials, JWT settings, etc.
3. Run the backend:

### **3. Frontend  Setup**  
 **Ensure Node.js (v16+) is installed**  
```sh
cd frontend
npm install
npm run dev
```
Now, visit `http://localhost:5173/` to see the application.

