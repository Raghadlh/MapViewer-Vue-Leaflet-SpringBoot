## 🗺️ MapViewer-Vue-Leaflet-SpringBoot 
<img width="1582" alt="Image" src="https://github.com/user-attachments/assets/1a221617-c84d-43a1-9f13-aa669c8f3a93" />
<img width="1582" alt="Image" src="https://github.com/user-attachments/assets/5831319b-e2e7-433c-b594-dfd54a367362" />
<img width="1582" alt="Image" src="https://github.com/user-attachments/assets/fa1ef260-0497-4105-9e86-ded21f5766df" />
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

