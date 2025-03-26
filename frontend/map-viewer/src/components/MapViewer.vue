<script setup>
import { onMounted, ref, defineExpose } from "vue";
import L from "leaflet";
import "leaflet-draw";
import "leaflet-draw/dist/leaflet.draw.css";
import leafletImage from "leaflet-image";


const mapContainer = ref(null);
const username = ref('Explorer')
const showWelcome = ref(true)

let map;
let drawnItems;


// Initialize the map
onMounted(() => {
  const stored = localStorage.getItem('username')
  if (stored) username.value = stored

  
  if (mapContainer.value) {

    map = L.map(mapContainer.value, { zoomControl: false,renderer: L.canvas() }).setView([24.7136, 46.6753], 10);
   
    L.marker([24.7136, 46.6753]).addTo(map);

    L.control.zoom({
      position: 'bottomright'
    }).addTo(map);

    // Tile layers
    const basemaps = {
      StreetView: L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: '&copy; OpenStreetMap contributors',
      }),
      Light: L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://carto.com/">CARTO</a>',
        subdomains: 'abcd',
        maxZoom: 20
      }),

      Dark: L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
        subdomains: 'abcd',
        maxZoom: 20
      }),
      Satellite: L.tileLayer.wms("https://gibs.earthdata.nasa.gov/wms/epsg3857/best/wms.cgi", {
        layers: "BlueMarble_ShadedRelief",
        format: "image/png",
        transparent: true,
        attribution: '&copy; NASA Blue Marble, image service provided by GIBS, U.S. Geological Survey',
        tileSize: 512,

      }),
    }

    L.control.layers(basemaps).addTo(map);
    basemaps.StreetView.addTo(map);
    map.setMinZoom(2);

    // Draw Layer
    drawnItems = new L.FeatureGroup();
    map.addLayer(drawnItems);

    // Add Drawing Controls
    const drawControl = new L.Control.Draw({
      draw: {
        polygon: {
          shapeOptions: {
            color: 'purple'
          },
          allowIntersection: false,
          drawError: {
            color: 'orange',
            timeout: 1000
          },
          showArea: true,
          metric: false,
          repeatMode: true
        },
        polyline: {
          shapeOptions: {
            color: 'red'
          },
        },
        rectangle: false,
        circle: {
          shapeOptions: {
            color: 'steelblue'
          },
        },
      },
      edit: {
        featureGroup: drawnItems,
        remove: true,
      },
      position: 'topright',
    });
    map.addControl(drawControl);
    L.svg({ padding: 0.5 }).addTo(map);
    map.on("draw:created", (event) => { drawnItems.addLayer(event.layer); });
    map.on("draw:deleted", (event) => { console.log("Deleted items:", event.layers); });

  
  }

});



//Search Function
const searchLocation = (locationData) => {
  try {
    console.log("searchLocation in MapViewer called with:", locationData); 
    if (!locationData || !map) return;

    const { lat, lon, displayName } = locationData;
    map.setView([lat, lon], 12);

    // Clear existing markers
    map.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        map.removeLayer(layer);
      }
    });

    // Add new marker
    L.marker([lat, lon])
      .addTo(map)
      .bindPopup(`<b>${displayName}</b>`)
      .openPopup();
  } catch (error) {
    console.error("Error in searchLocation (MapViewer):", error);
  }
};

// Geolocation
const useMyLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        map.setView([latitude, longitude], 12);
        L.marker([latitude, longitude]).addTo(map);
      },
      () => {
        alert("Failed to get location. Please enable GPS.");
      }
    );
  } else {
    alert("Geolocation is not supported by your browser.");
  }
};

// Take Screenshot of Map
const takeScreenshot = () => {
  leafletImage(map, (err, canvas) => {
    if (err) {
      console.error("Error capturing map:", err);
      alert("Failed to capture the map.");
      return;
    }

    const imgData = canvas.toDataURL("image/png");
    const link = document.createElement("a");
    link.href = imgData;
    link.download = "map-screenshot.png";
    link.click();
  });
};



defineExpose({ searchLocation, takeScreenshot, useMyLocation });
</script>

<template>

<v-dialog v-model="showWelcome" max-width="400" transition="dialog-transition" persistent>
  <v-card class="pa-4"  variant="flat">
    <v-row align="center">
      <v-col cols="9">
        <h3 class="text-subtitle-1 ">Welcome back, {{ username }} 👋🏼</h3>
        <p class="text-body-2">Ready to explore the world today?</p>
      </v-col>
    </v-row>
    <v-divider class="my-2"></v-divider>
    <v-card-actions>
      <v-btn color="green" class="rounded-pill" @click="showWelcome = false">Let’s go!</v-btn>
    </v-card-actions>
  </v-card>
</v-dialog>


  <div ref="mapContainer" class="map-container">
  </div>

</template>

<style scoped>

.map-container {
    height: calc(100vh - 50px); 
    width: 100%;
    overflow: hidden;
}


</style>
