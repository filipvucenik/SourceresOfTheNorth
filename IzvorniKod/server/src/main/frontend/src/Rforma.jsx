import React, { useState, useEffect } from "react";
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  useMapEvents,
} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import "./Rforma.css";
import L from "leaflet";
import markerIcon from "./marker.svg";
import apiConfig from "./apiConfig";
import FooterComponent from "./FooterComponent";
import HeaderComponent from "./HeaderComponent";

let categoryData = "";
try {
  const fetchCategory = await fetch(
    "https://ostecenja-progi-fer.onrender.com/category"
  );
  const fetchData = await fetchCategory.json();
  categoryData = Object.fromEntries(
    fetchData.map((item) => [item.categoryID, item.categoryName])
  );
} catch (error) {
  console.error(error);
  alert("Greška prilikom dohvaćanja kategorija, molimo osvježite stranicu");
}

let keyWordData = "";
try {
  const fecthKeyData = await fetch(
    "https://ostecenja-progi-fer.onrender.com/keywords"
  );
  const fetchData = await fecthKeyData.json();
  keyWordData = Object.fromEntries(
    fetchData.map((item) => [item.keyword.toLowerCase(), item.categoryID])
  );
} catch (error) {
  console.log(error);
  alert(
    "Greška prilikom učitavanja stranice.\n Automatski odabir kategorije neće raditi"
  );
}
const pinpointIconUrl = markerIcon;
const customIcon = new L.Icon({
  iconUrl: pinpointIconUrl,
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32],
});

const ReportCard = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");
  const [location, setLocation] = useState({
    lat: 45.804270084085914,
    lng: 15.978798866271974,
  }); // Default na Zg
  const [picture, setPicture] = useState(null);
  const [manualAddress, setManualAddress] = useState("");

  const manualAddressChange = (e) => {
    setManualAddress(e.target.value);
  };

  const handleMapClick = async (e) => {
    const clickedLatLng = e.latlng;
    setLocation({
      lat: clickedLatLng.lat,
      lng: clickedLatLng.lng,
    });

    // GEOCODING API
    const apiKey = "7fbe9533c0c9424aa41c500419e5ef83";
    const url = `https://api.opencagedata.com/geocode/v1/json?q=${clickedLatLng.lat}+${clickedLatLng.lng}&key=${apiKey}`;

    try {
      const response = await fetch(url);
      const data = await response.json();

      if (data.results.length > 0) {
        const formattedAddress = data.results[0].formatted;
        setManualAddress(formattedAddress);
      }
    } catch (error) {
      console.error("Error fetching address:", error);
    }
  };

  const checkForKeyword = (text) => {
    const words = text.trim().toLowerCase().split(/\s+/);

    const categoryCounts = {};

    words.forEach((word) => {
      if (keyWordData.hasOwnProperty(word)) {
        const categoryId = keyWordData[word];
        categoryCounts[categoryId] = (categoryCounts[categoryId] || 0) + 1;
      }
    });

    let maxCategory = null;
    let maxCount = 0;

    for (const categoryId in categoryCounts) {
      if (categoryCounts[categoryId] > maxCount) {
        maxCategory = categoryId;
        maxCount = categoryCounts[categoryId];
      }
    }

    if (maxCategory !== null) {
      setCategory(maxCategory.toString());
    }
  };

  const handlePictureChange = (file) => {
    setPicture(file);
  };

  const handleSubmit = () => {
    console.log("Report submitted:", {
      title,
      description,
      category,
      location,
      picture,
      manualAddress,
    });

    const jsonServerSendData = {
      reportHeadline: title,
      location: location.lat + "," + location.lng,
      description: description,
      categoryID: 1,
    };

    let url = apiConfig.getReportUrl;
    fetch(url, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonServerSendData),
    }).then((response) => {
      if (response.status === 200) {
        alert("Vaša prijava je podnešena");
      } else {
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
    setManualAddress("");
  }, [location]);

  return (
    <>
      <HeaderComponent />
      <div className="report-card">
        <h2>Prijava oštečenja</h2>

        <label htmlFor="title">Naslov:</label>
        <input
          type="text"
          id="title"
          value={title}
          onChange={(e) => {
            setTitle(e.target.value);
            checkForKeyword(e.target.value);
          }}
        />

        <label htmlFor="description">Opis:</label>
        <textarea
          id="description"
          value={description}
          onChange={(e) => {
            setDescription(e.target.value);
            checkForKeyword(e.target.value);
          }}
        />

        <label htmlFor="category">Kategorija:</label>
        <select
          id="category"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
        >
          {Object.entries(categoryData).map(([categoryId, categoryName]) => (
            <option key={categoryId} value={categoryId}>
              {categoryName}
            </option>
          ))}
        </select>
        <label htmlFor="picture">Prikvači sliku:</label>
        <input
          type="file"
          id="picture"
          accept="image/*"
          onChange={handlePictureChange}
        />
        <MapContainer
          center={[location.lat, location.lng]}
          zoom={13}
          style={{ height: "300px", width: "100%", margin: "10px 0" }}
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
          <input
            type="text"
            id="address"
            value={manualAddress}
            onChange={manualAddressChange}
          />
        </div>

        <button onClick={handleSubmit}>Predaj prijavu</button>
      </div>
      <FooterComponent />
    </>
  );
};

export default ReportCard;
