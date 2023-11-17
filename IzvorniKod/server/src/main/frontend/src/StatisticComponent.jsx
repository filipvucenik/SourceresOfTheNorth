import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Link } from "react-router-dom";
import "./App.css"
import FooterComponent from './FooterComponent';

const server = "http://localhost:8080/";

function StatisticComponent() {
    const [filteredData, setFilteredData] = useState([]);

    const handleSubmit = async (e) => {
        e.preventDefault();
    
        const categoryID = e.target.elements.categoryID.value;
        const status = e.target.elements.status.value;
        const fromDateTime = e.target.elements.fromDateTime.value;
        const toDateTime = e.target.elements.toDateTime.value;
        const location = e.target.elements.location.value;
        const radius = e.target.elements.radius.value;
    
        try {
          const response = await fetch(`${server}filtered?categoryID=${categoryID}&fromDateTime=${fromDateTime}&toDateTime=${toDateTime}
          &status=${status}&location=${location}&radius=${radius}`);
          const data = await response.json();
    
          setFilteredData(data);
          console.log('Rezultati filtriranja:', data);
        } catch (error) {
          console.error('Greška prilikom dohvaćanja podataka:', error);
        }
      };

  return (
  <>
  <div className="header">
          <Link to="/" className="profile-button">
            <button className="prijavaStete2">Home</button>
          </Link>
    </div>
    <div className="report-card">
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
        <label>
          Lokacija:
          <input type="text" name="location" />
        </label>
        <label>
          Radius(km):
          <input type="text" name="radius" />
        </label>
        <button className="submit-btn" type="submit">Filter</button>
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
