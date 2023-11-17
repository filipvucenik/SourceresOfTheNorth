import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import './Rforma.css'
import L from 'leaflet';
import markerIcon from'./marker.svg';
import apiConfig from './apiConfig';
import { BrowserRouter as Router, Link } from "react-router-dom";
import FooterComponent from "./FooterComponent.jsx";

const ReportCard = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [category, setCategory] = useState('');
  const [location, setLocation] = useState({ lat: 45.804270084085914, lng: 15.978798866271974 }); // Default na Zg
  const [picture, setPicture] = useState(null);
  const [manualAddress, setManualAddress] = useState('');
  const pinpointIconUrl = markerIcon;
  const customIcon = new L.Icon({
    iconUrl: pinpointIconUrl,
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });


  const handleMapClick = async (e) => {
    const clickedLatLng = e.latlng;
    
    setLocation({
      lat: clickedLatLng.lat,
      lng: clickedLatLng.lng,
    });
  
    // GEOCODING API
    const apiKey = '7fbe9533c0c9424aa41c500419e5ef83';
    const url = `https://api.opencagedata.com/geocode/v1/json?q=${clickedLatLng.lat}+${clickedLatLng.lng}&key=${apiKey}`;

    try {
      const response = await fetch(url);
      const data = await response.json();

      if (data.results.length > 0) {
        const formattedAddress = data.results[0].formatted;
        setManualAddress(formattedAddress);
      }
    } catch (error) {
      console.error('Error fetching address:', error);
    }
  };

  const handlePictureChange = (file) => {
    setPicture(file);
  };

  const handleSubmit = () => {
    console.log('Report submitted:', {
      title,
      description,
      category,
      location,
      picture,
      manualAddress,
    });
   
    const jsonServerSendData={
      "reportHeadline": title,
      "location":  location,
      "description":description,
      "categoryID" : 1

    };
    let url = apiConfig.getLoginUrl;
    fetch(url, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonServerSendData),
    })
      .then((response) => {
        if(response.status==200){
            alert("Vaša prijava je podnešena")
        }else{
            alert("DOŠLO JE DO GREŠKE!!!");
        }
      });

  };

  
  function MapClickHandler() {
    useMapEvents({
      click: handleMapClick,
    });

    return null;
  }

  useEffect(() => {
    setManualAddress('');
  }, [location]);

  return (
  <>
    <div className="header">
          <Link to="/" className="profile-button">
            <button className="prijavaStete2">Home</button>
          </Link>
    </div>
    <div className="report-card">
      <h2>Prijava oštečenja</h2>

      <label htmlFor="title">Naslov:</label>
      <input type="text" id="title" value={title} onChange={(e) => setTitle(e.target.value)} />

      <label htmlFor="description">Opis:</label>
      <textarea id="description" value={description} onChange={(e) => setDescription(e.target.value)} />

      <label htmlFor="category">Kategorija:</label>
      <select id="category" value={category} onChange={(e) => setCategory(e.target.value)}>
        <option value="category1">Kategorija 1</option>
        <option value="category2">Kategorija 1</option>
        <option value="category3">Kategorija 1</option>
      </select>
      <label htmlFor="picture">Prikvači sliku:</label>
      <input type="file" id="picture" accept="image/*" onChange={handlePictureChange} />
      <MapContainer
        center={[location.lat, location.lng]}
        zoom={13}
        style={{ height: '300px', width: '100%', margin: '10px 0' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        <MapClickHandler />
        <Marker position={location} icon={customIcon}>
          <Popup>Odaberi lokaciju</Popup>
        </Marker>
      </MapContainer>

      <div>
        <label htmlFor="address">Upiši adresu:</label>
        <input type="text" id="address" value={manualAddress} readOnly />
      </div>
      
      <button onClick={handleSubmit}>Predaj prijavu</button>
    </div>
    <FooterComponent />
  </>
  );
};

export default ReportCard;
