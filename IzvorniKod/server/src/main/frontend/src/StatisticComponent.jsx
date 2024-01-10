import React, { useState, useEffect, useRef } from "react";
import HeaderComponent from "./HeaderComponent";
import FooterComponent from "./FooterComponent";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import markerIcon from "./marker.svg";

const server = "http://localhost:8080/";

function StatisticComponent() {
  const [filteredData, setFilteredData] = useState([]);
  const [selectedMarker, setSelectedMarker] = useState(null);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");
  var lattitude;
  var longitude;

  const mapRef = useRef(null); // referenca za spremanje instance karte
  const uniqueMapId = `map-${Math.floor(Math.random() * 10000)}`;

  const getCategory = async () => {
    const fetchCategory = await fetch(
      "https://ostecenja-progi-fer.onrender.com/category"
    );
    const fetchData = await fetchCategory.json();
    const transformedData = Object.fromEntries(
      fetchData.map((item) => [item.categoryID, item.categoryName])
    );
    setCategoryData(transformedData);
  };
  useEffect(() => {
    getCategory();
  }, []);

  const handleCategoryChange = (event) => {
    const selectedID = event.target.value;
    setSelectedCategoryID(selectedID);
  };

  const customIcon = new L.Icon({
    iconUrl: markerIcon,
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });

  const createMarker = (map, lat, lng) => {
    if (selectedMarker) {
      map.removeLayer(selectedMarker);
    }

    const marker = L.marker([lat, lng], { icon: customIcon }).addTo(map);
    setSelectedMarker(marker);
    lattitude = lat;
    longitude = lng;
    let popupIsOpen = false;

    marker.on("click", () => {
      if (popupIsOpen) {
        marker.closePopup();
        popupIsOpen = false;
      } else {
        marker.bindPopup(`${lat},${lng}`).openPopup();
        popupIsOpen = true;
      }
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const dataForSend = {
     categoryID: selectedCategoryID,
     status: e.target.elements.status.value,
     radius: e.target.elements.radius.value,
     fromDateTime: e.target.elements.fromDateTime.value,
     toDateTime: e.target.elements.toDateTime.value,
      lattitude,
      longitude,
    };
    //console.log("Koordinate za slanje:", { lattitude, longitude });
      fetch(`https://progi-projekt-test.onrender.com/reports/filtered`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataForSend),
      })
      .then(response => response.json())
      .then(data => setFilteredData(data))
      //console.log('Odgovor od servera:', data));
      .catch(error => console.error('Greška prilikom slanja koordinata:', error));
  };

  useEffect(() => {
    if (!mapRef.current) {
      const map = L.map(uniqueMapId).setView([45.81, 15.985], 14);

      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 28,
      }).addTo(map);

      map.on("click", function (e) {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;
        createMarker(map, lat, lng);
      });

      mapRef.current = map;
      return () => {
        mapRef.current.off("click");
      };
    } else {
      mapRef.current.off("click");
      mapRef.current.on("click", function (e) {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;
        createMarker(mapRef.current, lat, lng);
      });

      mapRef.current.invalidateSize();
    }
  }, [selectedMarker]);

  return (
    <>
      <HeaderComponent />
      <div className="col-lg-6 col-md-10 col-sm-12 report-card">
        <h1>Statistika prijava</h1>
        <form onSubmit={handleSubmit}>
          <label>
            ID Kategorije:
            <select name="categoryID" onChange={handleCategoryChange}>
              <option value="" disabled>
                Select Category
              </option>
              {Object.keys(categoryData).map((key) => (
                <option key={key} value={key}>
                  {categoryData[key]}
                </option>
              ))}
            </select>
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
          <div
            id={uniqueMapId}
            style={{ width: "90%", height: "40vh", marginLeft: "5%" }}
          ></div>
          <label>
            Radius(km):
            <input type="text" name="radius" />
          </label>
          <button className="submit-btn" type="submit">
            Filter
          </button>
        </form>
        <hr />        
      </div>
      
      <ul className="statistika">
        {filteredData.map((item) => (
          <li key={item.reportID} className="statistika-child">
            <h2><b>{item.reportHeadline}</b></h2>
            <p>Report ID: {item.reportID}</p>
            <p>Category ID: {item.categoryID}</p>
            <p>Report Timestamp: {item.reportTS}</p>
            <p>Description: {item.description}</p>
            <p>
              Location: {item.lat}, {item.lng}
            </p>
            <p>Link na stranicu prijave</p>
          </li>
        ))}
      </ul>
      
      

      <FooterComponent />
    </>
  );
}

export default StatisticComponent;