[![Maven Central](https://img.shields.io/maven-central/v/com.mikepenz.hypnoticcanvas/hypnoticcanvas)](https://search.maven.org/search?q=g:com.mikepenz.hypnoticcanvas)

# HypnoticCanvas

This README is under construction.

HypnoticCanvas is a library to provide a convenient `Modifier` to use mesmerising shaders as
background in your Compose based UI.

![Preview](https://github.com/mikepenz/HypnoticCanvas/raw/dev/.github/preview/preview.mp4)

## Setup

### Core-module

```gradle
implementation "com.mikepenz.hypnoticcanvas:hypnoticcanvas:${version}"
```

### Shader-module

```gradle
implementation "com.mikepenz.hypnoticcanvas:hypnoticcanvas-shaders:${version}"
```

## Usage

```kotlin
Box(
    modifier = Modifier.fillMaxSize()
        .shaderBackground(BlackCherryCosmos)
)
```

## Compatiblity

HypnoticCanvas is built with [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/), meaning that it supports different platforms:

| Platform      | Supported        |
|---------------|------------------|
| Android       | ✅               |
| Desktop (JVM) | ✅               |
| iOS           | ✅               |
| Wasm          | ✅               |

## Build & Run Sample App

### Run Desktop App

```bash
./gradlew sample:run
```

### Run Web App

```bash
./gradlew sample:wasmJsRun
```

### Update aboutLibraries.json

```bash
 ./gradlew sample:exportLibraryDefinitions -PaboutLibraries.exportPath=src/commonMain/composeResources/files/
 ```

## Credit

The base project setup is strongly based on the [haze](https://github.com/chrisbanes/haze) project
by [Chris Banes](https://github.com/chrisbanes/).
Licensed under [Apache License 2.0](https://github.com/chrisbanes/haze/blob/main/LICENSE)

The individual shaders are based on their respective shaders
from [ShaderToy.com](https://www.shadertoy.com/) and are
licensed [CC BY-NC-SA 3.0 DEED](https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en).

## License

The core project code in this repository is licensed as under Apache
2.0. `SPDX-License-Identifier: Apache-2.0`.

All Shaders are provided under their respective Authors license.

Shaders in the `hypnoticcanvas` module are licensed either as `MIT, or Apache 2.0`.
Shaders in the `hypnoticcanvas-shaders` module are licensed
as `SPDX-License-Identifier: CC-BY-NC-SA-3.0`.

### Core library License

```
Copyright 2024 Mike Penz
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

### Shaders module License

[Attribution-NonCommercial-ShareAlike 3.0 Unported](https://creativecommons.org/licenses/by-nc-sa/3.0/).
Which is the default license as outlined by [ShaderToy.com](https://www.shadertoy.com/terms)



