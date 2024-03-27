./gradlew Emerald:shadowJar
./gradlew ExampleExtension:build
echo "Copying"
cp -f ./Emerald/build/libs/Emerald-1.0-SNAPSHOT-all.jar ./server/plugins
cp -f ./ExampleExtension/build/libs/ExampleExtension-1.0-SNAPSHOT.jar ./server/extensions
echo "Successfully copied"