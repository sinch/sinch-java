## Conversation api examples

### How to run
These examples have their credentials injected from system properties.
Find your Credentials at https://dashboard.sinch.com/settings/access-keys

#### Required system properties to run these examples
```properties
ignoreTestTags=true
sinch.project_id=__project_id__
sinch.key_id=__key_id__
sinch.key_secret=__key_secret__
```

#### Running an example from Gradle
```bash
./gradlew test --tests example.conversationapi.AppsExamples.listApps -DignoreTestTags=true -Dsinch.project_id=__project_id__ -Dsinch.key_id=__key_id__ -Dsinch.key_secret=__key_secret__
```