<template>
  <v-app-bar :elevation="0" class="px-3 border-b">
    <v-app-bar-nav-icon variant="text" @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

    <v-img src="/logo2.svg" max-height="150" max-width="150" class="mr-4"></v-img>
    <v-spacer></v-spacer>

    <!-- Search Bar -->
    <div style="position: relative; width: 500px; z-index: 9999;">
      <v-menu v-model="isSearching" :close-on-content-click="false" offset-y>
        <template v-slot:activator="{ props }">
          <v-text-field v-model="searchQuery" label="Search location..." hide-details dense rounded="xl"
            variant="outlined" single-line clearable append-inner-icon="mdi-magnify" @keydown.enter="onEnter"
            @input="fetchSuggestions" v-bind="props" />
        </template>

        <v-list class="search-results" v-if="searchQuery">
          <v-list-item v-for="item in searchSuggestions" :key="item.place_id" @click="onSelectResult(item)">
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </div>

    <v-btn elevation="0" rounded="xl" color="green" variant="flat" @click="onEnter" class="ml-3">
      Search
    </v-btn>

    <v-btn variant="outlined" color="green" class="ml-5"
      :icon="theme.global.current.value.dark ? 'mdi-lightbulb-outline' : 'mdi-lightbulb-on-outline'"
      @click="toggleTheme"></v-btn>

  </v-app-bar>

  <v-navigation-drawer v-model="drawer" :rail="rail" permanent @click="rail = false" :elevation="0"
    class="modern-drawer" width="300">
    <v-list class="d-flex flex-column fill-height">
      <!-- User Profile Section -->
      <v-list-item :prepend-avatar="'https://api.dicebear.com/9.x/dylan/svg?seed=' + randomSeed" class="py-4 border-b">
        <v-list-item-title> {{ username }} </v-list-item-title>
        <template v-slot:append>
          <v-btn icon="mdi-chevron-left" variant="text" @click.stop="rail = !rail"></v-btn>
        </template>
      </v-list-item>

      <v-list-item @click="toggleFullscreen" title="Fullscreen" prepend-icon="mdi-fullscreen"></v-list-item>

      <v-list-item @click="useMyLocation" title="Use My Location" prepend-icon="mdi-crosshairs-gps"></v-list-item>

      <!-- Tools Section -->
      <v-divider class="my-2"></v-divider>

      <v-list-item @click="takeScreenshot" title="Take Screenshot" prepend-icon="mdi-camera"></v-list-item>

      <!-- Routing Trigger -->
      <v-list-item @click="routingDialog = true" prepend-icon="mdi-map-marker-path" title="Routing"></v-list-item>

      <!-- Routing Dialog -->
      <v-dialog v-model="routingDialog" max-width="400">
        <v-card class="pa-4">
          <v-card-title>
            <span class="text-h6">Plan Route</span>
          </v-card-title>
          <v-card-text>
            <v-text-field v-model="routeStart" label="Start Location" variant="outlined" density="compact" rounded="xl"
              hide-details class="mb-3" />
            <v-text-field v-model="routeEnd" label="Destination" variant="outlined" density="compact" rounded="xl"
              hide-details />
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="grey" variant="outlined" rounded="xl" @click="routingDialog = false">Cancel</v-btn>
            <v-btn color="green" variant="flat" rounded="xl" @click="calculateRoute">Show Route</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>



      <!-- About Section -->
      <v-divider class="my-2"></v-divider>
      <v-list-item @click="$router.push('/about')" title="About" prepend-icon="mdi-information-outline"></v-list-item>


      <v-spacer></v-spacer>

      <!-- Logout Button -->
      <v-list-item @click="confirmLogout" prepend-icon="mdi-logout">
        Logout
      </v-list-item>

    </v-list>
  </v-navigation-drawer>

  <!-- Logout Confirmation Dialog -->
  <v-dialog v-model="dialogs.logout" max-width="400px">
    <v-card class="pa-4">
      <v-card-title>Confirm Logout</v-card-title>
      <v-card-text>Are you sure you want to logout?</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="green" rounded="xl" @click="dialogs.logout = false">Cancel</v-btn>
        <v-btn color="error" rounded="xl" @click="logout">Logout</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onMounted, onUnmounted, reactive } from "vue";
import { useTheme } from "vuetify";
import { useRouter } from "vue-router";


