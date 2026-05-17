# QRCraft Modularization Refactoring

This document tracks the progress and structure of the refactoring from a monolithic `app` module to a multi-module architecture.

## Target Architecture

```text
:app                            ← Wires all modules together
:build-logic                    ← Gradle convention plugins for centralizing build configurations
:core:domain                    ← Pure Kotlin. Shared domain models, repository interfaces
:core:data                      ← Android Library. Room DB, repository implementations, mappers
:core:presentation              ← Android Library + Compose. Shared UI components, icons
:core:design-system             ← Android Library + Compose. Theme, colors, typography
:scan:domain                    ← Scan feature domain logic
:scan:data                      ← Scan feature data logic
:scan:presentation              ← Scan feature screens and ViewModels
:generate:domain                ← Generate feature domain logic
:generate:data                  ← Generate feature data logic
:generate:presentation          ← Generate feature screens and ViewModels
:history:domain                 ← History feature domain logic
:history:data                   ← History feature data logic
:history:presentation           ← History feature screens and ViewModels
```

## Status Tracking

- [x] Step 1: Create `REFACTORING.md` and document plan
- [x] Step 2: Establish `:build-logic` convention plugins
- [x] Step 3: Create Core Modules (`:core:domain`, `:core:data`, `:core:presentation`, `:core:design-system`)
- [x] Step 4: Create Feature Modules (`:scan`, `:generate`, `:history` submodules)
- [x] Step 5: Migrate files and update `:app` module
- [x] Step 6: Final Verification & Cleanup

## Steps Detail

### Step 2: Build Logic
We will use Gradle convention plugins to keep our build files clean and consistent across modules.
Plugins to create:
- `qrcraft.android.application`
- `qrcraft.android.library`
- `qrcraft.android.feature`
- `qrcraft.android.compose`
- `qrcraft.jvm.library`

### Step 3: Core Modules Migration
- `core:domain`: No dependencies on Android. Contains models and interfaces.
- `core:data`: Depends on `core:domain`. Contains Room and Repository implementations.
- `core:presentation`: Shared UI components.
- `core:design-system`: Theme and base styling.

### Step 4: Feature Modules Migration
Each feature is split into `domain`, `data`, and `presentation` to maintain strict dependency rules and improve testability.
