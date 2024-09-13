#!/bin/bash
TEST_ID=$(date '+%s')
echo "TEST CASE: $TEST_ID"

REQUEST_BODY=$(cat <<EOF
{
  "customer": {
    "customer_id": 4
  },
  "booking": {
    "booking_date": "2024-09-15",
    "booking_time": "18:00",
    "table_size": 4
  }
}
EOF
)

echo REQUEST:
echo "$REQUEST_BODY"

echo REPONSE:
curl -X POST http://localhost:8080/api/book \
  -H "Content-Type: application/json" \
  -d "$REQUEST_BODY"
echo ""
