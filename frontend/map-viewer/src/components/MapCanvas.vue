<template>
  <v-app>
    <NavBar
      :searchLocation="searchLocation"
      :takeScreenshot="takeScreenshot"
      :useMyLocation="useMyLocation"
    />
    <v-main>
      
      <MapViewer ref="mapViewerRef" />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from "vue";
import MapViewer from "./MapViewer.vue";
import NavBar from "./NavBar.vue";

const mapViewerRef = ref(null);

const searchLocation = (locationData) => {
  try {
    console.log("searchLocation called with:", locationData); // Debugging
    if (mapViewerRef.value) {
      mapViewerRef.value.searchLocation(locationData);
    } else {
      console.error("MapViewer reference is not available.");
    }
  } catch (error) {
    console.error("Error in searchLocation:", error); // Debugging
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
</script>
