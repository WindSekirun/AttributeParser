gradlew clean
gradlew :dagger-auto-inject:install
gradlew :dagger-auto-inject:bintrayUpload
gradlew :dagger-auto-inject-compiler:install
gradlew./g :dagger-auto-inject-compiler:bintrayUpload