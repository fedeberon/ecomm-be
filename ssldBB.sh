#!/bin/sh

cd /etc/letsencrypt/live/vps-2124680-x.dattaweb.com-0003/

sudo openssl pkcs12 -export -out cert.p12   -in fullchain.pem   -inkey privkey.pem   -name tomcat

