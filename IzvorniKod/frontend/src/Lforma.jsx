import './Lforma.css';
const Lforma = () => {
    return ( 
        <>
  <div className="welcome">
    <h1>Dobro Došli!</h1>
  </div>
  <div className="form-container">
    <form className="login-form">
      <h2>Prijava</h2>
      <input
        type="text"
        placeholder="Korisničko ime"
        id="UserLogin"
        required=""
      />
      <input type="password" placeholder="Šifra" id="passLogin" required="" />
      <button type="submit">Prijava</button>
    </form>
    <form className="register-form">
      <h2>Registracija</h2>
      <input
        type="text"
        placeholder="Korisničko ime"
        id="userReg"
        required=""
      />
      <input type="password" placeholder="Šifra" id="passReg" required="" />
      <input type="text" placeholder="Ime" id="imeReg" required="" />
      <input type="text" placeholder="Prezime" id="prezReg" required="" />
      <input type="email" placeholder="E-mail" id="mailReg" required="" />
      <button type="submit">Registracija</button>
    </form>
  </div>
</>
     );
}
 
export default Lforma;