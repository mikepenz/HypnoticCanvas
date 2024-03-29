[![Maven Central](https://img.shields.io/maven-central/v/com.mikepenz.hypnoticcanvas/hypnoticcanvas)](https://search.maven.org/search?q=g:com.mikepenz.hypnoticcanvas)

# HypnoticCanvas

> [!NOTE]  
> This README is under construction.

HypnoticCanvas is a library providing a convenient `Modifier` offering mesmerising shaders as
background in your Compose based UI.

https://github.com/mikepenz/HypnoticCanvas/assets/1476232/ee120f1c-d18a-43c4-a7bc-a2d245e01482

## Setup

### Core-module

```gradle
implementation "com.mikepenz.hypnoticcanvas:hypnoticcanvas:${version}"
```

> [!NOTE]  
> All shaders provided in the `core` module are licensed either under `MIT` or `Apache 2.0` license.

### Shader-module

```gradle
implementation "com.mikepenz.hypnoticcanvas:hypnoticcanvas-shaders:${version}"
```

> [!IMPORTANT]  
> Shaders in this module have non permissive licenses. Ensure to read the
> [LICENSE](https://github.com/mikepenz/HypnoticCanvas?tab=readme-ov-file#shaders-module-license)
> section of the README.

## Usage

```kotlin
Box(
    modifier = Modifier
        .fillMaxSize()
        .shaderBackground(BlackCherryCosmos)
)
```

## Compatiblity

HypnoticCanvas is built
with [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/), meaning that it
supports different platforms:

| Platform      | Supported | Link                                                 |
|---------------|-----------|------------------------------------------------------|
| Android       | ✅         |                                                      |
| Desktop (JVM) | ✅         |                                                      |
| iOS           | ✅         |                                                      |
| Wasm          | ✅         | [Sample](https://mikepenz.github.io/HypnoticCanvas/) |

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
by [Chris Banes](https://github.com/chrisbanes/),
Licensed under [Apache License 2.0](https://github.com/chrisbanes/haze/blob/main/LICENSE)

The individual shaders are based on the respective shaders licenses. More details below.

### Shaders core-module

| Name                                                     | Author                                                          | License     | Note                                                                         |
|----------------------------------------------------------|-----------------------------------------------------------------|-------------|------------------------------------------------------------------------------|
| [GlossyGradients](https://www.shadertoy.com/view/lX2GDR) | [Giorgi Azmaipharashvili](https://www.shadertoy.com/user/Peace) | MIT License | Rights bought for this shader on Fiverr, included in this project under MIT. |

### Shaders shaders-module

| Name                                                                                                                                                                                      | Author                                              | License              | Note                                                                                              |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------|----------------------|---------------------------------------------------------------------------------------------------|
| [BlackCherryCosmos2](https://editor.isf.video/shaders/612cb473f4fe08001a0a6281) [via](https://glslsandbox.com/e#28545.0) [via](https://editor.isf.video/shaders/5e7a7fcf7c113618206de4cc) | [axiomcrux](https://editor.isf.video/u/axiomcrux)   | CC BY-NC-SA 3.0 DEED | Shader does not specifically include license, however found the shader it appears to be based on. |
| [GoldenMagma](https://www.shadertoy.com/view/tdBBRV)                                                                                                                                      | [TAKUSAKU](https://www.shadertoy.com/user/TAKUSAKU) | CC BY-NC-SA 3.0 DEED |                                                                                                   |
| [IceReflection](https://www.shadertoy.com/view/3djfzy)                                                                                                                                    | [TAKUSAKU](https://www.shadertoy.com/user/TAKUSAKU) | CC BY-NC-SA 3.0 DEED |                                                                                                   |
| [InkFlow](https://www.shadertoy.com/view/WdjBWD)                                                                                                                                          | [TAKUSAKU](https://www.shadertoy.com/user/TAKUSAKU) | CC BY-NC-SA 3.0 DEED |                                                                                                   |
| [OilFlow](https://www.shadertoy.com/view/Wd2fDW)                                                                                                                                          | [TAKUSAKU](https://www.shadertoy.com/user/TAKUSAKU) | CC BY-NC-SA 3.0 DEED |                                                                                                   |
| [PurpleLiquid](https://www.shadertoy.com/view/dsXyzf)                                                                                                                                     | [fouad](https://www.shadertoy.com/user/fouad)       | CC BY-NC-SA 3.0 DEED |                                                                                                   |
| [RainbowWater](https://www.shadertoy.com/view/dtySRR)                                                                                                                                     | [flylo](https://www.shadertoy.com/user/flylo)       | CC BY-NC-SA 3.0 DEED |                                                                                                   |
| [Stage](https://www.shadertoy.com/view/wtfcDj)                                                                                                                                            | [TAKUSAKU](https://www.shadertoy.com/user/TAKUSAKU) | CC BY-NC-SA 3.0 DEED |                                                                                                   |

## License

The core project code in this repository is licensed as under Apache
2.0. `SPDX-License-Identifier: Apache-2.0`.

All Shaders are provided under their respective Authors license.

Shaders in the `hypnoticcanvas` module are licensed either as `MIT, or Apache 2.0`.
Shaders in the `hypnoticcanvas-shaders` module are licensed
as `SPDX-License-Identifier: CC-BY-NC-SA-3.0`.

### Core module License

The source code for the core module is licensed under Apache 2.0, with the shaders provided in the
core module as MIT License.

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

Shaders in this module are most from [ShaderToy.com](https://www.shadertoy.com/) and are
licensed [Attribution-NonCommercial-ShareAlike 3.0 Unported](https://creativecommons.org/licenses/by-nc-sa/3.0/).
Which is the default license as outlined by [ShaderToy.com](https://www.shadertoy.com/terms)