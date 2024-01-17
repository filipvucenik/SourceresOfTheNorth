import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import markerIcon from "./marker.svg";
import apiConfig from "./apiConfig";

const server = apiConfig.getReportUrl;

const MapComponent = () => {
  const mapRef = useRef(null);
  const markersRef = useRef([]);
  const uniqueMapId = `map-${Math.floor(Math.random() * 10000)}`;
  const navigate = useNavigate();

  const [filteredData, setFilteredData] = useState([]);
  const [filteredDataFromEndpoint, setFilteredDataFromEndpoint] = useState([]);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");
  const [showFilterDiv, setShowFilterDiv] = useState(false);

  const customAlertReturn = (message, onOk) => {
    const overlay = document.createElement("div");
    overlay.style.cssText = `
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      z-index: 9998; 
    `;
    document.body.appendChild(overlay);

    const alertContainer = document.createElement("div");
    alertContainer.style.cssText = `
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      padding: 20px;
      background-color: white;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
      border-radius: 5px;
      text-align: center;
      z-index: 9999; 
    `;

    const alertText = document.createElement("p");
    alertText.style.cssText = `
      font-weight: bold;
      font-size: 16px;
    `;
    alertText.textContent = message;

    const closeButton = document.createElement("button");
    closeButton.textContent = "OK";
    closeButton.style.cssText = `
      margin-top: 10px;
      padding: 5px 10px;
      cursor: pointer;
      background-color: black;
      color: white;
      border: none;
      border-radius: 3px;
    `;

    closeButton.addEventListener("click", () => {
      document.body.removeChild(overlay);
      document.body.removeChild(alertContainer);
      navigate("/");
    });

    alertContainer.appendChild(alertText);
    alertContainer.appendChild(closeButton);
    document.body.appendChild(alertContainer);
  };

  const handleClick = () => {
    setShowFilterDiv(!showFilterDiv);
  };

  const handleSearch = async () => {
    // Assuming you have a reportId state variable for the input value
    const ReportId = document.getElementById("kodtrazilica").value;
    if (ReportId.trim() !== "") {
      try {
        const response = await fetch(`${server}/${ReportId}`);
        const data = await response.json();
        var trazilicaData = data;
        // Clear existing markers
        clearMarkers();
        createMarker(
          trazilicaData.report.lat,
          trazilicaData.report.lng,
          trazilicaData.report.reportID
        );
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    } else {
      // Handle the case when ReportId is an empty string
      customAlertReturn(
        "Niste unijeli kod prijave. Molimo unesite validan kod prijave."
      );
      // You might want to clear existing markers or handle it as per your requirements
    }
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

  useEffect(() => {
    console.log(filteredDataFromEndpoint);
    renderMarkers(filteredDataFromEndpoint);
  }, [filteredDataFromEndpoint]);

  useEffect(() => {
    console.log(filteredData);
    renderMarkers(filteredData);
  }, [filteredData]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Check if "Obriši filter" button is clicked
    const isDeleteFilterClicked =
      e.nativeEvent.submitter.textContent === "Obriši filter";

    // If "Obriši filter" button is clicked, show all locations
    if (isDeleteFilterClicked) {
      renderMarkers(filteredData);
      setShowFilterDiv(false); // Close the filter div
    } else {
      // If it's a regular filter submission
      const dataForSend = {
        categoryId: selectedCategoryID,
        startDate: e.target.elements.fromDateTime.value,
        endDate: e.target.elements.toDateTime.value,
        lat: "",
        lng: "",
        status: "",
        radius: "",
      };

      try {
        const response = await fetch(`${server}/filtered`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(dataForSend),
        });

        const data = await response.json();
        setFilteredDataFromEndpoint(data);
        handleClick();
      } catch (error) {
        console.error("Greška prilikom slanja koordinata:", error);
      }
    }
  };

  const createMarker = (lat, lng, id) => {
    const customIcon = new L.Icon({
      iconUrl: markerIcon,
      iconSize: [32, 32],
      iconAnchor: [16, 32],
      popupAnchor: [0, -32],
    });

    const marker = L.marker([lat, lng], { icon: customIcon });
    markersRef.current.push(marker);

    if (mapRef.current) {
      marker.addTo(mapRef.current);

      let popupIsOpen = false;

      marker.on("click", () => {
        if (popupIsOpen) {
          marker.closePopup();
          popupIsOpen = false;
        } else {
          marker
            .bindPopup(
              `Naslov prijave, kratki opis bude tu + <br/><a href='${apiConfig.getUrl}/report/${id}'>Otvori stranicu prijave</a>`
            )
            .openPopup();
          popupIsOpen = true;
        }
      });
    }
  };

  const fetchDataAndCreateMarkers = async () => {
    try {
      const response = await fetch(`${server}/unhandled`);
      const data = await response.json();
      setFilteredData(data);
      renderMarkers(data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const clearMarkers = () => {
    markersRef.current.forEach((marker) => marker.remove());
    markersRef.current.length = 0;
  };

  const renderMarkers = (dataToUse) => {
    clearMarkers();
    console.log(dataToUse);
    if (dataToUse && dataToUse.length > 0) {
      for (const lokacija of dataToUse) {
        if (lokacija.lat && lokacija.lng) {
          createMarker(lokacija.lat, lokacija.lng, lokacija.reportID);
        } else if (
          lokacija.report &&
          lokacija.report.lat &&
          lokacija.report.lng
        ) {
          createMarker(
            lokacija.report.lat,
            lokacija.report.lng,
            lokacija.report.reportID
          );
        }
      }
    }
  };

  useEffect(() => {
    if (!mapRef.current) {
      const map = L.map(uniqueMapId).setView([45.8, 15.967], 13);
      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
      }).addTo(map);
      mapRef.current = map;
    } else {
      mapRef.current.invalidateSize();
    }

    // Call renderMarkers after the conditions are checked
    if (showFilterDiv) {
      // If filter is applied, render markers based on filtered data
      renderMarkers(filteredDataFromEndpoint);
    } else {
      // If filter is not applied, render all markers
      fetchDataAndCreateMarkers();
    }
  }, []);

  return (
    <>
      <div className="title">
        <button className="btn btn-outline-dark m-2" onClick={handleClick}>
          Filter prijava
        </button>
        <input
          type="text"
          name="kodtrazilica"
          id="kodtrazilica"
          className="customPosition2"
          placeholder="Traži prijavu po kodu"
        />
        <button
          className="btn btn-outline-dark m-2 customPosition"
          onClick={handleSearch}
        >
          Traži
        </button>
      </div>
      <div
        id={uniqueMapId}
        style={{ width: "90%", height: "70vh", marginLeft: "5%", zIndex: 0 }}
      ></div>
      {showFilterDiv && (
        <div className="col-lg-6 col-md-10 col-sm-12 report-card2">
          <h1>Filter prijava</h1>
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
            <button className="submit-btn" type="submit">
              Filter
            </button>
            |
            <button className="submit-btn" type="submit">
              Obriši filter
            </button>
          </form>
          <hr />
        </div>
      )}
    </>
  );
};

export default MapComponent;
