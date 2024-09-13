#!/bin/bash
QUERY_DATE=$1

echo REQUEST:
echo http://localhost:8080/bookings/$QUERY_DATE

echo REPONSE:
curl http://localhost:8080/bookings/$QUERY_DATE \
  -H "Content-Type: application/json" \
echo ""
