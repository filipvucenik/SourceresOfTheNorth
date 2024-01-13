import React, { useEffect, useRef, useState } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import markerIcon from './marker.svg';
import apiConfig from "./apiConfig";

const server = apiConfig.getReportUrl;

const MapComponent = (props) => {
  const mapRef = useRef(null); // referenca za spremanje instance karte
  const markers = []; // Niz za pohranu svih markera
  const [mainData, setMainData] = useState([]);
  const uniqueMapId = `map-${Math.floor(Math.random() * 10000)}`;

  useEffect(() => {
    if (!mapRef.current) { // stvaranje karte samo ako ref nije postavljen (MapComponent already initialized error)
      const map = L.map(uniqueMapId).setView([45.800, 15.967], 13);//stvaranje mape s centrom na Zagrebu

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { //dodavanje plocice karti
        maxZoom: 19,
      }).addTo(map);

      const customIcon = new L.Icon({
        iconUrl: markerIcon,
        iconSize: [32, 32],
        iconAnchor: [16, 32],
        popupAnchor: [0, -32],
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
      
      const fetchDataAndCreateMarkers = async () => {
        try {
          const response = await fetch(`${server}/unhandled`);
          const data = await response.json();
          console.log(data);
          setMainData(data);
          ispisMarkera();
          /*for (const lokacija of data) {
            createMarker(lokacija.report.lat, lokacija.report.lng);
          }*/
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };

      const ispisMarkera = () => {
        console.log(props.sharedVariable);
        const dataToIterate = (props.sharedVariable !== "undefined") ? props.sharedVariable : mainData;
        console.log(dataToIterate);
        for (const lokacija of dataToIterate) {
          createMarker(lokacija.report.lat, lokacija.report.lng);
        }
      };

      fetchDataAndCreateMarkers();

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
