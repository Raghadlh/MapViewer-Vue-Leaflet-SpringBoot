<script setup>
import { onMounted, ref, defineExpose } from "vue";
import L from "leaflet";
import "leaflet-draw";
import "leaflet-draw/dist/leaflet.draw.css";
import leafletImage from "leaflet-image";
import "leaflet-routing-machine/dist/leaflet-routing-machine.css";
import "leaflet-routing-machine";
import "leaflet-minimap";
import "leaflet-minimap/dist/Control.MiniMap.min.css";
import axios from "axios";

axios.defaults.withCredentials = true;
const mapContainer = ref(null);
const username = ref("Explorer");
const showWelcome = ref(true);
const isDrawing = ref(false);
const showSavedSnack = ref(false);

const emit = defineEmits(["planRoute"]);
const API_URL = "http://localhost:8080/api/markers";

const showMarkerDialog = ref(false);
const currentMarker = ref({
  id: null,
  title: "",
  description: "",
  latitude: 0,
  longitude: 0,
});

let map;
let drawnItems;
let markerLayer = L.layerGroup();

//Icons colors
var greenIcon = new L.Icon({
  iconUrl: "/marker-icon-2x-green.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41],
});

var goldIcon = new L.Icon({
  iconUrl: "/marker-icon-2x-gold.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41],
});

