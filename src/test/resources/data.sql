INSERT INTO DATABASE.PUBLIC.TC_SERVERS(ID, REGISTRATION, LATITUDE, LONGITUDE, ZOOM, MAP, BINGKEY, MAPURL, READONLY, TWELVEHOURFORMAT, ATTRIBUTES, FORCESETTINGS, COORDINATEFORMAT, DEVICEREADONLY, LIMITCOMMANDS, POILAYER, ANNOUNCEMENT, DISABLEREPORTS, OVERLAYURL, FIXEDEMAIL) VALUES (1, TRUE, 0.0, 0.0, 0, null, null, null, FALSE, FALSE, null, FALSE, null, FALSE, FALSE, null, null, FALSE, null, FALSE);
INSERT INTO DATABASE.PUBLIC.TC_USERS(ID, NAME, EMAIL, HASHEDPASSWORD, SALT, READONLY, ADMINISTRATOR, MAP, LATITUDE, LONGITUDE, ZOOM, TWELVEHOURFORMAT, ATTRIBUTES, COORDINATEFORMAT, DISABLED, EXPIRATIONTIME, DEVICELIMIT, USERLIMIT, DEVICEREADONLY, PHONE, LIMITCOMMANDS, LOGIN, POILAYER, DISABLEREPORTS, FIXEDEMAIL) VALUES (1, 'admin', 'admin', 'D33DCA55ABD4CC5BC76F2BC0B4E603FE2C6F61F4C1EF2D47', '000000000000000000000000000000000000000000000000', FALSE, TRUE, null, 0.0, 0.0, 0, FALSE, null, null, FALSE, null, -1, 0, FALSE, null, FALSE, null, null, FALSE, FALSE);