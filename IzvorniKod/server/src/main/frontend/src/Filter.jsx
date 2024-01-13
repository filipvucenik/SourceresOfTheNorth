import React, { useState, useEffect, useRef } from "react";
import {useNavigate } from "react-router-dom";
import HeaderComponent from "./HeaderComponent";
import FooterComponent from "./FooterComponent";
import MapComponent from "./MapComponent";
import "leaflet/dist/leaflet.css";
import apiConfig from "./apiConfig";

function Filter() {
  const [filteredData, setFilteredData] = useState([]);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");
  const [showMap, setShowMap] = useState(false);
  const navigate = useNavigate();

  const getCategory = async () => {
    let url = apiConfig.getCategory
    const fetchCategory = await fetch(
      url
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

  useEffect(() => {
    // This useEffect will run whenever filteredData changes
    console.log(filteredData);
  }, [filteredData]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const dataForSend = {
      categoryId: selectedCategoryID,
      startDate: e.target.elements.fromDateTime.value,
      endDate: e.target.elements.toDateTime.value,
      lat: "",
      lng: "",
      status: "",
      radius: "",
    };
    console.log(dataForSend);
    try {
      const response = await fetch(`https://progi-projekt-test.onrender.com/reports/filtered`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataForSend),
      });
  
      const data = await response.json();
      setFilteredData(data);  // Move setFilteredData outside the then block
      navigate("/");
    } catch (error) {
      console.error('Greška prilikom slanja koordinata:', error);
    }
  };
  

  return (
    <>
      <div className="col-lg-6 col-md-10 col-sm-12 report-card2">
        <h1>Filter prijava</h1>
        <form onSubmit={handleSubmit}>
          <label>
            ID Kategorije:
            <select name="categoryID" onChange={handleCategoryChange}>
            <option key="default" value="default"> Izaberite kategoriju</option>
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
        </form>
        <hr />        
      </div>
      
      
    </>
  );
}

export default Filter;