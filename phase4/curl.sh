curl -X 'GET' \
  'http://localhost:8080/nutzer' \
  -H 'accept: application/json'


curl -X 'POST' \
  'http://localhost:8080/nutzer' \
  -H 'accept: */*' \
  -H 'Content-Type: multipart/form-data' \
  -F 'email=quentino@hotmail.com' \
  -F 'benutzername=Versa55' \
  -F 'passwort=pul55iCti' \
  -F 'iban=DE0330050540265'


curl -X 'GET' \
  'http://localhost:8080/premiumnutzer?gueltigkeitstage=10' \
  -H 'accept: application/json'


curl -X 'POST' \
  'http://localhost:8080/premiumnutzer' \
  -H 'accept: */*' \
  -H 'Content-Type: multipart/form-data' \
  -F 'email=clan@web.de' \
  -F 'benutzername=larmm11' \
  -F 'passwort=t1eDa4kni' \
  -F 'iban=DE033348488388229323' \
  -F 'gueltigkeit=2025-03-03 00:00:00'


curl -X 'GET' \
  'http://localhost:8080/kuenstler' \
  -H 'accept: application/json'

curl -X 'POST' \
  'http://localhost:8080/kuenstler' \
  -H 'accept: */*' \
  -H 'Content-Type: multipart/form-data' \
  -F 'email=johvu@disney.com' \
  -F 'benutzername=tanjo21' \
  -F 'passwort=pul55iCti' \
  -F 'iban=DE0212030000000021' \
  -F 'gueltigkeit=2025-04-04 00:00:00' \
  -F 'aktiv_seit=2024-09-05 00:00:00' \
  -F 'pseudonym=Tanjiro'

curl -X 'GET' \
  'http://localhost:8080/songs' \
  -H 'accept: application/json'

curl -X 'GET' \
  'http://localhost:8080/alben' \
  -H 'accept: application/json'

curl -X 'GET' \
  'http://localhost:8080/bands' \
  -H 'accept: application/json'

curl -X 'GET' \
  'http://localhost:8080/playlists' \
  -H 'accept: application/json'

curl -X 'GET' \
  'http://localhost:8080/playlists/1/aufnahmen' \
  -H 'accept: application/json'

curl -X 'GET' \
  'http://localhost:8080/alben/1/kritiken' \
  -H 'accept: application/json'

curl -X 'POST' \
  'http://localhost:8080/songs' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk=' \
  -H 'Content-Type: multipart/form-data' \
  -F 'titel=Bumba' \
  -F 'dauer=300' \
  -F 'speicherort_hq=hjghjjk' \
  -F 'speicherort_lq=uziiuzfgn' \
  -F 'pseudonym=' \
  -F 'genreid='

curl -X 'DELETE' \
  'http://localhost:8080/songs/11' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk='

curl -X 'POST' \
  'http://localhost:8080/playlists' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk=' \
  -H 'Content-Type: multipart/form-data' \
  -F 'name=hit für hit' \
  -F 'privat=true'

curl -X 'DELETE' \
  'http://localhost:8080/playlists/5' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk='

curl -X 'POST' \
  'http://localhost:8080/playlists/4/aufnahmen' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk=' \
  -H 'Content-Type: multipart/form-data' \
  -F 'aufnahmeid=3'

curl -X 'DELETE' \
  'http://localhost:8080/playlists/4/aufnahmen/3' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk='

curl -X 'POST' \
  'http://localhost:8080/alben/6/kritiken' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk=' \
  -H 'Content-Type: multipart/form-data' \
  -F 'text=poa poa poa'

curl -X 'DELETE' \
  'http://localhost:8080/kritiken/5' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk='

curl -X 'PATCH' \
  'http://localhost:8080/alben/6/kritiken/4' \
  -H 'accept: */*' \
  -H 'Authorization: Basic WWVsbGUxNDp0MWVEYTRrbmk=' \
  -H 'Content-Type: multipart/form-data' \
  -F 'text=tschuuuu'