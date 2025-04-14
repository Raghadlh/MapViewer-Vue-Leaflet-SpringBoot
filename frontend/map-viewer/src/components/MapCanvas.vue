<template>
  <v-app>
    <v-snackbar v-model="showError" color="error">
      {{ errorMessage }}
      <template v-slot:actions>
        <v-btn variant="text" @click="showError = false">Close</v-btn>
      </template>
    </v-snackbar>

    <NavBar
      :searchLocation="searchLocation"
      :takeScreenshot="takeScreenshot"
      :useMyLocation="useMyLocation"
      @planRoute="handleRoutePlanning"
    />
    <v-main>
      <MapViewer ref="mapViewerRef" @planRoute="handleRoutePlanning" />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from "vue";
import MapViewer from "./MapViewer.vue";
import NavBar from "./NavBar.vue";

const mapViewerRef = ref(null);
const showError = ref(false);
const errorMessage = ref("");

const showErrorNotification = (message) => {
  errorMessage.value = message;
  showError.value = true;
};

const searchLocation = (locationData) => {
  try {
    if (mapViewerRef.value) {
      mapViewerRef.value.searchLocation(locationData);
    }
  } catch (error) {
    showErrorNotification("Failed to search location: " + error.message);
    console.error("Error in searchLocation:", error);
  }
};

const takeScreenshot = () => {
  if (mapViewerRef.value) {
    mapViewerRef.value.takeScreenshot();
  }
};

const useMyLocation = () => {
  if (mapViewerRef.value) {
    mapViewerRef.value.useMyLocation();
  }
};

const handleRoutePlanning = (routeData) => {
  if (mapViewerRef.value) {
    mapViewerRef.value.planRoute(routeData);
  } else {
    console.error("MapViewer reference not ready.");
  }
};
</script>
