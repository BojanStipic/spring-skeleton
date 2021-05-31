#!/bin/bash

usage() {
cat << _EOF_
Usage: ${0##*/} <group> <artifact>
DESCRIPTION:
    Initializes a new project with the given group name and artifact name.
_EOF_
}

(( $# != 2 )) && echo "Wrong number of positional arguments" && usage && exit 1

GROUP="$1"
ARTIFACT="$2"

find . -name .git -prune -o -type f -exec sed -i -E "s/bojanstipic\./$GROUP./g" '{}' \;
mv "src/main/java/bojanstipic" "src/main/java/$GROUP"
mv "src/test/java/bojanstipic" "src/test/java/$GROUP"
find . -name .git -prune -o -type f -exec sed -i -E "s/skeleton/$ARTIFACT/g" '{}' \;
mv "src/main/java/$GROUP/skeleton" "src/main/java/$GROUP/$ARTIFACT"
mv "src/test/java/$GROUP/skeleton" "src/test/java/$GROUP/$ARTIFACT"
