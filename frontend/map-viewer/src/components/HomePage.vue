<template>
  <v-app>
    <v-main class="home-page">
      <!-- Navbar -->
      <v-app-bar flat color="transparent" elevate-on-scroll class="px-4 navbar">
        <v-img src="/logo2.svg" max-height="165" max-width="165" class="mt-2 mr-4 logo-hover"></v-img>
        <v-spacer></v-spacer>

        <v-btn text class="rounded-pill mx-2 nav-btn" @click="$router.push('/about')">About</v-btn>
        <v-divider vertical></v-divider>
        <v-btn text class="rounded-pill mx-2 nav-btn" @click="$router.push('/login')">Login</v-btn>
        <v-divider vertical></v-divider>
        <v-btn color="green darken-2" class="rounded-pill mx-2 signup-btn" @click="$router.push('/signup')">Sign
          Up</v-btn>
      </v-app-bar>

      <!-- Hero Section -->
      <v-container class="hero-section">
        <v-row align="center" justify="center">
          <!-- Left Side: Text & Button -->
          <v-col cols="12" md="6" class="text-left">
            <h1 class="text-h2 font-weight-bold mb-6 gradient-text fade-in">
              Explore the World with MapViewer
            </h1>
            <p class="text-h6 text-grey-darken-1 mb-8">
              Draw, plan, and explore interactive maps like never before.
            </p>
            <v-btn size="x-large" color="green" class="rounded-pill px-8 py-3 get-started-btn bounce"
              @click="$router.push('/signup')">
              Get Started <v-icon right class="ml-2">mdi-arrow-right</v-icon>
            </v-btn>
          </v-col>

          <!-- Right Side: Map Image -->
          <v-col cols="12" md="6" class="d-flex align-center justify-center">
            <img src="/map.svg" class="map-image floating" />
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>
/* Background */
.home-page {
  background: linear-gradient(135deg, #e8f5e9, #ffffff);
  min-height: 100vh;
}

/* Navbar */
.navbar {
  backdrop-filter: blur(10px);
}

.nav-btn {
  transition: all 0.3s ease;
}

.nav-btn:hover {
  transform: scale(1.05);
}

.signup-btn {
  transition: all 0.3s ease;
}

.signup-btn:hover {
  transform: scale(1.1);
}

/* Logo */
.logo-hover {
  transition: transform 0.3s ease;
}

.logo-hover:hover {
  transform: scale(1.05);
}

/* Hero Section */
.hero-section {
  display: flex;
  align-items: center;
  height: 100vh;
  padding: 0 10%;
}

/* Gradient Text */
.gradient-text {
  background: linear-gradient(45deg, #3bb371, #015b07);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* Get Started Button */
.get-started-btn {
  transition: all 0.3s ease;
}

.get-started-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(46, 125, 50, 0.4);
}

/* Map Image */
.map-image {
  max-width: 700px;
  width: 100%;
}

/* Floating Animation */
.floating {
  animation: float 5s infinite ease-in-out;
}

@keyframes float {

  0%,
  100% {
    transform: translateY(0px);
  }

  50% {
    transform: translateY(-15px);
  }
}

/* Fade-in Animation */
.fade-in {
  animation: fadeIn 1.5s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

.bounce {
  animation: bounce 2s infinite;
}

@keyframes bounce {

  0%,
  100% {
    transform: translateY(0);
  }

  50% {
    transform: translateY(-5px);
  }
}

/* Responsive Design */
@media (max-width: 960px) {
  .hero-section {
    flex-direction: column;
    text-align: center;
    padding: 5% 5%;
  }

  .map-image {
    max-width: 400px;
  }
}
</style>
