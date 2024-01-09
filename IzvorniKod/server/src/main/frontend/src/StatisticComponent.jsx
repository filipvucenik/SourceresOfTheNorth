import React, { useState, useEffect, useRef } from "react";
import HeaderComponent from "./HeaderComponent";
import FooterComponent from "./FooterComponent";
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import markerIcon from './marker.svg';

const server = "http://localhost:8080/";

function StatisticComponent() {
  const [filteredData, setFilteredData] = useState([]);
  const [selectedMarker, setSelectedMarker] = useState(null);
  const mapRef = useRef(null); // referenca za spremanje instance karte
  const uniqueMapId = `map-${Math.floor(Math.random() * 10000)}`;

  const customIcon = L.divIcon({
    html: `<div class="marker-container">
             <img class="marker-icon" src="${markerIcon}" />
           </div>`,
    iconSize: [26, 26],
    iconAnchor: [13, 26],
  });

  const createMarker = (map, lat, lng) => {
    if (selectedMarker) {
      map.removeLayer(selectedMarker);
    }
    
    const marker = L.marker([lat, lng], { icon: customIcon }).addTo(map);
    setSelectedMarker(marker);
    handleSendCoordinates();
    //promenit argument funkcije, saljem mu marker ili lat, lng
    let popupIsOpen = false;

    marker.on('click', () => {
      if (popupIsOpen) {
        marker.closePopup();
        popupIsOpen = false;
      } else {
        marker.bindPopup(`${lat},${lng}`).openPopup();
        popupIsOpen = true;
      }
    });
  };

  const handleSendCoordinates = () => {
    if (selectedMarker) {
      const { lat, lng } = selectedMarker.getLatLng();
      console.log('Koordinate za slanje:', { lat, lng });
  
      /*const dataForSend = {
        categoryID: document.getElementById("categoryID").value,
        status: document.getElementById("status").value,
        fromDateTime: document.getElementById("fromDateTime").value,
        toDateTime: document.getElementById("toDateTime").value,
        lat,
        lng,
      };
  
      fetch(`endpoint za statistiku`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataForSend),
      })
      .then(response => response.json())
      .then(data => console.log('Odgovor od servera:', data))
      .catch(error => console.error('Greška prilikom slanja koordinata:', error));*/
    }
  };
  

  useEffect(() => {
    if (!mapRef.current) {
      const map = L.map(uniqueMapId).setView([45.810, 15.985], 14);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 28,
      }).addTo(map);
      
      map.on('click', function (e) {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;
        createMarker(map, lat, lng);
      });

      mapRef.current = map;
      return () => {
        mapRef.current.off('click');
      };
    } else {
      mapRef.current.off('click');
      mapRef.current.on('click', function (e) {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;
        createMarker(mapRef.current, lat, lng);
      });

      mapRef.current.invalidateSize();
    }
  }, [selectedMarker]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const categoryID = e.target.elements.categoryID.value;
    const status = e.target.elements.status.value;
    const fromDateTime = e.target.elements.fromDateTime.value;
    const toDateTime = e.target.elements.toDateTime.value;
    const location = e.target.elements.location.value;
    const radius = e.target.elements.radius.value;

    try {
      const response =
        await fetch(`${server}filtered?categoryID=${categoryID}&fromDateTime=${fromDateTime}&toDateTime=${toDateTime}
          &status=${status}&location=${location}&radius=${radius}`);
      const data = await response.json();

      setFilteredData(data);
      console.log("Rezultati filtriranja:", data);
    } catch (error) {
      console.error("Greška prilikom dohvaćanja podataka:", error);
    }
  };

  return (
    <>
      <HeaderComponent />
      <div className="col-lg-6 col-md-10 col-sm-12 report-card">
        <h1>Statistika prijava</h1>
        <form onSubmit={handleSubmit}>
          <label>
            ID Kategorije:
            <input type="text" name="categoryID" />
          </label>
          <label>
            Status:
            <input type="text" name="status" />
          </label>
          <label>
            Datum prijave OD:
            <input type="text" name="fromDateTime" />
          </label>
          <label>
            Datum prijave DO:
            <input type="text" name="toDateTime" />
          </label>
          <p>Lokacija:</p>
          <div id={uniqueMapId} style={{ width: '90%', height: '40vh' , marginLeft: '5%'}}></div>
          <label>
            Radius(km):
            <input type="text" name="radius" />
          </label>
          <button className="submit-btn" type="submit">
            Filter
          </button>
        </form>

        <ul>
          {filteredData.map((item) => (
            <li key={item.categoryID}>
              <p>ID kategorije: {item.categoryID}</p>
              <p>Status: {item.status}</p>
              <p>Datum prijave OD: {item.fromDateTime}</p>
              <p>Datum prijave DO: {item.toDateTime}</p>
              <p>Lokacija: {item.location}</p>
              <p>Radius(km): {item.radius}</p>
            </li>
          ))}
        </ul>
      </div>
      <FooterComponent />
    </>
  );
}

export default StatisticComponent;
