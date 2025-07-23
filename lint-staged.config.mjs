export default {
  "*": files => `./gradlew spotlessApply -PspotlessFiles=${files.join(',')}`,
};
