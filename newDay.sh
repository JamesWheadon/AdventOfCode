path="./src/main/kotlin"
cd $path || exit
if [ ! -d "$1" ]; then
  mkdir "$1"
fi
cd "./$1" || exit
last_dir=$(find . -maxdepth 1 -type d -name "day*" | sort -n | tail -1)
if [ -z "$last_dir" ]; then
  next_dir="day01"
else
  last_num=$(ls | grep -o 'day[0-9]*' | cut -c4- | sed 's/^0*//' | sort -n | tail -n 1)
  next_num=$((last_num + 1))
  next_dir="day$(printf '%02d' $next_num)"
fi
mkdir "$next_dir"
day_num=$(echo "$next_dir" | grep -oE '[0-9]+')
cp ../dayTemplate/Day00.kt day"${day_num}"/Day"${day_num}".kt
cp ../dayTemplate/Day00_test.txt day"${day_num}"/Day"${day_num}"_test.txt
cp ../dayTemplate/Day00.txt day"${day_num}"/Day"${day_num}".txt
sed -i '' "s/package dayTemplate/package $1.day$day_num/g" day"${day_num}"/Day"${day_num}".kt
sed -i '' "s/dayTemplate/$1\/day$day_num/g" day"${day_num}"/Day"${day_num}".kt
sed -i '' "s/00/$day_num/g" day"${day_num}"/Day"${day_num}".kt
open "day${day_num}/Day${day_num}.kt"