// Profile
const username = ref("Explorer");
const stored = localStorage.getItem("username");
if (stored) username.value = stored;

// Generate a random seed for avatar
const randomSeed = Math.random().toString(36).substring(2);

// Dialogs state
const dialogs = reactive({
  logout: false,
});

// Theme
const theme = useTheme();
const toggleTheme = () => {
  theme.global.name.value = theme.global.current.value.dark ? "light" : "dark";
};

// Drawer
const drawer = ref(false);
const rail = ref(true)

// Search Functionality
const searchQuery = ref("");
const searchSuggestions = ref([]);
const isSearching = ref(false);

// Props
const props = defineProps(["searchLocation", "takeScreenshot", "useMyLocation", "map",
  "greenIcon"]);

const map = props.map;
const greenIcon = props.greenIcon;

// Close dropdown when clicking outside
const closeDropdown = (event) => {
  if (!event.target.closest(".search-container")) {
    isSearching.value = false;
  }
};

onMounted(() => {
  document.addEventListener("click", closeDropdown);
});

onUnmounted(() => {
  document.removeEventListener("click", closeDropdown);
});

// Fetch suggestions from Nominatim API
const fetchSuggestions = async () => {
  if (!searchQuery.value || searchQuery.value.length < 3) {
    searchSuggestions.value = [];
    isSearching.value = false;
    return;
  }

  const response = await fetch(
    `https://nominatim.openstreetmap.org/search?format=json&limit=5&q=${encodeURIComponent(searchQuery.value)}`
  );
  const results = await response.json();

  if (results.length > 0) {
    searchSuggestions.value = results.map((item) => ({
      title: item.display_name,
      place_id: item.place_id,
      lat: item.lat,
      lon: item.lon,
    }));
    isSearching.value = true;
  } else {
    searchSuggestions.value = [];
    isSearching.value = false;
  }
};

// Handle selection of search result
const onSelectResult = (item) => {
  searchQuery.value = item.title;
  searchSuggestions.value = [];
  isSearching.value = false;
};

// Handle Enter key
const onEnter = async () => {
  try {
    if (!searchQuery.value.trim()) return;

    isSearching.value = true;
    const response = await fetch(
      `https://nominatim.openstreetmap.org/search?format=json&limit=1&q=${encodeURIComponent(searchQuery.value)}`
    );
    const results = await response.json();

    if (results.length > 0) {
      const location = results[0];
      props.searchLocation({
        lat: parseFloat(location.lat),
        lon: parseFloat(location.lon),
        displayName: location.display_name,
      });
    } else {
      alert("No results found. Try a different search.");
    }
  } catch (error) {
    console.error("Error in onEnter:", error);
  } finally {
    isSearching.value = false;
  }
};

// Fullscreen Mode
const isFullscreen = ref(false);
const toggleFullscreen = () => {
  if (!isFullscreen.value) {
    document.documentElement.requestFullscreen();
  } else {
    document.exitFullscreen();
  }
  isFullscreen.value = !isFullscreen.value;
};

//Routing
const routingDialog = ref(false);
const routeStart = ref("");
const routeEnd = ref("");
const emit = defineEmits(["planRoute"]);
const calculateRoute = () => {
  if (!routeStart.value || !routeEnd.value) {
    alert("Please enter both start and end locations.");
    return;
  }
  emit("planRoute", {
    start: routeStart.value,
    end: routeEnd.value,
    routingDialog: routingDialog.value = false,
  });
};


// Logout
const confirmLogout = () => {
  dialogs.logout = true;
};

const router = useRouter();
const logout = async () => {
  try {
    await fetch("/logout", {
      method: "POST",
      credentials: "include",
    });
    router.push("/");
  } catch (error) {
    console.error("Logout failed", error);
  }
};
</script>

<style scoped>
.search-results {
  width: 300px;
  max-height: 200px;
  overflow-y: auto;
  border-radius: 8px;
  background: white;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
}

.v-list-item:hover {
  background: rgba(0, 0, 0, 0.05);
  cursor: pointer;
}

.v-app-bar {
  overflow: visible !important;
}

.place-item {
  transition: all 0.3s ease;
  cursor: pointer;
}

.place-item:hover {
  background: rgba(0, 128, 0, 0.05);
  transform: translateX(5px);
}

.v-list {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.pa-4 {
  margin-top: auto;
}
</style>
