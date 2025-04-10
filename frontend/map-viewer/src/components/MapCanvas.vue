<template>
  <v-app>
    <NavBar :searchLocation="searchLocation" :takeScreenshot="takeScreenshot" :useMyLocation="useMyLocation"
      @planRoute="handleRoutePlanning" />
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

const searchLocation = (locationData) => {
  try {
    console.log("searchLocation called with:", locationData);
    if (mapViewerRef.value) {
      mapViewerRef.value.searchLocation(locationData);
    } else {
      console.error("MapViewer reference is not available.");
    }
  } catch (error) {
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
