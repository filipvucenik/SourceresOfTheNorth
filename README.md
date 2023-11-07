# Programsko inženjerstvo - aplikacija za prijavu oštećenja

 Ovaj projekt je reultat timskog rada u sklopu projeknog zadatka kolegija [Programsko inženjerstvo](https://www.fer.unizg.hr/predmet/proinz) na Fakultetu elektrotehnike i računarstva Sveučilišta u Zagrebu. 

# Opis projekta

U sklopu ovog projekta razvijamo web aplikaciju za prijavu oštećenja na javnim površinama u gradu. Aplikacija omogućava građanima jednostavan način prijave oštećenja i praćenja postojećih prijava u sustavu. Gradskim uredima aplikacija omogućava bolji uvid u postojeće probleme u zajednici. Također unaprijeđuje transparentnost rada gradskih ureda s obzirom da će njihovo obrađivanje prijava biti javno dostupno. 

Cilj ovoga projekta je na praktičan način naučiti kako se razvija jednostavno programsko rješenje u timu. Kroz proces razvoja programske potpore cilj nam je naučiti kako se organizirati i raditi kao tim. Također kroz projekt želimo naučiti koristiti nove tehnologije i koristiti već postojeću programsku potporu za neke zahtjeve. Kroz projekt usvajamo korištenje React i Spring Boot radnih okvira.

# Funkcijski zahtjevi
Ključni funkcijski zahtjevi ovog projekta su:
- F01: registracija korisnika
- F02: prijava korisnika
- F03: anoniman korisnik
- F04: podnošenje prijave oštećenja (naslov, opis, lokacija, slika - opcionalno, kategorija)
- F05: odabir lokacije oštećenja preko interaktivne karte
- F06: odabir lokacije oštećenja preko unosa adrese
- F07: iščitavanje lokacije oštećenja iz metapodataka slike
- F08: automatsko prepoznavanje kategorije prijave
- F09: mogućnost automatskog predlaganja nadovezivanja prijave na već postojeću
- F10: Prijava ima status (na čekanju, u procesu rješavanja, riješena)
- F11: nakon podnošenja prijave korisnik dobiva kod
- F12: pregledavanje podataka o korisničkom računu prijavljenog korisnika
- F13: uređivanje podataka o korisničkom računu prijavljenog korisnika
- F14: pregledavanje prijava prijavljenog korisnika
- F15: pregledavanje svih prijava u sustavu preko interaktivne karte
- F16: prijave sa statusom riješena su dostupne isključivo pretraživanjem preko koda
- F17: mogućnost filtriranja prijava u sustavu prilikom pregledavanja
- F18: pretraživanje prijave preko koda
- F19: pregled statistike za prijave odabrane filtrom
- F20: prijava djelatnika gradskog ureda u sustav
- F21: djelatnik gradskog ureda može odbaciti nevažeću prijavu
- F22: djelatnik gradskog ureda može proslijediti prijavu drugom uredu
- F23: djelatnik gradskog ureda može mijenjati status prijave
- F24: povratna informacija korisniku (ako je registrirani) za svaku promjenu nad prijavom
- F25: djelatnik gradskog ureda ima uvid samo prijave pristigle na njegov ured
- F26: pregled prijava kroz 3 liste (jedna za svaki status) za djelatnike gradskog ureda
- F27: dodatno filtriranje liste prijava za djelatnike gradskog ureda
- F28: pregled statistike za prijave odabrane filtrom za djelatnike gradskog ureda

# Tehnologije
U ovom projektu se koriste Spring Boot i React radni okviri.

# Članovi tima 
- Petar Belošević (https://github.com/PetarBelosevic)
- Vinko Brkić (https://github.com/v-brkic)
- Tomislav Grudić (https://github.com/tomGru55)
- Fran Meglić (https://github.com/meglicfran)
- Bruno Mikulan (https://github.com/b-mikulan)
- Eno Peršić (https://github.com/wowow-02)
- Filip Vučenik (https://github.com/filipvucenik)

Voditelj tima: Petar Belošević

# 📝 Kodeks ponašanja [![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md)
Kroz rad na ovom projektu svi članovi slijede [KODEKS PONAŠANJA STUDENATA FAKULTETA ELEKTROTEHNIKE I RAČUNARSTVA SVEUČILIŠTA U ZAGREBU](https://www.fer.hr/_download/repository/Kodeks_ponasanja_studenata_FER-a_procisceni_tekst_2016%5B1%5D.pdf), naputke za timski rad sa predmeta [Programsko inženjerstvo](https://wwww.fer.hr) i poštuju [etički kodeks IEEE-a](https://www.ieee.org/about/corporate/governance/p7-8.html).

U timu imamo podjelu rada na frontend (3 člana), backend (3 člana) i dokumentaciju (1 član). Tim ima formalni sastanak jednom tjedno, a po potrebi i dodatne neformalne sastanke. Na sastanku svaki član izvještava ostatak tima o napretku na svojem radu, korigiraju se dosadašnja rješenja i definiraju daljnji koraci.

# 📝 Licenca
Važeča (1)
[![CC BY-NC-SA 4.0][cc-by-nc-sa-shield]][cc-by-nc-sa]

Ovaj repozitorij sadrži otvoreni obrazovni sadržaji (eng. Open Educational Resources)  i licenciran je prema pravilima Creative Commons licencije koja omogućava da preuzmete djelo, podijelite ga s drugima uz 
uvjet da navođenja autora, ne upotrebljavate ga u komercijalne svrhe te dijelite pod istim uvjetima [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License HR][cc-by-nc-sa].  

[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: https://creativecommons.org/licenses/by-nc/4.0/deed.hr 
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-by-nc-sa-shield]: https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg

### Reference na licenciranje repozitorija
