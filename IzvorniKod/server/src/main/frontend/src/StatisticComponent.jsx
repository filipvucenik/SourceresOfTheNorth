import React, { useState, useEffect, useRef } from "react";
import HeaderComponent from "./HeaderComponent";
import FooterComponent from "./FooterComponent";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import markerIcon from "./marker.svg";
import apiConfig from "./apiConfig";

const server = apiConfig.getReportUrl;

function StatisticComponent() {
  const [filteredData, setFilteredData] = useState([]);
  const [trueFalse,setTrueFalse]=useState(false);
  const [selectedMarker, setSelectedMarker] = useState(null);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");
  const [statusReport, setStatusReport] = useState("");
  const [lat, setLat] = useState("");
  const [lng, setLng] = useState("");
  const [renderData, setRenderData] = useState([]); // Initialize as an empty array
  const mapRef = useRef(null); // referenca za spremanje instance karte
  const uniqueMapId = `map-${Math.floor(Math.random() * 10000)}`;

  const dataVariable = React.useRef(null); // Obična varijabla za spremanje podataka

  const statusChange = (e) => {
    setStatusReport(e.target.value);
  };

  const formatirajVrijeme = (vrijemeString) => {
    const [dani, sati, minute] = vrijemeString.split(',').map(Number);
    return `${dani} dana, ${sati} sati, ${minute} minuta`;
  };

  const getCategory = async () => {
    let url = apiConfig.getCategory;
    const fetchCategory = await fetch(url);
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

    setLat(lat);
    setLng(lng);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

  
    if (lat !== "" && lng !== "") {
      // Ako su latitude i longitude označene, provjeri je li radius unesen
      const radius = e.target.elements.radius.value.trim();
      if (!radius) {
        console.error("Radius is required when location is selected");
        // Dodajte alert za obavještavanje korisnika
        alert("Molimo vas označite radius kad je označena lokacija");
        return;
      }
    }
    const dataForSend = {
      categoryID: selectedCategoryID,
      radius: e.target.elements.radius.value,
      fromDateTime: e.target.elements.fromDateTime.value,
      toDateTime: e.target.elements.toDateTime.value,
      lat: lat,
      lng: lng,
      status: "", 
    };



    fetch(`${server}/statistic`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dataForSend),
    })
      .then((response) => response.json())
      .then((data) => {
        setRenderData(data); // Update renderData only when data is received
        setTrueFalse(true);
        console.log(renderData);
      })
      .catch((error) =>
        console.error("Greška prilikom slanja koordinata:", error)
      );
  };
  useEffect(() => {
    console.log("Updated renderData:", renderData);
  }, [renderData]);

  

  useEffect(() => {
    console.log(trueFalse);
  }, [trueFalse]);

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
  }, [selectedMarker, lat, lng]);

  
  

  return (
    <>
      <HeaderComponent />
      <div className="col-lg-6 col-md-10 col-sm-12 report-card">
        <h1>Statistika prijava</h1>
        <form onSubmit={handleSubmit}>
          <label>
            ID Kategorije:
            <select name="categoryID" onChange={handleCategoryChange}>
              <option key="default" value="default">
                {" "}
                Izaberite kategoriju
              </option>
              {Object.keys(categoryData).map((key) => (
                <option key={key} value={key}>
                  {categoryData[key]}
                </option>
              ))}
            </select>
          </label>
          <label>
            Datum prijave OD:
            <input type="date" name="fromDateTime" />
          </label>
          <label>
            Datum prijave DO:
            <input type="date" name="toDateTime" />
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
        {trueFalse && (
  <ul className="statistika">
    {renderData ? (
      <li className="statistika-child">
        {Object.entries(renderData).map(([key, value]) => (
          <p key={key}>
            {key === "avgReportsByDay" ? "Prosječni broj podnesenih prijava po danu" : ""} 
            {key === "avgTimeInProgress" ? `Prosječno vrijeme prijave provedeno u statusu 'u procesu' (u satima): ${value !== null ? value : "-"}` : ""}
            {key === "avgTimeWaiting" ? `Prosječno vrijeme prijave provedeno u statusu 'na čekanju' (u satima): ${value !== null ? value : "-"}` : ""}
            {key === "reportCount" ? "Ukupan broj prijava" : ""} 
            {key === "reportInProgressCount" ? "Broj prijava sa statusom 'u procesu'" : ""}
            {key === "reportInProgressShare" ? "Postotak prijava sa statusom 'u tijeku'" : ""}
            {key === "reportSolvedCount" ? "Broj prijava sa statusom 'riješeno'" : ""}
            {key === "reportSolvedShare" ? "Postotak prijava sa statusom 'riješeno'" : ""}
            {key === "reportWaitingCount" ? "Broj prijava sa statusom 'na čekanju'" : ""} 
            {key === "reportWaitingShare" ? "Postotak prijava sa statusom 'na čekanju'" : ""}
            : {key.includes("Share") ? `${(value * 100).toFixed(2)}%` : value}
          </p>
        ))}
      </li>
    ) : (
      <p>Nema dostupnih podataka</p>
    )}
  </ul>
)}
<hr />
      </div>
      

      <FooterComponent />
    </>
  );
}

export default StatisticComponent;