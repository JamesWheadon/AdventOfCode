#!/bin/bash

set -e

if [ -z "$1" ]; then
    echo "Usage: $0 <package_name>"
    exit 1
fi

PACKAGE_NAME=$1
TEMPLATE_DIR="src/main/kotlin/dayTemplate"
BASE_DIR="src/main/kotlin/$PACKAGE_NAME"

if [ ! -d "$TEMPLATE_DIR" ]; then
    echo "Error: Template directory $TEMPLATE_DIR does not exist."
    exit 1
fi

mkdir -p "$BASE_DIR"

LAST_DAY=$(find $BASE_DIR -maxdepth 1 -type d -name "day*" | sort -n | tail -1)
if [ -z "$LAST_DAY" ]; then
  NEXT_DAY="01"
else
  LAST_NUM=$(ls $BASE_DIR | grep -o 'day[0-9]*' | cut -c4- | sed 's/^0*//' | sort -n | tail -n 1)
  NEXT_DAY=$((LAST_NUM + 1))
  NEXT_DAY="$(printf '%02d' $NEXT_DAY)"
fi

NEW_DAY_DIR="$BASE_DIR/day$NEXT_DAY"

cp -r "$TEMPLATE_DIR" "$NEW_DAY_DIR"

find "$NEW_DAY_DIR" -type f -exec sed -i \
    -e "s|/dayTemplate|/$PACKAGE_NAME/day$NEXT_DAY|g" \
    -e "s|00|$NEXT_DAY|g" \
    -e "s|package dayTemplate|package $PACKAGE_NAME.day$NEXT_DAY|g" {} \;

find "$NEW_DAY_DIR" -type f -name "*Day00*" | while read -r FILE; do
    NEW_NAME=$(echo "$FILE" | sed "s|Day00|Day$NEXT_DAY|g")
    mv "$FILE" "$NEW_NAME"
done

echo "New day package created at: $NEW_DAY_DIR"
