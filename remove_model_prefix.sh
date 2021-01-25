#!/bin/bash

MATCH=$1
PROJECT=$2
FILES=$(grep -lr "$MATCH" "build/swagger/src/main/java/com/sinch/sdk/model/$PROJECT")
REGEX="s/$MATCH(?!\s|\.|,|;|\(|\))//g"

for f in $FILES
do
  new_f=$(echo "$f" | perl -pe "$REGEX")
  perl -pi -e "$REGEX" "$f" && mv "$f" "$new_f"
done