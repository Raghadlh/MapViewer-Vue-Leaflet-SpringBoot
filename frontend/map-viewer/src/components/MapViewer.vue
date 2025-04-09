<script setup>
import { onMounted, ref, defineExpose } from "vue";
import L from "leaflet";
import "leaflet-draw";
import "leaflet-draw/dist/leaflet.draw.css";
import leafletImage from "leaflet-image";
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
import 'leaflet-routing-machine';


const mapContainer = ref(null);
const username = ref('Explorer')
const showWelcome = ref(true)

let map;
let drawnItems;

var greenIcon = new L.Icon({
      iconUrl: '/marker-icon-2x-green.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });


// Initialize the map
onMounted(() => {
  const stored = localStorage.getItem('username')
  if (stored) username.value = stored

  
  if (mapContainer.value) {

    map = L.map(mapContainer.value, { zoomControl: false,renderer: L.canvas() }).setView([24.7136, 46.6753], 10);

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
      
    WorldImagery: L.layerGroup([
    L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
    attribution: 'Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, etc.'
    }),
    L.tileLayer('https://tiles.stadiamaps.com/tiles/stamen_terrain_labels/{z}/{x}/{y}{r}.png', {
      minZoom: 0,
      maxZoom: 18,
      attribution: '&copy; <a href="https://www.stadiamaps.com/">Stadia</a> & Stamen & OpenMapTiles & OSM contributors',
    })
    ]),
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

  const ResetViewControl = L.Control.extend({
  options: {
    position: 'bottomright', 
  },
  onAdd: function () {
    const container = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom');
    container.innerHTML = '<i class="mdi mdi-refresh" style="font-size: 20px;"></i>';
    container.title = 'Reset Map: View, Markers & Routes';
    container.style.backgroundColor = 'white';
    container.style.width = '34px';
    container.style.height = '34px';
    container.style.display = 'flex';
    container.style.alignItems = 'center';
    container.style.justifyContent = 'center';
    container.style.cursor = 'pointer';

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
  }
  });
  map.addControl(new ResetViewControl());

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
    L.marker([lat, lon],{icon: greenIcon})
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
        `https://nominatim.openstreetmap.org/search?format=json&limit=1&q=${encodeURIComponent(query)}`
      );
      const [location] = await res.json();
      return location ? [parseFloat(location.lat), parseFloat(location.lon)] : null;
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
        L.marker([latitude, longitude],{icon: greenIcon}).addTo(map);
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


defineExpose({ searchLocation, takeScreenshot, useMyLocation,planRoute,map,
  greenIcon});
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
.leaflet-control-custom {
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
}

</style>
