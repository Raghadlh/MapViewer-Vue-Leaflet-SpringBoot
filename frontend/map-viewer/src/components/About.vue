<template>
  <v-app>
    <v-main class="about-page">
      <!-- Navigation Bar -->
      <v-app-bar flat color="transparent" elevate-on-scroll class="px-4">
        <v-img src="/logo2.svg" max-height="165" max-width="165" class="mt-2 logo-hover"></v-img>
        <v-spacer></v-spacer>

        <v-btn text class="rounded-pill mx-2" @click="$router.push('/')">Home</v-btn>
        <v-divider vertical="true"></v-divider>
        <v-btn text class="rounded-pill mx-2" @click="$router.push('/login')">Login</v-btn>
        <v-divider vertical="true"></v-divider>
        <v-btn color="green darken-2" class="rounded-pill mx-2 px-6" @click="$router.push('/signup')">Sign Up</v-btn>
      </v-app-bar>

      <!-- Hero Section -->
      <v-container class="hero-section text-center">
        <v-row justify="center">
          <v-col cols="12" md="8">
            <h1 class="text-h2 font-weight-bold gradient-text">Discover More with MapViewer</h1>
            <p class="text-h6 text-grey-darken-1 mt-4">
              A smarter way to explore, draw, and interact with maps. Plan routes, add markers, and visualize your world.
            </p>
          </v-col>
        </v-row>
        <v-row justify="center" class="mt-8">
          <v-col cols="12" md="8">
            <div class="glass-container">
              <img src="/about.png" class="feature-image" />
            </div>
          </v-col>
        </v-row>
      </v-container>

      <!-- Features Section -->
      <v-container class="features-section">
        <v-row justify="center">
          <v-col cols="12" md="4" v-for="(feature, index) in features" :key="index">
            <v-card elevation="0" class="glass-card h-100 rounded-xl text-center pa-6">
              <v-icon size="x-large" :color="feature.color" class="mb-4">{{ feature.icon }}</v-icon>
              <h3 class="text-h5 font-weight-bold mb-3">{{ feature.title }}</h3>
              <p class="text-body-1">{{ feature.description }}</p>
            </v-card>
          </v-col>
        </v-row>
      </v-container>

      <v-container class="cta-section text-center py-12">
        <v-row justify="center">
          <v-col cols="12" md="8">
            <h2 class="text-h4 font-weight-bold">Start Exploring Today!</h2>
            <p class="text-body-1 text-grey-lighten-2 mt-3">
              Sign up now and create your first interactive map in just a few clicks.
            </p>
            <v-btn color="white" variant="outlined" class="rounded-pill px-8  get-started-btn mt-4" elevation="0" @click="$router.push('/signup')">
              Get Started
              <v-icon right class="ml-2">mdi-arrow-right</v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
const features = [
  {
    icon: "mdi-map-marker-plus",
    color: "green-darken-2",
    title: "Custom Drawings",
    description: "Easily draw routes, add annotations, and mark locations on your own personalized map.",
  },
  {
    icon: "mdi-map-search",
    color: "blue-darken-2",
    title: "Smart Navigation",
    description: "Search and explore maps with advanced geolocation features and detailed views.",
  },
  {
    icon: "mdi-security",
    color: "purple-darken-2",
    title: "Privacy & Security",
    description: "Your data is secure with advanced encryption and authentication measures.",
  },
];
</script>

<style scoped>
.about-page {
  background: linear-gradient(135deg, #e8f5e9, #ffffff);
}

/* Hero Section */
.hero-section {
  padding-top: 80px;
}

.feature-image {
  max-width: 900px;
  width: 100%;
  animation: float 4s ease-in-out infinite;
}



/* Gradient Text */
.gradient-text {
  background: linear-gradient(45deg, #3BB371, #015b07);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* Features Section */
.features-section {
  padding: 50px 0;
}

.glass-card {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.glass-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}


.cta-section {
  background: linear-gradient(135deg, #3bb371, #015b07);
  color: white;
  border-radius: 15px;
  padding: 60px;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.1);
}

.get-started-btn {
  transition: all 0.3s ease;
}

.get-started-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(46, 125, 50, 0.4);
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

/* Logo Hover */
.logo-hover {
  transition: transform 0.3s ease;
}

.logo-hover:hover {
  transform: scale(1.05);
}
</style>
