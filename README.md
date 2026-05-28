# Introduction (Snoozeloo — Android Alarm App)

A native Android alarm application built as a focused study of modern Android architecture and design patterns. The aim was to practise structuring a real app the way production codebases are organised, rather than just wiring up screens.

## Architecture & patterns

Clean Architecture — business logic kept decoupled from framework and UI across separate layers

MVI / MVVM — structured, predictable state and UI flow

Offline-first — local persistence with Room

Dependency Injection — repositories and dependencies injected rather than constructed in place

Navigation Component — single, unified navigation graph for screen transitions

Jetpack Compose — declarative native UI in Kotlin
