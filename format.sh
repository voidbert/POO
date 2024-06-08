#!/bin/sh

# This script formats the source code, and asks the user if they wish to keep
# the changes.

# Copyright 2023 Diogo Costa, Humberto Gomes, José Lopes
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

REPO_DIR="$(realpath "$(dirname -- "$0")")"
cd "$REPO_DIR" || exit 1

assert_installed_command() {
    if ! command -v "$1" > /dev/null; then
        printf "%s is not installed! Please install it and try again. " "$1" >&2
        echo   "Leaving ..." >&2
        exit 1
    fi
}

yesno() {
    stdbuf -o 0 printf "$1"
    read -r yn

    if echo "$yn" | grep -Eq '^[Yy]([Ee][Ss])?$'; then
        return 0
    elif echo "$yn" | grep -Eq '^[Nn][Oo]?$'; then
        return 1
    else
        echo "Invalid input. Leaving ..." >&2
        return 1
    fi
}

assert_installed_command clang-format
assert_installed_command git

out_dir="$(mktemp -d)"
diff_path="$(mktemp)"
mkdir "$out_dir/src"

find src -type f | while read -r file; do
    mkdir -p "$(dirname "$out_dir/$file")"
    clang-format "$file" | sed "\$a\\"  | sed 's/\s*$//' > "$out_dir/$file"
done

git --no-pager -c color.ui=always diff --no-index "src" "$out_dir/src" > "$diff_path"

if ! [ -s "$diff_path" ]; then
    echo "Already formatted! Leaving ..."
    rm -r "$diff_path" "$out_dir"
    exit 0
elif [ "$1" = "--check" ]; then
    echo "Formatting errors!"
    cat "$diff_path"
    rm -r "$diff_path" "$out_dir"
    exit 1
else
    less -R "$diff_path"
fi

if yesno "Agree with these changes? [Y/n]: "; then
    cp -r "$out_dir/src" .
else
    echo "Source code left unformatted. Leaving ..."
fi
rm -r "$diff_path" "$out_dir"
