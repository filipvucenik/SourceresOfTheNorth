import React, { useState, useEffect, useRef } from "react";
import HeaderComponent from "./HeaderComponent";
import FooterComponent from "./FooterComponent";
import "leaflet/dist/leaflet.css";
import apiConfig from "./apiConfig";

function Filter() {
  const [filteredData, setFilteredData] = useState([]);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    const dataForSend = {
     categoryID: selectedCategoryID,
     fromDateTime: e.target.elements.fromDateTime.value,
     toDateTime: e.target.elements.toDateTime.value,
    };
    console.log(dataForSend)
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
      .catch(error => console.error('Greška prilikom slanja koordinata:', error));
  };

  return (
    <>
      <HeaderComponent />
      <div className="col-lg-6 col-md-10 col-sm-12 report-card">
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
      <FooterComponent />
    </>
  );
}

export default Filter;