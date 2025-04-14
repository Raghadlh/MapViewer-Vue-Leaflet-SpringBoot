## 🗺️ MapViewer-Vue-Leaflet-SpringBoot 
<img width="1582" alt="Image" src="https://github.com/user-attachments/assets/f0bb434f-334a-4762-acfa-5bda2f9a1c92" />
An interactive map viewer built using Vue.js, Leaflet, and Spring Boot. This project allows users to search locations, view maps, and authenticate securely using Spring Security.

## Features  
### **Frontend (Vue.js + Leaflet)**  
- **nteractive Map** – Pan, zoom, and explore with Leaflet.
- **Custom Marker Management** – Add, edit, and delete your personal landmarks.
- **Geolocation** – Center the map based on your current location.
- **Search Functionality** – Find places using the Nominatim API.
- **Route Planning** – Generate routes between locations using Leaflet Routing Machine.
- **Theming** – Switch between multiple basemap layers (Street, Satellite, Dark, etc.).
- **Authentication** – Login, signup, and logout seamlessly.

### **Backend (Spring Boot + Security)**  
- **User Authentication** – Secure login and signup using Spring Security.
- **JWT + Session Handling** – Manages sessions securely with token-based auth.
- **User-Specific Landmarks** – Each user has their own set of stored markers.
-**CORS Configured** – Allows frontend access during development.


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

