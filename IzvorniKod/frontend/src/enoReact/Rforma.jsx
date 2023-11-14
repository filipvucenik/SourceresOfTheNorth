import './Rforma.css';
const Rforma = () => {
    return ( <div className="report-form">
    <h1>Prijava štete na cestama i javnim površinama</h1>
    <form action="#" method="post">
      <div className="form-group">
        <label htmlFor="office">Nadležan ured:</label>
        <select id="office" name="office" required="">
          <option value="" disabled="" selected="">
            Ručni odabir ureda
          </option>
          <option value="office1">Ured 1</option>
          <option value="office2">Ured 2</option>
          <option value="office3">Ured 3</option>
        </select>
      </div>
      <div className="form-group">
        <label htmlFor="problem">Problem:</label>
        <input type="text" id="problem" name="problem" required="" />
      </div>
      <div className="form-group">
        <label htmlFor="description">Opis:</label>
        <textarea
          id="description"
          name="description"
          rows={4}
          required=""
          defaultValue={""}
        />
      </div>
      <div className="form-group">
        <label>Lokacija:</label>
        <div className="map-container" id="map" />
      </div>
      <div className="form-group">
        <label htmlFor="picture">Priloži sliku:</label>
        <input type="file" id="picture" name="picture" />
      </div>
      <button type="submit" className="submit-btn">
        Predaj
      </button>
      <div className="form-group">
        <label htmlFor="additional-info">Dodatne informacije(Izborno):</label>
        <textarea
          id="additional-info"
          name="additional-info"
          rows={4}
          defaultValue={""}
        />
      </div>
    </form>
  </div>
   );
}
 
export default Rforma;