---
name: KMP toolchain verification
description: Constraints discovered while validating this Kotlin Multiplatform project.
---

The Gradle wrapper checksum is specific to the distribution version; changing the
wrapper version requires changing the checksum at the same time. Android tasks
also require a locally installed Android SDK, while iOS targets are disabled on
Linux hosts.

**Why:** The imported reference project included a wrapper checksum for a
different Gradle version, and the Replit Linux environment did not include an
Android SDK.

**How to apply:** Keep wrapper URL and checksum paired, and perform Android
build verification on a host with SDK API 36 configured.