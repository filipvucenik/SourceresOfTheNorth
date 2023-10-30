CREATE TABLE User
(
  email VARCHAR NOT NULL,
  firstName VARCHAR(20) NOT NULL,
  lastName VARCHAR(20)  NOT NULL,
  userID BIGINT NOT NULL,
  password VARCHAR(20) NOT NULL,
  userName VARCHAR(10)  NOT NULL,
  PRIMARY KEY (userID)
);

CREATE TABLE CityOffice
(
  cityOfficeName VARCHAR(30) NOT NULL,
  cityOfficeEmail VARCHAR(20) NOT NULL,
  cityOfficePassword VARCHAR(10) NOT NULL,
  cityOfficeId BIGINT NOT NULL,
  PRIMARY KEY (cityOfficeId)
);

CREATE TABLE ReportGroup
(
  reportStatus VARCHAR(10) NOT NULL,
  groupID BIGINT NOT NULL,
  cityOfficeId BIGINT NOT NULL,
  PRIMARY KEY (groupID),
  FOREIGN KEY (cityOfficeId) REFERENCES CityOffice(cityOfficeId)
);

CREATE TABLE Category
(
  categoryKeywords VARCHAR NOT NULL,
  categoryName VARCHAR(20) NOT NULL,
  categoryID BIGINT NOT NULL,
  cityOfficeId BIGINT NOT NULL,
  PRIMARY KEY (categoryID),
  FOREIGN KEY (cityOfficeId) REFERENCES CityOffice(cityOfficeId)
);

CREATE TABLE Report
(
  reportID BIGINT NOT NULL,
  location VARCHAR(20) NOT NULL,
  description VARCHAR(100) NOT NULL,
  reportTS TIMESTAMP NOT NULL,
  userID BIGINT,
  groupID BIGINT NOT NULL,
  categoryID BIGINT NOT NULL,
  PRIMARY KEY (reportID),
  FOREIGN KEY (userID) REFERENCES User(userID),
  FOREIGN KEY (groupID) REFERENCES ReportGroup(groupID),
  FOREIGN KEY (categoryID) REFERENCES Category(categoryID)
);

CREATE TABLE Image
(
  imageID BIGINT NOT NULL,
  URL VARCHAR(100) NOT NULL,
  reportID BIGINT NOT NULL,
  PRIMARY KEY (imageID, reportID),
  FOREIGN KEY (reportID) REFERENCES Report(reportID)
);