var redIcon = new L.Icon({
  iconUrl: "/marker-icon-2x-red.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41],
});
// Initialize the map
onMounted(async () => {
  const stored = localStorage.getItem("username");
  if (stored) username.value = stored;

  if (mapContainer.value) {
    map = L.map(mapContainer.value, {
      zoomControl: false,
      renderer: L.canvas(),
    }).setView([24.7136, 46.6753], 10);

    L.control
      .zoom({
        position: "bottomright",
      })
      .addTo(map);

    // Tile layers
    const basemaps = {
      StreetView: L.tileLayer(
        "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
        {
          attribution: "&copy; OpenStreetMap contributors",
        }
      ),
      Light: L.tileLayer(
        "https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png",
        {
          attribution: '&copy; <a href="https://carto.com/">CARTO</a>',
          subdomains: "abcd",
          maxZoom: 20,
        }
      ),

      Dark: L.tileLayer(
        "https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png",
        {
          attribution:
            '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
          subdomains: "abcd",
          maxZoom: 20,
        }
      ),

      WorldImagery: L.layerGroup([
        L.tileLayer(
          "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}",
          {
            attribution:
              "Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, etc.",
          }
        ),
        L.tileLayer(
          "https://tiles.stadiamaps.com/tiles/stamen_terrain_labels/{z}/{x}/{y}{r}.png",
          {
            minZoom: 0,
            maxZoom: 18,
            attribution:
              '&copy; <a href="https://www.stadiamaps.com/">Stadia</a> & Stamen & OpenMapTiles & OSM contributors',
          }
        ),
      ]),
      Satellite: L.tileLayer.wms(
        "https://gibs.earthdata.nasa.gov/wms/epsg3857/best/wms.cgi",
        {
          layers: "BlueMarble_ShadedRelief",
          format: "image/png",
          transparent: true,
          attribution:
            "&copy; NASA Blue Marble, image service provided by GIBS, U.S. Geological Survey",
          tileSize: 512,
        }
      ),
    };

    L.control.layers(basemaps).addTo(map);
    basemaps.StreetView.addTo(map);
    map.setMinZoom(2);

    //MiniMap
    const miniMapLayer = L.tileLayer(
      "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
      {
        attribution: false,
        interactive: false,
      }
    );

    const miniMap = new L.Control.MiniMap(miniMapLayer, {
      toggleDisplay: true,
      minimized: false,
      position: "bottomleft",
      shadowRectOptions: { color: "#27ae60", weight: 1, opacity: 0.6 },
    }).addTo(map);

    // Draw Layer
    drawnItems = new L.FeatureGroup();
    map.addLayer(drawnItems);

    // Add Drawing Controls
    const drawControl = new L.Control.Draw({
      draw: {
        polygon: {
          shapeOptions: {
            color: "purple",
          },
          allowIntersection: false,
          drawError: {
            color: "orange",
            timeout: 1000,
          },
          showArea: true,
          metric: false,
          repeatMode: true,
        },
        polyline: {
          shapeOptions: {
            color: "red",
          },
        },
        rectangle: false,
        circle: {
          shapeOptions: {
            color: "steelblue",
          },
        },
      },
      edit: {
        featureGroup: drawnItems,
        remove: true,
      },
      position: "topright",
    });
    map.addControl(drawControl);
    L.svg({ padding: 0.5 }).addTo(map);
    map.on("draw:created", (event) => {
      drawnItems.addLayer(event.layer);
    });
    map.on("draw:deleted", (event) => {
      console.log("Deleted items:", event.layers);
    });

    map.on("draw:drawstart", () => {
      isDrawing.value = true;
    });
    map.on("draw:drawstop", () => {
      isDrawing.value = false;
    });

    const ResetViewControl = L.Control.extend({
      options: {
        position: "bottomright",
      },
      onAdd: function () {
        const container = L.DomUtil.create(
          "div",
          "leaflet-bar leaflet-control leaflet-control-custom"
        );
        container.innerHTML =
          '<i class="mdi mdi-refresh" style="font-size: 20px;"></i>';
        container.title = "Reset Map: View, Markers & Routes";
        container.style.backgroundColor = "white";
        container.style.width = "34px";
        container.style.height = "34px";
        container.style.display = "flex";
        container.style.alignItems = "center";
        container.style.justifyContent = "center";
        container.style.cursor = "pointer";

        container.onclick = function () {
          // Reset view
          map.setView([24.7136, 46.6753], 10);

          // Remove all markers
          map.eachLayer((layer) => {
            if (layer instanceof L.Marker) {
              map.removeLayer(layer);
            }
          });

          // Remove routing control if it exists
          if (routingControl) {
            map.removeControl(routingControl);
            routingControl = null;
          }

          if (drawnItems) {
            drawnItems.clearLayers();
          }
        };

        return container;
      },
    });
    map.addControl(new ResetViewControl());

    // Add click handler for creating markers
    map.on("click", (e) => {
      if (isDrawing.value) return;

      if (e.originalEvent.target.className !== "leaflet-popup-content") {
        currentMarker.value = {
          id: null,
          title: "",
          description: "",
          latitude: e.latlng.lat,
          longitude: e.latlng.lng,
        };
        showMarkerDialog.value = true;
      }
    });
    markerLayer = L.layerGroup().addTo(map);
    await loadMarkers();
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
    L.marker([lat, lon], { icon: redIcon })
      .addTo(map)
      .bindPopup(`<b>${displayName}</b>`)
      .openPopup();
  } catch (error) {
    console.error("Error in searchLocation (MapViewer):", error);
  }
};

//Routing
let routingControl = null;

const planRoute = async ({ start, end }) => {
  try {
    const fetchCoord = async (query) => {
      const res = await fetch(
        `https://nominatim.openstreetmap.org/search?format=json&limit=1&q=${encodeURIComponent(
          query
        )}`
      );
      const [location] = await res.json();
      return location
        ? [parseFloat(location.lat), parseFloat(location.lon)]
        : null;
    };

    const startCoords = await fetchCoord(start);
    const endCoords = await fetchCoord(end);

    if (!startCoords || !endCoords) {
      alert("Could not find one of the locations.");
      return;
    }

    if (routingControl) {
      map.removeControl(routingControl);
    }

    routingControl = L.Routing.control({
      waypoints: [L.latLng(startCoords), L.latLng(endCoords)],
      routeWhileDragging: true,
      createMarker: function (i, wp) {
        return L.marker(wp.latLng, { icon: greenIcon, draggable: true });
      },
    }).addTo(map);
  } catch (error) {
    console.error("Routing error:", error);
    alert("Failed to calculate route.");
  }
};

// Geolocation
const useMyLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        map.setView([latitude, longitude], 12);
        L.marker([latitude, longitude], { icon: greenIcon }).addTo(map);
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

// CRUD Operations
const loadMarkers = async () => {
  try {
    const userId = localStorage.getItem("userId");
    console.log("Loading markers with credentials...");
    const response = await axios.get(`${API_URL}/user/${userId}`);
    console.log("Markers response:", response.data);

    if (!response.data || !Array.isArray(response.data)) {
      console.error("Invalid markers data format:", response.data);
      return;
    }

    // Clear existing markers
    markerLayer.clearLayers();

    // Add markers to the map
    response.data.forEach((marker) => {
      if (!marker.latitude || !marker.longitude) {
        console.warn("Marker missing coordinates:", marker);
        return;
      }

      const popupContent = marker.id
        ? `
    <div style="min-width: 200px">
      <h4>${marker.title || "Untitled Marker"}</h4>
      ${marker.description ? `<p>${marker.description}</p>` : ""}
      <div style="display: flex; gap: 5px; margin-top: 10px;">
        <button onclick="event.stopPropagation(); window.vm.editMarker(${
          marker.id
        })" 
          style="padding: 3px 8px; background: #4CAF50; color: white; border: none; border-radius: 3px; cursor: pointer;">
          Edit
        </button>
        <button onclick="event.stopPropagation(); window.vm.deleteMarker(${
          marker.id
        })" 
          style="padding: 3px 8px; background: #f44336; color: white; border: none; border-radius: 3px; cursor: pointer;">
          Delete
        </button>
      </div>
    </div>
  `
        : `
    <div style="min-width: 200px">
      <h4>${marker.title || "Untitled Marker"}</h4>
      ${marker.description ? `<p>${marker.description}</p>` : ""}
      <p style="color: red; font-size: 13px;">(Marker ID is missing. Cannot edit or delete.)</p>
    </div>
  `;

      const newMarker = L.marker([marker.latitude, marker.longitude], {
        icon: goldIcon,
      })
        .bindPopup(popupContent)
        .addTo(markerLayer);

      if (!marker.id) {
        newMarker.openPopup();
      }
    });

    window.vm = {
      editMarker,
      deleteMarker,
    };
  } catch (error) {
    console.error("Error loading markers:", error);
    if (error.response) {
      console.error("Response data:", error.response.data);
      console.error("Response status:", error.response.status);
    }
  }
};

const saveMarker = async () => {
  try {
    const userId = localStorage.getItem("userId");
    if (currentMarker.value.id) {
      await axios.put(
        `${API_URL}/${currentMarker.value.id}`,
        currentMarker.value
      );
    } else {
      await axios.post(`${API_URL}/user/${userId}`, currentMarker.value);
    }
    showSavedSnack.value = true;
    showMarkerDialog.value = false;
    await loadMarkers();
  } catch (error) {
    console.error("Error saving marker:", error);
  }
};

const editMarker = async (id) => {
  try {
    const response = await axios.get(`${API_URL}/${id}`);
    currentMarker.value = response.data;
    showMarkerDialog.value = true;
  } catch (error) {
    console.error("Error editing marker:", error);
  }
};

const deleteMarker = async (id) => {
  if (confirm("Are you sure you want to delete this marker?")) {
    try {
      await axios.delete(`${API_URL}/${id}`);
      await loadMarkers();
    } catch (error) {
      console.error("Error deleting marker:", error);
    }
  }
};

defineExpose({
  searchLocation,
  takeScreenshot,
  useMyLocation,
  planRoute,
  map,
  greenIcon,
  editMarker,
  deleteMarker,
});
</script>

<template>
  <v-dialog
    class="pa-4 welcome-card"
    v-model="showWelcome"
    max-width="420"
    transition="dialog-bottom-transition"
    persistent
  >
    <v-card class="pa-5 rounded-xl elevation-2">
      <v-row align="center">
        <v-col cols="9">
          <h2 class="text-h6 font-weight-medium">
            Welcome back, {{ username }} 👋🏼
          </h2>
          <p class="text-body-2 mt-2">Ready to explore the world today?</p>
        </v-col>
      </v-row>
      <v-divider class="my-3"></v-divider>
      <v-card-actions class="justify-end">
        <v-btn
          color="green"
          class="rounded-pill px-6"
          variant="flat"
          @click="showWelcome = false"
        >
          Let’s go!
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- Marker CRUD Dialog -->
  <v-dialog
    v-model="showMarkerDialog"
    max-width="500"
    transition="dialog-bottom-transition"
  >
    <v-card class="rounded-lg elevation-4">
      <v-card-title class="bg-green text-white">
        <span class="text-h6">{{
          currentMarker.id ? "Edit Marker" : "Add New Marker"
        }}</span>
      </v-card-title>

      <v-card-text class="pt-4">
        <v-text-field
          rounded="xl"
          v-model="currentMarker.title"
          label="📍 Marker Title"
          density="comfortable"
          variant="outlined"
          color="green"
          required
        />
        <v-textarea
          rounded="xl"
          v-model="currentMarker.description"
          label="📝 Description"
          rows="2"
          variant="outlined"
          color="green"
        />
        <v-row>
          <v-col cols="6">
            <v-text-field
              rounded="xl"
              v-model="currentMarker.latitude"
              label="Latitude"
              type="number"
              variant="outlined"
              readonly
              density="comfortable"
            />
          </v-col>
          <v-col cols="6">
            <v-text-field
              rounded="xl"
              v-model="currentMarker.longitude"
              label="Longitude"
              type="number"
              variant="outlined"
              readonly
              density="comfortable"
            />
          </v-col>
        </v-row>
      </v-card-text>

      <v-divider></v-divider>
      <v-card-actions class="pa-3">
        <v-spacer></v-spacer>
        <v-btn
          rounded="xl"
          variant="tonal"
          color="grey"
          @click="showMarkerDialog = false"
          >Cancel</v-btn
        >
        <v-btn rounded="xl" variant="flat" color="green" @click="saveMarker"
          >Save</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-snackbar v-model="showSavedSnack" color="green" timeout="2000">
    Marker saved successfully!
  </v-snackbar>

  <div ref="mapContainer" class="map-container"></div>
</template>

<style scoped>
.map-container {
  height: calc(100vh - 50px);
  width: 100%;
  overflow: hidden;
}

.leaflet-control-custom {
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
}

.welcome-card {
  box-shadow: 0 4px 20px rgba(0, 128, 0, 0.15);
  border-radius: 16px;
}
</style>
