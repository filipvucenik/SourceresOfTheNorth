import React, { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import markerIcon from './marker.svg';

const server = "http://localhost:8080/";

function MapComponent() {
  const mapRef = useRef(null); // referenca za spremanje instance karte
  const markers = []; // Niz za pohranu svih markera

  const uniqueMapId = `map-${Math.floor(Math.random() * 10000)}`;

  useEffect(() => {
    if (!mapRef.current) { // stvaranje karte samo ako ref nije postavljen (MapComponent already initialized error)
      const map = L.map(uniqueMapId).setView([45.800, 15.967], 13);//stvaranje mape s centrom na Zagrebu

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { //dodavanje plocice karti
        maxZoom: 19,
      }).addTo(map);

      var customIcon = L.divIcon({ //ikona za prikaz markera
        html: `<div class="marker-container">
                 <img class="marker-icon" src="${markerIcon}" />
               </div>`,
               //<span class="delete-marker" style="position: absolute; top: -13px; right: -6px; cursor: pointer; color: red; font-size: 20px;">x</span>
        iconSize: [26, 26],
        iconAnchor: [13, 26], // promijenjen "anchor point" kako bi se ikona centrirala na točku
      });

      const createMarker = (lat, lng) => { //funkcija prikazivanja markera na temelju koordinata
        const marker = L.marker([lat, lng], { icon: customIcon }).addTo(map);
        markers.push(marker);

       /* marker.on('mouseover', function () { //prikaz gumba za brisanje markera kad je marker hoveran
          const deleteButton = marker.getElement().querySelector('.delete-marker');
          if (deleteButton) {
            deleteButton.style.display = 'block';
          }
        });

        marker.on('mouseout', function () { // sakrij gumb za brisanje kad mis napusti marker
          const deleteButton = marker.getElement().querySelector('.delete-marker');
          if (deleteButton) {
            deleteButton.style.display = 'none';
          }
        });

        marker.getElement().querySelector('.delete-marker').addEventListener('click', () => { // event listener za brisanje markera
          deleteMarker(marker);
        });*/

        let popupIsOpen = false;

        marker.on('click', () => { //otvaranje popUp-a svaki put kad se klikne na marker
          if (popupIsOpen) {
            marker.closePopup();
            popupIsOpen = false;
          } else {
            marker.bindPopup(`Naslov prijave, kratki opis bude tu + <br/><a href=''>Otvori stranicu prijave</a>`).openPopup();
            popupIsOpen = true;
          }
        });
      };
      //ovdje ce se dodavati markeri na koordinate prijavljenih steta
      var jason = '[{"lat":45.800, "lng" :15.967},{"lat":45.800, "lng":15.969}, {"lat":45.801, "lng":15.971}]';
      /*
      const fetchDataAndCreateMarkers = async () => {
        try {
          const response = await fetch(`${server}reports/unhandled`);
          const data = await response.json();
          console.log(data);
    
          for (const lokacija of data) {
            createMarker(lokacija.lat, lokacija.lng);
          }
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };

      fetchDataAndCreateMarkers();*/

      var lokacije = JSON.parse(jason);
      console.log(lokacije);
      for(var lokacija of lokacije){
        createMarker(lokacija.lat,lokacija.lng);
      }

      /*function deleteMarker(markerToDelete) { //brisanje markera i izbacivanje iz liste
        mapRef.current.removeLayer(markerToDelete);
        const index = markers.indexOf(markerToDelete);
        if (index !== -1) {
          markers.splice(index, 1);
        }
      }*/

      mapRef.current = map;
    } else {
      mapRef.current.invalidateSize();
    }
  }, []);

  return  <div id={uniqueMapId} style={{ width: '90%', height: '70vh' , marginLeft: '5%'}}></div>;
}

export default MapComponent;
